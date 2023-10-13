package com.frog.iot.mqtt;

import com.alibaba.fastjson.JSON;
import com.frog.common.core.redis.RedisCache;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.spring.SpringUtils;
import com.frog.iot.domain.*;
import com.frog.iot.mapper.*;
import com.frog.iot.model.Action;
import com.frog.iot.model.NtpModel;
import com.frog.iot.model.ThingsModels.ThingsModelSimpleItem;
import com.frog.iot.model.ThingsModels.ThingsModelValuesInput;
import com.frog.iot.model.TriggerParameter;
import com.frog.iot.service.IDeviceService;
import com.frog.iot.tdengine.service.ILogService;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;


@Service
public class EmqxService {
    private static final Logger logger = LoggerFactory.getLogger(EmqxService.class);

    // 物模型值命名空间：Key：TSLV:{productId}_{deviceNumber}   HKey:{identity#V/identity#S/identity#M/identity#N}
    private String devicePreKey = "TSLV:";

    @Autowired
    private EmqxClient emqxClient;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private ILogService logService;

    @Autowired
    private AlertTriggerMapper alertTriggerMapper;

    @Autowired
    private AlertMapper alertMapper;

    @Autowired
    private AlertLogMapper alertLogMapper;

    @Autowired
    private SceneMapper sceneMapper;

    @Autowired
    private SceneTriggerMapper sceneTriggerMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 订阅的主题
     */
    private static final String prefix = "/+/+/";
    String sInfoTopic = prefix + "info/post";
    String sNtpTopic = prefix + "ntp/post";
    String sPropertyTopic = prefix + "property/post";
    String sFunctionTopic = prefix + "function/post";
    String sEventTopic = prefix + "event/post";
    String sShadowPropertyTopic = prefix + "property-offline/post";
    String sShadowFunctionTopic = prefix + "function-offline/post";

    /**
     * 发布的主题
     */
    String pStatusTopic = "/status/post";
    String pInfoTopic = "/info/get";
    String pNtpTopic = "/ntp/get";
    String pPropertyTopic = "/property/get";
    String pFunctionTopic = "/function/get";

    public void subscribe(MqttAsyncClient client) throws MqttException {
        // 订阅设备信息
        client.subscribe(sInfoTopic, 1);
        // 订阅时钟同步
        client.subscribe(sNtpTopic, 1);
        // 订阅设备属性
        client.subscribe(sPropertyTopic, 1);
        // 订阅设备功能
        client.subscribe(sFunctionTopic, 1);
        // 订阅设备事件
        client.subscribe(sEventTopic, 1);
        // 订阅属性（影子模式）
        client.subscribe(sShadowPropertyTopic, 1);
        // 订阅功能（影子模式）
        client.subscribe(sShadowFunctionTopic, 1);
        logger.info("mqtt订阅了设备信息和物模型主题");
    }

    /**
     * 消息回调方法
     *
     * @param topic       主题
     * @param mqttMessage 消息体
     */
    @Async
    public void subscribeCallback(String topic, MqttMessage mqttMessage) throws InterruptedException {

        /**测试线程池使用*/
        logger.info("====>>>>线程名--{}", Thread.currentThread().getName());
        /**模拟耗时操作*/
        // Thread.sleep(1000);
        // subscribe后得到的消息会执行到这里面
        String message = new String(mqttMessage.getPayload());
        logger.info("接收消息主题 : " + topic);
        logger.info("接收消息Qos : " + mqttMessage.getQos());
        logger.info("接收消息内容 : " + message);

        String[] topicItem = topic.substring(1).split("/");
        Long productId = Long.valueOf(topicItem[0]);
        String deviceNum = topicItem[1];
        String name = topicItem[2];
        switch (name) {
            case "info":
                reportDevice(productId, deviceNum, message);
                break;
            case "ntp":
                publishNtp(productId, deviceNum, message);
                break;
            case "property":
                reportProperty(productId, deviceNum, message, false);
                ruleMatch(productId, deviceNum, message, 1);
                break;
            case "function":
                reportFunction(productId, deviceNum, message, false);
                ruleMatch(productId, deviceNum, message, 2);
                break;
            case "event":
                reportEvent(productId, deviceNum, message);
                ruleMatch(productId, deviceNum, message, 3);
                break;
            case "property-offline":
                reportProperty(productId, deviceNum, message, true);
                break;
            case "function-offline":
                reportFunction(productId, deviceNum, message, true);
                break;
        }
    }

    /**
     * 上报设备信息
     */
    private void reportDevice(Long productId, String deviceNum, String message) {
        try {
            // 设备实体
            Device deviceEntity = deviceService.selectDeviceBySerialNumber(deviceNum);
            // 上报设备信息
            Device device = JSON.parseObject(message, Device.class);
            device.setProductId(productId);
            device.setSerialNumber(deviceNum);
            deviceService.reportDevice(device, deviceEntity);
            // 发布设备状态
            publishStatus(productId, deviceNum, 3, deviceEntity.getIsShadow(), device.getRssi());
        } catch (Exception e) {
            logger.error("接收设备信息，解析数据时异常 message={}", e.getMessage());
        }
    }

    /**
     * 上报属性
     *
     * @param message
     */
    private void reportProperty(Long productId, String deviceNum, String message, boolean isShadow) {
        try {
            List<ThingsModelSimpleItem> thingsModelSimpleItems = JSON.parseArray(message, ThingsModelSimpleItem.class);
            ThingsModelValuesInput input = new ThingsModelValuesInput();
            input.setProductId(productId);
            input.setDeviceNumber(deviceNum);
            input.setThingsModelValueRemarkItem(thingsModelSimpleItems);
            deviceService.reportDeviceThingsModelValue(input, 1, isShadow);
        } catch (Exception e) {
            logger.error("接收属性数据，解析数据时异常 message={}", e.getMessage());
        }
    }

    /**
     * 上报功能
     *
     * @param message
     */
    private void reportFunction(Long productId, String deviceNum, String message, boolean isShadow) {
        try {
            List<ThingsModelSimpleItem> thingsModelSimpleItems = JSON.parseArray(message, ThingsModelSimpleItem.class);
            ThingsModelValuesInput input = new ThingsModelValuesInput();
            input.setProductId(productId);
            input.setDeviceNumber(deviceNum);
            input.setThingsModelValueRemarkItem(thingsModelSimpleItems);
            deviceService.reportDeviceThingsModelValue(input, 2, isShadow);
        } catch (Exception e) {
            logger.error("接收功能，解析数据时异常 message={}", e.getMessage());
        }
    }

    /**
     * 上报事件
     *
     * @param message
     */
    private void reportEvent(Long productId, String deviceNum, String message) {
        try {
            List<ThingsModelSimpleItem> thingsModelSimpleItems = JSON.parseArray(message, ThingsModelSimpleItem.class);
            for (int i = 0; i < thingsModelSimpleItems.size(); i++) {
                // 添加到设备日志
                DeviceLog deviceLog = new DeviceLog();
                // 设备日志值
                if (thingsModelSimpleItems.get(i).getId().startsWith("array_")) {
                    // 根据ID中包含的索引赋值
                    int index = Integer.parseInt(thingsModelSimpleItems.get(i).getId().substring(6, 7));
                    String identity = thingsModelSimpleItems.get(i).getId().substring(8);
                    String key = devicePreKey + productId + "_" + deviceNum;
                    String[] values = redisCache.getCacheMapValue(key, identity + "#V").toString().split(",");
                    values[index] = thingsModelSimpleItems.get(i).getValue();
                    String value = String.join(",", values);
                    deviceLog.setLogValue(value);
                }else{
                    deviceLog.setLogValue(thingsModelSimpleItems.get(i).getValue());
                }
                deviceLog.setRemark(thingsModelSimpleItems.get(i).getRemark());
                deviceLog.setSerialNumber(deviceNum);
                deviceLog.setIdentity(thingsModelSimpleItems.get(i).getId());
                deviceLog.setLogType(3);
                deviceLog.setIsMonitor(0);
                deviceLog.setCreateTime(DateUtils.getNowDate());
                // 1=影子模式，2=在线模式，3=其他
                deviceLog.setMode(2);
                logService.saveDeviceLog(deviceLog);
            }
        } catch (Exception e) {
            logger.error("接收事件，解析数据时异常 message={}", e.getMessage());
        }
    }


    /**
     * 1.发布设备状态
     */
    public void publishStatus(Long productId, String deviceNum, int deviceStatus, int isShadow, int rssi) {
        String message = "{\"status\":" + deviceStatus + ",\"isShadow\":" + isShadow + ",\"rssi\":" + rssi + "}";
        emqxClient.publish(1, false, "/" + productId + "/" + deviceNum + pStatusTopic, message);
    }

    /**
     * 2.发布设备信息
     */
    public void publishInfo(Long productId, String deviceNum) {
        emqxClient.publish(1, false, "/" + productId + "/" + deviceNum + pInfoTopic, "");
    }

    /**
     * 3.发布时钟同步信息
     *
     * @param message
     */
    private void publishNtp(Long productId, String deviceNum, String message) {
        NtpModel ntpModel = JSON.parseObject(message, NtpModel.class);
        ntpModel.setServerRecvTime(System.currentTimeMillis());
        ntpModel.setServerSendTime(System.currentTimeMillis());
        emqxClient.publish(1, false, "/" + productId + "/" + deviceNum + pNtpTopic, JSON.toJSONString(ntpModel));
    }

    /**
     * 4.发布属性
     * delay 延时，秒为单位
     */
    public void publishProperty(Long productId, String deviceNum, List<ThingsModelSimpleItem> thingsList, int delay) {
        String pre = "";
        if (delay > 0) {
            pre = "$delayed/" + String.valueOf(delay) + "/";
        }
        if (thingsList == null) {
            emqxClient.publish(1, true, "/" + productId + "/" + deviceNum + pPropertyTopic, "");
        } else {
            emqxClient.publish(1, true, "/" + productId + "/" + deviceNum + pPropertyTopic, JSON.toJSONString(thingsList));
        }
    }

    /**
     * 5.发布功能
     * delay 延时，秒为单位
     */
    public void publishFunction(Long productId, String deviceNum, List<ThingsModelSimpleItem> thingsList, int delay) {
        String pre = "";
        if (delay > 0) {
            pre = "$delayed/" + String.valueOf(delay) + "/";
        }
        if (thingsList == null) {
            emqxClient.publish(1, true, pre + "/" + productId + "/" + deviceNum + pFunctionTopic, "");
        } else {
            emqxClient.publish(1, true, pre + "/" + productId + "/" + deviceNum + pFunctionTopic, JSON.toJSONString(thingsList));
        }
        System.out.println();
    }

    /**
     * 设备数据同步
     *
     * @param deviceNumber 设备编号
     * @return 设备
     */
    public Device deviceSynchronization(String deviceNumber) {
        Device device = deviceService.selectDeviceBySerialNumber(deviceNumber);
        // 1-未激活，2-禁用，3-在线，4-离线
        if (device.getStatus() == 3) {
            device.setStatus(4);
            deviceService.updateDeviceStatus(device);
            // 发布设备信息
            publishInfo(device.getProductId(), device.getSerialNumber());
        }
        return device;
    }

    /**
     * 规则匹配(告警和场景联动)
     *
     * @param productId
     * @param deviceNum
     * @param message
     * @param type      1=属性 2=功能 3=事件 4=升级 5=上线 6=下线
     */
    public void ruleMatch(Long productId, String deviceNum, String message, int type) {
        try {
            // 告警处理 ，同一个类中异步需要手动获取代理对象
            SpringUtils.getBean(EmqxService.class).alertProcess(productId, deviceNum, message, type);
            // 场景联动处理
            SpringUtils.getBean(EmqxService.class).sceneProcess(productId, deviceNum, message, type);

        } catch (Exception e) {
            logger.error("接收数据，解析数据时异常 message={}", e.getMessage());
        }
    }

    /**
     * 场景处理
     *
     * @param productId
     * @param deviceNum
     * @param message
     * @param type      1=属性 2=功能 3=事件 4=升级 5=上线 6=下线
     */
    @Async
    public void sceneProcess(Long productId, String deviceNum, String message, int type) {
        TriggerParameter sceneTriggerParameter = new TriggerParameter();
        sceneTriggerParameter.setSerialNumber(deviceNum);
        sceneTriggerParameter.setStatus(1);
        if (type == 1 || type == 2 || type == 3) {
            List<ThingsModelSimpleItem> thingsModelSimpleItems = JSON.parseArray(message, ThingsModelSimpleItem.class);
            String[] ids = thingsModelSimpleItems.stream().map(ThingsModelSimpleItem::getId).toArray(String[]::new);
            sceneTriggerParameter.setIds(ids);
            List<SceneTrigger> sceneTriggers = sceneTriggerMapper.selectSceneTriggerList(sceneTriggerParameter);
            for (int i = 0; i < thingsModelSimpleItems.size(); i++) {
                for (int j = 0; j < sceneTriggers.size(); j++) {
                    if (thingsModelSimpleItems.get(i).getId().equals(sceneTriggers.get(j).getId())) {
                        // 获取匹配结果
                        boolean result = ruleResult(sceneTriggers.get(j).getOperator(),
                                thingsModelSimpleItems.get(i).getValue(),
                                sceneTriggers.get(j).getValue());
                        if (result) {
                            // 查询场景信息并执行动作
                            Scene scene = sceneMapper.selectSceneBySceneId(sceneTriggers.get(j).getSceneId());
                            sceneActionProcess(scene, type);
                        }

                    }
                }
            }
        } else {
            // 上线和下线匹配
            logger.info("上下线触发-----------------------------------------");
            sceneTriggerParameter.setType(type);
            List<SceneTrigger> sceneTriggers = sceneTriggerMapper.selectSceneTriggerList(sceneTriggerParameter);
            if (sceneTriggers.size() > 0) {
                // 查询场景信息并执行动作
                Scene scene = sceneMapper.selectSceneBySceneId(sceneTriggers.get(0).getSceneId());
                sceneActionProcess(scene, type);
            }
        }
    }

    /**
     * 场景联动动作处理
     *
     * @param scene
     */
    private void sceneActionProcess(Scene scene, int type) {
        logger.info("执行场景联动动作-----------------------------------------");
        List<Action> actions = JSON.parseArray(scene.getActions(), Action.class);
        for (int k = 0; k < actions.size(); k++) {
            ThingsModelSimpleItem model = new ThingsModelSimpleItem();
            model.setId(actions.get(k).getId());
            model.setValue(actions.get(k).getValue());
            model.setRemark("场景联动触发");
            // 设备上线，延迟3秒发布，等待设备完成订阅，后面可修改webhook事件类型解决
            if (actions.get(k).getType() == 1) {
                List<ThingsModelSimpleItem> propertys = new ArrayList<>();
                propertys.add(model);
                publishProperty(actions.get(k).getProductId(), actions.get(k).getSerialNumber(), propertys, type == 5 ? 3 : 0);
            } else if (actions.get(k).getType() == 2) {
                List<ThingsModelSimpleItem> functions = new ArrayList<>();
                functions.add(model);
                publishFunction(actions.get(k).getProductId(), actions.get(k).getSerialNumber(), functions, type == 5 ? 3 : 0);
            }
        }
    }

    /**
     * 告警处理
     *
     * @param productId
     * @param deviceNum
     * @param message
     * @param type      1=属性 2=功能 3=事件 4=升级 5=上线 6=下线
     */
    @Async
    public void alertProcess(Long productId, String deviceNum, String message, int type) {
        TriggerParameter triggerParameter = new TriggerParameter();
        triggerParameter.setProductId(productId);
        triggerParameter.setStatus(1);
        triggerParameter.setSource(1);
        if (type == 1 || type == 2 || type == 3) {
            List<ThingsModelSimpleItem> thingsModelSimpleItems = JSON.parseArray(message, ThingsModelSimpleItem.class);
            String[] ids = thingsModelSimpleItems.stream().map(ThingsModelSimpleItem::getId).toArray(String[]::new);
            triggerParameter.setIds(ids);
            List<AlertTrigger> alertTriggers = alertTriggerMapper.selectAlertTriggerList(triggerParameter);
            for (int i = 0; i < thingsModelSimpleItems.size(); i++) {
                for (int j = 0; j < alertTriggers.size(); j++) {
                    if (thingsModelSimpleItems.get(i).getId().equals(alertTriggers.get(j).getId())) {
                        // 获取匹配结果
                        boolean result = ruleResult(alertTriggers.get(j).getOperator(),
                                thingsModelSimpleItems.get(i).getValue(),
                                alertTriggers.get(j).getValue());
                        if (result) {
                            // 查询告警信息并执行动作
                            thingsModelSimpleItems.get(i).setRemark("设备触发");
                            Alert alert = alertMapper.selectAlertByAlertId(alertTriggers.get(j).getAlertId());
                            alertActionProcess(alert, productId, deviceNum, JSON.toJSONString(thingsModelSimpleItems.get(i)), type);
                        }
                    }
                }
            }
        } else {
            // 上线和下线匹配
            triggerParameter.setType(type);
            List<AlertTrigger> alertTriggers = alertTriggerMapper.selectAlertTriggerList(triggerParameter);
            if (alertTriggers.size() > 0) {
                // 查询告警信息并执行动作
                Alert alert = alertMapper.selectAlertByAlertId(alertTriggers.get(0).getAlertId());
                String jsonDetail = "";
                if (type == 5) {
                    jsonDetail = "[{\"id\":\"online\",\"value\":\"设备上线\",\"remark\":\"设备触发\"}]";
                } else if (type == 6) {
                    jsonDetail = "[{\"id\":\"offline\",\"value\":\"设备下线\",\"remark\":\"设备触发\"}]";
                }
                // 告警动作处理
                alertActionProcess(alert, productId, deviceNum, jsonDetail, type);
            }
        }
    }

    /**
     * 告警动作处理
     *
     * @param alert
     * @param productId
     * @param deviceNum
     * @param jsonDetail
     */
    private void alertActionProcess(Alert alert, Long productId, String deviceNum, String jsonDetail, int type) {
        logger.info("执行告警动作-----------------------------------------");
        List<Action> actions = JSON.parseArray(alert.getActions(), Action.class);
        List<ThingsModelSimpleItem> propertys = new ArrayList<>();
        List<ThingsModelSimpleItem> functions = new ArrayList<>();
        for (int k = 0; k < actions.size(); k++) {
            ThingsModelSimpleItem model = new ThingsModelSimpleItem();
            model.setId(actions.get(k).getId());
            model.setValue(actions.get(k).getValue());
            model.setRemark("告警触发");
            if (actions.get(k).getType() == 1) {
                propertys.add(model);
            } else if (actions.get(k).getType() == 2) {
                functions.add(model);
            }
        }
        // 发布属性，设备上线需要延迟3秒，等待设备订阅主题
        if (propertys.size() > 0) {
            publishProperty(productId, deviceNum, propertys, type == 5 ? 3 : 0);
        }
        // 发布功能
        if (functions.size() > 0) {
            publishFunction(productId, deviceNum, functions, type == 5 ? 3 : 0);
        }
        // 生成告警消息通知，TODO 其他通知方式
        AlertLog alertLog = new AlertLog();
        alertLog.setAlertName(alert.getAlertName());
        alertLog.setAlertLevel(alert.getAlertLevel());
        if (alert.getAlertLevel() == 1) {
            // 1=不需要处理,2=未处理,3=已处理
            alertLog.setStatus(1);
        } else {
            alertLog.setStatus(2);
        }
        alertLog.setProductId(productId);
        alertLog.setSerialNumber(deviceNum);
        alertLog.setDetail(jsonDetail);
        alertLog.setCreateTime(DateUtils.getNowDate());
        alertLogMapper.insertAlertLog(alertLog);
    }

    /**
     * 规则匹配结果
     *
     * @param operator     操作符
     * @param value        上报的值
     * @param triggerValue 触发器的值
     * @return
     */
    private boolean ruleResult(String operator, String value, String triggerValue) {
        boolean result = false;
        // 操作符比较
        switch (operator) {
            case "=":
                result = value.equals(triggerValue);
                break;
            case "!=":
                result = !value.equals(triggerValue);
                break;
            case ">":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) > Double.parseDouble(triggerValue);
                }
                break;
            case "<":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) < Double.parseDouble(triggerValue);
                }
                break;
            case ">=":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) >= Double.parseDouble(triggerValue);
                }
                break;
            case "<=":
                if (isNumeric(value) && isNumeric(triggerValue)) {
                    result = Double.parseDouble(value) <= Double.parseDouble(triggerValue);
                }
                break;
            case "contain":
                result = value.contains(triggerValue);
                break;
            case "notcontain":
                result = !value.contains(triggerValue);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 判断字符串是否为整数或小数
     */
    private boolean isNumeric(String str) {
        Pattern pattern = compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}

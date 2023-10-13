package com.frog.iot.util.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.frog.common.core.redis.RedisCache;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.spring.SpringUtils;
import com.frog.iot.domain.Alert;
import com.frog.iot.domain.AlertLog;
import com.frog.iot.domain.AlertTrigger;
import com.frog.iot.domain.DeviceJob;
import com.frog.iot.mapper.AlertLogMapper;
import com.frog.iot.mapper.AlertMapper;
import com.frog.iot.model.Action;
import com.frog.iot.model.ThingsModels.ThingsModelSimpleItem;
import com.frog.iot.mqtt.EmqxService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务执行工具
 *
 * @author kerwincui
 */
public class JobInvokeUtil {
    /**
     * 执行方法
     *
     * @param deviceJob 系统任务
     */
    public static void invokeMethod(DeviceJob deviceJob) throws Exception {
        if (deviceJob.getJobType() == 1) {
            System.out.println("------------------------执行定时任务-----------------------------");
            List<Action> actions = JSON.parseArray(deviceJob.getActions(), Action.class);
            List<ThingsModelSimpleItem> propertys = new ArrayList<>();
            List<ThingsModelSimpleItem> functions = new ArrayList<>();
            for (int i = 0; i < actions.size(); i++) {
                ThingsModelSimpleItem model = new ThingsModelSimpleItem();
                model.setId(actions.get(i).getId());
                model.setValue(actions.get(i).getValue());
                model.setRemark("设备定时");
                if (actions.get(i).getType() == 1) {
                    propertys.add(model);
                } else if (actions.get(i).getType() == 2) {
                    functions.add(model);
                }
            }
            EmqxService emqxService = SpringUtils.getBean(EmqxService.class);
            // 发布属性
            if (propertys.size() > 0) {
                emqxService.publishProperty(deviceJob.getProductId(), deviceJob.getSerialNumber(), propertys, 0);
            }
            // 发布功能
            if (functions.size() > 0) {
                emqxService.publishFunction(deviceJob.getProductId(), deviceJob.getSerialNumber(), functions, 0);
            }

        } else if (deviceJob.getJobType() == 2) {
            System.out.println("------------------------定时执行告警-----------------------------");
            // 查询触发器
            AlertTrigger alertTrigger = JSON.parseObject(deviceJob.getAlertTrigger(), AlertTrigger.class);
            if(alertTrigger==null){
                return;
            }
            // 查询产品下所有设备匹配的物模型值
            RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
            // 获取所有匹配的key
            Set<String> keys = redisCache.getListKeyByPrefix("TSLV:" + alertTrigger.getProductId());

            // 获取所有匹配的值
            Map<String, String> map = redisCache.hashGetAllMatchByKeys(keys, alertTrigger.getOperator(), alertTrigger.getId(), alertTrigger.getValue());
            if (map == null || map.size() == 0) {
                return;
            }
            // 执行动作
            List<Action> actions = JSON.parseArray(deviceJob.getActions(), Action.class);
            List<ThingsModelSimpleItem> propertys = new ArrayList<>();
            List<ThingsModelSimpleItem> functions = new ArrayList<>();
            for (int i = 0; i < actions.size(); i++) {
                ThingsModelSimpleItem model = new ThingsModelSimpleItem();
                model.setId(actions.get(i).getId());
                model.setValue(actions.get(i).getValue());
                model.setRemark("告警定时触发");
                if (actions.get(i).getType() == 1) {
                    propertys.add(model);
                } else if (actions.get(i).getType() == 2) {
                    functions.add(model);
                }
            }
            EmqxService emqxService = SpringUtils.getBean(EmqxService.class);
            AlertMapper alertMapper = SpringUtils.getBean(AlertMapper.class);
            AlertLogMapper alertLogMapper = SpringUtils.getBean(AlertLogMapper.class);
            Alert alert = alertMapper.selectAlertByAlertId(alertTrigger.getAlertId());
            List<AlertLog> alertLogList = new ArrayList<>();
            map.forEach((key, value) -> {
                // Key：TSLV:{productId}_{deviceNumber}
                String deviceNumber = key.substring(key.indexOf("_") + 1);
                System.out.println("deviceNumber:" + deviceNumber);
                System.out.println("productId" + actions.get(0).getProductId());
                // 发布属性
                if (propertys.size() > 0) {
                    emqxService.publishProperty(actions.get(0).getProductId(), deviceNumber, propertys, 0);
                }
                // 发布功能
                if (functions.size() > 0) {
                    emqxService.publishFunction(actions.get(0).getProductId(), deviceNumber, functions, 0);
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
                alertLog.setProductId(alertTrigger.getProductId());
                alertLog.setSerialNumber(deviceNumber);
                ThingsModelSimpleItem item = new ThingsModelSimpleItem();
                item.setId(alertTrigger.getId());
                item.setValue(value);
                item.setRemark("定时触发");
                alertLog.setDetail(JSONObject.toJSONString(item));
                alertLog.setCreateTime(DateUtils.getNowDate());
                alertLogList.add(alertLog);
            });
            // 批量插入告警日志
            alertLogMapper.insertAlertLogBatch(alertLogList);
        } else if (deviceJob.getJobType() == 3) {
            System.out.println("------------------------定时执行场景联动-----------------------------");
            List<Action> actions = JSON.parseArray(deviceJob.getActions(), Action.class);
            EmqxService emqxService = SpringUtils.getBean(EmqxService.class);
            for (int i = 0; i < actions.size(); i++) {
                ThingsModelSimpleItem model = new ThingsModelSimpleItem();
                model.setId(actions.get(i).getId());
                model.setValue(actions.get(i).getValue());
                model.setRemark("场景联动定时触发");
                if (actions.get(i).getType() == 1) {
                    List<ThingsModelSimpleItem> propertys = new ArrayList<>();
                    propertys.add(model);
                    emqxService.publishProperty(actions.get(i).getProductId(), actions.get(i).getSerialNumber(), propertys, 0);
                } else if (actions.get(i).getType() == 2) {
                    List<ThingsModelSimpleItem> functions = new ArrayList<>();
                    functions.add(model);
                    emqxService.publishFunction(actions.get(i).getProductId(), actions.get(i).getSerialNumber(), functions, 0);
                }
            }
        }
    }
}

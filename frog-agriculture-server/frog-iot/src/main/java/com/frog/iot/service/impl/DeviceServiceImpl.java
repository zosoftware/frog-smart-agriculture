package com.frog.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frog.common.constant.Constants;
import com.frog.common.core.domain.AjaxResult;
import com.frog.common.core.domain.entity.SysRole;
import com.frog.common.core.domain.entity.SysUser;
import com.frog.common.core.redis.RedisCache;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.StringUtils;
import com.frog.common.utils.http.HttpUtils;
import com.frog.common.utils.ip.IpUtils;
import com.frog.iot.domain.Device;
import com.frog.iot.domain.DeviceLog;
import com.frog.iot.domain.DeviceUser;
import com.frog.iot.domain.Product;
import com.frog.iot.mqtt.EmqxService;
import com.frog.iot.service.IAlertLogService;
import com.frog.iot.tdengine.service.ILogService;
import com.frog.iot.mapper.DeviceMapper;
import com.frog.iot.mapper.DeviceUserMapper;
import com.frog.iot.model.*;
import com.frog.iot.model.ThingsModelItem.*;
import com.frog.iot.model.ThingsModels.*;
import com.frog.iot.service.IDeviceService;
import com.frog.iot.service.IProductService;
import com.frog.iot.service.IToolService;
import com.frog.system.service.ISysUserService;
import org.apache.http.annotation.Obsolete;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.frog.common.utils.SecurityUtils.getLoginUser;

/**
 * 设备Service业务层处理
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Service
public class DeviceServiceImpl implements IDeviceService {
    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    // 物模型值命名空间：Key：TSLV:{productId}_{deviceNumber}   HKey:{identity#V/identity#S/identity#M/identity#N}
    private String devicePreKey = "TSLV:";

    // 物模型命名空间：Key:TSL:{productId}
    private String tslPreKey = "TSL:";

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceUserMapper deviceUserMapper;

    @Autowired
    private ThingsModelServiceImpl thingsModelService;

    @Autowired
    private DeviceJobServiceImpl deviceJobService;

    @Autowired
    private IToolService toolService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ILogService logService;

    @Autowired
    private IAlertLogService alertLogService;

    @Autowired
    @Lazy
    private EmqxService emqxService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public Device selectDeviceByDeviceId(Long deviceId) {
        Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
        // redis中获取设备状态（物模型值）
        device.setThingsModelValue(JSONObject.toJSONString(getCacheDeviceStatus(device.getProductId(), device.getSerialNumber())));
        return device;
    }

    /**
     * 查询设备统计信息
     *
     * @return 设备
     */
    @Override
    public DeviceStatistic selectDeviceStatistic() {
        Device device = new Device();
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleKey().equals("tenant")) {
                // 租户查看产品下所有设备
                device.setTenantId(user.getUserId());
            } else if (roles.get(i).getRoleKey().equals("general")) {
                // 用户查看自己设备
                device.setUserId(user.getUserId());
            }
        }
        // 获取设备、产品和告警数量
        DeviceStatistic statistic = deviceMapper.selectDeviceProductAlertCount(device);
        if (statistic == null) {
            statistic = new DeviceStatistic();
            return statistic;
        }
        // 获取属性、功能和事件
        DeviceStatistic thingsCount = logService.selectCategoryLogCount(device);
        if (thingsCount == null) {
            return statistic;
        }
        // 组合属性、功能、事件和监测数据
        statistic.setPropertyCount(thingsCount.getPropertyCount());
        statistic.setFunctionCount(thingsCount.getFunctionCount());
        statistic.setEventCount(thingsCount.getEventCount());
        statistic.setMonitorCount(thingsCount.getMonitorCount());
        return statistic;
    }

    /**
     * 根据设备编号查询设备
     *
     * @param serialNumber 设备主键
     * @return 设备
     */
    @Override
    public Device selectDeviceBySerialNumber(String serialNumber) {
        Device device = deviceMapper.selectDeviceBySerialNumber(serialNumber);
        // redis中获取设备状态（物模型值）
        device.setThingsModelValue(JSONObject.toJSONString(getCacheDeviceStatus(device.getProductId(), device.getSerialNumber())));
        return device;
    }

    /**
     * 根据设备编号查询简洁设备
     *
     * @param serialNumber 设备主键
     * @return 设备
     */
    @Override
    public Device selectShortDeviceBySerialNumber(String serialNumber) {
        Device device = deviceMapper.selectShortDeviceBySerialNumber(serialNumber);
        // redis中获取设备状态（物模型值）
        device.setThingsModelValue(JSONObject.toJSONString(getCacheDeviceStatus(device.getProductId(), device.getSerialNumber())));
        return device;
    }

    /**
     * 根据设备编号查询设备认证信息
     *
     * @param model 设备编号和产品ID
     * @return 设备
     */
    @Override
    public ProductAuthenticateModel selectProductAuthenticate(AuthenticateInputModel model) {
        return deviceMapper.selectProductAuthenticate(model);
    }

    /**
     * 更新设备的物模型
     *
     * @param input    设备ID和物模型值
     * @param type     1=属性 2=功能 3=事件
     * @param isShadow 是否影子值
     * @return 设备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reportDeviceThingsModelValue(ThingsModelValuesInput input, int type, boolean isShadow) {
        String key = devicePreKey + input.getProductId() + "_" + input.getDeviceNumber();
        Map<String, String> maps = new HashMap<String, String>();
        for (int i = 0; i < input.getThingsModelValueRemarkItem().size(); i++) {
            String id = input.getThingsModelValueRemarkItem().get(i).getId();
            String value = input.getThingsModelValueRemarkItem().get(i).getValue();
            String isMonitor = "";
            // 数组元素的值，通过前缀中的索引更新，数组元素格式：array_01_parentId_humidity
            if (id.startsWith("array_")) {
                int index = Integer.parseInt(id.substring(6, 8));
                String identity = id.substring(9);
                // 查询redis中是否存在对应物模型
                isMonitor = redisCache.getCacheMapValue(key, identity + "#M");
                // 跳过不存在的物模型ID
                if (isMonitor == null || "".equals(isMonitor)) {
                    continue;
                }
                // 设置值，获取数组值，然后替换其中元素
                if (!isShadow) {
                    String[] values = redisCache.getCacheMapValue(key, identity + "#V").toString().split(",");
                    values[index] = value;
                    maps.put(identity + "#V", String.join(",", values));
                }
                // 设置影子值
                String[] shadows = redisCache.getCacheMapValue(key, identity + "#S").toString().split(",");
                shadows[index] = value;
                maps.put(identity + "#S", String.join(",", shadows));
            } else {
                // 查询redis中是否存在对应物模型
                isMonitor = redisCache.getCacheMapValue(key, input.getThingsModelValueRemarkItem().get(i).getId() + "#M");
                // 跳过不存在的物模型ID
                if (isMonitor == null || "".equals(isMonitor)) {
                    continue;
                }
                // 设置值
                if (!isShadow) {
                    maps.put(input.getThingsModelValueRemarkItem().get(i).getId() + "#V", value);
                }
                // 设置影子值
                maps.put(input.getThingsModelValueRemarkItem().get(i).getId() + "#S", value);
            }

            // 添加到设备日志
            DeviceLog deviceLog = new DeviceLog();
            deviceLog.setSerialNumber(input.getDeviceNumber());
            deviceLog.setLogType(type);
            // 1=影子模式，2=在线模式，3=其他
            deviceLog.setMode(isShadow ? 1 : 2);
            // 设备日志值
            deviceLog.setLogValue(value);
            deviceLog.setRemark(input.getThingsModelValueRemarkItem().get(i).getRemark());
            deviceLog.setIdentity(input.getThingsModelValueRemarkItem().get(i).getId());
            deviceLog.setIsMonitor(Integer.parseInt(isMonitor));
            deviceLog.setCreateTime(DateUtils.getNowDate());
            logService.saveDeviceLog(deviceLog);
        }
        redisCache.hashPutAll(key, maps);
        return 1;
    }

    /**
     * 查询设备列表
     *
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<Device> selectDeviceList(Device device) {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleKey().equals("tenant")) {
                // 租户查看产品下所有设备
                device.setTenantId(user.getUserId());
            } else if (roles.get(i).getRoleKey().equals("general")) {
                // 用户查看自己设备
                device.setUserId(user.getUserId());
            }
        }
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 查询未分配授权码设备列表
     *
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<Device> selectUnAuthDeviceList(Device device) {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleKey().equals("tenant")) {
                // 租户查看产品下所有设备
                device.setTenantId(user.getUserId());
            } else if (roles.get(i).getRoleKey().equals("general")) {
                // 用户查看自己设备
                device.setUserId(user.getUserId());
            }
        }
        return deviceMapper.selectUnAuthDeviceList(device);
    }

    /**
     * 查询分组可添加设备分页列表（分组用户与设备用户匹配）
     *
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<Device> selectDeviceListByGroup(Device device) {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleKey().equals("tenant")) {
                // 租户查看产品下所有设备
                device.setTenantId(user.getUserId());
            } else if (roles.get(i).getRoleKey().equals("general")) {
                // 用户查看自己设备
                device.setUserId(user.getUserId());
            }
        }
        return deviceMapper.selectDeviceListByGroup(device);
    }

    /**
     * 查询所有设备简短列表
     *
     * @return 设备
     */
    @Override
    public List<DeviceAllShortOutput> selectAllDeviceShortList() {
        Device device = new Device();
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (SysRole role : roles) {
            if (role.getRoleKey().equals("tenant")) {
                // 租户查看产品下所有设备
                device.setTenantId(user.getUserId());
                break;
            } else if (role.getRoleKey().equals("general")) {
                // 用户查看自己设备
                device.setUserId(user.getUserId());
                break;
            }
        }
        return deviceMapper.selectAllDeviceShortList(device);
    }

    /**
     * 查询设备分页简短列表
     *
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<DeviceShortOutput> selectDeviceShortList(Device device) {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleKey().equals("tenant")) {
                // 租户查看产品下所有设备
                device.setTenantId(user.getUserId());
                break;
            } else if (roles.get(i).getRoleKey().equals("general")) {
                // 用户查看自己设备
                device.setUserId(user.getUserId());
                break;
            }
        }
        List<DeviceShortOutput> deviceList = deviceMapper.selectDeviceShortList(device);
        return deviceList;
    }

    /**
     * 查询设备
     *
     * @param deviceId 设备主键
     * @return 设备
     */
    @Override
    public DeviceShortOutput selectDeviceRunningStatusByDeviceId(Long deviceId) {
        DeviceShortOutput device = deviceMapper.selectDeviceRunningStatusByDeviceId(deviceId);
        JSONObject thingsModelObject = JSONObject.parseObject(thingsModelService.getCacheThingsModelByProductId(device.getProductId()));
        JSONArray properties = thingsModelObject.getJSONArray("properties");
        JSONArray functions = thingsModelObject.getJSONArray("functions");
        List<ThingsModelValueItem> thingsModelValueItems = getCacheDeviceStatus(device.getProductId(), device.getSerialNumber());
        // 物模型转换赋值
        List<ThingsModel> thingsPropertyList = convertJsonToThingsList(properties, thingsModelValueItems, false, 1);
        List<ThingsModel> thingsFunctionList = convertJsonToThingsList(functions, thingsModelValueItems, false, 2);
        thingsPropertyList.addAll(thingsFunctionList);
        // 排序
        thingsPropertyList = thingsPropertyList.stream().sorted(Comparator.comparing(ThingsModel::getOrder).reversed()).collect(Collectors.toList());
        device.setThingsModels(thingsPropertyList);
        device.setThingsModelValue("");
        return device;
    }

    /**
     * 物模型基本类型转换赋值
     *
     * @param jsonArray
     * @param thingsModelValues
     * @param isOnlyTop
     * @param type
     * @return
     */
    @Async
    public List<ThingsModel> convertJsonToThingsList(JSONArray jsonArray, List<ThingsModelValueItem> thingsModelValues, boolean isOnlyTop, Integer type) {
        List<ThingsModel> thingsModelList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            ThingsModel thingsModel = new ThingsModel();
            JSONObject thingsJson = jsonArray.getJSONObject(i);
            JSONObject datatypeJson = thingsJson.getJSONObject("datatype");
            thingsModel.setIsTop(thingsJson.getInteger("isTop"));
            // 只显示isTop数据
            if (thingsModel.getIsTop() == 0 && isOnlyTop == true) {
                continue;
            }
            thingsModel.setId(thingsJson.getString("id"));
            thingsModel.setName(thingsJson.getString("name"));
            thingsModel.setIsMonitor(thingsJson.getInteger("isMonitor") == null ? 0 : thingsJson.getInteger("isMonitor"));
            thingsModel.setIsReadonly(thingsJson.getInteger("isReadonly") == null ? 0 : thingsJson.getInteger("isReadonly"));
            thingsModel.setOrder(thingsJson.getInteger("order") == null ? 0 : thingsJson.getInteger("order"));
            thingsModel.setType(type);
            // 获取value
            for (int j = 0; j < thingsModelValues.size(); j++) {
                if (thingsModelValues.get(j).getId().equals(thingsModel.getId())) {
                    thingsModel.setValue(thingsModelValues.get(j).getValue());
                    thingsModel.setShadow(thingsModelValues.get(j).getShadow());
                    break;
                }
            }
            // json转DataType(DataType赋值)
            DataType dataType = convertJsonToDataType(datatypeJson, thingsModelValues, isOnlyTop, type, thingsModel.getId() + "_");
            thingsModel.setDataType(dataType);
            // 物模型项添加到集合
            thingsModelList.add(thingsModel);
        }
        return thingsModelList;
    }

    /**
     * 物模型DataType转换
     *
     * @param datatypeJson
     * @param thingsModelValues
     * @param isOnlyTop
     * @param type
     * @param parentIdentifier  上级标识符
     * @return
     */
    private DataType convertJsonToDataType(JSONObject datatypeJson, List<ThingsModelValueItem> thingsModelValues, boolean isOnlyTop, Integer type, String parentIdentifier) {
        DataType dataType = new DataType();
        dataType.setType(datatypeJson.getString("type"));
        if (dataType.getType().equals("decimal")) {
            dataType.setMax(datatypeJson.getBigDecimal("max"));
            dataType.setMin(datatypeJson.getBigDecimal("min"));
            dataType.setStep(datatypeJson.getBigDecimal("step"));
            dataType.setUnit(datatypeJson.getString("unit"));
        } else if (dataType.getType().equals("integer")) {
            dataType.setMax(datatypeJson.getBigDecimal("max"));
            dataType.setMin(datatypeJson.getBigDecimal("min"));
            dataType.setStep(datatypeJson.getBigDecimal("step"));
            dataType.setUnit(datatypeJson.getString("unit"));
        } else if (dataType.getType().equals("bool")) {
            dataType.setFalseText(datatypeJson.getString("falseText"));
            dataType.setTrueText(datatypeJson.getString("trueText"));
        } else if (dataType.getType().equals("string")) {
            dataType.setMaxLength(datatypeJson.getInteger("maxLength"));
        } else if (dataType.getType().equals("enum")) {
            List<EnumItem> enumItemList = JSONObject.parseArray(datatypeJson.getString("enumList"), EnumItem.class);
            dataType.setEnumList(enumItemList);
        } else if (dataType.getType().equals("object")) {
            JSONArray jsonArray = JSON.parseArray(datatypeJson.getString("params"));
            // 物模型值过滤（parentId_开头）
            thingsModelValues = thingsModelValues.stream().filter(x -> x.getId().startsWith(parentIdentifier)).collect(Collectors.toList());
            List<ThingsModel> thingsList = convertJsonToThingsList(jsonArray, thingsModelValues, isOnlyTop, type);
            // 排序
            thingsList = thingsList.stream().sorted(Comparator.comparing(ThingsModel::getOrder).reversed()).collect(Collectors.toList());
            dataType.setParams(thingsList);
        } else if (dataType.getType().equals("array")) {
            dataType.setArrayType(datatypeJson.getString("arrayType"));
            dataType.setArrayCount(datatypeJson.getInteger("arrayCount"));
            if ("object".equals(dataType.getArrayType())) {
                // 对象数组
                JSONArray jsonArray = datatypeJson.getJSONArray("params");
                // 物模型值过滤（parentId_开头）
                thingsModelValues = thingsModelValues.stream().filter(x -> x.getId().startsWith(parentIdentifier)).collect(Collectors.toList());
                List<ThingsModel> thingsList = convertJsonToThingsList(jsonArray, thingsModelValues, isOnlyTop, type);
                // 排序
                thingsList = thingsList.stream().sorted(Comparator.comparing(ThingsModel::getOrder).reversed()).collect(Collectors.toList());
                // 数组类型物模型里面对象赋值
                List<ThingsModel>[] arrayParams=new List[dataType.getArrayCount()];
                for(int i=0;i<dataType.getArrayCount();i++){
                    List<ThingsModel> thingsModels=new ArrayList<>();
                    for(int j=0;j<thingsList.size();j++){
                        ThingsModel thingsModel=new ThingsModel();
                        BeanUtils.copyProperties( thingsList.get(j),thingsModel);
                        String[] shadows = thingsList.get(j).getShadow().split(",");
                        String[] values = thingsList.get(j).getValue().split(",");
                        thingsModel.setValue(values[i]);
                        thingsModel.setShadow(shadows[i]);
                        thingsModels.add(thingsModel);
                    }
                    arrayParams[i]=thingsModels;
                }
                dataType.setArrayParams(arrayParams);
            }
        }
        return dataType;
    }

    /**
     * Json物模型集合转换为对象中的分类集合（1.3.1版本后不再使用）
     *
     * @param jsonArray         物模型集合
     * @param isOnlyTop         是否只显示置顶数据
     * @param device            设备
     * @param thingsModelValues 物模型值
     * @param type              1=属性 2=功能
     */
    @Async
    @Obsolete
    public void convertJsonToCategoryList(JSONArray jsonArray, DeviceShortOutput device, boolean isOnlyTop, List<ThingsModelValueItem> thingsModelValues, Integer type) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject thingsJson = jsonArray.getJSONObject(i);
            JSONObject datatypeJson = thingsJson.getJSONObject("datatype");
            ThingsModelItemBase thingsModel = new ThingsModelItemBase();
            thingsModel.setIsTop(thingsJson.getInteger("isTop"));
            // 只显示isTop数据
            if (thingsModel.getIsTop() == 0 && isOnlyTop == true) {
                continue;
            }

            thingsModel.setId(thingsJson.getString("id"));
            thingsModel.setName(thingsJson.getString("name"));
            thingsModel.setIsMonitor(thingsJson.getInteger("isMonitor") == null ? 0 : thingsJson.getInteger("isMonitor"));
            thingsModel.setDataType(datatypeJson.getString("type"));
            thingsModel.setType(type);
            thingsModel.setValue("");
            // 获取value
            for (int j = 0; j < thingsModelValues.size(); j++) {
                if (thingsModelValues.get(j).getId().equals(thingsModel.getId())) {
                    String value = thingsModelValues.get(j).getValue();
                    String shadow = thingsModelValues.get(j).getShadow();
                    thingsModel.setValue(value);
                    thingsModel.setShadow(shadow);
                    // bool 类型默认值为0，解决移动端报错问题
                    if (thingsModel.getDataType().equals("bool")) {
                        thingsModel.setValue(value.equals("") ? "0" : value);
                        thingsModel.setShadow(shadow.equals("") ? "0" : shadow);
                    }
                    break;
                }
            }
            // 根据分类不同，存储到不同集合
            if (datatypeJson.getString("type").equals("decimal")) {
                DecimalModelOutput model = new DecimalModelOutput();
                BeanUtils.copyProperties(thingsModel, model);
                model.setMax(datatypeJson.getBigDecimal("max"));
                model.setMin(datatypeJson.getBigDecimal("min"));
                model.setStep(datatypeJson.getBigDecimal("step"));
                model.setUnit(datatypeJson.getString("unit"));
                if (model.getIsMonitor() == 1) {
                    ReadOnlyModelOutput readonlyModel = new ReadOnlyModelOutput();
                    BeanUtils.copyProperties(model, readonlyModel);
                    device.getReadOnlyList().add(readonlyModel);
                } else {
                    device.getDecimalList().add(model);
                }
            } else if (datatypeJson.getString("type").equals("integer")) {
                IntegerModelOutput model = new IntegerModelOutput();
                BeanUtils.copyProperties(thingsModel, model);
                model.setMax(datatypeJson.getBigDecimal("max"));
                model.setMin(datatypeJson.getBigDecimal("min"));
                model.setStep(datatypeJson.getBigDecimal("step"));
                model.setUnit(datatypeJson.getString("unit"));
                if (model.getIsMonitor() == 1) {
                    ReadOnlyModelOutput readonlyModel = new ReadOnlyModelOutput();
                    BeanUtils.copyProperties(model, readonlyModel);
                    device.getReadOnlyList().add(readonlyModel);
                } else {
                    device.getIntegerList().add(model);
                }
            } else if (datatypeJson.getString("type").equals("bool")) {
                BoolModelOutput model = new BoolModelOutput();
                BeanUtils.copyProperties(thingsModel, model);
                model.setFalseText(datatypeJson.getString("falseText"));
                model.setTrueText(datatypeJson.getString("trueText"));
                if (model.getIsMonitor() == 1) {
                    ReadOnlyModelOutput readonlyModel = new ReadOnlyModelOutput();
                    BeanUtils.copyProperties(model, readonlyModel);
                    device.getReadOnlyList().add(readonlyModel);
                } else {
                    device.getBoolList().add(model);
                }
            } else if (datatypeJson.getString("type").equals("string")) {
                StringModelOutput model = new StringModelOutput();
                BeanUtils.copyProperties(thingsModel, model);
                model.setMaxLength(datatypeJson.getInteger("maxLength"));
                if (model.getIsMonitor() == 1) {
                    ReadOnlyModelOutput readonlyModel = new ReadOnlyModelOutput();
                    BeanUtils.copyProperties(model, readonlyModel);
                    device.getReadOnlyList().add(readonlyModel);
                } else {
                    device.getStringList().add(model);
                }
            } else if (datatypeJson.getString("type").equals("array")) {
                ArrayModelOutput model = new ArrayModelOutput();
                BeanUtils.copyProperties(thingsModel, model);
                model.setArrayType(datatypeJson.getString("arrayType"));
                model.setArrayCount(datatypeJson.getInteger("arrayCount"));
                if (model.getIsMonitor() == 1) {
                    ReadOnlyModelOutput readonlyModel = new ReadOnlyModelOutput();
                    BeanUtils.copyProperties(model, readonlyModel);
                    device.getReadOnlyList().add(readonlyModel);
                } else {
                    device.getArrayList().add(model);
                }
            } else if (datatypeJson.getString("type").equals("enum")) {
                EnumModelOutput model = new EnumModelOutput();
                BeanUtils.copyProperties(thingsModel, model);
                List<EnumItemOutput> enumItemList = JSONObject.parseArray(datatypeJson.getString("enumList"), EnumItemOutput.class);
                model.setEnumList(enumItemList);
                if (model.getIsMonitor() == 1) {
                    ReadOnlyModelOutput readonlyModel = new ReadOnlyModelOutput();
                    BeanUtils.copyProperties(model, readonlyModel);
                    device.getReadOnlyList().add(readonlyModel);
                } else {
                    device.getEnumList().add(model);
                }
            } else if (datatypeJson.getString("type").equals("object")) {

            }
        }
        // 排序
        device.setReadOnlyList(device.getReadOnlyList().stream().sorted(Comparator.comparing(ThingsModelItemBase::getIsMonitor).reversed()).collect(Collectors.toList()));
    }

    /**
     * 新增设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Device insertDevice(Device device) {
        // 设备编号唯一检查
        Device existDevice = deviceMapper.selectDeviceBySerialNumber(device.getSerialNumber());
        if (existDevice != null) {
            log.error("设备编号：" + device.getSerialNumber() + "已经存在了，新增设备失败");
            return device;
        }
        SysUser sysUser = getLoginUser().getUser();
        //添加设备
        device.setCreateTime(DateUtils.getNowDate());
        device.setUserId(sysUser.getUserId());
        device.setUserName(sysUser.getUserName());
        device.setRssi(0);

        // 设置租户
        Product product = productService.selectProductByProductId(device.getProductId());
        device.setTenantId(product.getTenantId());
        device.setTenantName(product.getTenantName());
        device.setImgUrl(product.getImgUrl());
        // 随机经纬度和地址
        SysUser user = getLoginUser().getUser();
        device.setNetworkIp(user.getLoginIp());
        setLocation(user.getLoginIp(), device);

        deviceMapper.insertDevice(device);
        // 添加设备用户
        DeviceUser deviceUser = new DeviceUser();
        deviceUser.setUserId(sysUser.getUserId());
        deviceUser.setUserName(sysUser.getUserName());
        deviceUser.setPhonenumber(sysUser.getPhonenumber());
        deviceUser.setDeviceId(device.getDeviceId());
        deviceUser.setDeviceName(device.getDeviceName());
        deviceUser.setTenantId(product.getTenantId());
        deviceUser.setTenantName(product.getTenantName());
        deviceUser.setIsOwner(1);
        deviceUser.setCreateTime(DateUtils.getNowDate());
        deviceUserMapper.insertDeviceUser(deviceUser);

        // redis缓存设备默认状态（物模型值）
        cacheDeviceStatus(device.getProductId(), device.getSerialNumber());
        return device;
    }

    /**
     * 设备关联用户
     *
     * @param deviceRelateUserInput
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deviceRelateUser(DeviceRelateUserInput deviceRelateUserInput) {
        // 查询用户信息
        SysUser sysUser = userService.selectUserById(deviceRelateUserInput.getUserId());
        for (int i = 0; i < deviceRelateUserInput.getDeviceNumberAndProductIds().size(); i++) {
            Device existDevice = deviceMapper.selectDeviceBySerialNumber(deviceRelateUserInput.getDeviceNumberAndProductIds().get(i).getDeviceNumber());
            if (existDevice != null) {
                if (existDevice.getUserId().longValue() == deviceRelateUserInput.getUserId().longValue()) {
                    return AjaxResult.error("用户已经拥有设备：" + existDevice.getDeviceName() + ", 设备编号：" + existDevice.getSerialNumber());
                }
                // 先删除设备的所有用户
                deviceUserMapper.deleteDeviceUserByDeviceId(new UserIdDeviceIdModel(null, existDevice.getDeviceId()));
                // 添加新的设备用户
                DeviceUser deviceUser = new DeviceUser();
                deviceUser.setUserId(sysUser.getUserId());
                deviceUser.setUserName(sysUser.getUserName());
                deviceUser.setPhonenumber(sysUser.getPhonenumber());
                deviceUser.setDeviceId(existDevice.getDeviceId());
                deviceUser.setDeviceName(existDevice.getDeviceName());
                deviceUser.setTenantId(existDevice.getTenantId());
                deviceUser.setTenantName(existDevice.getTenantName());
                deviceUser.setIsOwner(1);
                deviceUser.setCreateTime(DateUtils.getNowDate());
                deviceUserMapper.insertDeviceUser(deviceUser);
                // 更新设备用户信息
                existDevice.setUserId(deviceRelateUserInput.getUserId());
                existDevice.setUserName(sysUser.getUserName());
                deviceMapper.updateDevice(existDevice);
            } else {
                // 自动添加设备
                int result = insertDeviceAuto(
                        deviceRelateUserInput.getDeviceNumberAndProductIds().get(i).getDeviceNumber(),
                        deviceRelateUserInput.getUserId(),
                        deviceRelateUserInput.getDeviceNumberAndProductIds().get(i).getProductId());
                if (result == 0) {
                    return AjaxResult.error("设备不存在，自动添加设备时失败，请检查产品编号是否正确");
                }
            }
        }
        return AjaxResult.success("添加设备成功");
    }

    /**
     * 设备认证后自动添加设备
     *
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDeviceAuto(String serialNumber, Long userId, Long productId) {
        // 设备编号唯一检查
        int count = deviceMapper.selectDeviceCountBySerialNumber(serialNumber);
        if (count != 0) {
            log.error("设备编号：" + serialNumber + "已经存在了，新增设备失败");
            return 0;
        }
        Device device = new Device();
        device.setSerialNumber(serialNumber);
        SysUser user = userService.selectUserById(userId);
        device.setUserId(userId);
        device.setUserName(user.getUserName());
        device.setFirmwareVersion(BigDecimal.valueOf(1.0));
        // 设备状态（1-未激活，2-禁用，3-在线，4-离线）
        device.setStatus(1);
        device.setActiveTime(DateUtils.getNowDate());
        device.setIsShadow(0);
        device.setRssi(0);
        // 1-自动定位，2-设备定位，3-自定义位置
        device.setLocationWay(1);
        device.setCreateTime(DateUtils.getNowDate());
        // 随机位置
        device.setLongitude(BigDecimal.valueOf(116.23 - (Math.random() * 15)));
        device.setLatitude(BigDecimal.valueOf(39.54 - (Math.random() * 15)));
        device.setNetworkAddress("中国");
        device.setNetworkIp("127.0.0.1");
        // 设置租户
        Product product = productService.selectProductByProductId(productId);
        if (product == null) {
            log.error("自动添加设备时，根据产品ID查找不到对应产品");
            return 0;
        }
        int random = (int) (Math.random() * (90)) + 10;
        device.setDeviceName(product.getProductName() + random);
        device.setTenantId(product.getTenantId());
        device.setTenantName(product.getTenantName());
        device.setImgUrl(product.getImgUrl());
        device.setProductId(product.getProductId());
        device.setProductName(product.getProductName());
        deviceMapper.insertDevice(device);

        // 缓存设备状态
        cacheDeviceStatus(device.getProductId(), device.getSerialNumber());

        // 添加设备用户
        DeviceUser deviceUser = new DeviceUser();
        deviceUser.setUserId(userId);
        deviceUser.setUserName(user.getUserName());
        deviceUser.setPhonenumber(user.getPhonenumber());
        deviceUser.setDeviceId(device.getDeviceId());
        deviceUser.setDeviceName(device.getDeviceName());
        deviceUser.setTenantId(product.getTenantId());
        deviceUser.setTenantName(product.getTenantName());
        deviceUser.setIsOwner(1);
        return deviceUserMapper.insertDeviceUser(deviceUser);
    }

    /**
     * 获取用户操作设备的影子值
     *
     * @param device
     * @return
     */
    @Override
    public ThingsModelShadow getDeviceShadowThingsModel(Device device) {
        // 物模型
        String thingsModels = thingsModelService.getCacheThingsModelByProductId(device.getProductId());
        JSONObject thingsModelObject = JSONObject.parseObject(thingsModels);
        JSONArray properties = thingsModelObject.getJSONArray("properties");
        JSONArray functions = thingsModelObject.getJSONArray("functions");

        // 物模型值
        List<ThingsModelValueItem> thingsModelValueItems = getCacheDeviceStatus(device.getProductId(), device.getSerialNumber());

        // 查询出设置的影子值
        List<ThingsModelValueItem> shadowList = new ArrayList<>();
        for (int i = 0; i < thingsModelValueItems.size(); i++) {
            if (!thingsModelValueItems.get(i).getValue().equals(thingsModelValueItems.get(i).getShadow())) {
                shadowList.add(thingsModelValueItems.get(i));
                System.out.println("添加影子：" + thingsModelValueItems.get(i).getId());
            }
        }
        ThingsModelShadow shadow = new ThingsModelShadow();
        for (int i = 0; i < shadowList.size(); i++) {
            boolean isGetValue = false;
            for (int j = 0; j < properties.size(); j++) {
                if (properties.getJSONObject(j).getInteger("isMonitor") == 0 && properties.getJSONObject(j).getString("id").equals(shadowList.get(i).getId())) {
                    ThingsModelSimpleItem item = new ThingsModelSimpleItem(shadowList.get(i).getId(), shadowList.get(i).getShadow(), "");
                    shadow.getProperties().add(item);
                    System.out.println("添加影子属性：" + item.getId());
                    isGetValue = true;
                    break;
                }
            }
            if (!isGetValue) {
                for (int k = 0; k < functions.size(); k++) {
                    if (functions.getJSONObject(k).getString("id").equals(shadowList.get(i).getId())) {
                        ThingsModelSimpleItem item = new ThingsModelSimpleItem(shadowList.get(i).getId(), shadowList.get(i).getShadow(), "");
                        shadow.getFunctions().add(item);
                        System.out.println("添加影子功能：" + item.getId());
                        break;
                    }
                }
            }
        }
        return shadow;
    }


    /**
     * 修改设备
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    public AjaxResult updateDevice(Device device) {
        // 设备编号唯一检查
        Device oldDevice = deviceMapper.selectDeviceByDeviceId(device.getDeviceId());
        if (!oldDevice.getSerialNumber().equals(device.getSerialNumber())) {
            Device existDevice = deviceMapper.selectDeviceBySerialNumber(device.getSerialNumber());
            if (existDevice != null) {
                log.error("设备编号：" + device.getSerialNumber() + " 已经存在，新增设备失败");
                return AjaxResult.success("设备编号：" + device.getSerialNumber() + " 已经存在，修改失败", 0);
            }
        }
        device.setUpdateTime(DateUtils.getNowDate());
        // 未激活状态,可以修改产品以及物模型值
        if (device.getStatus() == 1) {
            // 缓存设备状态（物模型值）
            cacheDeviceStatus(device.getProductId(), device.getSerialNumber());

        } else {
            device.setProductId(null);
            device.setProductName(null);
        }
        deviceMapper.updateDevice(device);
        // 更新前设备状态为禁用，启用后状态默认为离线，满足时下发获取设备最新状态指令
        if (oldDevice.getStatus() == 2 && device.getStatus() == 4) {
            // 发布设备信息，设备收到该消息后上报最新状态
            emqxService.publishInfo(oldDevice.getProductId(), oldDevice.getSerialNumber());
        }
        return AjaxResult.success("修改成功", 1);
    }

    /**
     * 生成设备唯一编号
     *
     * @return 结果
     */
    @Override
    public String generationDeviceNum() {
        // 设备编号：D + userId + 15位随机字母和数字
        SysUser user = getLoginUser().getUser();
        String number = "D" + user.getUserId().toString() + toolService.getStringRandom(10);
        int count = deviceMapper.getDeviceNumCount(number);
        if (count == 0) {
            return number;
        } else {
            generationDeviceNum();
        }
        return "";
    }

    /**
     * @param device 设备状态和定位更新
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDeviceStatusAndLocation(Device device, String ipAddress) {
        // 设置自动定位和状态
        if (ipAddress != "") {
            if (device.getActiveTime() == null) {
                device.setActiveTime(DateUtils.getNowDate());
            }
            // 定位方式(1=ip自动定位，2=设备定位，3=自定义)
            if (device.getLocationWay() == 1) {
                device.setNetworkIp(ipAddress);
                setLocation(ipAddress, device);
            }
        }
        int result = deviceMapper.updateDeviceStatus(device);

        // 添加到设备日志
        DeviceLog deviceLog = new DeviceLog();
        deviceLog.setDeviceId(device.getDeviceId());
        deviceLog.setDeviceName(device.getDeviceName());
        deviceLog.setSerialNumber(device.getSerialNumber());
        deviceLog.setIsMonitor(0);
        deviceLog.setUserId(device.getUserId());
        deviceLog.setUserName(device.getUserName());
        deviceLog.setTenantId(device.getTenantId());
        deviceLog.setTenantName(device.getTenantName());
        deviceLog.setCreateTime(DateUtils.getNowDate());
        // 日志模式 1=影子模式，2=在线模式，3=其他
        deviceLog.setMode(3);
        if (device.getStatus() == 3) {
            deviceLog.setLogValue("1");
            deviceLog.setRemark("设备上线");
            deviceLog.setIdentity("online");
            deviceLog.setLogType(5);
        } else if (device.getStatus() == 4) {
            deviceLog.setLogValue("0");
            deviceLog.setRemark("设备离线");
            deviceLog.setIdentity("offline");
            deviceLog.setLogType(6);
        }
        logService.saveDeviceLog(deviceLog);
        return result;
    }

    /**
     * @param device 设备状态
     * @return 结果
     */
    @Override
    public int updateDeviceStatus(Device device) {
        return deviceMapper.updateDeviceStatus(device);
    }

    /**
     * 上报设备信息
     *
     * @param device 设备
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reportDevice(Device device, Device deviceEntity) {
        // 未采用设备定位则清空定位，定位方式(1=ip自动定位，2=设备定位，3=自定义)
        if (deviceEntity.getLocationWay() != 2) {
            device.setLatitude(null);
            device.setLongitude(null);
        }
        int result = 0;
        if (deviceEntity != null) {
            // 通过配网或者扫码关联设备后，设备的用户信息需要变更
            if (deviceEntity.getUserId().longValue() != device.getUserId().longValue()) {
                // 先删除设备的所有用户
                deviceUserMapper.deleteDeviceUserByDeviceId(new UserIdDeviceIdModel(null, deviceEntity.getDeviceId()));
                // 添加新的设备用户
                SysUser sysUser = userService.selectUserById(device.getUserId());
                DeviceUser deviceUser = new DeviceUser();
                deviceUser.setUserId(sysUser.getUserId());
                deviceUser.setUserName(sysUser.getUserName());
                deviceUser.setPhonenumber(sysUser.getPhonenumber());
                deviceUser.setDeviceId(deviceEntity.getDeviceId());
                deviceUser.setDeviceName(deviceEntity.getDeviceName());
                deviceUser.setTenantId(deviceEntity.getTenantId());
                deviceUser.setTenantName(deviceEntity.getTenantName());
                deviceUser.setIsOwner(1);
                deviceUser.setCreateTime(DateUtils.getNowDate());
                deviceUserMapper.insertDeviceUser(deviceUser);
                // 更新设备用户信息
                device.setUserId(device.getUserId());
                device.setUserName(sysUser.getUserName());
            }
            device.setUpdateTime(DateUtils.getNowDate());
            if (deviceEntity.getActiveTime() == null || deviceEntity.getActiveTime().equals("")) {
                device.setActiveTime(DateUtils.getNowDate());
            }
            // 不更新物模型
            device.setThingsModelValue(null);
            result = deviceMapper.updateDeviceBySerialNumber(device);
        }
        return result;
    }

    /**
     * 重置设备状态
     *
     * @return 结果
     */
    @Override
    public int resetDeviceStatus(String deviceNum) {
        int result = deviceMapper.resetDeviceStatus(deviceNum);
        return result;
    }

    /**
     * 删除设备
     *
     * @param deviceId 需要删除的设备主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDeviceByDeviceId(Long deviceId) throws SchedulerException {
        SysUser user = getLoginUser().getUser();
        // 是否为普通用户，普通用户如果不是设备所有者，只能删除设备用户和用户自己的设备关联分组信息
        boolean isGeneralUser = false;
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getRoleKey().equals("general")) {
                isGeneralUser = true;
                break;
            }
        }
        Device device = deviceMapper.selectDeviceByDeviceId(deviceId);
        if (isGeneralUser && device.getUserId().longValue() != user.getUserId()) {
            // 删除用户分组中的设备 普通用户，且不是设备所有者。
            deviceMapper.deleteDeviceGroupByDeviceId(new UserIdDeviceIdModel(user.getUserId(), deviceId));
            // 删除用户的设备用户信息。
            deviceUserMapper.deleteDeviceUserByDeviceId(new UserIdDeviceIdModel(user.getUserId(), deviceId));
        } else {
            // 删除设备分组。  租户、管理员和设备所有者
            deviceMapper.deleteDeviceGroupByDeviceId(new UserIdDeviceIdModel(null, deviceId));
            // 删除设备用户。
            deviceUserMapper.deleteDeviceUserByDeviceId(new UserIdDeviceIdModel(null, deviceId));
            // 删除定时任务
            deviceJobService.deleteJobByDeviceIds(new Long[]{deviceId});
            // 批量删除设备日志
            logService.deleteDeviceLogByDeviceNumber(device.getSerialNumber());
            // 批量删除设备告警记录
            alertLogService.deleteAlertLogBySerialNumber(device.getSerialNumber());
            // redis中删除设备物模型（状态）
            String key = devicePreKey + device.getProductId() + "_" + device.getSerialNumber();
            redisCache.deleteObject(key);
            // 删除设备
            deviceMapper.deleteDeviceByDeviceIds(new Long[]{deviceId});
        }
        return 1;
    }

    /**
     * 根据IP获取地址
     *
     * @param ip
     * @return
     */
    private void setLocation(String ip, Device device) {
        String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
        String address = "未知地址";
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            device.setNetworkAddress("内网IP");
        }
        try {
            String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
            if (!StringUtils.isEmpty(rspStr)) {
                JSONObject obj = JSONObject.parseObject(rspStr);
                device.setNetworkAddress(obj.getString("addr"));
                System.out.println(device.getSerialNumber() + "- 设置地址：" + obj.getString("addr"));
                // 查询经纬度
                setLatitudeAndLongitude(obj.getString("city"), device);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设置经纬度
     *
     * @param city
     */
    private void setLatitudeAndLongitude(String city, Device device) {
        String BAIDU_URL = "https://api.map.baidu.com/geocoder";
        String baiduResponse = HttpUtils.sendGet(BAIDU_URL, "address=" + city + "&output=json", Constants.GBK);
        if (!StringUtils.isEmpty(baiduResponse)) {
            JSONObject baiduObject = JSONObject.parseObject(baiduResponse);
            JSONObject location = baiduObject.getJSONObject("result").getJSONObject("location");
            device.setLongitude(location.getBigDecimal("lng"));
            device.setLatitude(location.getBigDecimal("lat"));
            System.out.println(device.getSerialNumber() + "- 设置经度：" + location.getBigDecimal("lng") + "，设置纬度：" + location.getBigDecimal("lat"));
        }
    }

    /**
     * 缓存设备状态到redis
     *
     * @return
     */
    public List<ThingsModelValueItem> cacheDeviceStatus(Long productId, String serialNumber) {
        // 获取物模型,设置默认值
        String thingsModels = thingsModelService.getCacheThingsModelByProductId(productId);
        JSONObject thingsModelObject = JSONObject.parseObject(thingsModels);
        JSONArray properties = thingsModelObject.getJSONArray("properties");
        JSONArray functions = thingsModelObject.getJSONArray("functions");
        List<ThingsModelValueItem> valueList = properties.toJavaList(ThingsModelValueItem.class);
        valueList.addAll(functions.toJavaList(ThingsModelValueItem.class));
        // redis存储设备默认状态 键：产品ID_设备编号
        String key = devicePreKey + productId + "_" + serialNumber;
        Map<String, String> maps = new HashMap<>();
        for (int i = 0; i < valueList.size(); i++) {
            valueList.get(i).setValue("");
            valueList.get(i).setShadow("");
            // TODO：待测试
            if (valueList.get(i).getDataType().getType().equals("array")) {
                // 数组元素赋值（英文逗号分割的字符串,包含简单类型数组和对象类型数组，数组元素ID格式：array_01_humidity）
                String defaultValue = " ";
                if (valueList.get(i).getDataType().getArrayType().equals("object")) {
                    // 对象数组赋默认值
                    for (int k = 1; k < valueList.get(i).getDataType().getArrayCount(); k++) {
                        // 默认值需要保留为空格,便于解析字符串为数组
                        defaultValue = defaultValue + ", ";
                    }
                    for (int j = 0; j < valueList.get(i).getDataType().getParams().size(); j++) {
                        maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#V", defaultValue);
                        maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#S", defaultValue);
                        maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#N", valueList.get(i).getDataType().getParams().get(j).getName());
                        maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#M", String.valueOf(valueList.get(i).getDataType().getParams().get(j).getIsMonitor()));
                    }
                } else {
                    // 普通数组赋默认值
                    for (int k = 1; k < valueList.get(i).getDataType().getArrayCount(); k++) {
                        defaultValue = defaultValue + ", ";
                    }
                    maps.put(valueList.get(i).getId() + "#V", defaultValue);
                    maps.put(valueList.get(i).getId() + "#S", defaultValue);
                    maps.put(valueList.get(i).getId() + "#N", valueList.get(i).getName());
                    maps.put(valueList.get(i).getId() + "#M", String.valueOf(valueList.get(i).getIsMonitor()));
                }
            } else if (valueList.get(i).getDataType().getType().equals("object")) {
                // 对象类型赋默认值
                for (int j = 0; j < valueList.get(i).getDataType().getParams().size(); j++) {
                    maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#V", "");
                    maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#S", "");
                    maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#N", valueList.get(i).getDataType().getParams().get(j).getName());
                    maps.put(valueList.get(i).getDataType().getParams().get(j).getId() + "#M", String.valueOf(valueList.get(i).getDataType().getParams().get(j).getIsMonitor()));
                }
            } else {
                //其他类型类型赋默认值（integer、decimal、string、enum）
                maps.put(valueList.get(i).getId() + "#V", "");
                maps.put(valueList.get(i).getId() + "#S", "");
                maps.put(valueList.get(i).getId() + "#N", valueList.get(i).getName());
                maps.put(valueList.get(i).getId() + "#M", String.valueOf(valueList.get(i).getIsMonitor()));
            }
        }
        redisCache.hashPutAll(key, maps);
        return valueList;
    }

    /**
     * 获取Redis缓存的设备全部状态（物模型值）
     *
     * @param productId    产品ID
     * @param deviceNumber 设备编号
     * @return
     */
    private List<ThingsModelValueItem> getCacheDeviceStatus(Long productId, String deviceNumber) {
        Map<String, String> map = redisCache.hashEntity(devicePreKey + productId + "_" + deviceNumber);
        List<ThingsModelValueItem> valueList = new ArrayList<>();
        if (map == null || map.size() == 0) {
            // 缓存设备状态（物模型值）到redis
            valueList = cacheDeviceStatus(productId, deviceNumber);
        } else {
            // 获取redis缓存的物模型值
            valueList = mapToValueList(map, "#S");
        }
        return valueList;
    }

    /**
     * 批量查询设备的缓存状态（物模型值）
     *
     * @param deviceList
     * @param condition  条件：#S=影子值，#V=值，#N=名称，#M=是否监测数据，空字符=所有数据
     * @return
     */
    private Map<String, List<ThingsModelValueItem>> getBatchCacheDeviceStatus(List<DeviceShortOutput> deviceList, String condition) {
        // 批量查询hkey和value
        Set<String> set = new HashSet<>();
        for (int i = 0; i < deviceList.size(); i++) {
            set.add(devicePreKey + deviceList.get(i).getProductId() + "_" + deviceList.get(i).getSerialNumber());
        }
        // 只查询影子值
        Map<String, List<ThingsModelValueItem>> mapThingsModelValue = new HashMap<>();
        Map<String, Map> map = redisCache.hashGetAllByKeys(set, condition);
        map.forEach((k, v) -> {
            mapThingsModelValue.put(k, mapToValueList(v, "#S"));
        });
        // 如果redis键和设备不匹配，添加redis缓存
        if (map.size() != deviceList.size()) {
            for (int i = 0; i < deviceList.size(); i++) {
                String key = devicePreKey + deviceList.get(i).getProductId() + "_" + deviceList.get(i).getSerialNumber();
                if (map.get(key) != null) {
                    continue;
                } else {
                    mapThingsModelValue.put(key, getCacheDeviceStatus(deviceList.get(i).getProductId(), deviceList.get(i).getSerialNumber()));
                }
            }
        }
        return mapThingsModelValue;
    }

    /**
     * 设备状态Map转物模型值集合
     *
     * @param map
     * @param primaryIdentity 主要标识符 #S、#V、#M、#N
     * @return
     */
    private List<ThingsModelValueItem> mapToValueList(Map<String, String> map, String primaryIdentity) {
        List<ThingsModelValueItem> valueList = new ArrayList<>();
        for (Object key : map.keySet()) {
            if (key.toString().endsWith(primaryIdentity)) {
                ThingsModelValueItem item = new ThingsModelValueItem();
                // 物模型ID
                item.setId(key.toString().substring(0, key.toString().length() - 2));
                // 物模型名称
                String name = map.get(key.toString().substring(0, key.toString().length() - 1) + "N");
                if (name == null) {
                    name = "";
                }
                item.setName(name.replace("\"", ""));
                // 物模型值
                String value = map.get(key.toString().substring(0, key.toString().length() - 1) + "V");
                if (value == null) {
                    value = "";
                }
                item.setValue(value.replace("\"", ""));
                // 物模型值
                String shadow = map.get(key.toString().substring(0, key.toString().length() - 1) + "S");
                if (shadow == null) {
                    shadow = "";
                }
                item.setShadow(shadow.replace("\"", ""));
                // 物模型值
                String isMonitor = map.get(key.toString().substring(0, key.toString().length() - 1) + "M");
                if (isMonitor == null) {
                    isMonitor = "";
                }
                item.setIsMonitor("".equals(isMonitor.replace("\"", "")) ? 0 : Integer.parseInt(isMonitor.replace("\"", "")));
                valueList.add(item);
            }
        }
        return valueList;
    }

}

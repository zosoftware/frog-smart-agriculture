package com.frog.agriculture.service.impl;

import com.frog.agriculture.domain.*;
import com.frog.agriculture.mapper.*;
import com.frog.agriculture.service.IDataStatisticsService;
import com.frog.common.core.domain.entity.SysUser;
import com.frog.iot.domain.Device;
import com.frog.iot.domain.Product;
import com.frog.iot.domain.ThingsModel;
import com.frog.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.iot.mapper.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class DataStatisticsServiceImpl implements IDataStatisticsService {

    @Autowired
    private DataStatisticsMapper dataStatisticsMapper;
    @Autowired
    private BaseinfoMapper baseinfoMapper;
    @Autowired
    private TraceRecordMapper traceRecordMapper;
    @Autowired
    private TraceVersionMapper traceVersionMapper;
    @Autowired
    private TraceCodeMapper traceCodeMapper;
    @Autowired
    private TraceSellproMapper traceSellproMapper;
    @Autowired
    private CropBatchMapper cropBatchMapper;
    @Autowired
    private BatchTaskMapper batchTaskMapper;
    @Autowired
    private DeviceLogMapper deviceLogMapper;
    @Autowired
    private TraceStaffMapper traceStaffMapper;
    @Autowired
    private TraceShopMapper traceShopMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ThingsModelMapper thingsModelMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    //工作台
    public List<HashMap> selectBaseInfo(){
        return dataStatisticsMapper.selectBaseInfo();
    }
    public List<HashMap> selectDeviceInfo(){
        return dataStatisticsMapper.selectDeviceInfo();
    }
    public List<HashMap> selectDeviceJobInfo(){
        return dataStatisticsMapper.selectDeviceJobInfo();
    }
    public List<HashMap> selectTraceInfo(){
        return  dataStatisticsMapper.selectTraceInfo();
    }
    public List<HashMap> selectTaskInfo(){
        return  dataStatisticsMapper.selectTaskInfo();
    }
    public List<HashMap> selectAreaInfo(){
        return  dataStatisticsMapper.selectAreaInfo();
    }
    //溯源页面
    public HashMap traceStatistics(String traceCode){
        HashMap data = new HashMap();
        //基地信息
       Baseinfo baseinfo = baseinfoMapper.selectBaseinfoLimitOne();

       //查询记录
       TraceRecord traceRecord = new TraceRecord();
       traceRecord.setTraceCode(traceCode);
       List<TraceRecord> traceRecordList = traceRecordMapper.selectTraceRecordList(traceRecord);

       //本条溯源码
        TraceCode tr = new TraceCode();
        tr.setTraceCode(traceCode);
        List<TraceCode> traceCodeList = traceCodeMapper.selectTraceCodeList(tr);
        tr = traceCodeList.get(0);

        //查询版本
        TraceVersion traceVersion = traceVersionMapper.selectTraceVersionByVersionId(tr.getVersionId());

        //查询产品
        TraceSellpro traceSellpro = traceSellproMapper.selectTraceSellproBySellproId(tr.getSellproId());

        //查询批次
        CropBatch cropBatch = cropBatchMapper.selectCropBatchByBatchId(Long.parseLong(traceSellpro.getBatchId()));

        //查询批次任务
        BatchTask batchTask = new BatchTask();
        batchTask.setBatchId(Long.parseLong(traceSellpro.getBatchId()));
        List<BatchTask> batchTaskList = batchTaskMapper.selectBatchTaskList(batchTask);
        for(BatchTask task : batchTaskList){
            SysUser sysUser = sysUserMapper.selectUserById(task.getTaskHead());
            task.setUserName(sysUser.getNickName());
        }

        //图表统计
        List<HashMap> chartList = new ArrayList<>() ;
        String[] serialNumbers = traceSellpro.getDeviceId().split(",");
        for(String item : serialNumbers) {
            //查询设备对应产品的模型
            Device device = deviceMapper.selectDeviceBySerialNumber(item);
            if(device!=null) {
                Product product = productMapper.selectProductByProductId(device.getProductId());
                ThingsModel thingsModel = new ThingsModel();
                thingsModel.setProductId(product.getProductId());
                thingsModel.setType(1);
                List<ThingsModel> thingsModelList = thingsModelMapper.selectThingsModelList(thingsModel);
                for (ThingsModel model : thingsModelList) {
                    HashMap hashMap = new HashMap();
                    List<HashMap> dlList = deviceLogMapper.selectCountLog(model.getIdentifier(), device.getSerialNumber());
                    if (dlList.size() != 0) {
                        hashMap.put("name", device.getDeviceName() + "-" + model.getModelName());
                        hashMap.put("data", dlList);
                        chartList.add(hashMap);
                    }
                }
            }
        }

        //查询业务员
        TraceStaff traceStaff = new TraceStaff();
        traceStaff.setStatus("1");
        List<TraceStaff> traceStaffList = traceStaffMapper.selectTraceStaffList(traceStaff);

        //查询店铺
        TraceShop traceShop = new TraceShop();
        traceShop.setStatus("1");
        List<TraceShop> traceShopList = traceShopMapper.selectTraceShopList(traceShop);

        data.put("traceCode",tr);
        data.put("baseinfo",baseinfo);
        data.put("traceRecordList",traceRecordList);
        data.put("traceVersion",traceVersion);
        data.put("traceSellpro",traceSellpro);
        data.put("batch",cropBatch);
        data.put("batchTaskList",batchTaskList);
        data.put("chart",chartList);
        data.put("traceStaffList",traceStaffList);
        data.put("traceShopList",traceShopList);

        return data;
    }
    //大屏-溯源
    public List<HashMap> selectRecordGroupByCity(){
        return  dataStatisticsMapper.selectRecordGroupByCity();
    }
    public List<HashMap> selectRecordStatistics(){
        return  dataStatisticsMapper.selectRecordStatistics();
    }
    public List<HashMap> selectRecordGroupBySellpro(){
        return  dataStatisticsMapper.selectRecordGroupBySellpro();
    }

}

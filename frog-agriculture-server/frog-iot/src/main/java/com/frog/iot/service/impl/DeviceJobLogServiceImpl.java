package com.frog.iot.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.iot.mapper.DeviceJobLogMapper;
import com.frog.iot.domain.DeviceJobLog;
import com.frog.iot.service.IDeviceJobLogService;

/**
 * 设备定时任务日志Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-09-14
 */
@Service
public class DeviceJobLogServiceImpl implements IDeviceJobLogService 
{
    @Autowired
    private DeviceJobLogMapper deviceJobLogMapper;

    /**
     * 查询设备定时任务日志
     * 
     * @param jobLogId 设备定时任务日志主键
     * @return 设备定时任务日志
     */
    @Override
    public DeviceJobLog selectDeviceJobLogByJobLogId(Long jobLogId)
    {
        return deviceJobLogMapper.selectDeviceJobLogByJobLogId(jobLogId);
    }

    /**
     * 查询设备定时任务日志列表
     * 
     * @param deviceJobLog 设备定时任务日志
     * @return 设备定时任务日志
     */
    @Override
    public List<DeviceJobLog> selectDeviceJobLogList(DeviceJobLog deviceJobLog)
    {
        return deviceJobLogMapper.selectDeviceJobLogList(deviceJobLog);
    }

    /**
     * 新增设备定时任务日志
     * 
     * @param deviceJobLog 设备定时任务日志
     * @return 结果
     */
    @Override
    public int insertDeviceJobLog(DeviceJobLog deviceJobLog)
    {
        deviceJobLog.setCreateTime(DateUtils.getNowDate());
        return deviceJobLogMapper.insertDeviceJobLog(deviceJobLog);
    }
}

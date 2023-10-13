package com.frog.iot.mapper;

import java.util.List;
import com.frog.iot.domain.DeviceJobLog;

/**
 * 设备定时任务日志Mapper接口
 * 
 * @author nealtsiao
 * @date 2023-09-14
 */
public interface DeviceJobLogMapper 
{
    /**
     * 查询设备定时任务日志
     * 
     * @param jobLogId 设备定时任务日志主键
     * @return 设备定时任务日志
     */
    public DeviceJobLog selectDeviceJobLogByJobLogId(Long jobLogId);

    /**
     * 查询设备定时任务日志列表
     * 
     * @param deviceJobLog 设备定时任务日志
     * @return 设备定时任务日志集合
     */
    public List<DeviceJobLog> selectDeviceJobLogList(DeviceJobLog deviceJobLog);

    /**
     * 新增设备定时任务日志
     * 
     * @param deviceJobLog 设备定时任务日志
     * @return 结果
     */
    public int insertDeviceJobLog(DeviceJobLog deviceJobLog);

}

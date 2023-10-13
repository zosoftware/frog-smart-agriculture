package com.frog.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 设备定时任务日志对象 iot_device_job_log
 * 
 * @author nealtsiao
 * @date 2023-09-14
 */
public class DeviceJobLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务日志ID */
    private Long jobLogId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long jobId;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 日志信息 */
    @Excel(name = "日志信息")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String exceptionInfo;

    public void setJobLogId(Long jobLogId) 
    {
        this.jobLogId = jobLogId;
    }

    public Long getJobLogId() 
    {
        return jobLogId;
    }
    public void setJobName(String jobName) 
    {
        this.jobName = jobName;
    }

    public String getJobName() 
    {
        return jobName;
    }
    public void setJobId(Long jobId) 
    {
        this.jobId = jobId;
    }

    public Long getJobId() 
    {
        return jobId;
    }
    public void setJobGroup(String jobGroup) 
    {
        this.jobGroup = jobGroup;
    }

    public String getJobGroup() 
    {
        return jobGroup;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setJobMessage(String jobMessage) 
    {
        this.jobMessage = jobMessage;
    }

    public String getJobMessage() 
    {
        return jobMessage;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setExceptionInfo(String exceptionInfo) 
    {
        this.exceptionInfo = exceptionInfo;
    }

    public String getExceptionInfo() 
    {
        return exceptionInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobLogId", getJobLogId())
            .append("jobName", getJobName())
            .append("jobId", getJobId())
            .append("jobGroup", getJobGroup())
            .append("deviceName", getDeviceName())
            .append("jobMessage", getJobMessage())
            .append("status", getStatus())
            .append("exceptionInfo", getExceptionInfo())
            .append("createTime", getCreateTime())
            .toString();
    }
}

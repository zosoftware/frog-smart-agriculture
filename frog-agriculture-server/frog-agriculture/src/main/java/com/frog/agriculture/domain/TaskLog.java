package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 批次任务日志对象 agriculture_task_log
 * 
 * @author nealtsiao
 * @date 2023-06-06
 */
public class TaskLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 操作人名称 */
    @Excel(name = "操作人名称")
    private String operName;

    /** 操作人Id */
    @Excel(name = "操作人Id")
    private Long operId;

    /** 操作描述 */
    @Excel(name = "操作描述")
    private String operDes;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }
    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }
    public void setOperName(String operName) 
    {
        this.operName = operName;
    }

    public String getOperName() 
    {
        return operName;
    }
    public void setOperId(Long operId) 
    {
        this.operId = operId;
    }

    public Long getOperId() 
    {
        return operId;
    }
    public void setOperDes(String operDes) 
    {
        this.operDes = operDes;
    }

    public String getOperDes() 
    {
        return operDes;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("taskId", getTaskId())
            .append("operName", getOperName())
            .append("operId", getOperId())
            .append("operDes", getOperDes())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("orderNum", getOrderNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}

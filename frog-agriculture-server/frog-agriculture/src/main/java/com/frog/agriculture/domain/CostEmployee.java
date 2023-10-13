package com.frog.agriculture.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 人工工时对象 agriculture_cost_employee
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public class CostEmployee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long costId;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 雇员ID */
    @Excel(name = "雇员ID")
    private Long employeeId;

    /** 工时 */
    @Excel(name = "工时")
    private BigDecimal workingHours;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workingStart;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workingFinish;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setCostId(Long costId) 
    {
        this.costId = costId;
    }

    public Long getCostId() 
    {
        return costId;
    }
    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }
    public void setEmployeeId(Long employeeId) 
    {
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() 
    {
        return employeeId;
    }
    public void setWorkingHours(BigDecimal workingHours) 
    {
        this.workingHours = workingHours;
    }

    public BigDecimal getWorkingHours() 
    {
        return workingHours;
    }
    public void setWorkingStart(Date workingStart) 
    {
        this.workingStart = workingStart;
    }

    public Date getWorkingStart() 
    {
        return workingStart;
    }
    public void setWorkingFinish(Date workingFinish) 
    {
        this.workingFinish = workingFinish;
    }

    public Date getWorkingFinish() 
    {
        return workingFinish;
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
            .append("costId", getCostId())
            .append("taskId", getTaskId())
            .append("employeeId", getEmployeeId())
            .append("workingHours", getWorkingHours())
            .append("workingStart", getWorkingStart())
            .append("workingFinish", getWorkingFinish())
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

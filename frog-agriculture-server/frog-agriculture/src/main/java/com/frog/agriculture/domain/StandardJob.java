package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 标准作业任务对象 agriculture_standard_job
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public class StandardJob extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 作业任务ID */
    private Long jobId;

    /** 种质ID */
    @Excel(name = "种质ID")
    private Long germplasmId;

    /** 作业任务名称 */
    @Excel(name = "作业任务名称")
    private String jobName;

    /** 作业周期单位（0代表周 1代表天） */
    @Excel(name = "作业周期单位", readConverterExp = "0=代表周,1=代表天")
    private String cycleUnit;

    /** 起始周/天 */
    @Excel(name = "起始周/天")
    private Long jobStart;

    /** 结束周/天 */
    @Excel(name = "结束周/天")
    private Long jobFinish;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setJobId(Long jobId) 
    {
        this.jobId = jobId;
    }

    public Long getJobId() 
    {
        return jobId;
    }
    public void setGermplasmId(Long germplasmId) 
    {
        this.germplasmId = germplasmId;
    }

    public Long getGermplasmId() 
    {
        return germplasmId;
    }
    public void setJobName(String jobName) 
    {
        this.jobName = jobName;
    }

    public String getJobName() 
    {
        return jobName;
    }
    public void setCycleUnit(String cycleUnit) 
    {
        this.cycleUnit = cycleUnit;
    }

    public String getCycleUnit() 
    {
        return cycleUnit;
    }
    public void setJobStart(Long jobStart) 
    {
        this.jobStart = jobStart;
    }

    public Long getJobStart() 
    {
        return jobStart;
    }
    public void setJobFinish(Long jobFinish) 
    {
        this.jobFinish = jobFinish;
    }

    public Long getJobFinish() 
    {
        return jobFinish;
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
            .append("jobId", getJobId())
            .append("germplasmId", getGermplasmId())
            .append("jobName", getJobName())
            .append("cycleUnit", getCycleUnit())
            .append("jobStart", getJobStart())
            .append("jobFinish", getJobFinish())
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

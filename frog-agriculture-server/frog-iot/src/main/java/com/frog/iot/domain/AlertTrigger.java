package com.frog.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 告警触发器对象 iot_alert_trigger
 * 
 * @author kerwincui
 * @date 2022-10-06
 */
public class AlertTrigger extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 告警触发器ID */
    private Long alertTriggerId;

    /** 告警ID */
    private Long alertId;

    /** 触发源（1=设备触发，2=定时触发） */
    @Excel(name = "触发源", readConverterExp = "1==设备触发，2=定时触发")
    private Integer source;

    /** 物模型标识符 */
    private String id;

    /** 物模型名称 */
    @Excel(name = "物模型名称")
    private String name;

    /** 物模型值 */
    @Excel(name = "物模型值")
    private String value;

    /** 操作符 */
    @Excel(name = "操作符")
    private String operator;

    /** 物模型类别（1=属性，2=功能，3=事件，4=设备升级，5=设备上线，6=设备下线） */
    @Excel(name = "物模型类别", readConverterExp = "1==属性，2=功能，3=事件，4=设备升级，5=设备上线，6=设备下线")
    private Integer type;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long jobId;

    /** cron执行表达式 */
    @Excel(name = "cron执行表达式")
    private String cronExpression;

    /** 是否详细corn表达式（1=是，0=否） */
    @Excel(name = "是否详细corn表达式", readConverterExp = "1==是，0=否")
    private Integer isAdvance;

    /**
     * 告警触发器状态 （1-启动，2-停止）
     **/
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public void setAlertTriggerId(Long alertTriggerId)
    {
        this.alertTriggerId = alertTriggerId;
    }

    public Long getAlertTriggerId() 
    {
        return alertTriggerId;
    }
    public void setSource(Integer source) 
    {
        this.source = source;
    }

    public Integer getSource() 
    {
        return source;
    }
    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setJobId(Long jobId) 
    {
        this.jobId = jobId;
    }

    public Long getJobId() 
    {
        return jobId;
    }
    public void setCronExpression(String cronExpression) 
    {
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() 
    {
        return cronExpression;
    }
    public void setIsAdvance(Integer isAdvance) 
    {
        this.isAdvance = isAdvance;
    }

    public Integer getIsAdvance() 
    {
        return isAdvance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("alertTriggerId", getAlertTriggerId())
            .append("source", getSource())
            .append("id", getId())
            .append("name", getName())
            .append("value", getValue())
            .append("operator", getOperator())
            .append("type", getType())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("jobId", getJobId())
            .append("cronExpression", getCronExpression())
            .append("isAdvance", getIsAdvance())
            .toString();
    }
}

package com.frog.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 场景联动触发器对象 iot_scene_trigger
 * 
 * @author kerwincui
 * @date 2022-10-06
 */
public class SceneTrigger extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 场景触发器ID */
    private Long sceneTriggerId;

    /** 场景ID */
    private Long sceneId;

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

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String serialNumber;

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
     * 场景状态 （1-启动，2-停止）
     **/
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public void setSceneTriggerId(Long sceneTriggerId)
    {
        this.sceneTriggerId = sceneTriggerId;
    }

    public Long getSceneTriggerId() 
    {
        return sceneTriggerId;
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
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setSerialNumber(String serialNumber) 
    {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() 
    {
        return serialNumber;
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
            .append("sceneTriggerId", getSceneTriggerId())
            .append("source", getSource())
            .append("id", getId())
            .append("name", getName())
            .append("value", getValue())
            .append("operator", getOperator())
            .append("type", getType())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("serialNumber", getSerialNumber())
            .append("jobId", getJobId())
            .append("cronExpression", getCronExpression())
            .append("isAdvance", getIsAdvance())
            .toString();
    }
}

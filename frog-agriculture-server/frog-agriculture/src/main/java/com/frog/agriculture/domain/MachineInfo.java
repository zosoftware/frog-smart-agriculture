package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 机械信息对象 agriculture_machine_info
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public class MachineInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 机械ID */
    private Long machineId;

    /** 机械编码 */
    @Excel(name = "机械编码")
    private String machineCode;

    /** 机械名称 */
    @Excel(name = "机械名称")
    private String machineName;

    /** 机械类别 */
    @Excel(name = "机械类别")
    private Long machineTypeId;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String measureUnit;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMachineId(Long machineId) 
    {
        this.machineId = machineId;
    }

    public Long getMachineId() 
    {
        return machineId;
    }
    public void setMachineCode(String machineCode) 
    {
        this.machineCode = machineCode;
    }

    public String getMachineCode() 
    {
        return machineCode;
    }
    public void setMachineName(String machineName) 
    {
        this.machineName = machineName;
    }

    public String getMachineName() 
    {
        return machineName;
    }
    public void setMachineTypeId(Long machineTypeId) 
    {
        this.machineTypeId = machineTypeId;
    }

    public Long getMachineTypeId() 
    {
        return machineTypeId;
    }
    public void setMeasureUnit(String measureUnit) 
    {
        this.measureUnit = measureUnit;
    }

    public String getMeasureUnit() 
    {
        return measureUnit;
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
            .append("machineId", getMachineId())
            .append("machineCode", getMachineCode())
            .append("machineName", getMachineName())
            .append("machineTypeId", getMachineTypeId())
            .append("measureUnit", getMeasureUnit())
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

package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 机械类别对象 agriculture_machine_type
 * 
 * @author kerwincui
 * @date 2023-05-24
 */
public class MachineType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 机械类别ID */
    private Long machineTypeId;

    /** 机械类别名称 */
    @Excel(name = "机械类别名称")
    private String machineTypeName;

    /** 状态 */
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMachineTypeId(Long machineTypeId) 
    {
        this.machineTypeId = machineTypeId;
    }

    public Long getMachineTypeId() 
    {
        return machineTypeId;
    }
    public void setMachineTypeName(String machineTypeName) 
    {
        this.machineTypeName = machineTypeName;
    }

    public String getMachineTypeName() 
    {
        return machineTypeName;
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
            .append("machineTypeId", getMachineTypeId())
            .append("machineTypeName", getMachineTypeName())
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

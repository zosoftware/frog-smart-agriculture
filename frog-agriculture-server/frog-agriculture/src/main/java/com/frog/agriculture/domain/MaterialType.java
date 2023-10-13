package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 农资类别对象 agriculture_material_type
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public class MaterialType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 农资类别ID */
    private Long materialTypeId;

    /** 农资类别名称 */
    @Excel(name = "农资类别名称")
    private String materialTypeName;

    /** 状态 */
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMaterialTypeId(Long materialTypeId) 
    {
        this.materialTypeId = materialTypeId;
    }

    public Long getMaterialTypeId() 
    {
        return materialTypeId;
    }
    public void setMaterialTypeName(String materialTypeName) 
    {
        this.materialTypeName = materialTypeName;
    }

    public String getMaterialTypeName() 
    {
        return materialTypeName;
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
            .append("materialTypeId", getMaterialTypeId())
            .append("materialTypeName", getMaterialTypeName())
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

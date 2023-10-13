package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 农资信息对象 agriculture_material_info
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public class MaterialInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 农资ID */
    private Long materialId;

    /** 农资编码 */
    @Excel(name = "农资编码")
    private String materialCode;

    /** 农资名称 */
    @Excel(name = "农资名称")
    private String materialName;

    /** 农资类别 */
    @Excel(name = "农资类别")
    private Long materialTypeId;

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

    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
    {
        return materialId;
    }
    public void setMaterialCode(String materialCode) 
    {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() 
    {
        return materialCode;
    }
    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }
    public void setMaterialTypeId(Long materialTypeId) 
    {
        this.materialTypeId = materialTypeId;
    }

    public Long getMaterialTypeId() 
    {
        return materialTypeId;
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
            .append("materialId", getMaterialId())
            .append("materialCode", getMaterialCode())
            .append("materialName", getMaterialName())
            .append("materialTypeId", getMaterialTypeId())
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

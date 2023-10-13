package com.frog.agriculture.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 地块对象 agriculture_land
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public class Land extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地块ID */
    private Long landId;

    /** 地块名称 */
    @Excel(name = "地块名称")
    private String landName;

    /** 字典 agriculture_land_type */
    @Excel(name = "字典 agriculture_land_type")
    private String landType;

    /** 地块面积 */
    @Excel(name = "地块面积")
    private BigDecimal landArea;

    /** 边框宽度 */
    @Excel(name = "边框宽度")
    private Integer strokeWeight;

    /** 边框颜色 */
    @Excel(name = "边框颜色")
    private String strokeColor;

    /** 边框透明度 */
    @Excel(name = "边框透明度")
    private BigDecimal strokeOpacity;

    /** 地块路径 */
    @Excel(name = "地块路径")
    private String landPath;

    /** 地块背景颜色 */
    @Excel(name = "地块背景颜色")
    private String fillColor;

    /** 地块透明度 */
    @Excel(name = "地块透明度")
    private BigDecimal fillOpacity;

    /** 当前种植批次 */
    @Excel(name = "当前种植批次")
    private Long currentBatch;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setLandId(Long landId) 
    {
        this.landId = landId;
    }

    public Long getLandId() 
    {
        return landId;
    }
    public void setLandName(String landName) 
    {
        this.landName = landName;
    }

    public String getLandName() 
    {
        return landName;
    }
    public void setLandType(String landType) 
    {
        this.landType = landType;
    }

    public String getLandType() 
    {
        return landType;
    }
    public void setLandArea(BigDecimal landArea) 
    {
        this.landArea = landArea;
    }

    public BigDecimal getLandArea() 
    {
        return landArea;
    }
    public void setStrokeWeight(Integer strokeWeight) 
    {
        this.strokeWeight = strokeWeight;
    }

    public Integer getStrokeWeight() 
    {
        return strokeWeight;
    }
    public void setStrokeColor(String strokeColor) 
    {
        this.strokeColor = strokeColor;
    }

    public String getStrokeColor() 
    {
        return strokeColor;
    }
    public void setStrokeOpacity(BigDecimal strokeOpacity) 
    {
        this.strokeOpacity = strokeOpacity;
    }

    public BigDecimal getStrokeOpacity() 
    {
        return strokeOpacity;
    }
    public void setLandPath(String landPath) 
    {
        this.landPath = landPath;
    }

    public String getLandPath() 
    {
        return landPath;
    }
    public void setFillColor(String fillColor) 
    {
        this.fillColor = fillColor;
    }

    public String getFillColor() 
    {
        return fillColor;
    }
    public void setFillOpacity(BigDecimal fillOpacity) 
    {
        this.fillOpacity = fillOpacity;
    }

    public BigDecimal getFillOpacity() 
    {
        return fillOpacity;
    }
    public void setCurrentBatch(Long currentBatch) 
    {
        this.currentBatch = currentBatch;
    }

    public Long getCurrentBatch() 
    {
        return currentBatch;
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
            .append("landId", getLandId())
            .append("landName", getLandName())
            .append("landType", getLandType())
            .append("landArea", getLandArea())
            .append("strokeWeight", getStrokeWeight())
            .append("strokeColor", getStrokeColor())
            .append("strokeOpacity", getStrokeOpacity())
            .append("landPath", getLandPath())
            .append("fillColor", getFillColor())
            .append("fillOpacity", getFillOpacity())
            .append("currentBatch", getCurrentBatch())
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

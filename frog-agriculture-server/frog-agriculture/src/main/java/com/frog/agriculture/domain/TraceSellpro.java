package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 溯源产品对象 agriculture_trace_sellpro
 * 
 * @author nealtsiao
 * @date 2023-08-07
 */
public class TraceSellpro extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品ID */
    private Long sellproId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String sellproName;

    /** 产地 */
    @Excel(name = "产地")
    private String sellproArea;

    /** 产品重量 */
    @Excel(name = "产品重量")
    private String sellproWeight;

    /** 产品规格 */
    @Excel(name = "产品规格")
    private String sellproGuige;

    /** 产品图片 */
    @Excel(name = "产品图片")
    private String sellproImg;

    /** 种质ID */
    @Excel(name = "种质ID")
    private String germplasmId;

    /** 批次ID */
    @Excel(name = "批次ID")
    private String batchId;

    /** 地块ID */
    @Excel(name = "地块ID")
    private String landId;

    /** 环境设备ID */
    @Excel(name = "环境设备ID")
    private String deviceId;

    /** 监控设备ID */
    @Excel(name = "监控设备ID")
    private String cameraId;

    /** 溯源人员ID */
    @Excel(name = "溯源人员ID")
    private String staffId;

    /** 店铺ID */
    @Excel(name = "店铺ID")
    private String shopId;

    /** 溯源页面 */
    @Excel(name = "溯源页面")
    private String h5Url;

    /** 状态 1可用 2不可用 trace_status */
    @Excel(name = "状态 1可用 2不可用 trace_status")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setSellproId(Long sellproId) 
    {
        this.sellproId = sellproId;
    }

    public Long getSellproId() 
    {
        return sellproId;
    }
    public void setSellproName(String sellproName) 
    {
        this.sellproName = sellproName;
    }

    public String getSellproName() 
    {
        return sellproName;
    }
    public void setSellproArea(String sellproArea) 
    {
        this.sellproArea = sellproArea;
    }

    public String getSellproArea() 
    {
        return sellproArea;
    }
    public void setSellproWeight(String sellproWeight) 
    {
        this.sellproWeight = sellproWeight;
    }

    public String getSellproWeight() 
    {
        return sellproWeight;
    }
    public void setSellproGuige(String sellproGuige) 
    {
        this.sellproGuige = sellproGuige;
    }

    public String getSellproGuige() 
    {
        return sellproGuige;
    }
    public void setSellproImg(String sellproImg) 
    {
        this.sellproImg = sellproImg;
    }

    public String getSellproImg() 
    {
        return sellproImg;
    }
    public void setGermplasmId(String germplasmId) 
    {
        this.germplasmId = germplasmId;
    }

    public String getGermplasmId() 
    {
        return germplasmId;
    }
    public void setBatchId(String batchId) 
    {
        this.batchId = batchId;
    }

    public String getBatchId() 
    {
        return batchId;
    }
    public void setLandId(String landId) 
    {
        this.landId = landId;
    }

    public String getLandId() 
    {
        return landId;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setCameraId(String cameraId) 
    {
        this.cameraId = cameraId;
    }

    public String getCameraId() 
    {
        return cameraId;
    }
    public void setStaffId(String staffId) 
    {
        this.staffId = staffId;
    }

    public String getStaffId() 
    {
        return staffId;
    }
    public void setShopId(String shopId) 
    {
        this.shopId = shopId;
    }

    public String getShopId() 
    {
        return shopId;
    }
    public void setH5Url(String h5Url) 
    {
        this.h5Url = h5Url;
    }

    public String getH5Url() 
    {
        return h5Url;
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
            .append("sellproId", getSellproId())
            .append("sellproName", getSellproName())
            .append("sellproArea", getSellproArea())
            .append("sellproWeight", getSellproWeight())
            .append("sellproGuige", getSellproGuige())
            .append("sellproImg", getSellproImg())
            .append("germplasmId", getGermplasmId())
            .append("batchId", getBatchId())
            .append("landId", getLandId())
            .append("deviceId", getDeviceId())
            .append("cameraId", getCameraId())
            .append("staffId", getStaffId())
            .append("shopId", getShopId())
            .append("h5Url", getH5Url())
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

package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 店铺对象 agriculture_trace_shop
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
public class TraceShop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 店铺ID */
    private Long shopId;

    /** 店铺名称 */
    @Excel(name = "店铺名称")
    private String shopName;

    /** 店铺类型 */
    @Excel(name = "店铺类型")
    private String shopType;

    /** 店铺图片 */
    @Excel(name = "店铺图片")
    private String shopImg;

    /** 店铺地址 */
    @Excel(name = "店铺地址")
    private String shopAddress;

    /** 店铺坐标 */
    @Excel(name = "店铺坐标")
    private String shopCoordinate;

    /** 店铺联系人 */
    @Excel(name = "店铺联系人")
    private String shopContacts;

    /** 店铺联系方式 */
    @Excel(name = "店铺联系方式")
    private String shopTel;

    /** 店铺链接 */
    @Excel(name = "店铺链接")
    private String shopUrl;

    /** 店铺描述 */
    @Excel(name = "店铺描述")
    private String shopDes;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setShopId(Long shopId) 
    {
        this.shopId = shopId;
    }

    public Long getShopId() 
    {
        return shopId;
    }
    public void setShopName(String shopName) 
    {
        this.shopName = shopName;
    }

    public String getShopName() 
    {
        return shopName;
    }
    public void setShopType(String shopType) 
    {
        this.shopType = shopType;
    }

    public String getShopType() 
    {
        return shopType;
    }
    public void setShopImg(String shopImg) 
    {
        this.shopImg = shopImg;
    }

    public String getShopImg() 
    {
        return shopImg;
    }
    public void setShopAddress(String shopAddress) 
    {
        this.shopAddress = shopAddress;
    }

    public String getShopAddress() 
    {
        return shopAddress;
    }
    public void setShopCoordinate(String shopCoordinate) 
    {
        this.shopCoordinate = shopCoordinate;
    }

    public String getShopCoordinate() 
    {
        return shopCoordinate;
    }
    public void setShopContacts(String shopContacts) 
    {
        this.shopContacts = shopContacts;
    }

    public String getShopContacts() 
    {
        return shopContacts;
    }
    public void setShopTel(String shopTel) 
    {
        this.shopTel = shopTel;
    }

    public String getShopTel() 
    {
        return shopTel;
    }
    public void setShopUrl(String shopUrl) 
    {
        this.shopUrl = shopUrl;
    }

    public String getShopUrl() 
    {
        return shopUrl;
    }
    public void setShopDes(String shopDes) 
    {
        this.shopDes = shopDes;
    }

    public String getShopDes() 
    {
        return shopDes;
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
            .append("shopId", getShopId())
            .append("shopName", getShopName())
            .append("shopType", getShopType())
            .append("shopImg", getShopImg())
            .append("shopAddress", getShopAddress())
            .append("shopCoordinate", getShopCoordinate())
            .append("shopContacts", getShopContacts())
            .append("shopTel", getShopTel())
            .append("shopUrl", getShopUrl())
            .append("shopDes", getShopDes())
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

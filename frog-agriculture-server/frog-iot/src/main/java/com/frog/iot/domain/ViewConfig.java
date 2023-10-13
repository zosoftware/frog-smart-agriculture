package com.frog.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 界面可视化配置对象 iot_view_config
 * 
 * @author kerwincui
 * @date 2022-11-15
 */
public class ViewConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 可视化配置ID */
    private Long viewId;

    /** 可视化配置数据 */
    @Excel(name = "可视化配置数据")
    private String viewData;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 租户ID */
    @Excel(name = "租户ID")
    private Long tenantId;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String tenantName;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setViewId(Long viewId) 
    {
        this.viewId = viewId;
    }

    public Long getViewId() 
    {
        return viewId;
    }
    public void setViewData(String viewData) 
    {
        this.viewData = viewData;
    }

    public String getViewData() 
    {
        return viewData;
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
    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }
    public void setTenantName(String tenantName) 
    {
        this.tenantName = tenantName;
    }

    public String getTenantName() 
    {
        return tenantName;
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
            .append("viewId", getViewId())
            .append("viewData", getViewData())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("tenantId", getTenantId())
            .append("tenantName", getTenantName())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 产品布局对象 iot_product_layout
 * 
 * @author nealtsiao
 * @date 2023-08-12
 */
public class IotProductLayout extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long layoutId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long productId;

    /** 标志符 */
    @Excel(name = "标志符")
    private String identifier;

    /** x位置 */
    @Excel(name = "x位置")
    private String x;

    /** y位置 */
    @Excel(name = "y位置")
    private String y;

    /** 宽度 */
    @Excel(name = "宽度")
    private String w;

    /** 高度 */
    @Excel(name = "高度")
    private String h;

    public void setLayoutId(Long layoutId) 
    {
        this.layoutId = layoutId;
    }

    public Long getLayoutId() 
    {
        return layoutId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setIdentifier(String identifier) 
    {
        this.identifier = identifier;
    }

    public String getIdentifier() 
    {
        return identifier;
    }
    public void setX(String x) 
    {
        this.x = x;
    }

    public String getX() 
    {
        return x;
    }
    public void setY(String y) 
    {
        this.y = y;
    }

    public String getY() 
    {
        return y;
    }
    public void setW(String w) 
    {
        this.w = w;
    }

    public String getW() 
    {
        return w;
    }
    public void setH(String h) 
    {
        this.h = h;
    }

    public String getH() 
    {
        return h;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("layoutId", getLayoutId())
            .append("userId", getUserId())
            .append("productId", getProductId())
            .append("identifier", getIdentifier())
            .append("x", getX())
            .append("y", getY())
            .append("w", getW())
            .append("h", getH())
            .toString();
    }
}

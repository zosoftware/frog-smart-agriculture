package com.frog.agriculture.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 溯源版本对象 agriculture_trace_version
 * 
 * @author nealtsiao
 * @date 2023-08-04
 */
public class TraceVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 溯源版本ID */
    private Long versionId;

    /** 溯源产品 */
    @Excel(name = "溯源产品")
    private Long sellproId;

    /** 溯源版本编号 */
    @Excel(name = "溯源版本编号")
    private String versionCode;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireDate;

    /** 状态 1可用 2不可用 trace_status */
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setVersionId(Long versionId) 
    {
        this.versionId = versionId;
    }

    public Long getVersionId() 
    {
        return versionId;
    }
    public void setSellproId(Long sellproId) 
    {
        this.sellproId = sellproId;
    }

    public Long getSellproId() 
    {
        return sellproId;
    }
    public void setVersionCode(String versionCode) 
    {
        this.versionCode = versionCode;
    }

    public String getVersionCode() 
    {
        return versionCode;
    }
    public void setExpireDate(Date expireDate) 
    {
        this.expireDate = expireDate;
    }

    public Date getExpireDate() 
    {
        return expireDate;
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
            .append("versionId", getVersionId())
            .append("sellproId", getSellproId())
            .append("versionCode", getVersionCode())
            .append("expireDate", getExpireDate())
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

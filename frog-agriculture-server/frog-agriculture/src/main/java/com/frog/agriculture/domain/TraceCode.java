package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 溯源码对象 agriculture_trace_code
 * 
 * @author nealtsiao
 * @date 2023-08-04
 */
public class TraceCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 溯源码ID */
    private Long codeId;

    /** 溯源产品ID */
    @Excel(name = "溯源产品ID")
    private Long sellproId;

    /** 溯源版本ID */
    @Excel(name = "溯源版本ID")
    private Long versionId;

    /** 溯源码 */
    @Excel(name = "溯源码")
    private String traceCode;

    /** 溯源链接 */
    @Excel(name = "溯源链接")
    private String traceUrl;

    /** 状态 1可用 2不可用 trace_status */
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setCodeId(Long codeId)
    {
        this.codeId = codeId;
    }

    public Long getCodeId()
    {
        return codeId;
    }
    public void setVersionId(Long versionId)
    {
        this.versionId = versionId;
    }

    public Long getSellproId() {
        return sellproId;
    }

    public void setSellproId(Long sellproId) {
        this.sellproId = sellproId;
    }


    public Long getVersionId()
    {
        return versionId;
    }
    public void setTraceCode(String traceCode)
    {
        this.traceCode = traceCode;
    }

    public String getTraceCode()
    {
        return traceCode;
    }
    public void setTraceUrl(String traceUrl)
    {
        this.traceUrl = traceUrl;
    }

    public String getTraceUrl()
    {
        return traceUrl;
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
            .append("codeId", getCodeId())
            .append("versionId", getVersionId())
            .append("sellproId",getSellproId())
            .append("traceCode", getTraceCode())
            .append("traceUrl", getTraceUrl())
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

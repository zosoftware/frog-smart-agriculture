package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 种质介绍对象 agriculture_germplasm_intro
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public class GermplasmIntro extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 介绍ID */
    private Long introId;

    /** 种质ID */
    @Excel(name = "种质ID")
    private Long germplasmId;

    /** 名称 */
    @Excel(name = "名称")
    private String introName;

    /** 图片 */
    @Excel(name = "图片")
    private String introImg;

    /** 描述 */
    @Excel(name = "描述")
    private String introDes;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setIntroId(Long introId) 
    {
        this.introId = introId;
    }

    public Long getIntroId() 
    {
        return introId;
    }
    public void setGermplasmId(Long germplasmId) 
    {
        this.germplasmId = germplasmId;
    }

    public Long getGermplasmId() 
    {
        return germplasmId;
    }
    public void setIntroName(String introName) 
    {
        this.introName = introName;
    }

    public String getIntroName() 
    {
        return introName;
    }
    public void setIntroImg(String introImg) 
    {
        this.introImg = introImg;
    }

    public String getIntroImg() 
    {
        return introImg;
    }
    public void setIntroDes(String introDes) 
    {
        this.introDes = introDes;
    }

    public String getIntroDes() 
    {
        return introDes;
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
            .append("introId", getIntroId())
            .append("germplasmId", getGermplasmId())
            .append("introName", getIntroName())
            .append("introImg", getIntroImg())
            .append("introDes", getIntroDes())
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

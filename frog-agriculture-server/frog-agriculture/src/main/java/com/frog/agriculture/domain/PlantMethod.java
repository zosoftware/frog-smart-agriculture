package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 种植方法对象 agriculture_plant_method
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public class PlantMethod extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 方法ID */
    private Long methodId;

    /** 种质ID */
    @Excel(name = "种质ID")
    private Long germplasmId;

    /** 名称 */
    @Excel(name = "名称")
    private String methodName;

    /** 图片 */
    @Excel(name = "图片")
    private String methodImg;

    /** 描述 */
    @Excel(name = "描述")
    private String methodDes;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMethodId(Long methodId) 
    {
        this.methodId = methodId;
    }

    public Long getMethodId() 
    {
        return methodId;
    }
    public void setGermplasmId(Long germplasmId) 
    {
        this.germplasmId = germplasmId;
    }

    public Long getGermplasmId() 
    {
        return germplasmId;
    }
    public void setMethodName(String methodName) 
    {
        this.methodName = methodName;
    }

    public String getMethodName() 
    {
        return methodName;
    }
    public void setMethodImg(String methodImg) 
    {
        this.methodImg = methodImg;
    }

    public String getMethodImg() 
    {
        return methodImg;
    }
    public void setMethodDes(String methodDes) 
    {
        this.methodDes = methodDes;
    }

    public String getMethodDes() 
    {
        return methodDes;
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
            .append("methodId", getMethodId())
            .append("germplasmId", getGermplasmId())
            .append("methodName", getMethodName())
            .append("methodImg", getMethodImg())
            .append("methodDes", getMethodDes())
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

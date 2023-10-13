package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 种质对象 agriculture_germplasm
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public class Germplasm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 种质ID */
    private Long germplasmId;

    /** 作物名称 */
    @Excel(name = "作物名称")
    private String cropName;

    /** 作物英文名称 */
    @Excel(name = "作物英文名称")
    private String cropEnName;

    /** 种质名称 */
    @Excel(name = "种质名称")
    private String germplasmName;

    /** 种质英文名称 */
    @Excel(name = "种质英文名称")
    private String germplasmEnName;

    /** 种质图片 */
    @Excel(name = "种质图片")
    private String germplasmImg;

    /** 宣传语 */
    @Excel(name = "宣传语")
    private String germplasmDes;

    /** 状态 */
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setGermplasmId(Long germplasmId) 
    {
        this.germplasmId = germplasmId;
    }

    public Long getGermplasmId() 
    {
        return germplasmId;
    }
    public void setCropName(String cropName) 
    {
        this.cropName = cropName;
    }

    public String getCropName() 
    {
        return cropName;
    }
    public void setCropEnName(String cropEnName) 
    {
        this.cropEnName = cropEnName;
    }

    public String getCropEnName() 
    {
        return cropEnName;
    }
    public void setGermplasmName(String germplasmName) 
    {
        this.germplasmName = germplasmName;
    }

    public String getGermplasmName() 
    {
        return germplasmName;
    }
    public void setGermplasmEnName(String germplasmEnName) 
    {
        this.germplasmEnName = germplasmEnName;
    }

    public String getGermplasmEnName() 
    {
        return germplasmEnName;
    }
    public void setGermplasmImg(String germplasmImg) 
    {
        this.germplasmImg = germplasmImg;
    }

    public String getGermplasmImg() 
    {
        return germplasmImg;
    }
    public void setGermplasmDes(String germplasmDes) 
    {
        this.germplasmDes = germplasmDes;
    }

    public String getGermplasmDes() 
    {
        return germplasmDes;
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
            .append("germplasmId", getGermplasmId())
            .append("cropName", getCropName())
            .append("cropEnName", getCropEnName())
            .append("germplasmName", getGermplasmName())
            .append("germplasmEnName", getGermplasmEnName())
            .append("germplasmImg", getGermplasmImg())
            .append("germplasmDes", getGermplasmDes())
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

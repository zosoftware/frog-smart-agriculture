package com.frog.agriculture.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 作物批次对象 agriculture_crop_batch
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@Data
public class CropBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 批次ID */
    private Long batchId;

    /** 批次名称 */
    @Excel(name = "批次名称")
    private String batchName;

    /** 种质ID */
    @Excel(name = "种质ID")
    private Long germplasmId;

    /** 地块ID */
    @Excel(name = "地块ID")
    private Long landId;

    /** 种植面积（亩） */
    @Excel(name = "种植面积", readConverterExp = "亩=")
    private BigDecimal cropArea;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 负责人 */
    @Excel(name = "负责人")
    private Long batchHead;

    /** 状态 */
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String germplasmImg;


//    public void setBatchId(Long batchId)
//    {
//        this.batchId = batchId;
//    }
//
//    public Long getBatchId()
//    {
//        return batchId;
//    }
//    public void setBatchName(String batchName)
//    {
//        this.batchName = batchName;
//    }
//
//    public String getBatchName()
//    {
//        return batchName;
//    }
//    public void setGermplasmId(Long germplasmId)
//    {
//        this.germplasmId = germplasmId;
//    }
//
//    public Long getGermplasmId()
//    {
//        return germplasmId;
//    }
//    public void setLandId(Long landId)
//    {
//        this.landId = landId;
//    }
//
//    public Long getLandId()
//    {
//        return landId;
//    }
//    public void setCropArea(BigDecimal cropArea)
//    {
//        this.cropArea = cropArea;
//    }
//
//    public BigDecimal getCropArea()
//    {
//        return cropArea;
//    }
//    public void setStartTime(Date startTime)
//    {
//        this.startTime = startTime;
//    }
//
//    public Date getStartTime()
//    {
//        return startTime;
//    }
//    public void setStatus(String status)
//    {
//        this.status = status;
//    }
//
//    public String getBatchHead() {
//        return batchHead;
//    }
//
//    public void setBatchHead(String batchHead) {
//        this.batchHead = batchHead;
//    }
//    public String getStatus()
//    {
//        return status;
//    }
//    public void setOrderNum(Long orderNum)
//    {
//        this.orderNum = orderNum;
//    }
//
//    public Long getOrderNum()
//    {
//        return orderNum;
//    }
//    public void setDelFlag(String delFlag)
//    {
//        this.delFlag = delFlag;
//    }
//
//    public String getDelFlag()
//    {
//        return delFlag;
//    }
//    public void setGermplasmImg(String germplasmImg)
//    {
//        this.germplasmImg = germplasmImg;
//    }
//
//    public String getGermplasmImg()
//    {
//        return germplasmImg;
//    }
//
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
//            .append("batchId", getBatchId())
//            .append("batchName", getBatchName())
//            .append("germplasmId", getGermplasmId())
//            .append("landId", getLandId())
//            .append("cropArea", getCropArea())
//            .append("startTime", getStartTime())
//             .append("batchHead",getBatchHead())
//            .append("remark", getRemark())
//            .append("status", getStatus())
//            .append("orderNum", getOrderNum())
//            .append("createBy", getCreateBy())
//            .append("createTime", getCreateTime())
//            .append("updateBy", getUpdateBy())
//            .append("updateTime", getUpdateTime())
//            .append("delFlag", getDelFlag())
//             .append("germplasmImg", getGermplasmImg())
//            .toString();
//    }
}

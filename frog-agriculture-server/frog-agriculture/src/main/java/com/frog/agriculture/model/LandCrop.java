package com.frog.agriculture.model;


import java.math.BigDecimal;

import lombok.Data;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 地块对象 agriculture_land
 *
 * @author nealtsiao
 * @date 2023-05-13
 */
@Data
public class LandCrop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地块ID */
    private Long landId;

    /** 地块名称 */
    @Excel(name = "地块名称")
    private String landName;

    /** 字典 agriculture_land_type */
    @Excel(name = "字典 agriculture_land_type")
    private String landType;

    /** 地块面积 */
    @Excel(name = "地块面积")
    private BigDecimal landArea;

    /** 边框宽度 */
    @Excel(name = "边框宽度")
    private Integer strokeWeight;

    /** 边框颜色 */
    @Excel(name = "边框颜色")
    private String strokeColor;

    /** 边框透明度 */
    @Excel(name = "边框透明度")
    private BigDecimal strokeOpacity;

    /** 地块路径 */
    @Excel(name = "地块路径")
    private String landPath;

    /** 地块背景颜色 */
    @Excel(name = "地块背景颜色")
    private String fillColor;

    /** 地块透明度 */
    @Excel(name = "地块透明度")
    private BigDecimal fillOpacity;

    /** 当前种植批次 */
    @Excel(name = "当前种植批次")
    private Long currentBatch;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String cropStatus;

    private String batchName;
    private Long germplasmId;
    private String cropName;
    private String germplasmImg;

}

package com.frog.agriculture.domain;

import lombok.Data;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 基地信息对象 agriculture_baseinfo
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@Data
public class Baseinfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 基地ID */
    private Long baseId;

    /** 基地简称 */
    @Excel(name = "基地简称")
    private String baseShortName;

    /** 基地编号 */
    @Excel(name = "基地编号")
    private String baseCode;

    /** 基地全称 */
    @Excel(name = "基地全称")
    private String baseName;

    /** 基地负责人 */
    @Excel(name = "基地负责人")
    private String baseLeader;

    /** 基地负责人电话 */
    @Excel(name = "基地负责人电话")
    private String leaderTel;

    /** 基地负责人电话 */
    @Excel(name = "基地负责人电话")
    private String baseAddress;

    /** 基地面积 */
    @Excel(name = "基地面积")
    private String baseArea;

    /** 基地海拔 */
    @Excel(name = "基地海拔")
    private Long baseAltitude;

    /** 现场图片 */
    @Excel(name = "现场图片")
    private String baseImg;

    /** 基地描述 */
    @Excel(name = "基地描述")
    private String baseDes;

    /** 状态 */
    @Excel(name = "状态")
    private String status;
    /** 基地坐标 */
    @Excel(name = "状态")
    private String baseCoordinate;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


}

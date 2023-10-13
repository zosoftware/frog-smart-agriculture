package com.frog.agriculture.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 溯源码查询记录对象 agriculture_trace_record
 * 
 * @author nealtsiao
 * @date 2023-08-07
 */
@Data
public class TraceRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 产品ID*/
    @Excel(name="产品ID")
    private Long sellproId;

    /** 溯源码 */
    @Excel(name = "溯源码")
    private String traceCode;

    /** 查询时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "查询时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date queryDate;

    /** 查询地点 */
    @Excel(name = "查询地点")
    private String queryAddress;

    /** 查询坐标 */
    @Excel(name = "查询坐标")
    private String queryCoordinate;

    /** 查询城市 */
    @Excel(name="查询城市")
    private String queryCity;

    /** 城市代码 */
    @Excel(name="城市代码")
    private String cityCode;

    /** 状态 */
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
}

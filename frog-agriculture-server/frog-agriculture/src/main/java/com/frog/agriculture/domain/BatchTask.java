package com.frog.agriculture.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;
import  lombok.Data;

/**
 * 批次任务对象 agriculture_batch_task
 *
 * @author nealtsiao
 * @date 2023-05-30
 */
@Data
public class BatchTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    /** 批次ID */
    @Excel(name = "批次ID")
    private Long batchId;


    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 计划开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStart;

    /** 计划结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planFinish;

    /** 实际开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualStart;

    /** 实际结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualFinish;

    /** 任务详情 */
    @Excel(name = "任务详情")
    private String taskDetail;

    /** 图片资料 */
    @Excel(name = "图片资料")
    private String taskImages;

    /** 视频资料 */
    @Excel(name = "视频资料")
    private String taskVideos;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private Long batchHead;

    private String userName;

    private  Long landId;

    private String landName;

    private  String batchName;
    private  Long taskHead;
}
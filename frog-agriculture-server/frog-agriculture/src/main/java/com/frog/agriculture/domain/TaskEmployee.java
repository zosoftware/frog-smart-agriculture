package com.frog.agriculture.domain;

import lombok.Data;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 批次任务工人对象 agriculture_task_employee
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@Data
public class TaskEmployee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    /** 雇员ID */
    @Excel(name = "雇员ID")
    private Long employeeId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 雇员姓名 */
    private String employeeName;
}

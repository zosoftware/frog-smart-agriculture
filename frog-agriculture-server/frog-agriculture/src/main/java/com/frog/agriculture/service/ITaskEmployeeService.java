package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.TaskEmployee;

/**
 * 批次任务工人Service接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface ITaskEmployeeService 
{
    /**
     * 查询批次任务工人
     * 
     * @param id 批次任务工人主键
     * @return 批次任务工人
     */
    public TaskEmployee selectTaskEmployeeById(Long id);

    /**
     * 查询批次任务工人列表
     * 
     * @param taskEmployee 批次任务工人
     * @return 批次任务工人集合
     */
    public List<TaskEmployee> selectTaskEmployeeList(TaskEmployee taskEmployee);

    /**
     * 新增批次任务工人
     * 
     * @param taskEmployee 批次任务工人
     * @return 结果
     */
    public int insertTaskEmployee(TaskEmployee taskEmployee);

    /**
     * 修改批次任务工人
     * 
     * @param taskEmployee 批次任务工人
     * @return 结果
     */
    public int updateTaskEmployee(TaskEmployee taskEmployee);

    /**
     * 批量删除批次任务工人
     * 
     * @param ids 需要删除的批次任务工人主键集合
     * @return 结果
     */
    public int deleteTaskEmployeeByIds(Long[] ids);

    /**
     * 删除批次任务工人信息
     * 
     * @param id 批次任务工人主键
     * @return 结果
     */
    public int deleteTaskEmployeeById(Long id);
    /**
     * 删除批次任务工人
     * @param taskId
     * @param employeeId
     * @return
     */
    public int deleteTaskEmployeeByTaskIdAndEmployeeId(Long taskId,Long employeeId);
}

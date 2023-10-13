package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.TaskEmployee;
import org.apache.ibatis.annotations.Param;

/**
 * 批次任务工人Mapper接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface TaskEmployeeMapper 
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
     * 删除批次任务工人
     * 
     * @param id 批次任务工人主键
     * @return 结果
     */
    public int deleteTaskEmployeeById(Long id);

    /**
     * 批量删除批次任务工人
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskEmployeeByIds(Long[] ids);

    /**
     * 删除批次任务工人
     * @param taskId
     * @param employeeId
     * @return
     */
    public int deleteTaskEmployeeByTaskIdAndEmployeeId(@Param("taskId") Long taskId ,@Param("employeeId") Long employeeId);
}

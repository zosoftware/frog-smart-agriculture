package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TaskEmployeeMapper;
import com.frog.agriculture.domain.TaskEmployee;
import com.frog.agriculture.service.ITaskEmployeeService;

/**
 * 批次任务工人Service业务层处理
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@Service
public class TaskEmployeeServiceImpl implements ITaskEmployeeService 
{
    @Autowired
    private TaskEmployeeMapper taskEmployeeMapper;

    /**
     * 查询批次任务工人
     * 
     * @param id 批次任务工人主键
     * @return 批次任务工人
     */
    @Override
    public TaskEmployee selectTaskEmployeeById(Long id)
    {
        return taskEmployeeMapper.selectTaskEmployeeById(id);
    }

    /**
     * 查询批次任务工人列表
     * 
     * @param taskEmployee 批次任务工人
     * @return 批次任务工人
     */
    @Override
    public List<TaskEmployee> selectTaskEmployeeList(TaskEmployee taskEmployee)
    {
        return taskEmployeeMapper.selectTaskEmployeeList(taskEmployee);
    }

    /**
     * 新增批次任务工人
     * 
     * @param taskEmployee 批次任务工人
     * @return 结果
     */
    @Override
    public int insertTaskEmployee(TaskEmployee taskEmployee)
    {
        taskEmployee.setCreateBy(SecurityUtils.getUserId().toString());
        taskEmployee.setCreateTime(DateUtils.getNowDate());
        return taskEmployeeMapper.insertTaskEmployee(taskEmployee);
    }

    /**
     * 修改批次任务工人
     * 
     * @param taskEmployee 批次任务工人
     * @return 结果
     */
    @Override
    public int updateTaskEmployee(TaskEmployee taskEmployee)
    {
        taskEmployee.setUpdateBy(SecurityUtils.getUserId().toString());
        taskEmployee.setUpdateTime(DateUtils.getNowDate());
        return taskEmployeeMapper.updateTaskEmployee(taskEmployee);
    }

    /**
     * 批量删除批次任务工人
     * 
     * @param ids 需要删除的批次任务工人主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmployeeByIds(Long[] ids)
    {
        return taskEmployeeMapper.deleteTaskEmployeeByIds(ids);
    }

    /**
     * 删除批次任务工人信息
     * 
     * @param id 批次任务工人主键
     * @return 结果
     */
    @Override
    public int deleteTaskEmployeeById(Long id)
    {
        return taskEmployeeMapper.deleteTaskEmployeeById(id);
    }

    /**
     * 删除批次任务工人
     * @param taskId
     * @param employeeId
     * @return
     */
    @Override
    public int deleteTaskEmployeeByTaskIdAndEmployeeId(Long taskId,Long employeeId){return taskEmployeeMapper.deleteTaskEmployeeByTaskIdAndEmployeeId(taskId,employeeId);}
}

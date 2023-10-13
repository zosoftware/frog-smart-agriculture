package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TaskLogMapper;
import com.frog.agriculture.domain.TaskLog;
import com.frog.agriculture.service.ITaskLogService;

/**
 * 批次任务日志Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-06-06
 */
@Service
public class TaskLogServiceImpl implements ITaskLogService 
{
    @Autowired
    private TaskLogMapper taskLogMapper;

    /**
     * 查询批次任务日志
     * 
     * @param logId 批次任务日志主键
     * @return 批次任务日志
     */
    @Override
    public TaskLog selectTaskLogByLogId(Long logId)
    {
        return taskLogMapper.selectTaskLogByLogId(logId);
    }

    /**
     * 查询批次任务日志列表
     * 
     * @param taskLog 批次任务日志
     * @return 批次任务日志
     */
    @Override
    public List<TaskLog> selectTaskLogList(TaskLog taskLog)
    {
        return taskLogMapper.selectTaskLogList(taskLog);
    }

    /**
     * 新增批次任务日志
     * 
     * @param taskLog 批次任务日志
     * @return 结果
     */
    @Override
    public int insertTaskLog(TaskLog taskLog)
    {
        taskLog.setCreateTime(DateUtils.getNowDate());
        taskLog.setOperName(SecurityUtils.getUsername());
        taskLog.setOperId(SecurityUtils.getUserId());
        return taskLogMapper.insertTaskLog(taskLog);
    }

    /**
     * 修改批次任务日志
     * 
     * @param taskLog 批次任务日志
     * @return 结果
     */
    @Override
    public int updateTaskLog(TaskLog taskLog)
    {
        taskLog.setUpdateBy(SecurityUtils.getUserId().toString());
        taskLog.setUpdateTime(DateUtils.getNowDate());
        return taskLogMapper.updateTaskLog(taskLog);
    }

    /**
     * 批量删除批次任务日志
     * 
     * @param logIds 需要删除的批次任务日志主键
     * @return 结果
     */
    @Override
    public int deleteTaskLogByLogIds(Long[] logIds)
    {
        return taskLogMapper.deleteTaskLogByLogIds(logIds);
    }

    /**
     * 删除批次任务日志信息
     * 
     * @param logId 批次任务日志主键
     * @return 结果
     */
    @Override
    public int deleteTaskLogByLogId(Long logId)
    {
        return taskLogMapper.deleteTaskLogByLogId(logId);
    }
}

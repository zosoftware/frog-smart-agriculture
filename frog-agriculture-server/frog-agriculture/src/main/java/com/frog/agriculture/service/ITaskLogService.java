package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.TaskLog;

/**
 * 批次任务日志Service接口
 * 
 * @author nealtsiao
 * @date 2023-06-06
 */
public interface ITaskLogService 
{
    /**
     * 查询批次任务日志
     * 
     * @param logId 批次任务日志主键
     * @return 批次任务日志
     */
    public TaskLog selectTaskLogByLogId(Long logId);

    /**
     * 查询批次任务日志列表
     * 
     * @param taskLog 批次任务日志
     * @return 批次任务日志集合
     */
    public List<TaskLog> selectTaskLogList(TaskLog taskLog);

    /**
     * 新增批次任务日志
     * 
     * @param taskLog 批次任务日志
     * @return 结果
     */
    public int insertTaskLog(TaskLog taskLog);

    /**
     * 修改批次任务日志
     * 
     * @param taskLog 批次任务日志
     * @return 结果
     */
    public int updateTaskLog(TaskLog taskLog);

    /**
     * 批量删除批次任务日志
     * 
     * @param logIds 需要删除的批次任务日志主键集合
     * @return 结果
     */
    public int deleteTaskLogByLogIds(Long[] logIds);

    /**
     * 删除批次任务日志信息
     * 
     * @param logId 批次任务日志主键
     * @return 结果
     */
    public int deleteTaskLogByLogId(Long logId);
}

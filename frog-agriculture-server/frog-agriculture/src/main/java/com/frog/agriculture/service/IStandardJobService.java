package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.StandardJob;

/**
 * 标准作业任务Service接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface IStandardJobService 
{
    /**
     * 查询标准作业任务
     * 
     * @param jobId 标准作业任务主键
     * @return 标准作业任务
     */
    public StandardJob selectStandardJobByJobId(Long jobId);

    /**
     * 查询标准作业任务列表
     * 
     * @param standardJob 标准作业任务
     * @return 标准作业任务集合
     */
    public List<StandardJob> selectStandardJobList(StandardJob standardJob);

    /**
     * 新增标准作业任务
     * 
     * @param standardJob 标准作业任务
     * @return 结果
     */
    public int insertStandardJob(StandardJob standardJob);

    /**
     * 修改标准作业任务
     * 
     * @param standardJob 标准作业任务
     * @return 结果
     */
    public int updateStandardJob(StandardJob standardJob);

    /**
     * 批量删除标准作业任务
     * 
     * @param jobIds 需要删除的标准作业任务主键集合
     * @return 结果
     */
    public int deleteStandardJobByJobIds(Long[] jobIds);

    /**
     * 删除标准作业任务信息
     * 
     * @param jobId 标准作业任务主键
     * @return 结果
     */
    public int deleteStandardJobByJobId(Long jobId);
}

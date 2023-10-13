package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.TraceVersion;

/**
 * 溯源版本Service接口
 * 
 * @author nealtsiao
 * @date 2023-08-04
 */
public interface ITraceVersionService 
{
    /**
     * 查询溯源版本
     * 
     * @param versionId 溯源版本主键
     * @return 溯源版本
     */
    public TraceVersion selectTraceVersionByVersionId(Long versionId);

    /**
     * 查询溯源版本列表
     * 
     * @param traceVersion 溯源版本
     * @return 溯源版本集合
     */
    public List<TraceVersion> selectTraceVersionList(TraceVersion traceVersion);

    /**
     * 新增溯源版本
     * 
     * @param traceVersion 溯源版本
     * @return 结果
     */
    public int insertTraceVersion(TraceVersion traceVersion);

    /**
     * 修改溯源版本
     * 
     * @param traceVersion 溯源版本
     * @return 结果
     */
    public int updateTraceVersion(TraceVersion traceVersion);

    /**
     * 批量删除溯源版本
     * 
     * @param versionIds 需要删除的溯源版本主键集合
     * @return 结果
     */
    public int deleteTraceVersionByVersionIds(Long[] versionIds);

    /**
     * 删除溯源版本信息
     * 
     * @param versionId 溯源版本主键
     * @return 结果
     */
    public int deleteTraceVersionByVersionId(Long versionId);
}

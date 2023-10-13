package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TraceVersionMapper;
import com.frog.agriculture.domain.TraceVersion;
import com.frog.agriculture.service.ITraceVersionService;

/**
 * 溯源版本Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-08-04
 */
@Service
public class TraceVersionServiceImpl implements ITraceVersionService 
{
    @Autowired
    private TraceVersionMapper traceVersionMapper;

    /**
     * 查询溯源版本
     * 
     * @param versionId 溯源版本主键
     * @return 溯源版本
     */
    @Override
    public TraceVersion selectTraceVersionByVersionId(Long versionId)
    {
        return traceVersionMapper.selectTraceVersionByVersionId(versionId);
    }

    /**
     * 查询溯源版本列表
     * 
     * @param traceVersion 溯源版本
     * @return 溯源版本
     */
    @Override
    public List<TraceVersion> selectTraceVersionList(TraceVersion traceVersion)
    {
        return traceVersionMapper.selectTraceVersionList(traceVersion);
    }

    /**
     * 新增溯源版本
     * 
     * @param traceVersion 溯源版本
     * @return 结果
     */
    @Override
    public int insertTraceVersion(TraceVersion traceVersion)
    {
        traceVersion.setCreateBy(SecurityUtils.getUserId().toString());
        traceVersion.setCreateTime(DateUtils.getNowDate());
        return traceVersionMapper.insertTraceVersion(traceVersion);
    }

    /**
     * 修改溯源版本
     * 
     * @param traceVersion 溯源版本
     * @return 结果
     */
    @Override
    public int updateTraceVersion(TraceVersion traceVersion)
    {
        traceVersion.setUpdateBy(SecurityUtils.getUserId().toString());
        traceVersion.setUpdateTime(DateUtils.getNowDate());
        return traceVersionMapper.updateTraceVersion(traceVersion);
    }

    /**
     * 批量删除溯源版本
     * 
     * @param versionIds 需要删除的溯源版本主键
     * @return 结果
     */
    @Override
    public int deleteTraceVersionByVersionIds(Long[] versionIds)
    {
        return traceVersionMapper.deleteTraceVersionByVersionIds(versionIds);
    }

    /**
     * 删除溯源版本信息
     * 
     * @param versionId 溯源版本主键
     * @return 结果
     */
    @Override
    public int deleteTraceVersionByVersionId(Long versionId)
    {
        return traceVersionMapper.deleteTraceVersionByVersionId(versionId);
    }
}

package com.frog.agriculture.service.impl;

import java.util.List;

import com.frog.agriculture.domain.TraceSellpro;
import com.frog.agriculture.domain.TraceVersion;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import com.frog.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TraceCodeMapper;
import com.frog.agriculture.domain.TraceCode;
import com.frog.agriculture.service.ITraceCodeService;
import com.frog.agriculture.mapper.TraceVersionMapper;
import com.frog.agriculture.mapper.TraceSellproMapper;

/**
 * 溯源码Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-08-04
 */
@Service
public class TraceCodeServiceImpl implements ITraceCodeService 
{
    @Autowired
    private TraceCodeMapper traceCodeMapper;
    @Autowired
    private TraceVersionMapper traceVersionMapper;
    @Autowired
    private TraceSellproMapper traceSellproMapper;

    /**
     * 查询溯源码
     * 
     * @param codeId 溯源码主键
     * @return 溯源码
     */
    @Override
    public TraceCode selectTraceCodeByCodeId(Long codeId)
    {
        return traceCodeMapper.selectTraceCodeByCodeId(codeId);
    }

    /**
     * 查询溯源码列表
     * 
     * @param traceCode 溯源码
     * @return 溯源码
     */
    @Override
    public List<TraceCode> selectTraceCodeList(TraceCode traceCode)
    {
        return traceCodeMapper.selectTraceCodeList(traceCode);
    }

    /**
     * 新增溯源码
     * 
     * @param traceCode 溯源码
     * @return 结果
     */
    @Override
    public int insertTraceCode(TraceCode traceCode)
    {
        traceCode.setCreateBy(SecurityUtils.getUserId().toString());
        traceCode.setCreateTime(DateUtils.getNowDate());
        return traceCodeMapper.insertTraceCode(traceCode);
    }

    /**
     * 批量生成溯源码
     * @param versionId
     * @param codeNum
     * @return
     */
    @Override
    public void batchInsertTraceCode(Long versionId,Long codeNum)
    {
       TraceVersion traceVersion = traceVersionMapper.selectTraceVersionByVersionId(versionId);
       TraceSellpro traceSellpro = traceSellproMapper.selectTraceSellproBySellproId(traceVersion.getSellproId());
        for(int i=1;i<=codeNum;i++){
            TraceCode traceCode = new TraceCode();
            traceCode.setVersionId(versionId);
            traceCode.setSellproId(traceSellpro.getSellproId());
            traceCode.setTraceCode(UUID.randomUUID().toString());
            traceCode.setTraceUrl(traceSellpro.getH5Url()+"?tracecode="+traceCode.getTraceCode());
            traceCode.setStatus("1");
            traceCode.setCreateBy(SecurityUtils.getUserId().toString());
            traceCode.setCreateTime(DateUtils.getNowDate());
            traceCodeMapper.insertTraceCode(traceCode);
        }
    }
    /**
     * 修改溯源码
     * 
     * @param traceCode 溯源码
     * @return 结果
     */
    @Override
    public int updateTraceCode(TraceCode traceCode)
    {
        traceCode.setUpdateBy(SecurityUtils.getUserId().toString());
        traceCode.setUpdateTime(DateUtils.getNowDate());
        return traceCodeMapper.updateTraceCode(traceCode);
    }

    /**
     * 批量删除溯源码
     * 
     * @param codeIds 需要删除的溯源码主键
     * @return 结果
     */
    @Override
    public int deleteTraceCodeByCodeIds(Long[] codeIds)
    {
        return traceCodeMapper.deleteTraceCodeByCodeIds(codeIds);
    }

    /**
     * 删除溯源码信息
     * 
     * @param codeId 溯源码主键
     * @return 结果
     */
    @Override
    public int deleteTraceCodeByCodeId(Long codeId)
    {
        return traceCodeMapper.deleteTraceCodeByCodeId(codeId);
    }
}

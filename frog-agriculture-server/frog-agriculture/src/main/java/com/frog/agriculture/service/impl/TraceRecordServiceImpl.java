package com.frog.agriculture.service.impl;

import java.util.HashMap;
import java.util.List;

import com.frog.agriculture.domain.TraceCode;
import com.frog.agriculture.mapper.TraceCodeMapper;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TraceRecordMapper;
import com.frog.agriculture.domain.TraceRecord;
import com.frog.agriculture.service.ITraceRecordService;

/**
 * 溯源码查询记录Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-08-07
 */
@Service
public class TraceRecordServiceImpl implements ITraceRecordService 
{
    @Autowired
    private TraceRecordMapper traceRecordMapper;
    @Autowired
    private TraceCodeMapper traceCodeMapper;

    /**
     * 查询溯源码查询记录
     * 
     * @param recordId 溯源码查询记录主键
     * @return 溯源码查询记录
     */
    @Override
    public TraceRecord selectTraceRecordByRecordId(Long recordId)
    {
        return traceRecordMapper.selectTraceRecordByRecordId(recordId);
    }

    /**
     * 查询溯源码查询记录列表
     * 
     * @param traceRecord 溯源码查询记录
     * @return 溯源码查询记录
     */
    @Override
    public List<TraceRecord> selectTraceRecordList(TraceRecord traceRecord)
    {
        return traceRecordMapper.selectTraceRecordList(traceRecord);
    }

    /**
     * 新增溯源码查询记录
     * 
     * @param traceRecord 溯源码查询记录
     * @return 结果
     */
    @Override
    public int insertTraceRecord(TraceRecord traceRecord)
    {
//        traceRecord.setCreateBy(SecurityUtils.getUserId().toString());
        traceRecord.setCreateTime(DateUtils.getNowDate());
        TraceCode  traceCode = new TraceCode();
        traceCode.setTraceCode(traceRecord.getTraceCode());
        List<TraceCode> list = traceCodeMapper.selectTraceCodeList(traceCode);
        int i=0;
        if(list.size() >0){
          i = traceRecordMapper.insertTraceRecord(traceRecord);
        }
        return i;
    }

    /**
     * 修改溯源码查询记录
     * 
     * @param traceRecord 溯源码查询记录
     * @return 结果
     */
    @Override
    public int updateTraceRecord(TraceRecord traceRecord)
    {
        traceRecord.setUpdateBy(SecurityUtils.getUserId().toString());
        traceRecord.setUpdateTime(DateUtils.getNowDate());
        return traceRecordMapper.updateTraceRecord(traceRecord);
    }

    /**
     * 批量删除溯源码查询记录
     * 
     * @param recordIds 需要删除的溯源码查询记录主键
     * @return 结果
     */
    @Override
    public int deleteTraceRecordByRecordIds(Long[] recordIds)
    {
        return traceRecordMapper.deleteTraceRecordByRecordIds(recordIds);
    }

    /**
     * 删除溯源码查询记录信息
     * 
     * @param recordId 溯源码查询记录主键
     * @return 结果
     */
    @Override
    public int deleteTraceRecordByRecordId(Long recordId)
    {
        return traceRecordMapper.deleteTraceRecordByRecordId(recordId);
    }

    /**
     * 查询统计
     * @param traceRecord
     * @return
     */
    @Override
    public List<HashMap> countRecordList(TraceRecord traceRecord){
        return traceRecordMapper.countRecordList(traceRecord);
    }
}

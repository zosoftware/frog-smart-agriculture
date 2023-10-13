package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TraceStaffMapper;
import com.frog.agriculture.domain.TraceStaff;
import com.frog.agriculture.service.ITraceStaffService;

/**
 * 溯源人员Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
@Service
public class TraceStaffServiceImpl implements ITraceStaffService 
{
    @Autowired
    private TraceStaffMapper traceStaffMapper;

    /**
     * 查询溯源人员
     * 
     * @param staffId 溯源人员主键
     * @return 溯源人员
     */
    @Override
    public TraceStaff selectTraceStaffByStaffId(Long staffId)
    {
        return traceStaffMapper.selectTraceStaffByStaffId(staffId);
    }

    /**
     * 查询溯源人员列表
     * 
     * @param traceStaff 溯源人员
     * @return 溯源人员
     */
    @Override
    public List<TraceStaff> selectTraceStaffList(TraceStaff traceStaff)
    {
        return traceStaffMapper.selectTraceStaffList(traceStaff);
    }

    /**
     * 新增溯源人员
     * 
     * @param traceStaff 溯源人员
     * @return 结果
     */
    @Override
    public int insertTraceStaff(TraceStaff traceStaff)
    {
        traceStaff.setCreateBy(SecurityUtils.getUserId().toString());
        traceStaff.setCreateTime(DateUtils.getNowDate());
        return traceStaffMapper.insertTraceStaff(traceStaff);
    }

    /**
     * 修改溯源人员
     * 
     * @param traceStaff 溯源人员
     * @return 结果
     */
    @Override
    public int updateTraceStaff(TraceStaff traceStaff)
    {
        traceStaff.setUpdateBy(SecurityUtils.getUserId().toString());
        traceStaff.setUpdateTime(DateUtils.getNowDate());
        return traceStaffMapper.updateTraceStaff(traceStaff);
    }

    /**
     * 批量删除溯源人员
     * 
     * @param staffIds 需要删除的溯源人员主键
     * @return 结果
     */
    @Override
    public int deleteTraceStaffByStaffIds(Long[] staffIds)
    {
        return traceStaffMapper.deleteTraceStaffByStaffIds(staffIds);
    }

    /**
     * 删除溯源人员信息
     * 
     * @param staffId 溯源人员主键
     * @return 结果
     */
    @Override
    public int deleteTraceStaffByStaffId(Long staffId)
    {
        return traceStaffMapper.deleteTraceStaffByStaffId(staffId);
    }
}

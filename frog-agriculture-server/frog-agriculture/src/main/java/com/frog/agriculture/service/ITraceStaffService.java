package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.TraceStaff;

/**
 * 溯源人员Service接口
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
public interface ITraceStaffService 
{
    /**
     * 查询溯源人员
     * 
     * @param staffId 溯源人员主键
     * @return 溯源人员
     */
    public TraceStaff selectTraceStaffByStaffId(Long staffId);

    /**
     * 查询溯源人员列表
     * 
     * @param traceStaff 溯源人员
     * @return 溯源人员集合
     */
    public List<TraceStaff> selectTraceStaffList(TraceStaff traceStaff);

    /**
     * 新增溯源人员
     * 
     * @param traceStaff 溯源人员
     * @return 结果
     */
    public int insertTraceStaff(TraceStaff traceStaff);

    /**
     * 修改溯源人员
     * 
     * @param traceStaff 溯源人员
     * @return 结果
     */
    public int updateTraceStaff(TraceStaff traceStaff);

    /**
     * 批量删除溯源人员
     * 
     * @param staffIds 需要删除的溯源人员主键集合
     * @return 结果
     */
    public int deleteTraceStaffByStaffIds(Long[] staffIds);

    /**
     * 删除溯源人员信息
     * 
     * @param staffId 溯源人员主键
     * @return 结果
     */
    public int deleteTraceStaffByStaffId(Long staffId);
}

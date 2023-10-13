package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.CostEmployee;

/**
 * 人工工时Mapper接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface CostEmployeeMapper 
{
    /**
     * 查询人工工时
     * 
     * @param costId 人工工时主键
     * @return 人工工时
     */
    public CostEmployee selectCostEmployeeByCostId(Long costId);

    /**
     * 查询人工工时列表
     * 
     * @param costEmployee 人工工时
     * @return 人工工时集合
     */
    public List<CostEmployee> selectCostEmployeeList(CostEmployee costEmployee);

    /**
     * 新增人工工时
     * 
     * @param costEmployee 人工工时
     * @return 结果
     */
    public int insertCostEmployee(CostEmployee costEmployee);

    /**
     * 修改人工工时
     * 
     * @param costEmployee 人工工时
     * @return 结果
     */
    public int updateCostEmployee(CostEmployee costEmployee);

    /**
     * 删除人工工时
     * 
     * @param costId 人工工时主键
     * @return 结果
     */
    public int deleteCostEmployeeByCostId(Long costId);

    /**
     * 批量删除人工工时
     * 
     * @param costIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCostEmployeeByCostIds(Long[] costIds);
}

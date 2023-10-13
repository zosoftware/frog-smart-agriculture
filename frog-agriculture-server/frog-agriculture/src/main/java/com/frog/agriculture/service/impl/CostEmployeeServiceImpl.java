package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.CostEmployeeMapper;
import com.frog.agriculture.domain.CostEmployee;
import com.frog.agriculture.service.ICostEmployeeService;

/**
 * 人工工时Service业务层处理
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@Service
public class CostEmployeeServiceImpl implements ICostEmployeeService 
{
    @Autowired
    private CostEmployeeMapper costEmployeeMapper;

    /**
     * 查询人工工时
     * 
     * @param costId 人工工时主键
     * @return 人工工时
     */
    @Override
    public CostEmployee selectCostEmployeeByCostId(Long costId)
    {
        return costEmployeeMapper.selectCostEmployeeByCostId(costId);
    }

    /**
     * 查询人工工时列表
     * 
     * @param costEmployee 人工工时
     * @return 人工工时
     */
    @Override
    public List<CostEmployee> selectCostEmployeeList(CostEmployee costEmployee)
    {
        return costEmployeeMapper.selectCostEmployeeList(costEmployee);
    }

    /**
     * 新增人工工时
     * 
     * @param costEmployee 人工工时
     * @return 结果
     */
    @Override
    public int insertCostEmployee(CostEmployee costEmployee)
    {
        costEmployee.setCreateBy(SecurityUtils.getUserId().toString());
        costEmployee.setCreateTime(DateUtils.getNowDate());
        return costEmployeeMapper.insertCostEmployee(costEmployee);
    }

    /**
     * 修改人工工时
     * 
     * @param costEmployee 人工工时
     * @return 结果
     */
    @Override
    public int updateCostEmployee(CostEmployee costEmployee)
    {
        costEmployee.setUpdateBy(SecurityUtils.getUserId().toString());
        costEmployee.setUpdateTime(DateUtils.getNowDate());
        return costEmployeeMapper.updateCostEmployee(costEmployee);
    }

    /**
     * 批量删除人工工时
     * 
     * @param costIds 需要删除的人工工时主键
     * @return 结果
     */
    @Override
    public int deleteCostEmployeeByCostIds(Long[] costIds)
    {
        return costEmployeeMapper.deleteCostEmployeeByCostIds(costIds);
    }

    /**
     * 删除人工工时信息
     * 
     * @param costId 人工工时主键
     * @return 结果
     */
    @Override
    public int deleteCostEmployeeByCostId(Long costId)
    {
        return costEmployeeMapper.deleteCostEmployeeByCostId(costId);
    }
}

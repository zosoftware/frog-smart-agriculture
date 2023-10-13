package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.CostMachineMapper;
import com.frog.agriculture.domain.CostMachine;
import com.frog.agriculture.service.ICostMachineService;

/**
 * 机械工时Service业务层处理
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@Service
public class CostMachineServiceImpl implements ICostMachineService 
{
    @Autowired
    private CostMachineMapper costMachineMapper;

    /**
     * 查询机械工时
     * 
     * @param costId 机械工时主键
     * @return 机械工时
     */
    @Override
    public CostMachine selectCostMachineByCostId(Long costId)
    {
        return costMachineMapper.selectCostMachineByCostId(costId);
    }

    /**
     * 查询机械工时列表
     * 
     * @param costMachine 机械工时
     * @return 机械工时
     */
    @Override
    public List<CostMachine> selectCostMachineList(CostMachine costMachine)
    {
        return costMachineMapper.selectCostMachineList(costMachine);
    }

    /**
     * 新增机械工时
     * 
     * @param costMachine 机械工时
     * @return 结果
     */
    @Override
    public int insertCostMachine(CostMachine costMachine)
    {
        costMachine.setCreateBy(SecurityUtils.getUserId().toString());
        costMachine.setCreateTime(DateUtils.getNowDate());
        return costMachineMapper.insertCostMachine(costMachine);
    }

    /**
     * 修改机械工时
     * 
     * @param costMachine 机械工时
     * @return 结果
     */
    @Override
    public int updateCostMachine(CostMachine costMachine)
    {
        costMachine.setUpdateBy(SecurityUtils.getUserId().toString());
        costMachine.setUpdateTime(DateUtils.getNowDate());
        return costMachineMapper.updateCostMachine(costMachine);
    }

    /**
     * 批量删除机械工时
     * 
     * @param costIds 需要删除的机械工时主键
     * @return 结果
     */
    @Override
    public int deleteCostMachineByCostIds(Long[] costIds)
    {
        return costMachineMapper.deleteCostMachineByCostIds(costIds);
    }

    /**
     * 删除机械工时信息
     * 
     * @param costId 机械工时主键
     * @return 结果
     */
    @Override
    public int deleteCostMachineByCostId(Long costId)
    {
        return costMachineMapper.deleteCostMachineByCostId(costId);
    }
}

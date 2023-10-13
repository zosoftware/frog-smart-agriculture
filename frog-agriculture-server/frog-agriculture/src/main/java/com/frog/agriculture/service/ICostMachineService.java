package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.CostMachine;

/**
 * 机械工时Service接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface ICostMachineService 
{
    /**
     * 查询机械工时
     * 
     * @param costId 机械工时主键
     * @return 机械工时
     */
    public CostMachine selectCostMachineByCostId(Long costId);

    /**
     * 查询机械工时列表
     * 
     * @param costMachine 机械工时
     * @return 机械工时集合
     */
    public List<CostMachine> selectCostMachineList(CostMachine costMachine);

    /**
     * 新增机械工时
     * 
     * @param costMachine 机械工时
     * @return 结果
     */
    public int insertCostMachine(CostMachine costMachine);

    /**
     * 修改机械工时
     * 
     * @param costMachine 机械工时
     * @return 结果
     */
    public int updateCostMachine(CostMachine costMachine);

    /**
     * 批量删除机械工时
     * 
     * @param costIds 需要删除的机械工时主键集合
     * @return 结果
     */
    public int deleteCostMachineByCostIds(Long[] costIds);

    /**
     * 删除机械工时信息
     * 
     * @param costId 机械工时主键
     * @return 结果
     */
    public int deleteCostMachineByCostId(Long costId);
}

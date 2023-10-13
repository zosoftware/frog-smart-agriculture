package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.CostMaterialMapper;
import com.frog.agriculture.domain.CostMaterial;
import com.frog.agriculture.service.ICostMaterialService;

/**
 * 农资用量Service业务层处理
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@Service
public class CostMaterialServiceImpl implements ICostMaterialService 
{
    @Autowired
    private CostMaterialMapper costMaterialMapper;

    /**
     * 查询农资用量
     * 
     * @param costId 农资用量主键
     * @return 农资用量
     */
    @Override
    public CostMaterial selectCostMaterialByCostId(Long costId)
    {
        return costMaterialMapper.selectCostMaterialByCostId(costId);
    }

    /**
     * 查询农资用量列表
     * 
     * @param costMaterial 农资用量
     * @return 农资用量
     */
    @Override
    public List<CostMaterial> selectCostMaterialList(CostMaterial costMaterial)
    {
        return costMaterialMapper.selectCostMaterialList(costMaterial);
    }

    /**
     * 新增农资用量
     * 
     * @param costMaterial 农资用量
     * @return 结果
     */
    @Override
    public int insertCostMaterial(CostMaterial costMaterial)
    {
        costMaterial.setCreateBy(SecurityUtils.getUserId().toString());
        costMaterial.setCreateTime(DateUtils.getNowDate());
        return costMaterialMapper.insertCostMaterial(costMaterial);
    }

    /**
     * 修改农资用量
     * 
     * @param costMaterial 农资用量
     * @return 结果
     */
    @Override
    public int updateCostMaterial(CostMaterial costMaterial)
    {
        costMaterial.setUpdateBy(SecurityUtils.getUserId().toString());
        costMaterial.setUpdateTime(DateUtils.getNowDate());
        return costMaterialMapper.updateCostMaterial(costMaterial);
    }

    /**
     * 批量删除农资用量
     * 
     * @param costIds 需要删除的农资用量主键
     * @return 结果
     */
    @Override
    public int deleteCostMaterialByCostIds(Long[] costIds)
    {
        return costMaterialMapper.deleteCostMaterialByCostIds(costIds);
    }

    /**
     * 删除农资用量信息
     * 
     * @param costId 农资用量主键
     * @return 结果
     */
    @Override
    public int deleteCostMaterialByCostId(Long costId)
    {
        return costMaterialMapper.deleteCostMaterialByCostId(costId);
    }
}

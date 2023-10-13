package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.CostMaterial;

/**
 * 农资用量Mapper接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface CostMaterialMapper 
{
    /**
     * 查询农资用量
     * 
     * @param costId 农资用量主键
     * @return 农资用量
     */
    public CostMaterial selectCostMaterialByCostId(Long costId);

    /**
     * 查询农资用量列表
     * 
     * @param costMaterial 农资用量
     * @return 农资用量集合
     */
    public List<CostMaterial> selectCostMaterialList(CostMaterial costMaterial);

    /**
     * 新增农资用量
     * 
     * @param costMaterial 农资用量
     * @return 结果
     */
    public int insertCostMaterial(CostMaterial costMaterial);

    /**
     * 修改农资用量
     * 
     * @param costMaterial 农资用量
     * @return 结果
     */
    public int updateCostMaterial(CostMaterial costMaterial);

    /**
     * 删除农资用量
     * 
     * @param costId 农资用量主键
     * @return 结果
     */
    public int deleteCostMaterialByCostId(Long costId);

    /**
     * 批量删除农资用量
     * 
     * @param costIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCostMaterialByCostIds(Long[] costIds);
}

package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.MaterialType;

/**
 * 农资类别Service接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface IMaterialTypeService 
{
    /**
     * 查询农资类别
     * 
     * @param materialTypeId 农资类别主键
     * @return 农资类别
     */
    public MaterialType selectMaterialTypeByMaterialTypeId(Long materialTypeId);

    /**
     * 查询农资类别列表
     * 
     * @param materialType 农资类别
     * @return 农资类别集合
     */
    public List<MaterialType> selectMaterialTypeList(MaterialType materialType);

    /**
     * 新增农资类别
     * 
     * @param materialType 农资类别
     * @return 结果
     */
    public int insertMaterialType(MaterialType materialType);

    /**
     * 修改农资类别
     * 
     * @param materialType 农资类别
     * @return 结果
     */
    public int updateMaterialType(MaterialType materialType);

    /**
     * 批量删除农资类别
     * 
     * @param materialTypeIds 需要删除的农资类别主键集合
     * @return 结果
     */
    public int deleteMaterialTypeByMaterialTypeIds(Long[] materialTypeIds);

    /**
     * 删除农资类别信息
     * 
     * @param materialTypeId 农资类别主键
     * @return 结果
     */
    public int deleteMaterialTypeByMaterialTypeId(Long materialTypeId);
}

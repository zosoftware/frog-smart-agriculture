package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.MaterialTypeMapper;
import com.frog.agriculture.domain.MaterialType;
import com.frog.agriculture.service.IMaterialTypeService;

/**
 * 农资类别Service业务层处理
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@Service
public class MaterialTypeServiceImpl implements IMaterialTypeService 
{
    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    /**
     * 查询农资类别
     * 
     * @param materialTypeId 农资类别主键
     * @return 农资类别
     */
    @Override
    public MaterialType selectMaterialTypeByMaterialTypeId(Long materialTypeId)
    {
        return materialTypeMapper.selectMaterialTypeByMaterialTypeId(materialTypeId);
    }

    /**
     * 查询农资类别列表
     * 
     * @param materialType 农资类别
     * @return 农资类别
     */
    @Override
    public List<MaterialType> selectMaterialTypeList(MaterialType materialType)
    {
        return materialTypeMapper.selectMaterialTypeList(materialType);
    }

    /**
     * 新增农资类别
     * 
     * @param materialType 农资类别
     * @return 结果
     */
    @Override
    public int insertMaterialType(MaterialType materialType)
    {
        materialType.setCreateBy(SecurityUtils.getUserId().toString());
        materialType.setCreateTime(DateUtils.getNowDate());
        return materialTypeMapper.insertMaterialType(materialType);
    }

    /**
     * 修改农资类别
     * 
     * @param materialType 农资类别
     * @return 结果
     */
    @Override
    public int updateMaterialType(MaterialType materialType)
    {
        materialType.setUpdateBy(SecurityUtils.getUserId().toString());
        materialType.setUpdateTime(DateUtils.getNowDate());
        return materialTypeMapper.updateMaterialType(materialType);
    }

    /**
     * 批量删除农资类别
     * 
     * @param materialTypeIds 需要删除的农资类别主键
     * @return 结果
     */
    @Override
    public int deleteMaterialTypeByMaterialTypeIds(Long[] materialTypeIds)
    {
        return materialTypeMapper.deleteMaterialTypeByMaterialTypeIds(materialTypeIds);
    }

    /**
     * 删除农资类别信息
     * 
     * @param materialTypeId 农资类别主键
     * @return 结果
     */
    @Override
    public int deleteMaterialTypeByMaterialTypeId(Long materialTypeId)
    {
        return materialTypeMapper.deleteMaterialTypeByMaterialTypeId(materialTypeId);
    }
}

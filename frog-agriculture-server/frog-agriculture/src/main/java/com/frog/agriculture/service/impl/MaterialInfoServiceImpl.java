package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.MaterialInfoMapper;
import com.frog.agriculture.domain.MaterialInfo;
import com.frog.agriculture.service.IMaterialInfoService;

/**
 * 农资信息Service业务层处理
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@Service
public class MaterialInfoServiceImpl implements IMaterialInfoService 
{
    @Autowired
    private MaterialInfoMapper materialInfoMapper;

    /**
     * 查询农资信息
     * 
     * @param materialId 农资信息主键
     * @return 农资信息
     */
    @Override
    public MaterialInfo selectMaterialInfoByMaterialId(Long materialId)
    {
        return materialInfoMapper.selectMaterialInfoByMaterialId(materialId);
    }

    /**
     * 查询农资信息列表
     * 
     * @param materialInfo 农资信息
     * @return 农资信息
     */
    @Override
    public List<MaterialInfo> selectMaterialInfoList(MaterialInfo materialInfo)
    {
        return materialInfoMapper.selectMaterialInfoList(materialInfo);
    }

    /**
     * 新增农资信息
     * 
     * @param materialInfo 农资信息
     * @return 结果
     */
    @Override
    public int insertMaterialInfo(MaterialInfo materialInfo)
    {
        materialInfo.setCreateBy(SecurityUtils.getUserId().toString());
        materialInfo.setCreateTime(DateUtils.getNowDate());
        return materialInfoMapper.insertMaterialInfo(materialInfo);
    }

    /**
     * 修改农资信息
     * 
     * @param materialInfo 农资信息
     * @return 结果
     */
    @Override
    public int updateMaterialInfo(MaterialInfo materialInfo)
    {
        materialInfo.setUpdateBy(SecurityUtils.getUserId().toString());
        materialInfo.setUpdateTime(DateUtils.getNowDate());
        return materialInfoMapper.updateMaterialInfo(materialInfo);
    }

    /**
     * 批量删除农资信息
     * 
     * @param materialIds 需要删除的农资信息主键
     * @return 结果
     */
    @Override
    public int deleteMaterialInfoByMaterialIds(Long[] materialIds)
    {
        return materialInfoMapper.deleteMaterialInfoByMaterialIds(materialIds);
    }

    /**
     * 删除农资信息信息
     * 
     * @param materialId 农资信息主键
     * @return 结果
     */
    @Override
    public int deleteMaterialInfoByMaterialId(Long materialId)
    {
        return materialInfoMapper.deleteMaterialInfoByMaterialId(materialId);
    }
}

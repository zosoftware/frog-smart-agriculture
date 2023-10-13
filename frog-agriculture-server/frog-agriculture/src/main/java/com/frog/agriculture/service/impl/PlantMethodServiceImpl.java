package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.PlantMethodMapper;
import com.frog.agriculture.domain.PlantMethod;
import com.frog.agriculture.service.IPlantMethodService;

/**
 * 种植方法Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@Service
public class PlantMethodServiceImpl implements IPlantMethodService 
{
    @Autowired
    private PlantMethodMapper plantMethodMapper;

    /**
     * 查询种植方法
     * 
     * @param methodId 种植方法主键
     * @return 种植方法
     */
    @Override
    public PlantMethod selectPlantMethodByMethodId(Long methodId)
    {
        return plantMethodMapper.selectPlantMethodByMethodId(methodId);
    }

    /**
     * 查询种植方法列表
     * 
     * @param plantMethod 种植方法
     * @return 种植方法
     */
    @Override
    public List<PlantMethod> selectPlantMethodList(PlantMethod plantMethod)
    {
        return plantMethodMapper.selectPlantMethodList(plantMethod);
    }

    /**
     * 新增种植方法
     * 
     * @param plantMethod 种植方法
     * @return 结果
     */
    @Override
    public int insertPlantMethod(PlantMethod plantMethod)
    {
        plantMethod.setCreateTime(DateUtils.getNowDate());
        return plantMethodMapper.insertPlantMethod(plantMethod);
    }

    /**
     * 修改种植方法
     * 
     * @param plantMethod 种植方法
     * @return 结果
     */
    @Override
    public int updatePlantMethod(PlantMethod plantMethod)
    {
        plantMethod.setUpdateTime(DateUtils.getNowDate());
        return plantMethodMapper.updatePlantMethod(plantMethod);
    }

    /**
     * 批量删除种植方法
     * 
     * @param methodIds 需要删除的种植方法主键
     * @return 结果
     */
    @Override
    public int deletePlantMethodByMethodIds(Long[] methodIds)
    {
        return plantMethodMapper.deletePlantMethodByMethodIds(methodIds);
    }

    /**
     * 删除种植方法信息
     * 
     * @param methodId 种植方法主键
     * @return 结果
     */
    @Override
    public int deletePlantMethodByMethodId(Long methodId)
    {
        return plantMethodMapper.deletePlantMethodByMethodId(methodId);
    }
}

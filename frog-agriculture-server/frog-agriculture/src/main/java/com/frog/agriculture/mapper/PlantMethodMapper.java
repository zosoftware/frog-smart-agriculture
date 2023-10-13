package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.PlantMethod;

/**
 * 种植方法Mapper接口
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public interface PlantMethodMapper 
{
    /**
     * 查询种植方法
     * 
     * @param methodId 种植方法主键
     * @return 种植方法
     */
    public PlantMethod selectPlantMethodByMethodId(Long methodId);

    /**
     * 查询种植方法列表
     * 
     * @param plantMethod 种植方法
     * @return 种植方法集合
     */
    public List<PlantMethod> selectPlantMethodList(PlantMethod plantMethod);

    /**
     * 新增种植方法
     * 
     * @param plantMethod 种植方法
     * @return 结果
     */
    public int insertPlantMethod(PlantMethod plantMethod);

    /**
     * 修改种植方法
     * 
     * @param plantMethod 种植方法
     * @return 结果
     */
    public int updatePlantMethod(PlantMethod plantMethod);

    /**
     * 删除种植方法
     * 
     * @param methodId 种植方法主键
     * @return 结果
     */
    public int deletePlantMethodByMethodId(Long methodId);

    /**
     * 批量删除种植方法
     * 
     * @param methodIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlantMethodByMethodIds(Long[] methodIds);
}

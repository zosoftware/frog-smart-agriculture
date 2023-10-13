package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.Land;
import com.frog.agriculture.model.LandCrop;

/**
 * 地块Service接口
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public interface ILandService 
{
    /**
     * 查询地块
     * 
     * @param landId 地块主键
     * @return 地块
     */
    public Land selectLandByLandId(Long landId);

    /**
     * 查询地块列表
     * 
     * @param land 地块
     * @return 地块集合
     */
    public List<Land> selectLandList(Land land);

    /**
     * 查询种植地块列表
     * @param land
     * @return
     */
    public List<LandCrop> selectLandCropList(LandCrop land);

    /**
     * 新增地块
     * 
     * @param land 地块
     * @return 结果
     */
    public int insertLand(Land land);

    /**
     * 修改地块
     * 
     * @param land 地块
     * @return 结果
     */
    public int updateLand(Land land);

    /**
     * 批量删除地块
     * 
     * @param landIds 需要删除的地块主键集合
     * @return 结果
     */
    public int deleteLandByLandIds(Long[] landIds);

    /**
     * 删除地块信息
     * 
     * @param landId 地块主键
     * @return 结果
     */
    public int deleteLandByLandId(Long landId);
}

package com.frog.agriculture.service.impl;

import java.util.List;

import com.frog.agriculture.model.LandCrop;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.LandMapper;
import com.frog.agriculture.domain.Land;
import com.frog.agriculture.service.ILandService;

/**
 * 地块Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@Service
public class LandServiceImpl implements ILandService 
{
    @Autowired
    private LandMapper landMapper;

    /**
     * 查询地块
     * 
     * @param landId 地块主键
     * @return 地块
     */
    @Override
    public Land selectLandByLandId(Long landId)
    {
        return landMapper.selectLandByLandId(landId);
    }

    /**
     * 查询地块列表
     * 
     * @param land 地块
     * @return 地块
     */
    @Override
    public List<Land> selectLandList(Land land)
    {
        return landMapper.selectLandList(land);
    }
    /**
     * 查询种植地块列表
     *
     * @param land 地块
     * @return 地块
     */
    @Override
    public List<LandCrop> selectLandCropList(LandCrop land)
    {
        return landMapper.selectLandCropList(land);
    }

    /**
     * 新增地块
     * 
     * @param land 地块
     * @return 结果
     */
    @Override
    public int insertLand(Land land)
    {
        land.setCreateBy(SecurityUtils.getUserId().toString());
        land.setCreateTime(DateUtils.getNowDate());
        return landMapper.insertLand(land);
    }

    /**
     * 修改地块
     * 
     * @param land 地块
     * @return 结果
     */
    @Override
    public int updateLand(Land land)
    {
        land.setUpdateBy(SecurityUtils.getUserId().toString());
        land.setUpdateTime(DateUtils.getNowDate());
        return landMapper.updateLand(land);
    }

    /**
     * 批量删除地块
     * 
     * @param landIds 需要删除的地块主键
     * @return 结果
     */
    @Override
    public int deleteLandByLandIds(Long[] landIds)
    {
        return landMapper.deleteLandByLandIds(landIds);
    }

    /**
     * 删除地块信息
     * 
     * @param landId 地块主键
     * @return 结果
     */
    @Override
    public int deleteLandByLandId(Long landId)
    {
        return landMapper.deleteLandByLandId(landId);
    }
}

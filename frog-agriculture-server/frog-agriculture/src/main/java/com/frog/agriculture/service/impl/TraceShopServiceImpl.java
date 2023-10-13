package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TraceShopMapper;
import com.frog.agriculture.domain.TraceShop;
import com.frog.agriculture.service.ITraceShopService;

/**
 * 店铺Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
@Service
public class TraceShopServiceImpl implements ITraceShopService 
{
    @Autowired
    private TraceShopMapper traceShopMapper;

    /**
     * 查询店铺
     * 
     * @param shopId 店铺主键
     * @return 店铺
     */
    @Override
    public TraceShop selectTraceShopByShopId(Long shopId)
    {
        return traceShopMapper.selectTraceShopByShopId(shopId);
    }

    /**
     * 查询店铺列表
     * 
     * @param traceShop 店铺
     * @return 店铺
     */
    @Override
    public List<TraceShop> selectTraceShopList(TraceShop traceShop)
    {
        return traceShopMapper.selectTraceShopList(traceShop);
    }

    /**
     * 新增店铺
     * 
     * @param traceShop 店铺
     * @return 结果
     */
    @Override
    public int insertTraceShop(TraceShop traceShop)
    {
        traceShop.setCreateBy(SecurityUtils.getUserId().toString());
        traceShop.setCreateTime(DateUtils.getNowDate());
        return traceShopMapper.insertTraceShop(traceShop);
    }

    /**
     * 修改店铺
     * 
     * @param traceShop 店铺
     * @return 结果
     */
    @Override
    public int updateTraceShop(TraceShop traceShop)
    {
        traceShop.setUpdateBy(SecurityUtils.getUserId().toString());
        traceShop.setUpdateTime(DateUtils.getNowDate());
        return traceShopMapper.updateTraceShop(traceShop);
    }

    /**
     * 批量删除店铺
     * 
     * @param shopIds 需要删除的店铺主键
     * @return 结果
     */
    @Override
    public int deleteTraceShopByShopIds(Long[] shopIds)
    {
        return traceShopMapper.deleteTraceShopByShopIds(shopIds);
    }

    /**
     * 删除店铺信息
     * 
     * @param shopId 店铺主键
     * @return 结果
     */
    @Override
    public int deleteTraceShopByShopId(Long shopId)
    {
        return traceShopMapper.deleteTraceShopByShopId(shopId);
    }
}

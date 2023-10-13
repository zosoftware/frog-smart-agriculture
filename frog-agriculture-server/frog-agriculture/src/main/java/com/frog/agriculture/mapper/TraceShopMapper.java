package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.TraceShop;

/**
 * 店铺Mapper接口
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
public interface TraceShopMapper 
{
    /**
     * 查询店铺
     * 
     * @param shopId 店铺主键
     * @return 店铺
     */
    public TraceShop selectTraceShopByShopId(Long shopId);

    /**
     * 查询店铺列表
     * 
     * @param traceShop 店铺
     * @return 店铺集合
     */
    public List<TraceShop> selectTraceShopList(TraceShop traceShop);

    /**
     * 新增店铺
     * 
     * @param traceShop 店铺
     * @return 结果
     */
    public int insertTraceShop(TraceShop traceShop);

    /**
     * 修改店铺
     * 
     * @param traceShop 店铺
     * @return 结果
     */
    public int updateTraceShop(TraceShop traceShop);

    /**
     * 删除店铺
     * 
     * @param shopId 店铺主键
     * @return 结果
     */
    public int deleteTraceShopByShopId(Long shopId);

    /**
     * 批量删除店铺
     * 
     * @param shopIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTraceShopByShopIds(Long[] shopIds);
}

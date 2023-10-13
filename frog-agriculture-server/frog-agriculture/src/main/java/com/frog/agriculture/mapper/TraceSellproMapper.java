package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.TraceSellpro;

/**
 * 溯源产品Mapper接口
 * 
 * @author nealtsiao
 * @date 2023-08-07
 */
public interface TraceSellproMapper 
{
    /**
     * 查询溯源产品
     * 
     * @param sellproId 溯源产品主键
     * @return 溯源产品
     */
    public TraceSellpro selectTraceSellproBySellproId(Long sellproId);

    /**
     * 查询溯源产品列表
     * 
     * @param traceSellpro 溯源产品
     * @return 溯源产品集合
     */
    public List<TraceSellpro> selectTraceSellproList(TraceSellpro traceSellpro);

    /**
     * 新增溯源产品
     * 
     * @param traceSellpro 溯源产品
     * @return 结果
     */
    public int insertTraceSellpro(TraceSellpro traceSellpro);

    /**
     * 修改溯源产品
     * 
     * @param traceSellpro 溯源产品
     * @return 结果
     */
    public int updateTraceSellpro(TraceSellpro traceSellpro);

    /**
     * 删除溯源产品
     * 
     * @param sellproId 溯源产品主键
     * @return 结果
     */
    public int deleteTraceSellproBySellproId(Long sellproId);

    /**
     * 批量删除溯源产品
     * 
     * @param sellproIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTraceSellproBySellproIds(Long[] sellproIds);
}

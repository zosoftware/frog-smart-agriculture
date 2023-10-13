package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.TraceSellproMapper;
import com.frog.agriculture.domain.TraceSellpro;
import com.frog.agriculture.service.ITraceSellproService;

/**
 * 溯源产品Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-08-07
 */
@Service
public class TraceSellproServiceImpl implements ITraceSellproService 
{
    @Autowired
    private TraceSellproMapper traceSellproMapper;

    /**
     * 查询溯源产品
     * 
     * @param sellproId 溯源产品主键
     * @return 溯源产品
     */
    @Override
    public TraceSellpro selectTraceSellproBySellproId(Long sellproId)
    {
        return traceSellproMapper.selectTraceSellproBySellproId(sellproId);
    }

    /**
     * 查询溯源产品列表
     * 
     * @param traceSellpro 溯源产品
     * @return 溯源产品
     */
    @Override
    public List<TraceSellpro> selectTraceSellproList(TraceSellpro traceSellpro)
    {
        return traceSellproMapper.selectTraceSellproList(traceSellpro);
    }

    /**
     * 新增溯源产品
     * 
     * @param traceSellpro 溯源产品
     * @return 结果
     */
    @Override
    public int insertTraceSellpro(TraceSellpro traceSellpro)
    {
        traceSellpro.setCreateBy(SecurityUtils.getUserId().toString());
        traceSellpro.setCreateTime(DateUtils.getNowDate());
        return traceSellproMapper.insertTraceSellpro(traceSellpro);
    }

    /**
     * 修改溯源产品
     * 
     * @param traceSellpro 溯源产品
     * @return 结果
     */
    @Override
    public int updateTraceSellpro(TraceSellpro traceSellpro)
    {
        traceSellpro.setUpdateBy(SecurityUtils.getUserId().toString());
        traceSellpro.setUpdateTime(DateUtils.getNowDate());
        return traceSellproMapper.updateTraceSellpro(traceSellpro);
    }

    /**
     * 批量删除溯源产品
     * 
     * @param sellproIds 需要删除的溯源产品主键
     * @return 结果
     */
    @Override
    public int deleteTraceSellproBySellproIds(Long[] sellproIds)
    {
        return traceSellproMapper.deleteTraceSellproBySellproIds(sellproIds);
    }

    /**
     * 删除溯源产品信息
     * 
     * @param sellproId 溯源产品主键
     * @return 结果
     */
    @Override
    public int deleteTraceSellproBySellproId(Long sellproId)
    {
        return traceSellproMapper.deleteTraceSellproBySellproId(sellproId);
    }
}

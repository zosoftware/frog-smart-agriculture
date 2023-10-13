package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.BaseinfoMapper;
import com.frog.agriculture.domain.Baseinfo;
import com.frog.agriculture.service.IBaseinfoService;

/**
 * 基地信息Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@Service
public class BaseinfoServiceImpl implements IBaseinfoService 
{
    @Autowired
    private BaseinfoMapper baseinfoMapper;

    /**
     * 查询基地信息
     * 
     * @param baseId 基地信息主键
     * @return 基地信息
     */
    @Override
    public Baseinfo selectBaseinfoByBaseId(Long baseId)
    {
        return baseinfoMapper.selectBaseinfoByBaseId(baseId);
    }

    /**
     * 查询基地信息
     *
     * @return 基地信息
     */
    @Override
    public Baseinfo selectBaseinfoLimitOne()
    {
        return baseinfoMapper.selectBaseinfoLimitOne();
    }

    /**
     * 查询基地信息列表
     * 
     * @param baseinfo 基地信息
     * @return 基地信息
     */
    @Override
    public List<Baseinfo> selectBaseinfoList(Baseinfo baseinfo)
    {
        return baseinfoMapper.selectBaseinfoList(baseinfo);
    }

    /**
     * 新增基地信息
     * 
     * @param baseinfo 基地信息
     * @return 结果
     */
    @Override
    public int insertBaseinfo(Baseinfo baseinfo)
    {
        baseinfo.setCreateTime(DateUtils.getNowDate());
        return baseinfoMapper.insertBaseinfo(baseinfo);
    }

    /**
     * 修改基地信息
     * 
     * @param baseinfo 基地信息
     * @return 结果
     */
    @Override
    public int updateBaseinfo(Baseinfo baseinfo)
    {
        baseinfo.setUpdateTime(DateUtils.getNowDate());
        return baseinfoMapper.updateBaseinfo(baseinfo);
    }

    /**
     * 批量删除基地信息
     * 
     * @param baseIds 需要删除的基地信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseinfoByBaseIds(Long[] baseIds)
    {
        return baseinfoMapper.deleteBaseinfoByBaseIds(baseIds);
    }

    /**
     * 删除基地信息信息
     * 
     * @param baseId 基地信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseinfoByBaseId(Long baseId)
    {
        return baseinfoMapper.deleteBaseinfoByBaseId(baseId);
    }
}

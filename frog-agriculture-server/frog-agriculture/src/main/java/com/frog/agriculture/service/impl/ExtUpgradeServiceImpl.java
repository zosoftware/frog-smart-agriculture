package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.ExtUpgradeMapper;
import com.frog.agriculture.domain.ExtUpgrade;
import com.frog.agriculture.service.IExtUpgradeService;

/**
 * App升级Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-08-23
 */
@Service
public class ExtUpgradeServiceImpl implements IExtUpgradeService 
{
    @Autowired
    private ExtUpgradeMapper extUpgradeMapper;

    /**
     * 查询App升级
     * 
     * @param recordId App升级主键
     * @return App升级
     */
    @Override
    public ExtUpgrade selectExtUpgradeByRecordId(Long recordId)
    {
        return extUpgradeMapper.selectExtUpgradeByRecordId(recordId);
    }

    /**
     * 查询App升级列表
     * 
     * @param extUpgrade App升级
     * @return App升级
     */
    @Override
    public List<ExtUpgrade> selectExtUpgradeList(ExtUpgrade extUpgrade)
    {
        return extUpgradeMapper.selectExtUpgradeList(extUpgrade);
    }

    /**
     * 新增App升级
     * 
     * @param extUpgrade App升级
     * @return 结果
     */
    @Override
    public int insertExtUpgrade(ExtUpgrade extUpgrade)
    {
        extUpgrade.setCreateBy(SecurityUtils.getUserId().toString());
        extUpgrade.setCreateTime(DateUtils.getNowDate());
        return extUpgradeMapper.insertExtUpgrade(extUpgrade);
    }

    /**
     * 修改App升级
     * 
     * @param extUpgrade App升级
     * @return 结果
     */
    @Override
    public int updateExtUpgrade(ExtUpgrade extUpgrade)
    {
        extUpgrade.setUpdateBy(SecurityUtils.getUserId().toString());
        extUpgrade.setUpdateTime(DateUtils.getNowDate());
        return extUpgradeMapper.updateExtUpgrade(extUpgrade);
    }

    /**
     * 批量删除App升级
     * 
     * @param recordIds 需要删除的App升级主键
     * @return 结果
     */
    @Override
    public int deleteExtUpgradeByRecordIds(Long[] recordIds)
    {
        return extUpgradeMapper.deleteExtUpgradeByRecordIds(recordIds);
    }

    /**
     * 删除App升级信息
     * 
     * @param recordId App升级主键
     * @return 结果
     */
    @Override
    public int deleteExtUpgradeByRecordId(Long recordId)
    {
        return extUpgradeMapper.deleteExtUpgradeByRecordId(recordId);
    }
}

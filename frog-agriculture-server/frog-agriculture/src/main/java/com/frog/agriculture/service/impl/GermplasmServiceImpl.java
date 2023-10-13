package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.GermplasmMapper;
import com.frog.agriculture.domain.Germplasm;
import com.frog.agriculture.service.IGermplasmService;

/**
 * 种质Service业务层处理
 *
 * @author nealtsiao
 * @date 2023-05-18
 */
@Service
public class GermplasmServiceImpl implements IGermplasmService
{
    @Autowired
    private GermplasmMapper germplasmMapper;

    /**
     * 查询种质
     *
     * @param germplasmId 种质主键
     * @return 种质
     */
    @Override
    public Germplasm selectGermplasmByGermplasmId(Long germplasmId)
    {
        return germplasmMapper.selectGermplasmByGermplasmId(germplasmId);
    }

    /**
     * 查询种质列表
     *
     * @param germplasm 种质
     * @return 种质
     */
    @Override
    public List<Germplasm> selectGermplasmList(Germplasm germplasm)
    {
        return germplasmMapper.selectGermplasmList(germplasm);
    }

    /**
     * 新增种质
     *
     * @param germplasm 种质
     * @return 结果
     */
    @Override
    public int insertGermplasm(Germplasm germplasm)
    {
        germplasm.setCreateBy(SecurityUtils.getUserId().toString());
        germplasm.setCreateTime(DateUtils.getNowDate());
        return germplasmMapper.insertGermplasm(germplasm);
    }

    /**
     * 修改种质
     *
     * @param germplasm 种质
     * @return 结果
     */
    @Override
    public int updateGermplasm(Germplasm germplasm)
    {
        germplasm.setUpdateBy(SecurityUtils.getUserId().toString());
        germplasm.setUpdateTime(DateUtils.getNowDate());
        return germplasmMapper.updateGermplasm(germplasm);
    }

    /**
     * 批量删除种质
     *
     * @param germplasmIds 需要删除的种质主键
     * @return 结果
     */
    @Override
    public int deleteGermplasmByGermplasmIds(Long[] germplasmIds)
    {
        return germplasmMapper.deleteGermplasmByGermplasmIds(germplasmIds);
    }

    /**
     * 删除种质信息
     *
     * @param germplasmId 种质主键
     * @return 结果
     */
    @Override
    public int deleteGermplasmByGermplasmId(Long germplasmId)
    {
        return germplasmMapper.deleteGermplasmByGermplasmId(germplasmId);
    }
}
package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.GermplasmIntroMapper;
import com.frog.agriculture.domain.GermplasmIntro;
import com.frog.agriculture.service.IGermplasmIntroService;

/**
 * 种质介绍Service业务层处理
 *
 * @author nealtsiao
 * @date 2023-05-13
 */
@Service
public class GermplasmIntroServiceImpl implements IGermplasmIntroService
{
    @Autowired
    private GermplasmIntroMapper germplasmIntroMapper;

    /**
     * 查询种质介绍
     *
     * @param introId 种质介绍主键
     * @return 种质介绍
     */
    @Override
    public GermplasmIntro selectGermplasmIntroByIntroId(Long introId)
    {
        return germplasmIntroMapper.selectGermplasmIntroByIntroId(introId);
    }

    /**
     * 查询种质介绍列表
     *
     * @param germplasmIntro 种质介绍
     * @return 种质介绍
     */
    @Override
    public List<GermplasmIntro> selectGermplasmIntroList(GermplasmIntro germplasmIntro)
    {
        return germplasmIntroMapper.selectGermplasmIntroList(germplasmIntro);
    }

    /**
     * 新增种质介绍
     *
     * @param germplasmIntro 种质介绍
     * @return 结果
     */
    @Override
    public int insertGermplasmIntro(GermplasmIntro germplasmIntro)
    {
        germplasmIntro.setCreateTime(DateUtils.getNowDate());
        return germplasmIntroMapper.insertGermplasmIntro(germplasmIntro);
    }

    /**
     * 修改种质介绍
     *
     * @param germplasmIntro 种质介绍
     * @return 结果
     */
    @Override
    public int updateGermplasmIntro(GermplasmIntro germplasmIntro)
    {
        germplasmIntro.setUpdateTime(DateUtils.getNowDate());
        return germplasmIntroMapper.updateGermplasmIntro(germplasmIntro);
    }

    /**
     * 批量删除种质介绍
     *
     * @param introIds 需要删除的种质介绍主键
     * @return 结果
     */
    @Override
    public int deleteGermplasmIntroByIntroIds(Long[] introIds)
    {
        return germplasmIntroMapper.deleteGermplasmIntroByIntroIds(introIds);
    }

    /**
     * 删除种质介绍信息
     *
     * @param introId 种质介绍主键
     * @return 结果
     */
    @Override
    public int deleteGermplasmIntroByIntroId(Long introId)
    {
        return germplasmIntroMapper.deleteGermplasmIntroByIntroId(introId);
    }
}

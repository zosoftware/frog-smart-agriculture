package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.GermplasmIntro;

/**
 * 种质介绍Service接口
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public interface IGermplasmIntroService 
{
    /**
     * 查询种质介绍
     * 
     * @param introId 种质介绍主键
     * @return 种质介绍
     */
    public GermplasmIntro selectGermplasmIntroByIntroId(Long introId);

    /**
     * 查询种质介绍列表
     * 
     * @param germplasmIntro 种质介绍
     * @return 种质介绍集合
     */
    public List<GermplasmIntro> selectGermplasmIntroList(GermplasmIntro germplasmIntro);

    /**
     * 新增种质介绍
     * 
     * @param germplasmIntro 种质介绍
     * @return 结果
     */
    public int insertGermplasmIntro(GermplasmIntro germplasmIntro);

    /**
     * 修改种质介绍
     * 
     * @param germplasmIntro 种质介绍
     * @return 结果
     */
    public int updateGermplasmIntro(GermplasmIntro germplasmIntro);

    /**
     * 批量删除种质介绍
     * 
     * @param introIds 需要删除的种质介绍主键集合
     * @return 结果
     */
    public int deleteGermplasmIntroByIntroIds(Long[] introIds);

    /**
     * 删除种质介绍信息
     * 
     * @param introId 种质介绍主键
     * @return 结果
     */
    public int deleteGermplasmIntroByIntroId(Long introId);
}

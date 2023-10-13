package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.Germplasm;

/**
 * 种质Service接口
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public interface IGermplasmService 
{
    /**
     * 查询种质
     * 
     * @param germplasmId 种质主键
     * @return 种质
     */
    public Germplasm selectGermplasmByGermplasmId(Long germplasmId);

    /**
     * 查询种质列表
     * 
     * @param germplasm 种质
     * @return 种质集合
     */
    public List<Germplasm> selectGermplasmList(Germplasm germplasm);

    /**
     * 新增种质
     * 
     * @param germplasm 种质
     * @return 结果
     */
    public int insertGermplasm(Germplasm germplasm);

    /**
     * 修改种质
     * 
     * @param germplasm 种质
     * @return 结果
     */
    public int updateGermplasm(Germplasm germplasm);

    /**
     * 批量删除种质
     * 
     * @param germplasmIds 需要删除的种质主键集合
     * @return 结果
     */
    public int deleteGermplasmByGermplasmIds(Long[] germplasmIds);

    /**
     * 删除种质信息
     * 
     * @param germplasmId 种质主键
     * @return 结果
     */
    public int deleteGermplasmByGermplasmId(Long germplasmId);
}

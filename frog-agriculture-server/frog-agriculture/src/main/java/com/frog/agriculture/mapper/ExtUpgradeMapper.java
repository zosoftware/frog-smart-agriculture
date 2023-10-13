package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.ExtUpgrade;

/**
 * App升级Mapper接口
 * 
 * @author nealtsiao
 * @date 2023-08-23
 */
public interface ExtUpgradeMapper 
{
    /**
     * 查询App升级
     * 
     * @param recordId App升级主键
     * @return App升级
     */
    public ExtUpgrade selectExtUpgradeByRecordId(Long recordId);

    /**
     * 查询App升级列表
     * 
     * @param extUpgrade App升级
     * @return App升级集合
     */
    public List<ExtUpgrade> selectExtUpgradeList(ExtUpgrade extUpgrade);

    /**
     * 新增App升级
     * 
     * @param extUpgrade App升级
     * @return 结果
     */
    public int insertExtUpgrade(ExtUpgrade extUpgrade);

    /**
     * 修改App升级
     * 
     * @param extUpgrade App升级
     * @return 结果
     */
    public int updateExtUpgrade(ExtUpgrade extUpgrade);

    /**
     * 删除App升级
     * 
     * @param recordId App升级主键
     * @return 结果
     */
    public int deleteExtUpgradeByRecordId(Long recordId);

    /**
     * 批量删除App升级
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExtUpgradeByRecordIds(Long[] recordIds);
}

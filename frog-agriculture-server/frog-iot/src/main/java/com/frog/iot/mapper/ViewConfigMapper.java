package com.frog.iot.mapper;

import java.util.List;
import com.frog.iot.domain.ViewConfig;
import org.springframework.stereotype.Repository;

/**
 * 界面可视化配置Mapper接口
 * 
 * @author kerwincui
 * @date 2022-11-15
 */
@Repository
public interface ViewConfigMapper 
{
    /**
     * 查询界面可视化配置
     * 
     * @param viewId 界面可视化配置主键
     * @return 界面可视化配置
     */
    public ViewConfig selectViewConfigByViewId(Long viewId);

    /**
     * 查询界面可视化配置列表
     * 
     * @param viewConfig 界面可视化配置
     * @return 界面可视化配置集合
     */
    public List<ViewConfig> selectViewConfigList(ViewConfig viewConfig);

    /**
     * 新增界面可视化配置
     * 
     * @param viewConfig 界面可视化配置
     * @return 结果
     */
    public int insertViewConfig(ViewConfig viewConfig);

    /**
     * 修改界面可视化配置
     * 
     * @param viewConfig 界面可视化配置
     * @return 结果
     */
    public int updateViewConfig(ViewConfig viewConfig);

    /**
     * 删除界面可视化配置
     * 
     * @param viewId 界面可视化配置主键
     * @return 结果
     */
    public int deleteViewConfigByViewId(Long viewId);

    /**
     * 批量删除界面可视化配置
     * 
     * @param viewIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteViewConfigByViewIds(Long[] viewIds);
}

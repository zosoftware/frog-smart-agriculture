package com.frog.iot.service;

import java.util.List;
import com.frog.iot.domain.ViewConfig;

/**
 * 界面可视化配置Service接口
 * 
 * @author kerwincui
 * @date 2022-11-15
 */
public interface IViewConfigService 
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
    public Long insertViewConfig(ViewConfig viewConfig);

    /**
     * 修改界面可视化配置
     * 
     * @param viewConfig 界面可视化配置
     * @return 结果
     */
    public int updateViewConfig(ViewConfig viewConfig);

    /**
     * 批量删除界面可视化配置
     * 
     * @param viewIds 需要删除的界面可视化配置主键集合
     * @return 结果
     */
    public int deleteViewConfigByViewIds(Long[] viewIds);

    /**
     * 删除界面可视化配置信息
     * 
     * @param viewId 界面可视化配置主键
     * @return 结果
     */
    public int deleteViewConfigByViewId(Long viewId);
}

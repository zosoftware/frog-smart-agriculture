package com.frog.iot.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.iot.mapper.ViewConfigMapper;
import com.frog.iot.domain.ViewConfig;
import com.frog.iot.service.IViewConfigService;

/**
 * 界面可视化配置Service业务层处理
 * 
 * @author kerwincui
 * @date 2022-11-15
 */
@Service
public class ViewConfigServiceImpl implements IViewConfigService 
{
    @Autowired
    private ViewConfigMapper viewConfigMapper;

    /**
     * 查询界面可视化配置
     * 
     * @param viewId 界面可视化配置主键
     * @return 界面可视化配置
     */
    @Override
    public ViewConfig selectViewConfigByViewId(Long viewId)
    {
        return viewConfigMapper.selectViewConfigByViewId(viewId);
    }

    /**
     * 查询界面可视化配置列表
     * 
     * @param viewConfig 界面可视化配置
     * @return 界面可视化配置
     */
    @Override
    public List<ViewConfig> selectViewConfigList(ViewConfig viewConfig)
    {
        return viewConfigMapper.selectViewConfigList(viewConfig);
    }

    /**
     * 新增界面可视化配置
     * 
     * @param viewConfig 界面可视化配置
     * @return 结果
     */
    @Override
    public Long insertViewConfig(ViewConfig viewConfig)
    {
        viewConfig.setCreateTime(DateUtils.getNowDate());
        viewConfigMapper.insertViewConfig(viewConfig);
        return viewConfig.getViewId();
    }

    /**
     * 修改界面可视化配置
     * 
     * @param viewConfig 界面可视化配置
     * @return 结果
     */
    @Override
    public int updateViewConfig(ViewConfig viewConfig)
    {
        viewConfig.setUpdateTime(DateUtils.getNowDate());
        return viewConfigMapper.updateViewConfig(viewConfig);
    }

    /**
     * 批量删除界面可视化配置
     * 
     * @param viewIds 需要删除的界面可视化配置主键
     * @return 结果
     */
    @Override
    public int deleteViewConfigByViewIds(Long[] viewIds)
    {
        return viewConfigMapper.deleteViewConfigByViewIds(viewIds);
    }

    /**
     * 删除界面可视化配置信息
     * 
     * @param viewId 界面可视化配置主键
     * @return 结果
     */
    @Override
    public int deleteViewConfigByViewId(Long viewId)
    {
        return viewConfigMapper.deleteViewConfigByViewId(viewId);
    }
}

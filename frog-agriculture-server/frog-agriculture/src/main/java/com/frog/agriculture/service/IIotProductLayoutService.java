package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.IotProductLayout;

/**
 * 产品布局Service接口
 * 
 * @author nealtsiao
 * @date 2023-08-11
 */
public interface IIotProductLayoutService 
{

    /**
     * 查询产品布局列表
     * 
     * @param iotProductLayout 产品布局
     * @return 产品布局集合
     */
    public List<IotProductLayout> selectIotProductLayoutList(IotProductLayout iotProductLayout);

    /**
     * 新增产品布局
     * 
     * @param iotProductLayoutList 产品布局
     * @return 结果
     */
    public int insertIotProductLayout(List<IotProductLayout> iotProductLayoutList);


    /**
     * 删除产品布局信息
     * 
     * @param productId 产品布局主键
     * @return 结果
     */
    public int deleteIotProductLayoutByProductIdId(Long productId);
}

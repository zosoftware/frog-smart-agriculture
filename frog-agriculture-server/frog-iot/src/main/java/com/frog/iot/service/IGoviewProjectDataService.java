package com.frog.iot.service;

import java.util.List;
import com.frog.iot.domain.GoviewProjectData;

/**
 * 项目数据关联Service接口
 * 
 * @author kami
 * @date 2022-10-27
 */
public interface IGoviewProjectDataService 
{
    /**
     * 查询项目数据关联
     * 
     * @param id 项目数据关联主键
     * @return 项目数据关联
     */
    public GoviewProjectData selectGoviewProjectDataById(String id);

    /**
     * 查询项目数据关联列表
     * 
     * @param goviewProjectData 项目数据关联
     * @return 项目数据关联集合
     */
    public List<GoviewProjectData> selectGoviewProjectDataList(GoviewProjectData goviewProjectData);

    /**
     * 新增项目数据关联
     * 
     * @param goviewProjectData 项目数据关联
     * @return 结果
     */
    public int insertGoviewProjectData(GoviewProjectData goviewProjectData);

    /**
     * 修改项目数据关联
     * 
     * @param goviewProjectData 项目数据关联
     * @return 结果
     */
    public int updateGoviewProjectData(GoviewProjectData goviewProjectData);

    /**
     * 批量删除项目数据关联
     * 
     * @param ids 需要删除的项目数据关联主键集合
     * @return 结果
     */
    public int deleteGoviewProjectDataByIds(String[] ids);

    /**
     * 删除项目数据关联信息
     * 
     * @param id 项目数据关联主键
     * @return 结果
     */
    public int deleteGoviewProjectDataById(String id);

    /**
     * 根据projectID查询内容数据
     * @param projectId
     * @return
     */
	public GoviewProjectData selectGoviewProjectDataByProjectId(String projectId);

    /**
     * 插入或新增大屏数据
     * @param data
     * @return
     */
	public int insertOrUpdateGoviewProjectData(GoviewProjectData data);

}

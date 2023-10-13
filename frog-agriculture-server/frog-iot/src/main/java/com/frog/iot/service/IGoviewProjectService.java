package com.frog.iot.service;

import java.util.List;
import com.frog.iot.domain.GoviewProject;

/**
 * 项目Service接口
 * 
 * @author kami
 * @date 2022-10-27
 */
public interface IGoviewProjectService 
{
    /**
     * 查询项目
     * 
     * @param id 项目主键
     * @return 项目
     */
    public GoviewProject selectGoviewProjectById(String id);

    /**
     * 查询项目列表
     * 
     * @param goviewProject 项目
     * @return 项目集合
     */
    public List<GoviewProject> selectGoviewProjectList(GoviewProject goviewProject);

    /**
     * 新增项目
     * 
     * @param goviewProject 项目
     * @return 结果
     */
    public String insertGoviewProject(GoviewProject goviewProject);

    /**
     * 修改项目
     * 
     * @param goviewProject 项目
     * @return 结果
     */
    public int updateGoviewProject(GoviewProject goviewProject);

    /**
     * 批量删除项目
     * 
     * @param ids 需要删除的项目主键集合
     * @return 结果
     */
    public int deleteGoviewProjectByIds(String[] ids);

    /**
     * 删除项目信息
     * 
     * @param id 项目主键
     * @return 结果
     */
    public int deleteGoviewProjectById(String id);
}

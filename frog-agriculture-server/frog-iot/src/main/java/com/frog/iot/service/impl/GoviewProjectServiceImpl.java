package com.frog.iot.service.impl;

import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import com.frog.common.utils.uuid.IdUtils;
import com.frog.iot.domain.GoviewProject;
import com.frog.iot.mapper.GoviewProjectMapper;
import com.frog.iot.service.IGoviewProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目Service业务层处理
 * 
 * @author kami
 * @date 2022-10-27
 */
@Service
public class GoviewProjectServiceImpl implements IGoviewProjectService 
{
    @Autowired
    private GoviewProjectMapper goviewProjectMapper;

    /**
     * 查询项目
     * 
     * @param id 项目主键
     * @return 项目
     */
    @Override
    public GoviewProject selectGoviewProjectById(String id)
    {
        return goviewProjectMapper.selectGoviewProjectById(id);
    }

    /**
     * 查询项目列表
     * 
     * @param goviewProject 项目
     * @return 项目
     */
    @Override
    public List<GoviewProject> selectGoviewProjectList(GoviewProject goviewProject)
    {
        return goviewProjectMapper.selectGoviewProjectList(goviewProject);
    }

    /**
     * 新增项目
     * 
     * @param goviewProject 项目
     * @return 结果
     */
    @Override
    public String insertGoviewProject(GoviewProject goviewProject)
    {
        goviewProject.setId(IdUtils.simpleUUID());
        goviewProject.setCreateTime(DateUtils.getNowDate());
        goviewProject.setCreateBy(SecurityUtils.getUserId().toString());
        goviewProject.setUpdateTime(DateUtils.getNowDate());
        int i = goviewProjectMapper.insertGoviewProject(goviewProject);
        if(i > 0){
            return goviewProject.getId();
        }
        return null;
    }

    /**
     * 修改项目
     * 
     * @param goviewProject 项目
     * @return 结果
     */
    @Override
    public int updateGoviewProject(GoviewProject goviewProject)
    {
        goviewProject.setUpdateTime(DateUtils.getNowDate());
        return goviewProjectMapper.updateGoviewProject(goviewProject);
    }

    /**
     * 批量删除项目
     * 
     * @param ids 需要删除的项目主键
     * @return 结果
     */
    @Override
    public int deleteGoviewProjectByIds(String[] ids)
    {
        return goviewProjectMapper.deleteGoviewProjectByIds(ids);
    }

    /**
     * 删除项目信息
     * 
     * @param id 项目主键
     * @return 结果
     */
    @Override
    public int deleteGoviewProjectById(String id)
    {
        return goviewProjectMapper.deleteGoviewProjectById(id);
    }
}

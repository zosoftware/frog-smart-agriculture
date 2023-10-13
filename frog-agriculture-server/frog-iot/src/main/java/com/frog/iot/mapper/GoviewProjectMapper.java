package com.frog.iot.mapper;

import java.util.List;
import com.frog.iot.domain.GoviewProject;
import org.springframework.stereotype.Repository;

/**
 * 项目Mapper接口
 * 
 * @author kami
 * @date 2022-10-27
 */
@Repository
public interface GoviewProjectMapper 
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
    public int insertGoviewProject(GoviewProject goviewProject);

    /**
     * 修改项目
     * 
     * @param goviewProject 项目
     * @return 结果
     */
    public int updateGoviewProject(GoviewProject goviewProject);

    /**
     * 删除项目
     * 
     * @param id 项目主键
     * @return 结果
     */
    public int deleteGoviewProjectById(String id);

    /**
     * 批量删除项目
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoviewProjectByIds(String[] ids);
}

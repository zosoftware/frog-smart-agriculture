package com.frog.iot.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import com.frog.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.iot.mapper.GoviewProjectDataMapper;
import com.frog.iot.domain.GoviewProjectData;
import com.frog.iot.service.IGoviewProjectDataService;

/**
 * 项目数据关联Service业务层处理
 * 
 * @author kami
 * @date 2022-10-27
 */
@Service
public class GoviewProjectDataServiceImpl implements IGoviewProjectDataService 
{
    @Autowired
    private GoviewProjectDataMapper goviewProjectDataMapper;

    /**
     * 查询项目数据关联
     * 
     * @param id 项目数据关联主键
     * @return 项目数据关联
     */
    @Override
    public GoviewProjectData selectGoviewProjectDataById(String id)
    {
        return goviewProjectDataMapper.selectGoviewProjectDataById(id);
    }

    /**
     * 查询项目数据关联列表
     * 
     * @param goviewProjectData 项目数据关联
     * @return 项目数据关联
     */
    @Override
    public List<GoviewProjectData> selectGoviewProjectDataList(GoviewProjectData goviewProjectData)
    {
        return goviewProjectDataMapper.selectGoviewProjectDataList(goviewProjectData);
    }

    /**
     * 新增项目数据关联
     * 
     * @param goviewProjectData 项目数据关联
     * @return 结果
     */
    @Override
    public int insertGoviewProjectData(GoviewProjectData goviewProjectData)
    {
        goviewProjectData.setId(IdUtils.simpleUUID());
        goviewProjectData.setCreateBy(SecurityUtils.getUserId().toString());
        goviewProjectData.setCreateTime(DateUtils.getNowDate());
        goviewProjectData.setUpdateTime(DateUtils.getNowDate());
        return goviewProjectDataMapper.insertGoviewProjectData(goviewProjectData);
    }

    /**
     * 修改项目数据关联
     * 
     * @param goviewProjectData 项目数据关联
     * @return 结果
     */
    @Override
    public int updateGoviewProjectData(GoviewProjectData goviewProjectData)
    {
        goviewProjectData.setUpdateTime(DateUtils.getNowDate());
        return goviewProjectDataMapper.updateGoviewProjectData(goviewProjectData);
    }

    /**
     * 批量删除项目数据关联
     * 
     * @param ids 需要删除的项目数据关联主键
     * @return 结果
     */
    @Override
    public int deleteGoviewProjectDataByIds(String[] ids)
    {
        return goviewProjectDataMapper.deleteGoviewProjectDataByIds(ids);
    }

    /**
     * 删除项目数据关联信息
     * 
     * @param id 项目数据关联主键
     * @return 结果
     */
    @Override
    public int deleteGoviewProjectDataById(String id)
    {
        return goviewProjectDataMapper.deleteGoviewProjectDataById(id);
    }

    @Override
    public GoviewProjectData selectGoviewProjectDataByProjectId(String projectId) {
        return goviewProjectDataMapper.selectGoviewProjectDataByProjectId(projectId);
    }

	@Override
	public int insertOrUpdateGoviewProjectData(GoviewProjectData data) {
        int i = 0;
        GoviewProjectData goviewProjectData = goviewProjectDataMapper.selectGoviewProjectDataByProjectId(data.getProjectId());
        if (goviewProjectData == null){
            i = insertGoviewProjectData(data);
        }else {
            data.setId(goviewProjectData.getId());
            i = updateGoviewProjectData(data);
        }
        return i;
	}
}

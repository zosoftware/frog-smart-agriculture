package com.frog.iot.mapper;

import java.util.List;
import com.frog.iot.domain.SceneTrigger;
import com.frog.iot.model.TriggerParameter;
import org.springframework.stereotype.Repository;

/**
 * 场景联动触发器Mapper接口
 * 
 * @author kerwincui
 * @date 2022-10-06
 */
@Repository
public interface SceneTriggerMapper 
{
    /**
     * 查询场景联动触发器
     * 
     * @param sceneTriggerId 场景联动触发器主键
     * @return 场景联动触发器
     */
    public SceneTrigger selectSceneTriggerBySceneTriggerId(Long sceneTriggerId);

    /**
     * 根据场景id数组查询场景联动触发器列表
     * 
     * @param sceneIds 场景联动ID数组
     * @return 场景联动触发器集合
     */
    public List<SceneTrigger> selectSceneTriggerListBySceneIds(Long[] sceneIds);

    /**
     * 查询场景联动触发器列表
     *
     * @param triggerParameter 参数
     * @return 场景联动触发器集合
     */
    public List<SceneTrigger> selectSceneTriggerList(TriggerParameter triggerParameter);

    /**
     * 批量新增场景联动触发器
     * 
     * @param sceneTriggerList 场景联动触发器集合
     * @return 结果
     */
    public int insertSceneTriggerList(List<SceneTrigger> sceneTriggerList);

    /**
     * 修改场景联动触发器
     * 
     * @param sceneTrigger 场景联动触发器
     * @return 结果
     */
    public int updateSceneTrigger(SceneTrigger sceneTrigger);

    /**
     * 删除场景联动触发器
     * 
     * @param sceneTriggerId 场景联动触发器主键
     * @return 结果
     */
    public int deleteSceneTriggerBySceneTriggerId(Long sceneTriggerId);


    /**
     * 批量删除场景联动的触发器
     *
     * @param sceneIds 需要删除的数据场景ID集合
     * @return 结果
     */
    public int deleteSceneTriggerBySceneIds(Long[] sceneIds);
}

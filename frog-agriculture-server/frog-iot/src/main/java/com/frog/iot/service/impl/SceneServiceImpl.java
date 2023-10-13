package com.frog.iot.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frog.common.core.domain.entity.SysRole;
import com.frog.common.core.domain.entity.SysUser;
import com.frog.common.exception.job.TaskException;
import com.frog.common.utils.DateUtils;
import com.frog.iot.domain.*;
import com.frog.iot.mapper.SceneTriggerMapper;
import com.frog.iot.service.IDeviceJobService;
import com.frog.quartz.util.CronUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.iot.mapper.SceneMapper;
import com.frog.iot.service.ISceneService;
import org.springframework.transaction.annotation.Transactional;

import static com.frog.common.utils.SecurityUtils.getLoginUser;

/**
 * 场景联动Service业务层处理
 *
 * @author kerwincui
 * @date 2022-01-13
 */
@Service
public class SceneServiceImpl implements ISceneService {

    private static final Logger log = LoggerFactory.getLogger(SceneServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SceneMapper sceneMapper;

    @Autowired
    private SceneTriggerMapper sceneTriggerMapper;

    @Autowired
    private IDeviceJobService jobService;

    /**
     * 查询场景联动
     *
     * @param sceneId 场景联动主键
     * @return 场景联动
     */
    @Override
    public Scene selectSceneBySceneId(Long sceneId) {
        Scene scene = sceneMapper.selectSceneBySceneId(sceneId);
        if (scene == null) {
            return scene;
        }
        List<SceneTrigger> sceneTriggerList = sceneTriggerMapper.selectSceneTriggerListBySceneIds(new Long[]{scene.getSceneId()});
        scene.setTriggers(JSONObject.toJSONString(sceneTriggerList));
        return scene;
    }

    /**
     * 查询场景联动列表
     *
     * @param scene 场景联动
     * @return 场景联动
     */
    @Override
    public List<Scene> selectSceneList(Scene scene) {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            // 租户和用户，只查看自己分组
            if (roles.get(i).getRoleKey().equals("tenant") || roles.get(i).getRoleKey().equals("general")) {
                scene.setUserId(user.getUserId());
                break;
            }
        }
        List<Scene> sceneList = sceneMapper.selectSceneList(scene);
        if (sceneList.size() == 0) {
            return sceneList;
        }
        Long[] sceneIds = sceneList.stream().map(Scene::getSceneId).toArray(Long[]::new);
        List<SceneTrigger> sceneTriggerList = sceneTriggerMapper.selectSceneTriggerListBySceneIds(sceneIds);
        for (int i = 0; i < sceneList.size(); i++) {
            List<SceneTrigger> triggerList = new ArrayList<>();
            for (int j = 0; j < sceneTriggerList.size(); j++) {
                if (sceneTriggerList.get(j).getSceneId().longValue() == sceneList.get(i).getSceneId()) {
                    triggerList.add(sceneTriggerList.get(j));
                }
            }
            sceneList.get(i).setTriggers(JSONObject.toJSONString(triggerList));
        }
        return sceneList;
    }

    /**
     * 新增场景联动
     *
     * @param scene 场景联动
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertScene(Scene scene) {
        scene.setCreateTime(DateUtils.getNowDate());
        sceneMapper.insertScene(scene);
        // 批量插入触发器
        List<SceneTrigger> sceneTriggerList = JSONArray.parseArray(scene.getTriggers(), SceneTrigger.class);
        for (int i = 0; i < sceneTriggerList.size(); i++) {
            sceneTriggerList.get(i).setSceneId(scene.getSceneId());
            sceneTriggerList.get(i).setStatus(scene.getStatus());
            // 1==设备触发，2=定时触发
            if (sceneTriggerList.get(i).getSource() == 2) {
                // 创建告警定时任务
                createSceneTask(scene, sceneTriggerList.get(i));
            }
        }
        return sceneTriggerMapper.insertSceneTriggerList(sceneTriggerList);
    }

    /**
     * 修改场景联动
     *
     * @param scene 场景联动
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateScene(Scene scene) {
        scene.setUpdateTime(DateUtils.getNowDate());
        sceneMapper.updateScene(scene);
        // 批量删除场景触发器和任务
        sceneTriggerMapper.deleteSceneTriggerBySceneIds(new Long[]{scene.getSceneId()});
        // 批量删除定时任务
        try {
            jobService.deleteJobBySceneIds(new Long[]{scene.getSceneId()});
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        // 批量新场景触发器和任务
        List<SceneTrigger> sceneTriggerList = JSONArray.parseArray(scene.getTriggers(), SceneTrigger.class);
        for (int i = 0; i < sceneTriggerList.size(); i++) {
            sceneTriggerList.get(i).setSceneId(scene.getSceneId());
            sceneTriggerList.get(i).setStatus(scene.getStatus());
            // 1==设备触发，2=定时触发
            if (sceneTriggerList.get(i).getSource() == 2) {
                // 创建告警定时任务
                createSceneTask(scene, sceneTriggerList.get(i));
            }
        }
        return sceneTriggerMapper.insertSceneTriggerList(sceneTriggerList);
    }

    /**
     * 创建场景定时任务
     *
     * @param scene
     * @param sceneTrigger
     */
    private void createSceneTask(Scene scene, SceneTrigger sceneTrigger) {
        // 创建定时任务
        try {
            if (!CronUtils.isValid(sceneTrigger.getCronExpression())) {
                log.error("新增场景联动定时任务失败，Cron表达式不正确");
                throw new Exception("新增场景联动定时任务失败，Cron表达式不正确");
            }
            DeviceJob deviceJob = new DeviceJob();
            deviceJob.setJobName("场景联动定时触发");
            deviceJob.setJobType(3);
            deviceJob.setJobGroup("DEFAULT");
            deviceJob.setConcurrent("1");
            deviceJob.setMisfirePolicy("2");
            deviceJob.setStatus(scene.getStatus() == 1 ? "0" : "1");
            deviceJob.setCronExpression(sceneTrigger.getCronExpression());
            deviceJob.setIsAdvance(sceneTrigger.getIsAdvance());
            deviceJob.setSceneId(scene.getSceneId());
            deviceJob.setActions(scene.getActions());
            deviceJob.setDeviceName("场景联动定时触发");
            jobService.insertJob(deviceJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (TaskException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除场景联动
     *
     * @param sceneIds 需要删除的场景联动主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSceneBySceneIds(Long[] sceneIds) {
        // 批量删除场景的触发器
        sceneTriggerMapper.deleteSceneTriggerBySceneIds(sceneIds);
        // 批量删除定时任务
        try {
            jobService.deleteJobBySceneIds(sceneIds);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return sceneMapper.deleteSceneBySceneIds(sceneIds);
    }

    /**
     * 删除场景联动信息
     *
     * @param sceneId 场景联动主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSceneBySceneId(Long sceneId) {
        // 批量删除场景的触发器
        sceneTriggerMapper.deleteSceneTriggerBySceneIds(new Long[]{sceneId});
        // 批量删除定时任务
        try {
            jobService.deleteJobBySceneIds(new Long[]{sceneId});
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return sceneMapper.deleteSceneBySceneId(sceneId);
    }
}

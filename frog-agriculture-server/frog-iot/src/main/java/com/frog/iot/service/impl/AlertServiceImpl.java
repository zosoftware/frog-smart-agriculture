package com.frog.iot.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frog.common.exception.job.TaskException;
import com.frog.common.utils.DateUtils;
import com.frog.iot.domain.*;
import com.frog.iot.mapper.AlertTriggerMapper;
import com.frog.iot.service.IDeviceJobService;
import com.frog.quartz.util.CronUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.iot.mapper.AlertMapper;
import com.frog.iot.service.IAlertService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备告警Service业务层处理
 *
 * @author kerwincui
 * @date 2022-01-13
 */
@Service
public class AlertServiceImpl implements IAlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private AlertMapper alertMapper;

    @Autowired
    private AlertTriggerMapper alertTriggerMapper;

    @Autowired
    private IDeviceJobService jobService;

    /**
     * 查询设备告警
     *
     * @param alertId 设备告警主键
     * @return 设备告警
     */
    @Override
    public Alert selectAlertByAlertId(Long alertId) {
        Alert alert = alertMapper.selectAlertByAlertId(alertId);
        if (alert == null) {
            return alert;
        }
        List<AlertTrigger> alertTriggerList = alertTriggerMapper.selectAlertTriggerListByAlertIds(new Long[]{alert.getAlertId()});
        alert.setTriggers(JSONObject.toJSONString(alertTriggerList));
        return alert;
    }

    /**
     * 查询设备告警列表
     *
     * @param alert 设备告警
     * @return 设备告警
     */
    @Override
    public List<Alert> selectAlertList(Alert alert) {
        List<Alert> alertList = alertMapper.selectAlertList(alert);
        if (alertList.size() == 0) {
            return alertList;
        }
        Long[] alertIds = alertList.stream().map(Alert::getAlertId).toArray(Long[]::new);
        List<AlertTrigger> alertTriggerList = alertTriggerMapper.selectAlertTriggerListByAlertIds(alertIds);
        for (int i = 0; i < alertList.size(); i++) {
            List<AlertTrigger> triggerList = new ArrayList<>();
            for (int j = 0; j < alertTriggerList.size(); j++) {
                if (alertTriggerList.get(j).getAlertId().longValue() == alertList.get(i).getAlertId()) {
                    triggerList.add(alertTriggerList.get(j));
                }
            }
            alertList.get(i).setTriggers(JSONObject.toJSONString(triggerList));
        }
        return alertList;
    }

    /**
     * 新增设备告警
     *
     * @param alert 设备告警
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAlert(Alert alert) {
        alert.setCreateTime(DateUtils.getNowDate());
        alertMapper.insertAlert(alert);
        // 批量插入触发器
        List<AlertTrigger> alertTriggerList = JSONArray.parseArray(alert.getTriggers(), AlertTrigger.class);
        for (int i = 0; i < alertTriggerList.size(); i++) {
            alertTriggerList.get(i).setAlertId(alert.getAlertId());
            alertTriggerList.get(i).setStatus(alert.getStatus());
            // 1==设备触发，2=定时触发
            if (alertTriggerList.get(i).getSource() == 2) {
                // 创建告警定时任务
                alertTriggerList.get(i).setJobId(createAlertTask(alert, alertTriggerList.get(i)));
            }
        }
        return alertTriggerMapper.insertAlertTriggerList(alertTriggerList);
    }

    /**
     * 修改设备告警
     *
     * @param alert 设备告警
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAlert(Alert alert) {
        alert.setUpdateTime(DateUtils.getNowDate());
        alertMapper.updateAlert(alert);
        // 批量删除场景触发器和任务
        alertTriggerMapper.deleteAlertTriggerByAlertIds(new Long[]{alert.getAlertId()});
        // 批量删除定时任务
        try {
            jobService.deleteJobByAlertIds(new Long[]{alert.getAlertId()});
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        // 批量新建告警触发器和任务
        List<AlertTrigger> alertTriggerList = JSONArray.parseArray(alert.getTriggers(), AlertTrigger.class);
        for (int i = 0; i < alertTriggerList.size(); i++) {
            alertTriggerList.get(i).setAlertId(alert.getAlertId());
            alertTriggerList.get(i).setStatus(alert.getStatus());
            // 1==设备触发，2=定时触发
            if (alertTriggerList.get(i).getSource() == 2) {
                // 创建告警定时任务
                alertTriggerList.get(i).setJobId(createAlertTask(alert, alertTriggerList.get(i)));
            }
        }

        return alertTriggerMapper.insertAlertTriggerList(alertTriggerList);
    }

    /**
     * 创建告警定时任务
     *
     * @param alert
     * @param alertTrigger
     */
    private Long createAlertTask(Alert alert, AlertTrigger alertTrigger) {
        // 创建定时任务
        try {
            if (!CronUtils.isValid(alertTrigger.getCronExpression())) {
                log.error("新增告警定时任务失败，Cron表达式不正确");
                throw new Exception("新增告警定时任务失败，Cron表达式不正确");
            }
            DeviceJob deviceJob = new DeviceJob();
            deviceJob.setJobName("告警定时触发");
            deviceJob.setJobType(2);
            deviceJob.setJobGroup("DEFAULT");
            deviceJob.setConcurrent("1");
            deviceJob.setMisfirePolicy("2");
            deviceJob.setStatus(alert.getStatus() == 1 ? "0" : "1");
            deviceJob.setCronExpression(alertTrigger.getCronExpression());
            deviceJob.setIsAdvance(alertTrigger.getIsAdvance());
            deviceJob.setAlertId(alert.getAlertId());
            deviceJob.setActions(alert.getActions());
            deviceJob.setDeviceName("告警定时触发");
            deviceJob.setAlertTrigger(JSONObject.toJSONString(alertTrigger));
            deviceJob.setProductId(alertTrigger.getProductId());
            deviceJob.setProductName(alertTrigger.getProductName());
            jobService.insertJob(deviceJob);
            return deviceJob.getJobId();
        } catch (SchedulerException e) {
            e.printStackTrace();
            return 0L;
        } catch (TaskException e) {
            e.printStackTrace();
            return 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 批量删除设备告警
     *
     * @param alertIds 需要删除的设备告警主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAlertByAlertIds(Long[] alertIds) {
        // 批量删除场景的触发器
        alertTriggerMapper.deleteAlertTriggerByAlertIds(alertIds);
        // 批量删除定时任务
        try {
            jobService.deleteJobByAlertIds(alertIds);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return alertMapper.deleteAlertByAlertIds(alertIds);
    }

    /**
     * 删除设备告警信息
     *
     * @param alertId 设备告警主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAlertByAlertId(Long alertId) {
        // 批量删除场景的触发器
        alertTriggerMapper.deleteAlertTriggerByAlertIds(new Long[]{alertId});
        // 批量删除定时任务
        try {
            jobService.deleteJobByAlertIds(new Long[]{alertId});
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return alertMapper.deleteAlertByAlertId(alertId);
    }
}

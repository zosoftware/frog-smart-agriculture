package com.frog.iot.service.impl;

import java.util.List;

import com.frog.common.core.domain.entity.SysRole;
import com.frog.common.core.domain.entity.SysUser;
import com.frog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.iot.mapper.AlertLogMapper;
import com.frog.iot.domain.AlertLog;
import com.frog.iot.service.IAlertLogService;

import static com.frog.common.utils.SecurityUtils.getLoginUser;

/**
 * 设备告警Service业务层处理
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@Service
public class AlertLogServiceImpl implements IAlertLogService 
{
    @Autowired
    private AlertLogMapper alertLogMapper;

    /**
     * 查询设备告警
     * 
     * @param alertLogId 设备告警主键
     * @return 设备告警
     */
    @Override
    public AlertLog selectAlertLogByAlertLogId(Long alertLogId)
    {
        return alertLogMapper.selectAlertLogByAlertLogId(alertLogId);
    }

    /**
     * 查询设备告警列表
     * 
     * @param alertLog 设备告警
     * @return 设备告警
     */
    @Override
    public List<AlertLog> selectAlertLogList(AlertLog alertLog)
    {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            // 租户和用户，只查看自己分组
            if (roles.get(i).getRoleKey().equals("tenant") || roles.get(i).getRoleKey().equals("general")) {
                alertLog.setUserId(user.getUserId());
                break;
            }
        }
        return alertLogMapper.selectAlertLogList(alertLog);
    }

    /**
     * 查询设备告警列表
     *
     * @param alertLog 设备告警
     * @return 设备告警
     */
    @Override
    public Long selectAlertLogListCount(AlertLog alertLog)
    {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles = user.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            // 租户和用户，只查看自己分组
            if (roles.get(i).getRoleKey().equals("tenant") || roles.get(i).getRoleKey().equals("general")) {
                alertLog.setUserId(user.getUserId());
                break;
            }
        }
        return alertLogMapper.selectAlertLogListCount(alertLog);
    }

    /**
     * 新增设备告警
     * 
     * @param alertLog 设备告警
     * @return 结果
     */
    @Override
    public int insertAlertLog(AlertLog alertLog)
    {
        alertLog.setCreateTime(DateUtils.getNowDate());
        return alertLogMapper.insertAlertLog(alertLog);
    }

    /**
     * 修改设备告警
     * 
     * @param alertLog 设备告警
     * @return 结果
     */
    @Override
    public int updateAlertLog(AlertLog alertLog)
    {
        alertLog.setUpdateTime(DateUtils.getNowDate());
        if(alertLog.getRemark().length()>0){
            // 1=不需要处理,2=未处理,3=已处理
            alertLog.setStatus(3);
        }
        return alertLogMapper.updateAlertLog(alertLog);
    }

    /**
     * 批量删除设备告警
     * 
     * @param alertLogIds 需要删除的设备告警主键
     * @return 结果
     */
    @Override
    public int deleteAlertLogByAlertLogIds(Long[] alertLogIds)
    {
        return alertLogMapper.deleteAlertLogByAlertLogIds(alertLogIds);
    }

    /**
     * 删除设备告警信息
     * 
     * @param alertLogId 设备告警主键
     * @return 结果
     */
    @Override
    public int deleteAlertLogByAlertLogId(Long alertLogId)
    {
        return alertLogMapper.deleteAlertLogByAlertLogId(alertLogId);
    }

    /**
     * 通过设备编号删除设备告警信息
     *
     * @param SerialNumber 设备告警主键
     * @return 结果
     */
    @Override
    public int deleteAlertLogBySerialNumber(String SerialNumber)
    {
        return alertLogMapper.deleteAlertLogBySerialNumber(SerialNumber);
    }
}

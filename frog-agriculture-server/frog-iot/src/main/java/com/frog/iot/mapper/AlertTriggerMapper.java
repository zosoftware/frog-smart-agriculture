package com.frog.iot.mapper;

import java.util.List;
import com.frog.iot.domain.AlertTrigger;
import com.frog.iot.model.TriggerParameter;
import org.springframework.stereotype.Repository;

/**
 * 告警触发器Mapper接口
 * 
 * @author kerwincui
 * @date 2022-10-06
 */
@Repository
public interface AlertTriggerMapper 
{
    /**
     * 查询告警触发器
     * 
     * @param alertTriggerId 告警触发器主键
     * @return 告警触发器
     */
    public AlertTrigger selectAlertTriggerByAlertTriggerId(Long alertTriggerId);

    /**
     * 通过告警ID数组，查询告警触发器列表
     * 
     * @param alertIds 告警ID集合
     * @return 告警触发器集合
     */
    public List<AlertTrigger> selectAlertTriggerListByAlertIds(Long[] alertIds);

    /**
     * 查询告警触发器列表
     *
     * @param triggerParameter 参数
     * @return 告警触发器集合
     */
    public List<AlertTrigger> selectAlertTriggerList(TriggerParameter triggerParameter);

    /**
     * 批量新增告警触发器
     * 
     * @param alertTriggerList 告警触发器集合
     * @return 结果
     */
    public int insertAlertTriggerList(List<AlertTrigger> alertTriggerList);

    /**
     * 修改告警触发器
     * 
     * @param alertTrigger 告警触发器
     * @return 结果
     */
    public int updateAlertTrigger(AlertTrigger alertTrigger);

    /**
     * 删除告警触发器
     * 
     * @param alertTriggerId 告警触发器主键
     * @return 结果
     */
    public int deleteAlertTriggerByAlertTriggerId(Long alertTriggerId);

    /**
     * 批量删除告警的触发器
     *
     * @param alertIds 需要删除的数据告警ID集合
     * @return 结果
     */
    public int deleteAlertTriggerByAlertIds(Long[] alertIds);
}

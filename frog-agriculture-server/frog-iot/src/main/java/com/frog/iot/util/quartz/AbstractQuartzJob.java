package com.frog.iot.util.quartz;

import com.frog.common.constant.Constants;
import com.frog.common.constant.ScheduleConstants;
import com.frog.common.utils.ExceptionUtil;
import com.frog.common.utils.StringUtils;
import com.frog.common.utils.bean.BeanUtils;
import com.frog.common.utils.spring.SpringUtils;
import com.frog.iot.domain.DeviceJob;
import com.frog.iot.domain.DeviceJobLog;
import com.frog.iot.service.IDeviceJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author ruoyi
 */
public abstract class AbstractQuartzJob implements Job
{
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        DeviceJob deviceJob = new DeviceJob();
        BeanUtils.copyBeanProp(deviceJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try
        {
            before(context, deviceJob);
            if (deviceJob != null)
            {
                doExecute(context, deviceJob);
            }
            after(context, deviceJob, null);
        }
        catch (Exception e)
        {
            log.error("任务执行异常  - ：", e);
            after(context, deviceJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param deviceJob 系统计划任务
     */
    protected void before(JobExecutionContext context, DeviceJob deviceJob)
    {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param deviceJob 系统计划任务
     */
    protected void after(JobExecutionContext context, DeviceJob deviceJob, Exception e)
    {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final DeviceJobLog deviceJobLog = new DeviceJobLog();
        deviceJobLog.setJobName(deviceJob.getJobName());
        deviceJobLog.setJobId(deviceJob.getJobId());
        deviceJobLog.setJobGroup(deviceJob.getJobGroup());
        deviceJobLog.setDeviceName(deviceJob.getDeviceName());
        deviceJobLog.setCreateTime(startTime);
        long runMs = new Date().getTime()-startTime.getTime();
        deviceJobLog.setJobMessage("总共耗时:" + runMs + "毫秒");
        if (e != null){
            deviceJobLog.setStatus(Constants.FAIL);
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            deviceJobLog.setExceptionInfo(errorMsg);
        }else
        {
            deviceJobLog.setStatus(Constants.SUCCESS);
        }
        // 写入数据库当中
        SpringUtils.getBean(IDeviceJobLogService.class).insertDeviceJobLog(deviceJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param deviceJob 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, DeviceJob deviceJob) throws Exception;
}

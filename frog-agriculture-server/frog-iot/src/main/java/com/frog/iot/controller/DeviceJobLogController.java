package com.frog.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.frog.common.annotation.Log;
import com.frog.common.core.controller.BaseController;
import com.frog.common.core.domain.AjaxResult;
import com.frog.common.enums.BusinessType;
import com.frog.iot.domain.DeviceJobLog;
import com.frog.iot.service.IDeviceJobLogService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 设备定时任务日志Controller
 * 
 * @author nealtsiao
 * @date 2023-09-14
 */
@RestController
@RequestMapping("/iot/deviceJobLog")
public class DeviceJobLogController extends BaseController
{
    @Autowired
    private IDeviceJobLogService deviceJobLogService;

    /**
     * 查询设备定时任务日志列表
     */
    @PreAuthorize("@ss.hasPermi('iot:devicejoblog:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceJobLog deviceJobLog)
    {
        startPage();
        List<DeviceJobLog> list = deviceJobLogService.selectDeviceJobLogList(deviceJobLog);
        return getDataTable(list);
    }

    /**
     * 导出设备定时任务日志列表
     */
    @PreAuthorize("@ss.hasPermi('iot:devicejoblog:export')")
    @Log(title = "设备定时任务日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceJobLog deviceJobLog)
    {
        List<DeviceJobLog> list = deviceJobLogService.selectDeviceJobLogList(deviceJobLog);
        ExcelUtil<DeviceJobLog> util = new ExcelUtil<DeviceJobLog>(DeviceJobLog.class);
        util.exportExcel(response, list, "设备定时任务日志数据");
    }

    /**
     * 获取设备定时任务日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:devicejoblog:query')")
    @GetMapping(value = "/{jobLogId}")
    public AjaxResult getInfo(@PathVariable("jobLogId") Long jobLogId)
    {
        return AjaxResult.success(deviceJobLogService.selectDeviceJobLogByJobLogId(jobLogId));
    }

    /**
     * 新增设备定时任务日志
     */
    @PreAuthorize("@ss.hasPermi('iot:devicejoblog:add')")
    @Log(title = "设备定时任务日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceJobLog deviceJobLog)
    {
        return toAjax(deviceJobLogService.insertDeviceJobLog(deviceJobLog));
    }
}

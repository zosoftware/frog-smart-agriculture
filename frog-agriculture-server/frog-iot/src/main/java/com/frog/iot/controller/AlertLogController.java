package com.frog.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.frog.common.constant.HttpStatus;
import com.frog.common.utils.ServletUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.frog.common.annotation.Log;
import com.frog.common.core.controller.BaseController;
import com.frog.common.core.domain.AjaxResult;
import com.frog.common.enums.BusinessType;
import com.frog.iot.domain.AlertLog;
import com.frog.iot.service.IAlertLogService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 设备告警Controller
 * 
 * @author kerwincui
 * @date 2022-01-13
 */
@RestController
@RequestMapping("/iot/alertLog")
public class AlertLogController extends BaseController
{
    @Autowired
    private IAlertLogService alertLogService;

    /**
     * 查询设备告警列表
     */
    @PreAuthorize("@ss.hasPermi('iot:alert:list')")
    @GetMapping("/list")
    public TableDataInfo list(AlertLog alertLog)
    {
        Integer pageNum=ServletUtils.getParameterToInt("pageNum");
        alertLog.setPageSize(ServletUtils.getParameterToInt("pageSize"));
        alertLog.setOffSet((pageNum-1)*alertLog.getPageSize());
        List<AlertLog> list = alertLogService.selectAlertLogList(alertLog);
        Long total=alertLogService.selectAlertLogListCount(alertLog);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        // 查询总数
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 导出设备告警列表
     */
    @PreAuthorize("@ss.hasPermi('iot:alert:export')")
    @Log(title = "设备告警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlertLog alertLog)
    {
        List<AlertLog> list = alertLogService.selectAlertLogList(alertLog);
        ExcelUtil<AlertLog> util = new ExcelUtil<AlertLog>(AlertLog.class);
        util.exportExcel(response, list, "设备告警数据");
    }

    /**
     * 获取设备告警详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:alert:query')")
    @GetMapping(value = "/{alertLogId}")
    public AjaxResult getInfo(@PathVariable("alertLogId") Long alertLogId)
    {
        return AjaxResult.success(alertLogService.selectAlertLogByAlertLogId(alertLogId));
    }

    /**
     * 新增设备告警
     */
    @PreAuthorize("@ss.hasPermi('iot:alert:add')")
    @Log(title = "设备告警", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlertLog alertLog)
    {
        return toAjax(alertLogService.insertAlertLog(alertLog));
    }

    /**
     * 修改设备告警
     */
    @PreAuthorize("@ss.hasPermi('iot:alert:edit')")
    @Log(title = "设备告警", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlertLog alertLog)
    {
        return toAjax(alertLogService.updateAlertLog(alertLog));
    }

    /**
     * 删除设备告警
     */
    @PreAuthorize("@ss.hasPermi('iot:alert:remove')")
    @Log(title = "设备告警", businessType = BusinessType.DELETE)
	@DeleteMapping("/{alertLogIds}")
    public AjaxResult remove(@PathVariable Long[] alertLogIds)
    {
        return toAjax(alertLogService.deleteAlertLogByAlertLogIds(alertLogIds));
    }
}

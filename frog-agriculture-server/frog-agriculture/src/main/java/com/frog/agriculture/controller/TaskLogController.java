package com.frog.agriculture.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.frog.agriculture.domain.TaskLog;
import com.frog.agriculture.service.ITaskLogService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 批次任务日志Controller
 * 
 * @author nealtsiao
 * @date 2023-06-06
 */
@RestController
@RequestMapping("/agriculture/log")
public class TaskLogController extends BaseController
{
    @Autowired
    private ITaskLogService taskLogService;

    /**
     * 查询批次任务日志列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaskLog taskLog)
    {
        startPage();
        List<TaskLog> list = taskLogService.selectTaskLogList(taskLog);
        return getDataTable(list);
    }

    /**
     * 导出批次任务日志列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:export')")
    @Log(title = "批次任务日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskLog taskLog)
    {
        List<TaskLog> list = taskLogService.selectTaskLogList(taskLog);
        ExcelUtil<TaskLog> util = new ExcelUtil<TaskLog>(TaskLog.class);
        util.exportExcel(response, list, "批次任务日志数据");
    }

    /**
     * 获取批次任务日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return AjaxResult.success(taskLogService.selectTaskLogByLogId(logId));
    }

    /**
     * 新增批次任务日志
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:add')")
    @Log(title = "批次任务日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskLog taskLog)
    {
        return toAjax(taskLogService.insertTaskLog(taskLog));
    }

    /**
     * 修改批次任务日志
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:edit')")
    @Log(title = "批次任务日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskLog taskLog)
    {
        return toAjax(taskLogService.updateTaskLog(taskLog));
    }

    /**
     * 删除批次任务日志
     */
    @PreAuthorize("@ss.hasPermi('agriculture:log:remove')")
    @Log(title = "批次任务日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(taskLogService.deleteTaskLogByLogIds(logIds));
    }
}

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
import com.frog.agriculture.domain.TaskEmployee;
import com.frog.agriculture.service.ITaskEmployeeService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 批次任务工人Controller
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/taskEmployee")
public class TaskEmployeeController extends BaseController
{
    @Autowired
    private ITaskEmployeeService taskEmployeeService;

    /**
     * 查询批次任务工人列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaskEmployee taskEmployee)
    {
        startPage();
        List<TaskEmployee> list = taskEmployeeService.selectTaskEmployeeList(taskEmployee);
        return getDataTable(list);
    }

    /**
     * 导出批次任务工人列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:export')")
    @Log(title = "批次任务工人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskEmployee taskEmployee)
    {
        List<TaskEmployee> list = taskEmployeeService.selectTaskEmployeeList(taskEmployee);
        ExcelUtil<TaskEmployee> util = new ExcelUtil<TaskEmployee>(TaskEmployee.class);
        util.exportExcel(response, list, "批次任务工人数据");
    }

    /**
     * 获取批次任务工人详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(taskEmployeeService.selectTaskEmployeeById(id));
    }

    /**
     * 新增批次任务工人
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:add')")
    @Log(title = "批次任务工人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskEmployee taskEmployee)
    {
        return toAjax(taskEmployeeService.insertTaskEmployee(taskEmployee));
    }

    /**
     * 修改批次任务工人
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:edit')")
    @Log(title = "批次任务工人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskEmployee taskEmployee)
    {
        return toAjax(taskEmployeeService.updateTaskEmployee(taskEmployee));
    }

    /**
     * 删除批次任务工人
     */
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:remove')")
    @Log(title = "批次任务工人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(taskEmployeeService.deleteTaskEmployeeByIds(ids));
    }
    @PreAuthorize("@ss.hasPermi('agriculture:taskEmployee:remove')")
    @Log(title = "批次任务工人", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskId}/{employeeId}")
    public AjaxResult remove(@PathVariable Long taskId,@PathVariable Long employeeId)
    {
        return toAjax(taskEmployeeService.deleteTaskEmployeeByTaskIdAndEmployeeId(taskId,employeeId));
    }
}

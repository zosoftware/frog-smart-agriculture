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
import com.frog.agriculture.domain.Employee;
import com.frog.agriculture.service.IEmployeeService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 雇员Controller
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/agriculture/employee")
public class EmployeeController extends BaseController
{
    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询雇员列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:list')")
    @GetMapping("/list")
    public TableDataInfo list(Employee employee)
    {
        startPage();
        List<Employee> list = employeeService.selectEmployeeList(employee);
        return getDataTable(list);
    }

    /**
     * 导出雇员列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:export')")
    @Log(title = "雇员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Employee employee)
    {
        List<Employee> list = employeeService.selectEmployeeList(employee);
        ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
        util.exportExcel(response, list, "雇员数据");
    }

    /**
     * 获取雇员详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:query')")
    @GetMapping(value = "/{employeeId}")
    public AjaxResult getInfo(@PathVariable("employeeId") Long employeeId)
    {
        return AjaxResult.success(employeeService.selectEmployeeByEmployeeId(employeeId));
    }

    /**
     * 新增雇员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:add')")
    @Log(title = "雇员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Employee employee)
    {
        return toAjax(employeeService.insertEmployee(employee));
    }

    /**
     * 修改雇员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:edit')")
    @Log(title = "雇员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Employee employee)
    {
        return toAjax(employeeService.updateEmployee(employee));
    }

    /**
     * 删除雇员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:employee:remove')")
    @Log(title = "雇员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{employeeIds}")
    public AjaxResult remove(@PathVariable Long[] employeeIds)
    {
        return toAjax(employeeService.deleteEmployeeByEmployeeIds(employeeIds));
    }
}

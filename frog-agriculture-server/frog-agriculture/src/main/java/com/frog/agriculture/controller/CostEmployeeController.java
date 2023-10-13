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
import com.frog.agriculture.domain.CostEmployee;
import com.frog.agriculture.service.ICostEmployeeService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 人工工时Controller
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/costEmployee")
public class CostEmployeeController extends BaseController
{
    @Autowired
    private ICostEmployeeService costEmployeeService;

    /**
     * 查询人工工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostEmployee costEmployee)
    {
        startPage();
        List<CostEmployee> list = costEmployeeService.selectCostEmployeeList(costEmployee);
        return getDataTable(list);
    }

    /**
     * 导出人工工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:export')")
    @Log(title = "人工工时", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostEmployee costEmployee)
    {
        List<CostEmployee> list = costEmployeeService.selectCostEmployeeList(costEmployee);
        ExcelUtil<CostEmployee> util = new ExcelUtil<CostEmployee>(CostEmployee.class);
        util.exportExcel(response, list, "人工工时数据");
    }

    /**
     * 获取人工工时详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:query')")
    @GetMapping(value = "/{costId}")
    public AjaxResult getInfo(@PathVariable("costId") Long costId)
    {
        return AjaxResult.success(costEmployeeService.selectCostEmployeeByCostId(costId));
    }

    /**
     * 新增人工工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:add')")
    @Log(title = "人工工时", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostEmployee costEmployee)
    {
        return toAjax(costEmployeeService.insertCostEmployee(costEmployee));
    }

    /**
     * 修改人工工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:edit')")
    @Log(title = "人工工时", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostEmployee costEmployee)
    {
        return toAjax(costEmployeeService.updateCostEmployee(costEmployee));
    }

    /**
     * 删除人工工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costEmployee:remove')")
    @Log(title = "人工工时", businessType = BusinessType.DELETE)
	@DeleteMapping("/{costIds}")
    public AjaxResult remove(@PathVariable Long[] costIds)
    {
        return toAjax(costEmployeeService.deleteCostEmployeeByCostIds(costIds));
    }
}

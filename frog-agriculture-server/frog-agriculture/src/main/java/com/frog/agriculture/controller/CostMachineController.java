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
import com.frog.agriculture.domain.CostMachine;
import com.frog.agriculture.service.ICostMachineService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 机械工时Controller
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/costMachine")
public class CostMachineController extends BaseController
{
    @Autowired
    private ICostMachineService costMachineService;

    /**
     * 查询机械工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostMachine costMachine)
    {
        startPage();
        List<CostMachine> list = costMachineService.selectCostMachineList(costMachine);
        return getDataTable(list);
    }

    /**
     * 导出机械工时列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:export')")
    @Log(title = "机械工时", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostMachine costMachine)
    {
        List<CostMachine> list = costMachineService.selectCostMachineList(costMachine);
        ExcelUtil<CostMachine> util = new ExcelUtil<CostMachine>(CostMachine.class);
        util.exportExcel(response, list, "机械工时数据");
    }

    /**
     * 获取机械工时详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:query')")
    @GetMapping(value = "/{costId}")
    public AjaxResult getInfo(@PathVariable("costId") Long costId)
    {
        return AjaxResult.success(costMachineService.selectCostMachineByCostId(costId));
    }

    /**
     * 新增机械工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:add')")
    @Log(title = "机械工时", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostMachine costMachine)
    {
        return toAjax(costMachineService.insertCostMachine(costMachine));
    }

    /**
     * 修改机械工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:edit')")
    @Log(title = "机械工时", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostMachine costMachine)
    {
        return toAjax(costMachineService.updateCostMachine(costMachine));
    }

    /**
     * 删除机械工时
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMachine:remove')")
    @Log(title = "机械工时", businessType = BusinessType.DELETE)
	@DeleteMapping("/{costIds}")
    public AjaxResult remove(@PathVariable Long[] costIds)
    {
        return toAjax(costMachineService.deleteCostMachineByCostIds(costIds));
    }
}

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
import com.frog.agriculture.domain.MachineType;
import com.frog.agriculture.service.IMachineTypeService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 机械类别Controller
 * 
 * @author kerwincui
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/machineType")
public class MachineTypeController extends BaseController
{
    @Autowired
    private IMachineTypeService machineTypeService;

    /**
     * 查询机械类别列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineType:list')")
    @GetMapping("/list")
    public TableDataInfo list(MachineType machineType)
    {
        startPage();
        List<MachineType> list = machineTypeService.selectMachineTypeList(machineType);
        return getDataTable(list);
    }

    /**
     * 导出机械类别列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineType:export')")
    @Log(title = "机械类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MachineType machineType)
    {
        List<MachineType> list = machineTypeService.selectMachineTypeList(machineType);
        ExcelUtil<MachineType> util = new ExcelUtil<MachineType>(MachineType.class);
        util.exportExcel(response, list, "机械类别数据");
    }

    /**
     * 获取机械类别详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineType:query')")
    @GetMapping(value = "/{machineTypeId}")
    public AjaxResult getInfo(@PathVariable("machineTypeId") Long machineTypeId)
    {
        return AjaxResult.success(machineTypeService.selectMachineTypeByMachineTypeId(machineTypeId));
    }

    /**
     * 新增机械类别
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineType:add')")
    @Log(title = "机械类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MachineType machineType)
    {
        return toAjax(machineTypeService.insertMachineType(machineType));
    }

    /**
     * 修改机械类别
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineType:edit')")
    @Log(title = "机械类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MachineType machineType)
    {
        return toAjax(machineTypeService.updateMachineType(machineType));
    }

    /**
     * 删除机械类别
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineType:remove')")
    @Log(title = "机械类别", businessType = BusinessType.DELETE)
	@DeleteMapping("/{machineTypeIds}")
    public AjaxResult remove(@PathVariable Long[] machineTypeIds)
    {
        return toAjax(machineTypeService.deleteMachineTypeByMachineTypeIds(machineTypeIds));
    }
}

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
import com.frog.agriculture.domain.MachineInfo;
import com.frog.agriculture.service.IMachineInfoService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 机械信息Controller
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/machineInfo")
public class MachineInfoController extends BaseController
{
    @Autowired
    private IMachineInfoService machineInfoService;

    /**
     * 查询机械信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(MachineInfo machineInfo)
    {
        startPage();
        List<MachineInfo> list = machineInfoService.selectMachineInfoList(machineInfo);
        return getDataTable(list);
    }

    /**
     * 导出机械信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineInfo:export')")
    @Log(title = "机械信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MachineInfo machineInfo)
    {
        List<MachineInfo> list = machineInfoService.selectMachineInfoList(machineInfo);
        ExcelUtil<MachineInfo> util = new ExcelUtil<MachineInfo>(MachineInfo.class);
        util.exportExcel(response, list, "机械信息数据");
    }

    /**
     * 获取机械信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineInfo:query')")
    @GetMapping(value = "/{machineId}")
    public AjaxResult getInfo(@PathVariable("machineId") Long machineId)
    {
        return AjaxResult.success(machineInfoService.selectMachineInfoByMachineId(machineId));
    }

    /**
     * 新增机械信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineInfo:add')")
    @Log(title = "机械信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MachineInfo machineInfo)
    {
        return toAjax(machineInfoService.insertMachineInfo(machineInfo));
    }

    /**
     * 修改机械信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineInfo:edit')")
    @Log(title = "机械信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MachineInfo machineInfo)
    {
        return toAjax(machineInfoService.updateMachineInfo(machineInfo));
    }

    /**
     * 删除机械信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:machineInfo:remove')")
    @Log(title = "机械信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{machineIds}")
    public AjaxResult remove(@PathVariable Long[] machineIds)
    {
        return toAjax(machineInfoService.deleteMachineInfoByMachineIds(machineIds));
    }
}

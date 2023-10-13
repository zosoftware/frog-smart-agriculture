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
import com.frog.agriculture.domain.TraceStaff;
import com.frog.agriculture.service.ITraceStaffService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 溯源人员Controller
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
@RestController
@RequestMapping("/agriculture/staff")
public class TraceStaffController extends BaseController
{
    @Autowired
    private ITraceStaffService traceStaffService;

    /**
     * 查询溯源人员列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:staff:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceStaff traceStaff)
    {
        startPage();
        List<TraceStaff> list = traceStaffService.selectTraceStaffList(traceStaff);
        return getDataTable(list);
    }

    /**
     * 导出溯源人员列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:staff:export')")
    @Log(title = "溯源人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraceStaff traceStaff)
    {
        List<TraceStaff> list = traceStaffService.selectTraceStaffList(traceStaff);
        ExcelUtil<TraceStaff> util = new ExcelUtil<TraceStaff>(TraceStaff.class);
        util.exportExcel(response, list, "溯源人员数据");
    }

    /**
     * 获取溯源人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:staff:query')")
    @GetMapping(value = "/{staffId}")
    public AjaxResult getInfo(@PathVariable("staffId") Long staffId)
    {
        return AjaxResult.success(traceStaffService.selectTraceStaffByStaffId(staffId));
    }

    /**
     * 新增溯源人员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:staff:add')")
    @Log(title = "溯源人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraceStaff traceStaff)
    {
        return toAjax(traceStaffService.insertTraceStaff(traceStaff));
    }

    /**
     * 修改溯源人员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:staff:edit')")
    @Log(title = "溯源人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraceStaff traceStaff)
    {
        return toAjax(traceStaffService.updateTraceStaff(traceStaff));
    }

    /**
     * 删除溯源人员
     */
    @PreAuthorize("@ss.hasPermi('agriculture:staff:remove')")
    @Log(title = "溯源人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{staffIds}")
    public AjaxResult remove(@PathVariable Long[] staffIds)
    {
        return toAjax(traceStaffService.deleteTraceStaffByStaffIds(staffIds));
    }
}

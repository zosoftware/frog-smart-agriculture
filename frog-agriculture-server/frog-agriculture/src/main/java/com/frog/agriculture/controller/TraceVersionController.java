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
import com.frog.agriculture.domain.TraceVersion;
import com.frog.agriculture.service.ITraceVersionService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 溯源版本Controller
 * 
 * @author nealtsiao
 * @date 2023-08-04
 */
@RestController
@RequestMapping("/agriculture/version")
public class TraceVersionController extends BaseController
{
    @Autowired
    private ITraceVersionService traceVersionService;

    /**
     * 查询溯源版本列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:version:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceVersion traceVersion)
    {
        startPage();
        List<TraceVersion> list = traceVersionService.selectTraceVersionList(traceVersion);
        return getDataTable(list);
    }

    /**
     * 导出溯源版本列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:version:export')")
    @Log(title = "溯源版本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraceVersion traceVersion)
    {
        List<TraceVersion> list = traceVersionService.selectTraceVersionList(traceVersion);
        ExcelUtil<TraceVersion> util = new ExcelUtil<TraceVersion>(TraceVersion.class);
        util.exportExcel(response, list, "溯源版本数据");
    }

    /**
     * 获取溯源版本详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:version:query')")
    @GetMapping(value = "/{versionId}")
    public AjaxResult getInfo(@PathVariable("versionId") Long versionId)
    {
        return AjaxResult.success(traceVersionService.selectTraceVersionByVersionId(versionId));
    }

    /**
     * 新增溯源版本
     */
    @PreAuthorize("@ss.hasPermi('agriculture:version:add')")
    @Log(title = "溯源版本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraceVersion traceVersion)
    {
        return toAjax(traceVersionService.insertTraceVersion(traceVersion));
    }

    /**
     * 修改溯源版本
     */
    @PreAuthorize("@ss.hasPermi('agriculture:version:edit')")
    @Log(title = "溯源版本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraceVersion traceVersion)
    {
        return toAjax(traceVersionService.updateTraceVersion(traceVersion));
    }

    /**
     * 删除溯源版本
     */
    @PreAuthorize("@ss.hasPermi('agriculture:version:remove')")
    @Log(title = "溯源版本", businessType = BusinessType.DELETE)
	@DeleteMapping("/{versionIds}")
    public AjaxResult remove(@PathVariable Long[] versionIds)
    {
        return toAjax(traceVersionService.deleteTraceVersionByVersionIds(versionIds));
    }
}

package com.frog.agriculture.controller;

import java.util.HashMap;
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
import com.frog.agriculture.domain.TraceRecord;
import com.frog.agriculture.service.ITraceRecordService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 溯源码查询记录Controller
 * 
 * @author nealtsiao
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/agriculture/record")
public class TraceRecordController extends BaseController
{
    @Autowired
    private ITraceRecordService traceRecordService;

    /**
     * 查询溯源码查询记录列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceRecord traceRecord)
    {
        startPage();
        List<TraceRecord> list = traceRecordService.selectTraceRecordList(traceRecord);
        return getDataTable(list);
    }

    /**
     * 导出溯源码查询记录列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:record:export')")
    @Log(title = "溯源码查询记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraceRecord traceRecord)
    {
        List<TraceRecord> list = traceRecordService.selectTraceRecordList(traceRecord);
        ExcelUtil<TraceRecord> util = new ExcelUtil<TraceRecord>(TraceRecord.class);
        util.exportExcel(response, list, "溯源码查询记录数据");
    }

    /**
     * 获取溯源码查询记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return AjaxResult.success(traceRecordService.selectTraceRecordByRecordId(recordId));
    }

    /**
     * 新增溯源码查询记录
     */
//    @PreAuthorize("@ss.hasPermi('agriculture:record:add')")
//    @Log(title = "溯源码查询记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TraceRecord traceRecord)
    {
        return toAjax(traceRecordService.insertTraceRecord(traceRecord));
    }

    /**
     * 修改溯源码查询记录
     */
    @PreAuthorize("@ss.hasPermi('agriculture:record:edit')")
    @Log(title = "溯源码查询记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraceRecord traceRecord)
    {
        return toAjax(traceRecordService.updateTraceRecord(traceRecord));
    }

    /**
     * 删除溯源码查询记录
     */
    @PreAuthorize("@ss.hasPermi('agriculture:record:remove')")
    @Log(title = "溯源码查询记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(traceRecordService.deleteTraceRecordByRecordIds(recordIds));
    }

    /**
     * 查询溯源码统计列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:record:list')")
    @GetMapping("/countList")
    public TableDataInfo countList(TraceRecord traceRecord)
    {
        startPage();
        List<HashMap> list = traceRecordService.countRecordList(traceRecord);
        return getDataTable(list);
    }

}

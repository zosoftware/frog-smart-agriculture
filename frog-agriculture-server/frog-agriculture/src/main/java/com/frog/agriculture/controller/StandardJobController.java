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
import com.frog.agriculture.domain.StandardJob;
import com.frog.agriculture.service.IStandardJobService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 标准作业任务Controller
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/standardJob")
public class StandardJobController extends BaseController
{
    @Autowired
    private IStandardJobService standardJobService;

    /**
     * 查询标准作业任务列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:standardJob:list')")
    @GetMapping("/list")
    public TableDataInfo list(StandardJob standardJob)
    {
        startPage();
        List<StandardJob> list = standardJobService.selectStandardJobList(standardJob);
        return getDataTable(list);
    }

    /**
     * 导出标准作业任务列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:standardJob:export')")
    @Log(title = "标准作业任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StandardJob standardJob)
    {
        List<StandardJob> list = standardJobService.selectStandardJobList(standardJob);
        ExcelUtil<StandardJob> util = new ExcelUtil<StandardJob>(StandardJob.class);
        util.exportExcel(response, list, "标准作业任务数据");
    }

    /**
     * 获取标准作业任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:standardJob:query')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId)
    {
        return AjaxResult.success(standardJobService.selectStandardJobByJobId(jobId));
    }

    /**
     * 新增标准作业任务
     */
    @PreAuthorize("@ss.hasPermi('agriculture:standardJob:add')")
    @Log(title = "标准作业任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StandardJob standardJob)
    {
        return toAjax(standardJobService.insertStandardJob(standardJob));
    }

    /**
     * 修改标准作业任务
     */
    @PreAuthorize("@ss.hasPermi('agriculture:standardJob:edit')")
    @Log(title = "标准作业任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StandardJob standardJob)
    {
        return toAjax(standardJobService.updateStandardJob(standardJob));
    }

    /**
     * 删除标准作业任务
     */
    @PreAuthorize("@ss.hasPermi('agriculture:standardJob:remove')")
    @Log(title = "标准作业任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds)
    {
        return toAjax(standardJobService.deleteStandardJobByJobIds(jobIds));
    }
}

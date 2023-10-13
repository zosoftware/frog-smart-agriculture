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
import com.frog.agriculture.domain.CropBatch;
import com.frog.agriculture.service.ICropBatchService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 作物批次Controller
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/agriculture/batch")
public class CropBatchController extends BaseController
{
    @Autowired
    private ICropBatchService cropBatchService;

    /**
     * 查询作物批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:list')")
    @GetMapping("/list")
    public TableDataInfo list(CropBatch cropBatch)
    {
        startPage();
        List<CropBatch> list = cropBatchService.selectCropBatchList(cropBatch);
        return getDataTable(list);
    }

    /**
     * 导出作物批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:export')")
    @Log(title = "作物批次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CropBatch cropBatch)
    {
        List<CropBatch> list = cropBatchService.selectCropBatchList(cropBatch);
        ExcelUtil<CropBatch> util = new ExcelUtil<CropBatch>(CropBatch.class);
        util.exportExcel(response, list, "作物批次数据");
    }

    /**
     * 获取作物批次详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:query')")
    @GetMapping(value = "/{batchId}")
    public AjaxResult getInfo(@PathVariable("batchId") Long batchId)
    {
        return AjaxResult.success(cropBatchService.selectCropBatchByBatchId(batchId));
    }

    /**
     * 新增作物批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:add')")
    @Log(title = "作物批次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CropBatch cropBatch)
    {
        return toAjax(cropBatchService.insertCropBatch(cropBatch));
    }

    /**
     * 修改作物批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:edit')")
    @Log(title = "作物批次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CropBatch cropBatch)
    {
        return toAjax(cropBatchService.updateCropBatch(cropBatch));
    }

    /**
     * 删除作物批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:remove')")
    @Log(title = "作物批次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{batchIds}")
    public AjaxResult remove(@PathVariable Long[] batchIds)
    {
        return toAjax(cropBatchService.deleteCropBatchByBatchIds(batchIds));
    }
}

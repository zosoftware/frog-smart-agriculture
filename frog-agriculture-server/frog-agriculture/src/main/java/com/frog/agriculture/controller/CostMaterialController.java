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
import com.frog.agriculture.domain.CostMaterial;
import com.frog.agriculture.service.ICostMaterialService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 农资用量Controller
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/costMaterial")
public class CostMaterialController extends BaseController
{
    @Autowired
    private ICostMaterialService costMaterialService;

    /**
     * 查询农资用量列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMaterial:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostMaterial costMaterial)
    {
        startPage();
        List<CostMaterial> list = costMaterialService.selectCostMaterialList(costMaterial);
        return getDataTable(list);
    }

    /**
     * 导出农资用量列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMaterial:export')")
    @Log(title = "农资用量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostMaterial costMaterial)
    {
        List<CostMaterial> list = costMaterialService.selectCostMaterialList(costMaterial);
        ExcelUtil<CostMaterial> util = new ExcelUtil<CostMaterial>(CostMaterial.class);
        util.exportExcel(response, list, "农资用量数据");
    }

    /**
     * 获取农资用量详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMaterial:query')")
    @GetMapping(value = "/{costId}")
    public AjaxResult getInfo(@PathVariable("costId") Long costId)
    {
        return AjaxResult.success(costMaterialService.selectCostMaterialByCostId(costId));
    }

    /**
     * 新增农资用量
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMaterial:add')")
    @Log(title = "农资用量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostMaterial costMaterial)
    {
        return toAjax(costMaterialService.insertCostMaterial(costMaterial));
    }

    /**
     * 修改农资用量
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMaterial:edit')")
    @Log(title = "农资用量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostMaterial costMaterial)
    {
        return toAjax(costMaterialService.updateCostMaterial(costMaterial));
    }

    /**
     * 删除农资用量
     */
    @PreAuthorize("@ss.hasPermi('agriculture:costMaterial:remove')")
    @Log(title = "农资用量", businessType = BusinessType.DELETE)
	@DeleteMapping("/{costIds}")
    public AjaxResult remove(@PathVariable Long[] costIds)
    {
        return toAjax(costMaterialService.deleteCostMaterialByCostIds(costIds));
    }
}

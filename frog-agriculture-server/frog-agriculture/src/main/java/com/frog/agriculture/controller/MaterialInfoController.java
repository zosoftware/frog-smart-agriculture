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
import com.frog.agriculture.domain.MaterialInfo;
import com.frog.agriculture.service.IMaterialInfoService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 农资信息Controller
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
@RestController
@RequestMapping("/agriculture/materialInfo")
public class MaterialInfoController extends BaseController
{
    @Autowired
    private IMaterialInfoService materialInfoService;

    /**
     * 查询农资信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:materialInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(MaterialInfo materialInfo)
    {
        startPage();
        List<MaterialInfo> list = materialInfoService.selectMaterialInfoList(materialInfo);
        return getDataTable(list);
    }

    /**
     * 导出农资信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:materialInfo:export')")
    @Log(title = "农资信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialInfo materialInfo)
    {
        List<MaterialInfo> list = materialInfoService.selectMaterialInfoList(materialInfo);
        ExcelUtil<MaterialInfo> util = new ExcelUtil<MaterialInfo>(MaterialInfo.class);
        util.exportExcel(response, list, "农资信息数据");
    }

    /**
     * 获取农资信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:materialInfo:query')")
    @GetMapping(value = "/{materialId}")
    public AjaxResult getInfo(@PathVariable("materialId") Long materialId)
    {
        return AjaxResult.success(materialInfoService.selectMaterialInfoByMaterialId(materialId));
    }

    /**
     * 新增农资信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:materialInfo:add')")
    @Log(title = "农资信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialInfo materialInfo)
    {
        return toAjax(materialInfoService.insertMaterialInfo(materialInfo));
    }

    /**
     * 修改农资信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:materialInfo:edit')")
    @Log(title = "农资信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialInfo materialInfo)
    {
        return toAjax(materialInfoService.updateMaterialInfo(materialInfo));
    }

    /**
     * 删除农资信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:materialInfo:remove')")
    @Log(title = "农资信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{materialIds}")
    public AjaxResult remove(@PathVariable Long[] materialIds)
    {
        return toAjax(materialInfoService.deleteMaterialInfoByMaterialIds(materialIds));
    }
}

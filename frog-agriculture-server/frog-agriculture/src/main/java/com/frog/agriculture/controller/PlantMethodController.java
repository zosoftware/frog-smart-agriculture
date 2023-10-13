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
import com.frog.agriculture.domain.PlantMethod;
import com.frog.agriculture.service.IPlantMethodService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 种植方法Controller
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/agriculture/method")
public class PlantMethodController extends BaseController
{
    @Autowired
    private IPlantMethodService plantMethodService;

    /**
     * 查询种植方法列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:method:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlantMethod plantMethod)
    {
        startPage();
        List<PlantMethod> list = plantMethodService.selectPlantMethodList(plantMethod);
        return getDataTable(list);
    }

    /**
     * 导出种植方法列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:method:export')")
    @Log(title = "种植方法", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlantMethod plantMethod)
    {
        List<PlantMethod> list = plantMethodService.selectPlantMethodList(plantMethod);
        ExcelUtil<PlantMethod> util = new ExcelUtil<PlantMethod>(PlantMethod.class);
        util.exportExcel(response, list, "种植方法数据");
    }

    /**
     * 获取种植方法详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:method:query')")
    @GetMapping(value = "/{methodId}")
    public AjaxResult getInfo(@PathVariable("methodId") Long methodId)
    {
        return AjaxResult.success(plantMethodService.selectPlantMethodByMethodId(methodId));
    }

    /**
     * 新增种植方法
     */
    @PreAuthorize("@ss.hasPermi('agriculture:method:add')")
    @Log(title = "种植方法", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlantMethod plantMethod)
    {
        return toAjax(plantMethodService.insertPlantMethod(plantMethod));
    }

    /**
     * 修改种植方法
     */
    @PreAuthorize("@ss.hasPermi('agriculture:method:edit')")
    @Log(title = "种植方法", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlantMethod plantMethod)
    {
        return toAjax(plantMethodService.updatePlantMethod(plantMethod));
    }

    /**
     * 删除种植方法
     */
    @PreAuthorize("@ss.hasPermi('agriculture:method:remove')")
    @Log(title = "种植方法", businessType = BusinessType.DELETE)
	@DeleteMapping("/{methodIds}")
    public AjaxResult remove(@PathVariable Long[] methodIds)
    {
        return toAjax(plantMethodService.deletePlantMethodByMethodIds(methodIds));
    }
}

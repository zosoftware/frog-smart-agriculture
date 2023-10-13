package com.frog.agriculture.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.frog.agriculture.model.LandCrop;
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
import com.frog.agriculture.domain.Land;
import com.frog.agriculture.service.ILandService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 地块Controller
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/agriculture/land")
public class LandController extends BaseController
{
    @Autowired
    private ILandService landService;

    /**
     * 查询地块列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:land:list')")
    @GetMapping("/list")
    public TableDataInfo list(Land land)
    {
        startPage();
        List<Land> list = landService.selectLandList(land);
        return getDataTable(list);
    }

    /**
     * 查询地块列表
     */
    @GetMapping("/cropLandList")
    public TableDataInfo cropLandList(LandCrop land)
    {
        startPage();
        List<LandCrop> list = landService.selectLandCropList(land);
        return getDataTable(list);
    }

    /**
     * 导出地块列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:land:export')")
    @Log(title = "地块", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Land land)
    {
        List<Land> list = landService.selectLandList(land);
        ExcelUtil<Land> util = new ExcelUtil<Land>(Land.class);
        util.exportExcel(response, list, "地块数据");
    }

    /**
     * 获取地块详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:land:query')")
    @GetMapping(value = "/{landId}")
    public AjaxResult getInfo(@PathVariable("landId") Long landId)
    {
        return AjaxResult.success(landService.selectLandByLandId(landId));
    }

    /**
     * 新增地块
     */
    @PreAuthorize("@ss.hasPermi('agriculture:land:add')")
    @Log(title = "地块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Land land)
    {
        return toAjax(landService.insertLand(land));
    }

    /**
     * 修改地块
     */
    @PreAuthorize("@ss.hasPermi('agriculture:land:edit')")
    @Log(title = "地块", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Land land)
    {
        return toAjax(landService.updateLand(land));
    }

    /**
     * 删除地块
     */
    @PreAuthorize("@ss.hasPermi('agriculture:land:remove')")
    @Log(title = "地块", businessType = BusinessType.DELETE)
	@DeleteMapping("/{landIds}")
    public AjaxResult remove(@PathVariable Long[] landIds)
    {
        return toAjax(landService.deleteLandByLandIds(landIds));
    }
}

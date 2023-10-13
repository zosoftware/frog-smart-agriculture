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
import com.frog.agriculture.domain.ExtUpgrade;
import com.frog.agriculture.service.IExtUpgradeService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * App升级Controller
 * 
 * @author nealtsiao
 * @date 2023-08-23
 */
@RestController
@RequestMapping("/agriculture/upgrade")
public class ExtUpgradeController extends BaseController
{
    @Autowired
    private IExtUpgradeService extUpgradeService;

    /**
     * 查询App升级列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:upgrade:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExtUpgrade extUpgrade)
    {
        startPage();
        List<ExtUpgrade> list = extUpgradeService.selectExtUpgradeList(extUpgrade);
        return getDataTable(list);
    }

    /**
     * 导出App升级列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:upgrade:export')")
    @Log(title = "App升级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExtUpgrade extUpgrade)
    {
        List<ExtUpgrade> list = extUpgradeService.selectExtUpgradeList(extUpgrade);
        ExcelUtil<ExtUpgrade> util = new ExcelUtil<ExtUpgrade>(ExtUpgrade.class);
        util.exportExcel(response, list, "App升级数据");
    }

    /**
     * 获取App升级详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:upgrade:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return AjaxResult.success(extUpgradeService.selectExtUpgradeByRecordId(recordId));
    }

    /**
     * 新增App升级
     */
    @PreAuthorize("@ss.hasPermi('agriculture:upgrade:add')")
    @Log(title = "App升级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExtUpgrade extUpgrade)
    {
        return toAjax(extUpgradeService.insertExtUpgrade(extUpgrade));
    }

    /**
     * 修改App升级
     */
    @PreAuthorize("@ss.hasPermi('agriculture:upgrade:edit')")
    @Log(title = "App升级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExtUpgrade extUpgrade)
    {
        return toAjax(extUpgradeService.updateExtUpgrade(extUpgrade));
    }

    /**
     * 删除App升级
     */
    @PreAuthorize("@ss.hasPermi('agriculture:upgrade:remove')")
    @Log(title = "App升级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(extUpgradeService.deleteExtUpgradeByRecordIds(recordIds));
    }
}

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
import com.frog.agriculture.domain.TraceSellpro;
import com.frog.agriculture.service.ITraceSellproService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 溯源产品Controller
 * 
 * @author nealtsiao
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/agriculture/sellpro")
public class TraceSellproController extends BaseController
{
    @Autowired
    private ITraceSellproService traceSellproService;

    /**
     * 查询溯源产品列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:sellpro:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceSellpro traceSellpro)
    {
        startPage();
        List<TraceSellpro> list = traceSellproService.selectTraceSellproList(traceSellpro);
        return getDataTable(list);
    }

    /**
     * 导出溯源产品列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:sellpro:export')")
    @Log(title = "溯源产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraceSellpro traceSellpro)
    {
        List<TraceSellpro> list = traceSellproService.selectTraceSellproList(traceSellpro);
        ExcelUtil<TraceSellpro> util = new ExcelUtil<TraceSellpro>(TraceSellpro.class);
        util.exportExcel(response, list, "溯源产品数据");
    }

    /**
     * 获取溯源产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:sellpro:query')")
    @GetMapping(value = "/{sellproId}")
    public AjaxResult getInfo(@PathVariable("sellproId") Long sellproId)
    {
        return AjaxResult.success(traceSellproService.selectTraceSellproBySellproId(sellproId));
    }

    /**
     * 新增溯源产品
     */
    @PreAuthorize("@ss.hasPermi('agriculture:sellpro:add')")
    @Log(title = "溯源产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraceSellpro traceSellpro)
    {
        return toAjax(traceSellproService.insertTraceSellpro(traceSellpro));
    }

    /**
     * 修改溯源产品
     */
    @PreAuthorize("@ss.hasPermi('agriculture:sellpro:edit')")
    @Log(title = "溯源产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraceSellpro traceSellpro)
    {
        return toAjax(traceSellproService.updateTraceSellpro(traceSellpro));
    }

    /**
     * 删除溯源产品
     */
    @PreAuthorize("@ss.hasPermi('agriculture:sellpro:remove')")
    @Log(title = "溯源产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sellproIds}")
    public AjaxResult remove(@PathVariable Long[] sellproIds)
    {
        return toAjax(traceSellproService.deleteTraceSellproBySellproIds(sellproIds));
    }
}

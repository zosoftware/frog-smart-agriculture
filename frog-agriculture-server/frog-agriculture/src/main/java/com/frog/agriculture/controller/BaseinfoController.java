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
import com.frog.agriculture.domain.Baseinfo;
import com.frog.agriculture.service.IBaseinfoService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 基地信息Controller
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/agriculture/baseinfo")
public class BaseinfoController extends BaseController
{
    @Autowired
    private IBaseinfoService baseinfoService;

    /**
     * 查询基地信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:baseinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(Baseinfo baseinfo)
    {
        startPage();
        List<Baseinfo> list = baseinfoService.selectBaseinfoList(baseinfo);
        return getDataTable(list);
    }

    /**
     * 导出基地信息列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:baseinfo:export')")
    @Log(title = "基地信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Baseinfo baseinfo)
    {
        List<Baseinfo> list = baseinfoService.selectBaseinfoList(baseinfo);
        ExcelUtil<Baseinfo> util = new ExcelUtil<Baseinfo>(Baseinfo.class);
        util.exportExcel(response, list, "基地信息数据");
    }

    /**
     * 获取基地信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:baseinfo:query')")
    @GetMapping(value = "/{baseId}")
    public AjaxResult getInfo(@PathVariable("baseId") Long baseId)
    {
        return AjaxResult.success(baseinfoService.selectBaseinfoByBaseId(baseId));
    }

    /**
     * 获取基地信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:baseinfo:query')")
    @GetMapping
    public AjaxResult getInfoLimitOne()
    {
        return AjaxResult.success(baseinfoService.selectBaseinfoLimitOne());
    }

    /**
     * 新增基地信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:baseinfo:add')")
    @Log(title = "基地信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Baseinfo baseinfo)
    {
        return toAjax(baseinfoService.insertBaseinfo(baseinfo));
    }

    /**
     * 修改基地信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:baseinfo:edit')")
    @Log(title = "基地信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Baseinfo baseinfo)
    {
        return toAjax(baseinfoService.updateBaseinfo(baseinfo));
    }

    /**
     * 删除基地信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:baseinfo:remove')")
    @Log(title = "基地信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{baseIds}")
    public AjaxResult remove(@PathVariable Long[] baseIds)
    {
        return toAjax(baseinfoService.deleteBaseinfoByBaseIds(baseIds));
    }
}

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
import com.frog.agriculture.domain.Germplasm;
import com.frog.agriculture.service.IGermplasmService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 种质Controller
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/agriculture/germplasm")
public class GermplasmController extends BaseController
{
    @Autowired
    private IGermplasmService germplasmService;

    /**
     * 查询种质列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:germplasm:list')")
    @GetMapping("/list")
    public TableDataInfo list(Germplasm germplasm)
    {
        startPage();
        List<Germplasm> list = germplasmService.selectGermplasmList(germplasm);
        return getDataTable(list);
    }

    /**
     * 导出种质列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:germplasm:export')")
    @Log(title = "种质", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Germplasm germplasm)
    {
        List<Germplasm> list = germplasmService.selectGermplasmList(germplasm);
        ExcelUtil<Germplasm> util = new ExcelUtil<Germplasm>(Germplasm.class);
        util.exportExcel(response, list, "种质数据");
    }

    /**
     * 获取种质详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:germplasm:query')")
    @GetMapping(value = "/{germplasmId}")
    public AjaxResult getInfo(@PathVariable("germplasmId") Long germplasmId)
    {
        return AjaxResult.success(germplasmService.selectGermplasmByGermplasmId(germplasmId));
    }

    /**
     * 新增种质
     */
    @PreAuthorize("@ss.hasPermi('agriculture:germplasm:add')")
    @Log(title = "种质", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Germplasm germplasm)
    {
        return toAjax(germplasmService.insertGermplasm(germplasm));
    }

    /**
     * 修改种质
     */
    @PreAuthorize("@ss.hasPermi('agriculture:germplasm:edit')")
    @Log(title = "种质", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Germplasm germplasm)
    {
        return toAjax(germplasmService.updateGermplasm(germplasm));
    }

    /**
     * 删除种质
     */
    @PreAuthorize("@ss.hasPermi('agriculture:germplasm:remove')")
    @Log(title = "种质", businessType = BusinessType.DELETE)
	@DeleteMapping("/{germplasmIds}")
    public AjaxResult remove(@PathVariable Long[] germplasmIds)
    {
        return toAjax(germplasmService.deleteGermplasmByGermplasmIds(germplasmIds));
    }
}

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
import com.frog.agriculture.domain.GermplasmIntro;
import com.frog.agriculture.service.IGermplasmIntroService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 种质介绍Controller
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/agriculture/intro")
public class GermplasmIntroController extends BaseController
{
    @Autowired
    private IGermplasmIntroService germplasmIntroService;

    /**
     * 查询种质介绍列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:intro:list')")
    @GetMapping("/list")
    public TableDataInfo list(GermplasmIntro germplasmIntro)
    {
        startPage();
        List<GermplasmIntro> list = germplasmIntroService.selectGermplasmIntroList(germplasmIntro);
        return getDataTable(list);
    }

    /**
     * 导出种质介绍列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:intro:export')")
    @Log(title = "种质介绍", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GermplasmIntro germplasmIntro)
    {
        List<GermplasmIntro> list = germplasmIntroService.selectGermplasmIntroList(germplasmIntro);
        ExcelUtil<GermplasmIntro> util = new ExcelUtil<GermplasmIntro>(GermplasmIntro.class);
        util.exportExcel(response, list, "种质介绍数据");
    }

    /**
     * 获取种质介绍详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:intro:query')")
    @GetMapping(value = "/{introId}")
    public AjaxResult getInfo(@PathVariable("introId") Long introId)
    {
        return AjaxResult.success(germplasmIntroService.selectGermplasmIntroByIntroId(introId));
    }

    /**
     * 新增种质介绍
     */
    @PreAuthorize("@ss.hasPermi('agriculture:intro:add')")
    @Log(title = "种质介绍", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GermplasmIntro germplasmIntro)
    {
        return toAjax(germplasmIntroService.insertGermplasmIntro(germplasmIntro));
    }

    /**
     * 修改种质介绍
     */
    @PreAuthorize("@ss.hasPermi('agriculture:intro:edit')")
    @Log(title = "种质介绍", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GermplasmIntro germplasmIntro)
    {
        return toAjax(germplasmIntroService.updateGermplasmIntro(germplasmIntro));
    }

    /**
     * 删除种质介绍
     */
    @PreAuthorize("@ss.hasPermi('agriculture:intro:remove')")
    @Log(title = "种质介绍", businessType = BusinessType.DELETE)
	@DeleteMapping("/{introIds}")
    public AjaxResult remove(@PathVariable Long[] introIds)
    {
        return toAjax(germplasmIntroService.deleteGermplasmIntroByIntroIds(introIds));
    }
}

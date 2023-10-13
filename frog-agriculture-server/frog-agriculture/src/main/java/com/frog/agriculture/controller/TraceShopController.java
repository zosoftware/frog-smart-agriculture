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
import com.frog.agriculture.domain.TraceShop;
import com.frog.agriculture.service.ITraceShopService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 店铺Controller
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
@RestController
@RequestMapping("/agriculture/shop")
public class TraceShopController extends BaseController
{
    @Autowired
    private ITraceShopService traceShopService;

    /**
     * 查询店铺列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:shop:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceShop traceShop)
    {
        startPage();
        List<TraceShop> list = traceShopService.selectTraceShopList(traceShop);
        return getDataTable(list);
    }

    /**
     * 导出店铺列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:shop:export')")
    @Log(title = "店铺", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TraceShop traceShop)
    {
        List<TraceShop> list = traceShopService.selectTraceShopList(traceShop);
        ExcelUtil<TraceShop> util = new ExcelUtil<TraceShop>(TraceShop.class);
        util.exportExcel(response, list, "店铺数据");
    }

    /**
     * 获取店铺详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:shop:query')")
    @GetMapping(value = "/{shopId}")
    public AjaxResult getInfo(@PathVariable("shopId") Long shopId)
    {
        return AjaxResult.success(traceShopService.selectTraceShopByShopId(shopId));
    }

    /**
     * 新增店铺
     */
    @PreAuthorize("@ss.hasPermi('agriculture:shop:add')")
    @Log(title = "店铺", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraceShop traceShop)
    {
        return toAjax(traceShopService.insertTraceShop(traceShop));
    }

    /**
     * 修改店铺
     */
    @PreAuthorize("@ss.hasPermi('agriculture:shop:edit')")
    @Log(title = "店铺", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraceShop traceShop)
    {
        return toAjax(traceShopService.updateTraceShop(traceShop));
    }

    /**
     * 删除店铺
     */
    @PreAuthorize("@ss.hasPermi('agriculture:shop:remove')")
    @Log(title = "店铺", businessType = BusinessType.DELETE)
	@DeleteMapping("/{shopIds}")
    public AjaxResult remove(@PathVariable Long[] shopIds)
    {
        return toAjax(traceShopService.deleteTraceShopByShopIds(shopIds));
    }
}

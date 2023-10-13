package com.frog.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.frog.iot.domain.ViewConfig;
import com.frog.iot.service.IViewConfigService;
import com.frog.common.utils.poi.ExcelUtil;
import com.frog.common.core.page.TableDataInfo;

/**
 * 界面可视化配置Controller
 * 
 * @author kerwincui
 * @date 2022-11-15
 */
@Api(tags = "界面可视化配置")
@RestController
@RequestMapping("/iot/viewConfig")
public class ViewConfigController extends BaseController
{
    @Autowired
    private IViewConfigService viewConfigService;

    /**
     * 查询界面可视化配置列表
     */
    @GetMapping("/list")
    @ApiOperation("界面可视化配置分页列表")
    public TableDataInfo list(ViewConfig viewConfig)
    {
        startPage();
        List<ViewConfig> list = viewConfigService.selectViewConfigList(viewConfig);
        return getDataTable(list);
    }

    /**
     * 导出界面可视化配置列表
     */
    @Log(title = "界面可视化配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出界面可视化配置")
    public void export(HttpServletResponse response, ViewConfig viewConfig)
    {
        List<ViewConfig> list = viewConfigService.selectViewConfigList(viewConfig);
        ExcelUtil<ViewConfig> util = new ExcelUtil<ViewConfig>(ViewConfig.class);
        util.exportExcel(response, list, "界面可视化配置数据");
    }

    /**
     * 获取界面可视化配置详细信息
     */
    @GetMapping(value = "/{viewId}")
    @ApiOperation("获取界面可视化详情")
    public AjaxResult getInfo(@PathVariable("viewId") Long viewId)
    {
        return AjaxResult.success(viewConfigService.selectViewConfigByViewId(viewId));
    }

    /**
     * 新增界面可视化配置
     */
    @Log(title = "界面可视化配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增界面可视化配置")
    public AjaxResult add(@RequestBody ViewConfig viewConfig)
    {
        Long viewId=viewConfigService.insertViewConfig(viewConfig);
        return AjaxResult.success(viewId);
    }

    /**
     * 修改界面可视化配置
     */
    @Log(title = "界面可视化配置", businessType = BusinessType.UPDATE)
    @ApiOperation("编辑界面可视化配置")
    @PutMapping
    public AjaxResult edit(@RequestBody ViewConfig viewConfig)
    {
        return toAjax(viewConfigService.updateViewConfig(viewConfig));
    }

    /**
     * 删除界面可视化配置
     */
    @Log(title = "界面可视化配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{viewIds}")
    @ApiOperation("批量删除界面可视化配置")
    public AjaxResult remove(@PathVariable Long[] viewIds)
    {
        return toAjax(viewConfigService.deleteViewConfigByViewIds(viewIds));
    }
}

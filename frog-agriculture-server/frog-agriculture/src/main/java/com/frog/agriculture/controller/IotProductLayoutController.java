package com.frog.agriculture.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.frog.common.annotation.Log;
import com.frog.common.core.controller.BaseController;
import com.frog.common.core.domain.AjaxResult;
import com.frog.common.enums.BusinessType;
import com.frog.agriculture.domain.IotProductLayout;
import com.frog.agriculture.service.IIotProductLayoutService;
import com.frog.common.core.page.TableDataInfo;

/**
 * 产品布局Controller
 * 
 * @author nealtsiao
 * @date 2023-08-11
 */
@RestController
@RequestMapping("/agriculture/layout")
public class IotProductLayoutController extends BaseController
{
    @Autowired
    private IIotProductLayoutService iotProductLayoutService;

    /**
     * 查询产品布局列表
     */
    @GetMapping("/list")
    public TableDataInfo list(IotProductLayout iotProductLayout)
    {
        startPage();
        List<IotProductLayout> list = iotProductLayoutService.selectIotProductLayoutList(iotProductLayout);
        return getDataTable(list);
    }


    /**
     * 保存产品布局
     */
    @PreAuthorize("@ss.hasPermi('agriculture:layout:add')")
    @Log(title = "产品布局", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<IotProductLayout> iotProductLayoutList)
    {
        return toAjax(iotProductLayoutService.insertIotProductLayout(iotProductLayoutList));
    }


    /**
     * 重置产品布局
     */
    @PreAuthorize("@ss.hasPermi('agriculture:layout:remove')")
    @Log(title = "产品布局", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productId}")
    public AjaxResult remove(@PathVariable Long productId)
    {
        return toAjax(iotProductLayoutService.deleteIotProductLayoutByProductIdId(productId));
    }
}

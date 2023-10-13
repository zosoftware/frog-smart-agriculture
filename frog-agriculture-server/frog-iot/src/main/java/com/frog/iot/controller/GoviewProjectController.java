package com.frog.iot.controller;

import com.frog.common.annotation.Log;
import com.frog.common.config.RuoYiConfig;
import com.frog.common.core.controller.BaseController;
import com.frog.common.core.domain.AjaxResult;
import com.frog.common.core.page.TableDataInfo;
import com.frog.common.enums.BusinessType;
import com.frog.common.utils.StringUtils;
import com.frog.common.utils.bean.BeanUtils;
import com.frog.iot.domain.GoviewProject;
import com.frog.iot.domain.GoviewProjectData;
import com.frog.iot.model.goview.GoviewProjectVo;
import com.frog.iot.service.IGoviewProjectDataService;
import com.frog.iot.service.IGoviewProjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目Controller
 * 
 * @author kami
 * @date 2022-10-27
 */
@RestController
@RequestMapping("/goview/project")
public class GoviewProjectController extends BaseController {

    @Autowired
    private IGoviewProjectService goviewProjectService;

    @Autowired
    private IGoviewProjectDataService goviewProjectDataService;

    /**
     * 查询项目列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GoviewProject goviewProject) {
        startPage();
        List<GoviewProject> list = goviewProjectService.selectGoviewProjectList(goviewProject);
        return getDataTable(list);
    }

    /**
     * 获取项目详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(goviewProjectService.selectGoviewProjectById(id));
    }

    /**
     * 新增项目
     */
    @Log(title = "项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoviewProject goviewProject) {
        String projectId = goviewProjectService.insertGoviewProject(goviewProject);
        if(StringUtils.isNotEmpty(projectId)){
            return AjaxResult.success("创建成功",goviewProject);
        }else {
            return AjaxResult.error("创建失败");
        }
    }

    /**
     * 修改项目
     */
    @Log(title = "项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoviewProject goviewProject) {
        return toAjax(goviewProjectService.updateGoviewProject(goviewProject));
    }

    /**
     * 删除项目
     */
    @Log(title = "项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(goviewProjectService.deleteGoviewProjectByIds(ids));
    }


    /**
     * 获取项目存储数据
     * @param projectId 项目id
     * @return
     */
    @GetMapping("/getData")
    public AjaxResult getData(String projectId) {
        GoviewProject goviewProject = goviewProjectService.selectGoviewProjectById(projectId);
        GoviewProjectData projectData = goviewProjectDataService.selectGoviewProjectDataByProjectId(projectId);
        GoviewProjectVo goviewProjectVo = new GoviewProjectVo();
        BeanUtils.copyBeanProp(goviewProjectVo,goviewProject);
        if(projectData != null) {
            goviewProjectVo.setContent(projectData.getDataToStr());
        }
        return AjaxResult.success(goviewProjectVo);
    }


    /**
     * 保存大屏内部数据(字节)
     * @param data
     * @return
     */
    @PostMapping("/save/data")
    public AjaxResult saveData(GoviewProjectData data) {
        GoviewProject goviewProject= goviewProjectService.selectGoviewProjectById(data.getProjectId());
        if(goviewProject == null) {
            return AjaxResult.error("没有该项目ID");
        }
        int i = goviewProjectDataService.insertOrUpdateGoviewProjectData(data);
        if(i > 0) {
            return AjaxResult.success("数据保存成功");
        }
        return AjaxResult.error("保存失败");
    }


    /**
     * goview文件上传（同一个大屏覆盖保存）
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public AjaxResult uploadFile(@RequestBody MultipartFile object) throws Exception {
        try {
            String filePath = RuoYiConfig.getProfile();
            // 获取文件名和文件类型
            String fileName = object.getOriginalFilename();
            fileName = "/goview/" + getLoginUser().getUserId().toString() + "/" + fileName;
            //创建目录
            File desc = new File(filePath + File.separator + fileName);
            if (!desc.exists()) {
                if (!desc.getParentFile().exists()) {
                    desc.getParentFile().mkdirs();
                }
            }
            // 存储文件-覆盖存储（一个文件一个图，防止过多）
            object.transferTo(desc);
            String url = "/profile" + fileName;
            Map<String, Object> map=new HashMap<String, Object>(2);
            map.put("fileName", url);
            map.put("url", url);
            return AjaxResult.success("上传成功",map);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

}

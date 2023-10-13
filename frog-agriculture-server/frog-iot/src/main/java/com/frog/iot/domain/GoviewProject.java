package com.frog.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 项目对象 iot_goview_project
 * 
 * @author kami
 * @date 2022-10-27
 */
public class GoviewProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目状态[-1未发布,1发布] */
    @Excel(name = "项目状态[0未发布,1发布]")
    private Integer state;

    /** 删除状态[1删除,-1未删除] */
    private Long delFlag;

    /** 首页图片 */
    @Excel(name = "首页图片")
    private String indexImage;

    /** 项目介绍 */
    @Excel(name = "项目介绍")
    private String remarks;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }
    public void setState(Integer state) 
    {
        this.state = state;
    }

    public Integer getState() 
    {
        return state;
    }
    public void setDelFlag(Long delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Long getDelFlag() 
    {
        return delFlag;
    }
    public void setIndexImage(String indexImage) 
    {
        this.indexImage = indexImage;
    }

    public String getIndexImage() 
    {
        return indexImage;
    }
    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectName", getProjectName())
            .append("state", getState())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("delFlag", getDelFlag())
            .append("indexImage", getIndexImage())
            .append("remarks", getRemarks())
            .toString();
    }
}

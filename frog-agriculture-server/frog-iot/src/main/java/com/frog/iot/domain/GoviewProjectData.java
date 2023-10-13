package com.frog.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.core.domain.BaseEntity;

/**
 * 项目数据关联对象 iot_goview_project_data
 * 
 * @author kami
 * @date 2022-10-27
 */
public class GoviewProjectData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 项目id */
    private String projectId;

    /** 存储数据 */
    private byte[] content;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setProjectId(String projectId) 
    {
        this.projectId = projectId;
    }

    public String getProjectId() 
    {
        return projectId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectId", getProjectId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("content", getContent())
            .toString();
    }

    public String getDataToStr() {
        byte[] bs = getContent();
        String str = "二进制转换错误";
        str = new String(bs);
        return str;
    }
}

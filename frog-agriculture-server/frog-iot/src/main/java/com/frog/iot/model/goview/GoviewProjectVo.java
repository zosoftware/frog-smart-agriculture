package com.frog.iot.model.goview;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.frog.common.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class GoviewProjectVo implements Serializable {
    private static final long serialVersionUID = 1L;

	
	private String id;
	
	private String projectName;
	
	private Integer state;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;
	
	private String createBy;
	
	private Integer delFlag;
	
	private String indexImage;
	
	private String remarks;
	
	private String content;
	
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id =  id;
	}
	@JsonProperty("projectName")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName =  projectName;
	}
	@JsonProperty("state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state =  state;
	}
	@JsonProperty("createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime =  createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@JsonProperty("indexImage")
	public String getIndexImage() {
		return indexImage;
	}

	public void setIndexImage(String indexImage) {
		this.indexImage =  indexImage;
	}
	@JsonProperty("remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks =  remarks;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String dateToStringConvert(Date date) {
		if(date!=null) {
			return DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",date);
		}
		return "";
	}
}
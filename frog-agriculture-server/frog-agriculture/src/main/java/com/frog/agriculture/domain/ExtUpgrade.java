package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * App升级对象 ext_upgrade
 * 
 * @author nealtsiao
 * @date 2023-08-23
 */
public class ExtUpgrade extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 升级类型 */
    @Excel(name = "升级类型")
    private String updateType;

    /** 是否强制升级 */
    @Excel(name = "是否强制升级")
    private String isForceUpdate;

    /** apk升级地址 */
    @Excel(name = "apk升级地址")
    private String androidUrl;

    /** ios升级地址 */
    @Excel(name = "ios升级地址")
    private String iosUrl;

    /** 升级内容描述 */
    @Excel(name = "升级内容描述")
    private String con;

    /** 版本号 */
    @Excel(name = "版本号")
    private String version;

    /** wgt版本号 */
    @Excel(name = "wgt版本号")
    private String wgtVersion;

    /** 版本名称 */
    @Excel(name = "版本名称")
    private String versionName;

    /** 应用版本号 */
    @Excel(name = "应用版本号")
    private String versionCode;

    /** 测试用户 */
    @Excel(name = "测试用户")
    private String testUser;

    /** 是否启用 0不启用 1启用 */
    @Excel(name = "是否启用 0不启用 1启用")
    private String isCurrent;

    /** 状态  */
    @Excel(name = "状态 ")
    private String status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setUpdateType(String updateType) 
    {
        this.updateType = updateType;
    }

    public String getUpdateType() 
    {
        return updateType;
    }
    public void setIsForceUpdate(String isForceUpdate) 
    {
        this.isForceUpdate = isForceUpdate;
    }

    public String getIsForceUpdate() 
    {
        return isForceUpdate;
    }
    public void setAndroidUrl(String androidUrl) 
    {
        this.androidUrl = androidUrl;
    }

    public String getAndroidUrl() 
    {
        return androidUrl;
    }
    public void setIosUrl(String iosUrl) 
    {
        this.iosUrl = iosUrl;
    }

    public String getIosUrl() 
    {
        return iosUrl;
    }
    public void setCon(String con) 
    {
        this.con = con;
    }

    public String getCon() 
    {
        return con;
    }
    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
    }
    public void setWgtVersion(String wgtVersion) 
    {
        this.wgtVersion = wgtVersion;
    }

    public String getWgtVersion() 
    {
        return wgtVersion;
    }
    public void setVersionName(String versionName) 
    {
        this.versionName = versionName;
    }

    public String getVersionName() 
    {
        return versionName;
    }
    public void setVersionCode(String versionCode) 
    {
        this.versionCode = versionCode;
    }

    public String getVersionCode() 
    {
        return versionCode;
    }
    public void setTestUser(String testUser) 
    {
        this.testUser = testUser;
    }

    public String getTestUser() 
    {
        return testUser;
    }
    public void setIsCurrent(String isCurrent) 
    {
        this.isCurrent = isCurrent;
    }

    public String getIsCurrent() 
    {
        return isCurrent;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() 
    {
        return orderNum;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("updateType", getUpdateType())
            .append("isForceUpdate", getIsForceUpdate())
            .append("androidUrl", getAndroidUrl())
            .append("iosUrl", getIosUrl())
            .append("con", getCon())
            .append("version", getVersion())
            .append("wgtVersion", getWgtVersion())
            .append("versionName", getVersionName())
            .append("versionCode", getVersionCode())
            .append("testUser", getTestUser())
            .append("isCurrent", getIsCurrent())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("orderNum", getOrderNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}

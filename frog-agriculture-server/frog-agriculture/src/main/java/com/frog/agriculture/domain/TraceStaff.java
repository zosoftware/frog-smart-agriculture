package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 溯源人员对象 agriculture_trace_staff
 * 
 * @author nealtsiao
 * @date 2023-08-02
 */
public class TraceStaff extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工ID */
    private Long staffId;

    /** 员工职位 */
    @Excel(name = "员工职位")
    private String staffPosition;

    /** 员工名称 */
    @Excel(name = "员工名称")
    private String staffName;

    /** 员工联系方式 */
    @Excel(name = "员工联系方式")
    private String staffTel;

    /** 员工描述 */
    @Excel(name = "员工描述")
    private String staffDes;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setStaffId(Long staffId) 
    {
        this.staffId = staffId;
    }

    public Long getStaffId() 
    {
        return staffId;
    }
    public void setStaffPosition(String staffPosition) 
    {
        this.staffPosition = staffPosition;
    }

    public String getStaffPosition() 
    {
        return staffPosition;
    }
    public void setStaffName(String staffName) 
    {
        this.staffName = staffName;
    }

    public String getStaffName() 
    {
        return staffName;
    }
    public void setStaffTel(String staffTel) 
    {
        this.staffTel = staffTel;
    }

    public String getStaffTel() 
    {
        return staffTel;
    }
    public void setStaffDes(String staffDes) 
    {
        this.staffDes = staffDes;
    }

    public String getStaffDes() 
    {
        return staffDes;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
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
            .append("staffId", getStaffId())
            .append("staffPosition", getStaffPosition())
            .append("staffName", getStaffName())
            .append("staffTel", getStaffTel())
            .append("staffDes", getStaffDes())
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

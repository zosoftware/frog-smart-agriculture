package com.frog.agriculture.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;

/**
 * 雇员对象 agriculture_employee
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public class Employee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 雇员ID */
    private Long employeeId;

    /** 编码 */
    @Excel(name = "编码")
    private String employeeCode;

    /** 姓名 */
    @Excel(name = "姓名")
    private String employeeName;

    /** 字典 agriculture_employee_type */
    @Excel(name = "字典 agriculture_employee_type")
    private String employeeType;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String employeeTel;

    /** 字典 sys_user_sex */
    @Excel(name = "字典 sys_user_sex")
    private String employeeSex;

    /** 地址 */
    @Excel(name = "地址")
    private String employeeAddress;

    /** 状态 */
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setEmployeeId(Long employeeId) 
    {
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() 
    {
        return employeeId;
    }
    public void setEmployeeCode(String employeeCode) 
    {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCode() 
    {
        return employeeCode;
    }
    public void setEmployeeName(String employeeName) 
    {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() 
    {
        return employeeName;
    }
    public void setEmployeeType(String employeeType) 
    {
        this.employeeType = employeeType;
    }

    public String getEmployeeType() 
    {
        return employeeType;
    }
    public void setEmployeeTel(String employeeTel) 
    {
        this.employeeTel = employeeTel;
    }

    public String getEmployeeTel() 
    {
        return employeeTel;
    }
    public void setEmployeeSex(String employeeSex) 
    {
        this.employeeSex = employeeSex;
    }

    public String getEmployeeSex() 
    {
        return employeeSex;
    }
    public void setEmployeeAddress(String employeeAddress) 
    {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeAddress() 
    {
        return employeeAddress;
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
            .append("employeeId", getEmployeeId())
            .append("employeeCode", getEmployeeCode())
            .append("employeeName", getEmployeeName())
            .append("employeeType", getEmployeeType())
            .append("employeeTel", getEmployeeTel())
            .append("employeeSex", getEmployeeSex())
            .append("employeeAddress", getEmployeeAddress())
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

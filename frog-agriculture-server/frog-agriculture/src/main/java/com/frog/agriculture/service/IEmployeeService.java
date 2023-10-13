package com.frog.agriculture.service;

import java.util.List;
import com.frog.agriculture.domain.Employee;

/**
 * 雇员Service接口
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public interface IEmployeeService 
{
    /**
     * 查询雇员
     * 
     * @param employeeId 雇员主键
     * @return 雇员
     */
    public Employee selectEmployeeByEmployeeId(Long employeeId);

    /**
     * 查询雇员列表
     * 
     * @param employee 雇员
     * @return 雇员集合
     */
    public List<Employee> selectEmployeeList(Employee employee);

    /**
     * 新增雇员
     * 
     * @param employee 雇员
     * @return 结果
     */
    public int insertEmployee(Employee employee);

    /**
     * 修改雇员
     * 
     * @param employee 雇员
     * @return 结果
     */
    public int updateEmployee(Employee employee);

    /**
     * 批量删除雇员
     * 
     * @param employeeIds 需要删除的雇员主键集合
     * @return 结果
     */
    public int deleteEmployeeByEmployeeIds(Long[] employeeIds);

    /**
     * 删除雇员信息
     * 
     * @param employeeId 雇员主键
     * @return 结果
     */
    public int deleteEmployeeByEmployeeId(Long employeeId);
}

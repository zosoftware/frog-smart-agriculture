package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.Employee;

/**
 * 雇员Mapper接口
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
public interface EmployeeMapper 
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
     * 删除雇员
     * 
     * @param employeeId 雇员主键
     * @return 结果
     */
    public int deleteEmployeeByEmployeeId(Long employeeId);

    /**
     * 批量删除雇员
     * 
     * @param employeeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmployeeByEmployeeIds(Long[] employeeIds);
}

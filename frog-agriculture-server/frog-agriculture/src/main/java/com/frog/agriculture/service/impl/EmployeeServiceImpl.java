package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.EmployeeMapper;
import com.frog.agriculture.domain.Employee;
import com.frog.agriculture.service.IEmployeeService;

/**
 * 雇员Service业务层处理
 *
 * @author nealtsiao
 * @date 2023-05-18
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService
{
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 查询雇员
     *
     * @param employeeId 雇员主键
     * @return 雇员
     */
    @Override
    public Employee selectEmployeeByEmployeeId(Long employeeId)
    {
        return employeeMapper.selectEmployeeByEmployeeId(employeeId);
    }

    /**
     * 查询雇员列表
     *
     * @param employee 雇员
     * @return 雇员
     */
    @Override
    public List<Employee> selectEmployeeList(Employee employee)
    {
        return employeeMapper.selectEmployeeList(employee);
    }

    /**
     * 新增雇员
     *
     * @param employee 雇员
     * @return 结果
     */
    @Override
    public int insertEmployee(Employee employee)
    {
        employee.setCreateBy(SecurityUtils.getUserId().toString());
        employee.setCreateTime(DateUtils.getNowDate());
        return employeeMapper.insertEmployee(employee);
    }

    /**
     * 修改雇员
     *
     * @param employee 雇员
     * @return 结果
     */
    @Override
    public int updateEmployee(Employee employee)
    {
        employee.setUpdateBy(SecurityUtils.getUserId().toString());
        employee.setUpdateTime(DateUtils.getNowDate());
        return employeeMapper.updateEmployee(employee);
    }

    /**
     * 批量删除雇员
     *
     * @param employeeIds 需要删除的雇员主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeByEmployeeIds(Long[] employeeIds)
    {
        return employeeMapper.deleteEmployeeByEmployeeIds(employeeIds);
    }

    /**
     * 删除雇员信息
     *
     * @param employeeId 雇员主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeByEmployeeId(Long employeeId)
    {
        return employeeMapper.deleteEmployeeByEmployeeId(employeeId);
    }
}
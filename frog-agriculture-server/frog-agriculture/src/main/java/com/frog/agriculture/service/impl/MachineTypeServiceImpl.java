package com.frog.agriculture.service.impl;

import java.util.List;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.MachineTypeMapper;
import com.frog.agriculture.domain.MachineType;
import com.frog.agriculture.service.IMachineTypeService;

/**
 * 机械类别Service业务层处理
 * 
 * @author kerwincui
 * @date 2023-05-24
 */
@Service
public class MachineTypeServiceImpl implements IMachineTypeService 
{
    @Autowired
    private MachineTypeMapper machineTypeMapper;

    /**
     * 查询机械类别
     * 
     * @param machineTypeId 机械类别主键
     * @return 机械类别
     */
    @Override
    public MachineType selectMachineTypeByMachineTypeId(Long machineTypeId)
    {
        return machineTypeMapper.selectMachineTypeByMachineTypeId(machineTypeId);
    }

    /**
     * 查询机械类别列表
     * 
     * @param machineType 机械类别
     * @return 机械类别
     */
    @Override
    public List<MachineType> selectMachineTypeList(MachineType machineType)
    {
        return machineTypeMapper.selectMachineTypeList(machineType);
    }

    /**
     * 新增机械类别
     * 
     * @param machineType 机械类别
     * @return 结果
     */
    @Override
    public int insertMachineType(MachineType machineType)
    {
        machineType.setCreateBy(SecurityUtils.getUserId().toString());
        machineType.setCreateTime(DateUtils.getNowDate());
        return machineTypeMapper.insertMachineType(machineType);
    }

    /**
     * 修改机械类别
     * 
     * @param machineType 机械类别
     * @return 结果
     */
    @Override
    public int updateMachineType(MachineType machineType)
    {
        machineType.setUpdateBy(SecurityUtils.getUserId().toString());
        machineType.setUpdateTime(DateUtils.getNowDate());
        return machineTypeMapper.updateMachineType(machineType);
    }

    /**
     * 批量删除机械类别
     * 
     * @param machineTypeIds 需要删除的机械类别主键
     * @return 结果
     */
    @Override
    public int deleteMachineTypeByMachineTypeIds(Long[] machineTypeIds)
    {
        return machineTypeMapper.deleteMachineTypeByMachineTypeIds(machineTypeIds);
    }

    /**
     * 删除机械类别信息
     * 
     * @param machineTypeId 机械类别主键
     * @return 结果
     */
    @Override
    public int deleteMachineTypeByMachineTypeId(Long machineTypeId)
    {
        return machineTypeMapper.deleteMachineTypeByMachineTypeId(machineTypeId);
    }
}

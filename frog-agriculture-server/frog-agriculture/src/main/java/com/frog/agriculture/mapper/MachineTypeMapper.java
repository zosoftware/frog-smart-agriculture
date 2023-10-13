package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.MachineType;

/**
 * 机械类别Mapper接口
 * 
 * @author kerwincui
 * @date 2023-05-24
 */
public interface MachineTypeMapper 
{
    /**
     * 查询机械类别
     * 
     * @param machineTypeId 机械类别主键
     * @return 机械类别
     */
    public MachineType selectMachineTypeByMachineTypeId(Long machineTypeId);

    /**
     * 查询机械类别列表
     * 
     * @param machineType 机械类别
     * @return 机械类别集合
     */
    public List<MachineType> selectMachineTypeList(MachineType machineType);

    /**
     * 新增机械类别
     * 
     * @param machineType 机械类别
     * @return 结果
     */
    public int insertMachineType(MachineType machineType);

    /**
     * 修改机械类别
     * 
     * @param machineType 机械类别
     * @return 结果
     */
    public int updateMachineType(MachineType machineType);

    /**
     * 删除机械类别
     * 
     * @param machineTypeId 机械类别主键
     * @return 结果
     */
    public int deleteMachineTypeByMachineTypeId(Long machineTypeId);

    /**
     * 批量删除机械类别
     * 
     * @param machineTypeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMachineTypeByMachineTypeIds(Long[] machineTypeIds);
}

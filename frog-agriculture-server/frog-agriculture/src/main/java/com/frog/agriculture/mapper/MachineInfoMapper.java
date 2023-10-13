package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.MachineInfo;

/**
 * 机械信息Mapper接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface MachineInfoMapper 
{
    /**
     * 查询机械信息
     * 
     * @param machineId 机械信息主键
     * @return 机械信息
     */
    public MachineInfo selectMachineInfoByMachineId(Long machineId);

    /**
     * 查询机械信息列表
     * 
     * @param machineInfo 机械信息
     * @return 机械信息集合
     */
    public List<MachineInfo> selectMachineInfoList(MachineInfo machineInfo);

    /**
     * 新增机械信息
     * 
     * @param machineInfo 机械信息
     * @return 结果
     */
    public int insertMachineInfo(MachineInfo machineInfo);

    /**
     * 修改机械信息
     * 
     * @param machineInfo 机械信息
     * @return 结果
     */
    public int updateMachineInfo(MachineInfo machineInfo);

    /**
     * 删除机械信息
     * 
     * @param machineId 机械信息主键
     * @return 结果
     */
    public int deleteMachineInfoByMachineId(Long machineId);

    /**
     * 批量删除机械信息
     * 
     * @param machineIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMachineInfoByMachineIds(Long[] machineIds);
}

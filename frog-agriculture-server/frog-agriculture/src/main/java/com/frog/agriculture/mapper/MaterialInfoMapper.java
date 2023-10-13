package com.frog.agriculture.mapper;

import java.util.List;
import com.frog.agriculture.domain.MaterialInfo;

/**
 * 农资信息Mapper接口
 * 
 * @author xuweidong
 * @date 2023-05-24
 */
public interface MaterialInfoMapper 
{
    /**
     * 查询农资信息
     * 
     * @param materialId 农资信息主键
     * @return 农资信息
     */
    public MaterialInfo selectMaterialInfoByMaterialId(Long materialId);

    /**
     * 查询农资信息列表
     * 
     * @param materialInfo 农资信息
     * @return 农资信息集合
     */
    public List<MaterialInfo> selectMaterialInfoList(MaterialInfo materialInfo);

    /**
     * 新增农资信息
     * 
     * @param materialInfo 农资信息
     * @return 结果
     */
    public int insertMaterialInfo(MaterialInfo materialInfo);

    /**
     * 修改农资信息
     * 
     * @param materialInfo 农资信息
     * @return 结果
     */
    public int updateMaterialInfo(MaterialInfo materialInfo);

    /**
     * 删除农资信息
     * 
     * @param materialId 农资信息主键
     * @return 结果
     */
    public int deleteMaterialInfoByMaterialId(Long materialId);

    /**
     * 批量删除农资信息
     * 
     * @param materialIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMaterialInfoByMaterialIds(Long[] materialIds);
}

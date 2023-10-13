package com.frog.agriculture.service.impl;

import java.text.ParseException;
import java.util.List;

import com.frog.agriculture.domain.BatchTask;
import com.frog.agriculture.domain.StandardJob;
import com.frog.agriculture.domain.TaskLog;
import com.frog.agriculture.mapper.BatchTaskMapper;
import com.frog.agriculture.mapper.TaskLogMapper;
import com.frog.common.utils.DateUtils;
import com.frog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.agriculture.mapper.CropBatchMapper;
import com.frog.agriculture.mapper.StandardJobMapper;
import com.frog.agriculture.domain.CropBatch;
import com.frog.agriculture.service.ICropBatchService;

/**
 * 作物批次Service业务层处理
 * 
 * @author nealtsiao
 * @date 2023-05-13
 */
@Service
public class CropBatchServiceImpl implements ICropBatchService 
{
    @Autowired
    private CropBatchMapper cropBatchMapper;
    @Autowired
    private StandardJobMapper standardJobMapper;
    @Autowired
    private BatchTaskMapper batchTaskMapper;
    @Autowired
    private TaskLogMapper taskLogMapper;

    /**
     * 查询作物批次
     * 
     * @param batchId 作物批次主键
     * @return 作物批次
     */
    @Override
    public CropBatch selectCropBatchByBatchId(Long batchId)
    {
        return cropBatchMapper.selectCropBatchByBatchId(batchId);
    }

    /**
     * 查询作物批次列表
     * 
     * @param cropBatch 作物批次
     * @return 作物批次
     */
    @Override
    public List<CropBatch> selectCropBatchList(CropBatch cropBatch)
    {
        return cropBatchMapper.selectCropBatchList(cropBatch);
    }

    /**
     * 新增作物批次
     * 
     * @param cropBatch 作物批次
     * @return 结果
     */
    @Override
    public int insertCropBatch(CropBatch cropBatch)
    {
        cropBatch.setCreateTime(DateUtils.getNowDate());
        cropBatch.setCreateBy(SecurityUtils.getUserId().toString());
        int i =cropBatchMapper.insertCropBatch(cropBatch);
        StandardJob queryPar = new StandardJob();
        queryPar.setGermplasmId(cropBatch.getGermplasmId());
        List<StandardJob> sjList = standardJobMapper.selectStandardJobList(queryPar);
        for(StandardJob sj : sjList){
            BatchTask bt = new BatchTask();
            bt.setBatchId(cropBatch.getBatchId());
            bt.setTaskHead(cropBatch.getBatchHead());
            bt.setTaskName(sj.getJobName());

            int mult = sj.getCycleUnit()=="0"?1:7;
            try {
                bt.setPlanStart(DateUtils.plusDay((int)(sj.getJobStart() * mult),cropBatch.getStartTime()));
                bt.setPlanFinish(DateUtils.plusDay((int)(sj.getJobFinish() * mult),cropBatch.getStartTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            batchTaskMapper.insertBatchTask(bt);
            TaskLog tl = new TaskLog();
            tl.setTaskId(bt.getTaskId());
            tl.setOperName(SecurityUtils.getUsername());
            tl.setOperId(SecurityUtils.getUserId());
            tl.setOperDes("创建任务");
            tl.setCreateTime(DateUtils.getNowDate());
            taskLogMapper.insertTaskLog(tl);
        }
        return i;

    }

    /**
     * 修改作物批次
     * 
     * @param cropBatch 作物批次
     * @return 结果
     */
    @Override
    public int updateCropBatch(CropBatch cropBatch)
    {
        cropBatch.setUpdateTime(DateUtils.getNowDate());
        cropBatch.setUpdateBy(SecurityUtils.getUserId().toString());
        return cropBatchMapper.updateCropBatch(cropBatch);
    }

    /**
     * 批量删除作物批次
     * 
     * @param batchIds 需要删除的作物批次主键
     * @return 结果
     */
    @Override
    public int deleteCropBatchByBatchIds(Long[] batchIds)
    {
        return cropBatchMapper.deleteCropBatchByBatchIds(batchIds);
    }

    /**
     * 删除作物批次信息
     * 
     * @param batchId 作物批次主键
     * @return 结果
     */
    @Override
    public int deleteCropBatchByBatchId(Long batchId)
    {
        return cropBatchMapper.deleteCropBatchByBatchId(batchId);
    }
}

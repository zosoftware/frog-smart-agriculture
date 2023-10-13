package com.frog.agriculture.service;

import java.util.HashMap;
import java.util.List;

public interface IDataStatisticsService {
    public List<HashMap> selectBaseInfo();
    public List<HashMap> selectDeviceInfo();
    public List<HashMap> selectDeviceJobInfo();
    public List<HashMap> selectTraceInfo();
    public List<HashMap> selectTaskInfo();
    public List<HashMap> selectAreaInfo();
    public HashMap traceStatistics(String traceCode);
    public List<HashMap> selectRecordGroupByCity();
    public List<HashMap> selectRecordStatistics();
    public List<HashMap> selectRecordGroupBySellpro();

}

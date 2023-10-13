package com.frog.agriculture.mapper;

import java.util.HashMap;
import java.util.List;

public interface DataStatisticsMapper {

    public List<HashMap> selectBaseInfo();
    public List<HashMap> selectDeviceInfo();
    public List<HashMap> selectDeviceJobInfo();
    public List<HashMap> selectTraceInfo();
    public List<HashMap> selectTaskInfo();
    public List<HashMap> selectAreaInfo();
    public List<HashMap> selectRecordGroupByCity();
    public List<HashMap> selectRecordStatistics();
    public List<HashMap> selectRecordGroupBySellpro();

}

package com.frog.iot.model.ThingsModels;

import com.frog.iot.model.ThingsModelItem.DataType;

/**
 * 物模型值的项
 *
 * @author kerwincui
 * @date 2021-12-16
 */
public class ThingsModelValueItem {
    /**
     * 物模型唯一标识符
     */
    private String id;

    /**
     * 物模型值
     */
    private String value;

    /**
     * 影子值
     **/
    private String shadow;

    /**
     * 是否为监测值
     **/
    private int isMonitor;

    /**
     * 物模型名称
     **/
    private String name;

    private DataType dataType;

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(int isMonitor) {
        this.isMonitor = isMonitor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShadow() {
        return shadow;
    }

    public void setShadow(String shadow) {
        this.shadow = shadow;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}

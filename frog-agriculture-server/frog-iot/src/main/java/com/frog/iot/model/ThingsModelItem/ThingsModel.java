package com.frog.iot.model.ThingsModelItem;

public class ThingsModel
{
    /** 物模型唯一标识符 */
    private String id;
    /** 物模型名称 */
    private String name;
    /** 物模型值 */
    private String value;
    /** 影子值 */
    private String shadow;
    /** 是否首页显示（0-否，1-是） */
    private Integer isTop;
    /** 是否实时监测（0-否，1-是） */
    private Integer isMonitor;
    /** 是否实时监测（0-否，1-是） */
    private Integer isReadonly;
    /** 类型 1=属性，2=功能，3=事件 */
    private Integer type;
    /** 排序 */
    private Integer order;
    /** 数据类型 */
    private DataType dataType;

    public ThingsModel(){
        value="";
        shadow="";
        order=0;
        isMonitor=0;
        isTop=0;
        isReadonly=0;
    }

    public Integer getIsReadonly() {
        return isReadonly;
    }

    public void setIsReadonly(Integer isReadonly) {
        this.isReadonly = isReadonly;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getShadow() {
        return shadow;
    }

    public void setShadow(String shadow) {
        this.shadow = shadow;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(Integer isMonitor) {
        this.isMonitor = isMonitor;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}

package com.frog.iot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frog.common.annotation.Excel;
import com.frog.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 监控设备通道信息对象 sip_device_channel
 *
 * @author zhuangpeng.li
 * @date 2023-02-23
 */
public class SipDeviceChannel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    private Long tenantId;

    /**
     * 租户名称
     */
    @Excel(name = "租户名称")
    private String tenantName;

    /**
     * 产品ID
     */
    @Excel(name = "产品ID")
    private Long productId;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String productName;

    /**
     * 产品ID
     */
    @Excel(name = "产品ID")
    private Long userId;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String userName;

    /**
     * 设备SipID
     */
    private String deviceSipId;

    /**
     * 通道SipID
     */
    @Excel(name = "通道SipID")
    private String channelSipId;

    /**
     * 通道名称
     */
    @Excel(name = "通道名称")
    private String channelName;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date registerTime;

    /**
     * 设备类型
     */
    @Excel(name = "设备类型")
    private String deviceType;

    /**
     * 通道类型
     */
    @Excel(name = "通道类型")
    private String channelType;

    /**
     * 城市编码
     */
    @Excel(name = "城市编码")
    private String citycode;

    /**
     * 行政区域
     */
    @Excel(name = "行政区域")
    private String civilcode;

    /**
     * 厂商名称
     */
    @Excel(name = "厂商名称")
    private String manufacture;

    /**
     * 产品型号
     */
    @Excel(name = "产品型号")
    private String model;

    /**
     * 设备归属
     */
    @Excel(name = "设备归属")
    private String owner;

    /**
     * 警区
     */
    @Excel(name = "警区")
    private String block;

    /**
     * 安装地址
     */
    @Excel(name = "安装地址")
    private String address;

    /** 父级id */
    @Excel(name = "父级id")
    private String parentid;

    /** 设备入网IP */
    @Excel(name = "设备入网IP")
    private String ipaddress;

    /** 设备接入端口号 */
    @Excel(name = "设备接入端口号")
    private Integer port;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** PTZ类型 */
    @Excel(name = "PTZ类型")
    private Long ptztype;

    /** PTZ类型描述字符串 */
    @Excel(name = "PTZ类型描述字符串")
    private String ptztypetext;

    /**
     * 设备状态（1-未激活，2-禁用，3-在线，4-离线）
     */
    @Excel(name = "设备状态", readConverterExp = "1-未激活，2-在线，3-在线，4-离线")
    private Integer sipStatus;

    /**
     * 设备经度
     */
    @Excel(name = "设备经度")
    private BigDecimal longitude;

    /**
     * 设备纬度
     */
    @Excel(name = "设备纬度")
    private BigDecimal latitude;

    /**
     * 流媒体ID
     */
    @Excel(name = "流媒体ID")
    private String streamid;

    /**
     * 子设备数
     */
    @Excel(name = "子设备数")
    private Long subcount;

    /**
     * 是否有子设备（1-有, 0-没有）
     */
    @Excel(name = "是否有子设备", readConverterExp = "1=-有,,0=-没有")
    private Integer parental;

    /**
     * 是否含有音频（1-有, 0-没有）
     */
    @Excel(name = "是否含有音频", readConverterExp = "1=-有,,0=-没有")
    private Integer hasaudio;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setDeviceSipId(String deviceSipId) {
        this.deviceSipId = deviceSipId;
    }

    public String getDeviceSipId() {
        return deviceSipId;
    }

    public void setChannelSipId(String channelSipId) {
        this.channelSipId = channelSipId;
    }

    public String getChannelSipId() {
        return channelSipId;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCivilcode(String civilcode) {
        this.civilcode = civilcode;
    }

    public String getCivilcode() {
        return civilcode;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getManufacture() {
        return manufacture;
    }
    public void setModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return model;
    }
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return owner;
    }
    public void setBlock(String block)
    {
        this.block = block;
    }

    public String getBlock()
    {
        return block;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setParentid(String parentid)
    {
        this.parentid = parentid;
    }

    public String getParentid()
    {
        return parentid;
    }
    public void setIpaddress(String ipaddress)
    {
        this.ipaddress = ipaddress;
    }

    public String getIpaddress()
    {
        return ipaddress;
    }
    public void setPort(Integer port)
    {
        this.port = port;
    }

    public Integer getPort()
    {
        return port;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPtztype(Long ptztype)
    {
        this.ptztype = ptztype;
    }

    public Long getPtztype()
    {
        return ptztype;
    }
    public void setPtztypetext(String ptztypetext)
    {
        this.ptztypetext = ptztypetext;
    }

    public String getPtztypetext()
    {
        return ptztypetext;
    }
    public void setSipStatus(Integer sipStatus)
    {
        this.sipStatus = sipStatus;
    }

    public Integer getSipStatus()
    {
        return sipStatus;
    }
    public void setLongitude(BigDecimal longitude)
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude()
    {
        return longitude;
    }
    public void setLatitude(BigDecimal latitude)
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude()
    {
        return latitude;
    }
    public void setStreamid(String streamid) {
        this.streamid = streamid;
    }

    public String getStreamid() {
        return streamid;
    }

    public void setSubcount(Long subcount) {
        this.subcount = subcount;
    }

    public Long getSubcount() {
        return subcount;
    }

    public void setParental(Integer parental) {
        this.parental = parental;
    }

    public Integer getParental() {
        return parental;
    }

    public void setHasaudio(Integer hasaudio) {
        this.hasaudio = hasaudio;
    }

    public Integer getHasaudio() {
        return hasaudio;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("tenantName", getTenantName())
                .append("productId", getProductId())
                .append("productName", getProductName())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("deviceSipId", getDeviceSipId())
                .append("channelSipId", getChannelSipId())
                .append("channelName", getChannelName())
                .append("registerTime", getRegisterTime())
                .append("deviceType", getDeviceType())
                .append("channelType", getChannelType())
                .append("citycode", getCitycode())
                .append("civilcode", getCivilcode())
                .append("manufacture", getManufacture())
                .append("model", getModel())
                .append("owner", getOwner())
                .append("block", getBlock())
                .append("address", getAddress())
                .append("parentid", getParentid())
                .append("ipaddress", getIpaddress())
                .append("port", getPort())
                .append("password", getPassword())
                .append("ptztype", getPtztype())
                .append("ptztypetext", getPtztypetext())
                .append("sipStatus", getSipStatus())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("streamid", getStreamid())
                .append("subcount", getSubcount())
                .append("parental", getParental())
                .append("hasaudio", getHasaudio())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
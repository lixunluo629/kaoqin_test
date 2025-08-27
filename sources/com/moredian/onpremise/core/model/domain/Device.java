package com.moredian.onpremise.core.model.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "on_premise_device")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/domain/Device.class */
public class Device {

    @Id
    @GeneratedValue
    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "device_type")
    private Integer deviceType;

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "net_type")
    private Integer netType;

    @Column(name = "device_sn")
    private String deviceSn;

    @Column(name = "gen_type")
    private Integer genType;

    @Column(name = "opening_time")
    private Integer openingTime;

    @Column(name = "online_status")
    private Integer onlineStatus;

    @Column(name = "device_status")
    private Integer deviceStatus;

    @Column(name = "mac_address")
    private String macAddress;
    private String version;

    @Column(name = "rom_version")
    private String romVersion;

    @Column(name = "device_voice")
    private Integer deviceVoice;

    @Column(name = "identify_distance")
    private Integer identifyDistance;

    @Column(name = "identify_module")
    private Integer identifyModule;

    @Column(name = "threshold_score")
    private Integer thresholdScore;

    @Column(name = "device_ip")
    private String deviceIp;

    @Column(name = "font_size")
    private Integer fontSize;

    @Column(name = "show_time")
    private Integer showTime;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modify")
    private Date gmtModify;

    @Column(name = "delete_or_not")
    private Integer deleteOrNot;

    @Column(name = "fast_mode")
    private Integer fastMode;

    @Column(name = "light")
    private Integer light;

    @Column(name = "mask_mode")
    private Integer maskMode;

    @Column(name = "ring_mode")
    private Integer ringMode;

    @Column(name = "sync_mode")
    private Integer syncMode;

    @Column(name = "direction")
    private Integer direction;

    @Column(name = "focus")
    private Integer focus;

    @Column(name = "maskRemindPicture")
    private Integer maskRemindPicture;

    @Column(name = "maskRemindVoice")
    private Integer maskRemindVoice;

    @Column(name = "device_group_id")
    private Long deviceGroupId;

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getNetType() {
        return this.netType;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getOnlineStatus() {
        return this.onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getDeviceStatus() {
        return this.deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRomVersion() {
        return this.romVersion;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }

    public String getDeviceIp() {
        return this.deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getShowTime() {
        return this.showTime;
    }

    public void setShowTime(Integer showTime) {
        this.showTime = showTime;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return this.gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public Integer getDeviceVoice() {
        return this.deviceVoice;
    }

    public void setDeviceVoice(Integer deviceVoice) {
        this.deviceVoice = deviceVoice;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Integer getGenType() {
        return this.genType;
    }

    public void setGenType(Integer genType) {
        this.genType = genType;
    }

    public Integer getOpeningTime() {
        return this.openingTime;
    }

    public void setOpeningTime(Integer openingTime) {
        this.openingTime = openingTime;
    }

    public Integer getIdentifyDistance() {
        return this.identifyDistance;
    }

    public void setIdentifyDistance(Integer identifyDistance) {
        this.identifyDistance = identifyDistance;
    }

    public Integer getIdentifyModule() {
        return this.identifyModule;
    }

    public void setIdentifyModule(Integer identifyModule) {
        this.identifyModule = identifyModule;
    }

    public Integer getThresholdScore() {
        return this.thresholdScore;
    }

    public void setThresholdScore(Integer thresholdScore) {
        this.thresholdScore = thresholdScore;
    }

    public Integer getFastMode() {
        return this.fastMode;
    }

    public void setFastMode(Integer fastMode) {
        this.fastMode = fastMode;
    }

    public Integer getLight() {
        return this.light;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public Integer getMaskMode() {
        return this.maskMode;
    }

    public void setMaskMode(Integer maskMode) {
        this.maskMode = maskMode;
    }

    public Integer getRingMode() {
        return this.ringMode;
    }

    public void setRingMode(Integer ringMode) {
        this.ringMode = ringMode;
    }

    public Long getDeviceGroupId() {
        return this.deviceGroupId;
    }

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public Integer getSyncMode() {
        return this.syncMode;
    }

    public void setSyncMode(Integer syncMode) {
        this.syncMode = syncMode;
    }

    public Integer getDirection() {
        return this.direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getFocus() {
        return this.focus;
    }

    public void setFocus(Integer focus) {
        this.focus = focus;
    }

    public Integer getMaskRemindPicture() {
        return this.maskRemindPicture;
    }

    public void setMaskRemindPicture(Integer maskRemindPicture) {
        this.maskRemindPicture = maskRemindPicture;
    }

    public Integer getMaskRemindVoice() {
        return this.maskRemindVoice;
    }

    public void setMaskRemindVoice(Integer maskRemindVoice) {
        this.maskRemindVoice = maskRemindVoice;
    }
}

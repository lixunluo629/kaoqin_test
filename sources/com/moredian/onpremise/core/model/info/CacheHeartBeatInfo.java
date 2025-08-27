package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "设备心跳信息缓存")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/CacheHeartBeatInfo.class */
public class CacheHeartBeatInfo implements Serializable {

    @ApiModelProperty(name = "ipAddress", value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(name = "macAddress", value = "mac地址")
    private String macAddress;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "expireTime", value = "过期时间")
    private Long expireTime;

    @ApiModelProperty(name = "softVersion", value = "软件版本")
    private String softVersion;

    @ApiModelProperty(name = "romVersion", value = "rom版本")
    private String romVersion;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }

    public String toString() {
        return "CacheHeartBeatInfo(ipAddress=" + getIpAddress() + ", macAddress=" + getMacAddress() + ", deviceSn=" + getDeviceSn() + ", expireTime=" + getExpireTime() + ", softVersion=" + getSoftVersion() + ", romVersion=" + getRomVersion() + ")";
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public String getSoftVersion() {
        return this.softVersion;
    }

    public String getRomVersion() {
        return this.romVersion;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CacheHeartBeatInfo info = (CacheHeartBeatInfo) o;
        return Objects.equals(this.ipAddress, info.ipAddress) && Objects.equals(this.macAddress, info.macAddress) && Objects.equals(this.deviceSn, info.deviceSn) && Objects.equals(this.softVersion, info.softVersion) && Objects.equals(this.romVersion, info.romVersion);
    }

    public int hashCode() {
        return Objects.hash(this.ipAddress, this.macAddress, this.deviceSn, this.softVersion, this.romVersion);
    }
}

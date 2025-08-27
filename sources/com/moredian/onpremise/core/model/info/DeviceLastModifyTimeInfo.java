package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "最近一次同步时间信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/DeviceLastModifyTimeInfo.class */
public class DeviceLastModifyTimeInfo implements Serializable {

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "expireTime", value = "过期时间")
    private Long expireTime;

    @ApiModelProperty(name = "lastSyncTime", value = "最近修改时间")
    private Long lastModifyTime = 0L;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceLastModifyTimeInfo)) {
            return false;
        }
        DeviceLastModifyTimeInfo other = (DeviceLastModifyTimeInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$expireTime = getExpireTime();
        Object other$expireTime = other.getExpireTime();
        if (this$expireTime == null) {
            if (other$expireTime != null) {
                return false;
            }
        } else if (!this$expireTime.equals(other$expireTime)) {
            return false;
        }
        Object this$lastModifyTime = getLastModifyTime();
        Object other$lastModifyTime = other.getLastModifyTime();
        return this$lastModifyTime == null ? other$lastModifyTime == null : this$lastModifyTime.equals(other$lastModifyTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceLastModifyTimeInfo;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $expireTime = getExpireTime();
        int result2 = (result * 59) + ($expireTime == null ? 43 : $expireTime.hashCode());
        Object $lastModifyTime = getLastModifyTime();
        return (result2 * 59) + ($lastModifyTime == null ? 43 : $lastModifyTime.hashCode());
    }

    public String toString() {
        return "DeviceLastModifyTimeInfo(deviceSn=" + getDeviceSn() + ", expireTime=" + getExpireTime() + ", lastModifyTime=" + getLastModifyTime() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }
}

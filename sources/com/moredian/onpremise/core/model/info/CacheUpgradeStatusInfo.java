package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "缓存设备升级状态")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/CacheUpgradeStatusInfo.class */
public class CacheUpgradeStatusInfo implements Serializable {

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "upgradeStatus", value = "升级状态:1-升级成功；2-升级中；3-升级失败；4-无需升级")
    private Integer upgradeStatus;

    @ApiModelProperty(name = "upgradeType", value = "升级类型:1-固件升级，2-app升级")
    private Integer upgradeType;

    @ApiModelProperty(name = "upgradeScheduleId", value = "升级任务id")
    private Long upgradeScheduleId;

    @ApiModelProperty(name = "expireTime", value = "有效时间")
    private Long expireTime;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }

    public void setUpgradeScheduleId(Long upgradeScheduleId) {
        this.upgradeScheduleId = upgradeScheduleId;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CacheUpgradeStatusInfo)) {
            return false;
        }
        CacheUpgradeStatusInfo other = (CacheUpgradeStatusInfo) o;
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
        Object this$upgradeStatus = getUpgradeStatus();
        Object other$upgradeStatus = other.getUpgradeStatus();
        if (this$upgradeStatus == null) {
            if (other$upgradeStatus != null) {
                return false;
            }
        } else if (!this$upgradeStatus.equals(other$upgradeStatus)) {
            return false;
        }
        Object this$upgradeType = getUpgradeType();
        Object other$upgradeType = other.getUpgradeType();
        if (this$upgradeType == null) {
            if (other$upgradeType != null) {
                return false;
            }
        } else if (!this$upgradeType.equals(other$upgradeType)) {
            return false;
        }
        Object this$upgradeScheduleId = getUpgradeScheduleId();
        Object other$upgradeScheduleId = other.getUpgradeScheduleId();
        if (this$upgradeScheduleId == null) {
            if (other$upgradeScheduleId != null) {
                return false;
            }
        } else if (!this$upgradeScheduleId.equals(other$upgradeScheduleId)) {
            return false;
        }
        Object this$expireTime = getExpireTime();
        Object other$expireTime = other.getExpireTime();
        return this$expireTime == null ? other$expireTime == null : this$expireTime.equals(other$expireTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CacheUpgradeStatusInfo;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $upgradeStatus = getUpgradeStatus();
        int result2 = (result * 59) + ($upgradeStatus == null ? 43 : $upgradeStatus.hashCode());
        Object $upgradeType = getUpgradeType();
        int result3 = (result2 * 59) + ($upgradeType == null ? 43 : $upgradeType.hashCode());
        Object $upgradeScheduleId = getUpgradeScheduleId();
        int result4 = (result3 * 59) + ($upgradeScheduleId == null ? 43 : $upgradeScheduleId.hashCode());
        Object $expireTime = getExpireTime();
        return (result4 * 59) + ($expireTime == null ? 43 : $expireTime.hashCode());
    }

    public String toString() {
        return "CacheUpgradeStatusInfo(deviceSn=" + getDeviceSn() + ", upgradeStatus=" + getUpgradeStatus() + ", upgradeType=" + getUpgradeType() + ", upgradeScheduleId=" + getUpgradeScheduleId() + ", expireTime=" + getExpireTime() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getUpgradeStatus() {
        return this.upgradeStatus;
    }

    public Integer getUpgradeType() {
        return this.upgradeType;
    }

    public Long getUpgradeScheduleId() {
        return this.upgradeScheduleId;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }
}

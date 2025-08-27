package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.UpgradeConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/UploadUpgradePackageResponse.class */
public class UploadUpgradePackageResponse implements Serializable {
    private static final long serialVersionUID = 1825231310159038016L;

    @ApiModelProperty(name = "packagePath", value = "升级包保存路径")
    public String packagePath;

    @ApiModelProperty(name = UpgradeConstants.UPGRADE_PARAM_SCHEDULE_TYPE_KEY, value = "任务类型")
    public String scheduleType = "0";

    @ApiModelProperty(name = UpgradeConstants.UPGRADE_PARAM_VERSION_CODE_KEY, value = "版本号")
    public String versionCode = "";

    @ApiModelProperty(name = UpgradeConstants.UPGRADE_PARAM_RELEASE_TIME_KEY, value = "发布时间")
    public String releaseTime = "";

    @ApiModelProperty(name = UpgradeConstants.UPGRADE_PARAM_SUPPORT_DEVICE_KEY, value = "支持设备")
    public String supportDevice = "";

    @ApiModelProperty(name = UpgradeConstants.UPGRADE_PARAM_RELEASE_NOTE_KEY, value = "发布内容")
    public String releaseNote = "";

    @ApiModelProperty(name = "upgradeDeviceNum", value = "可升级设备数量")
    public Integer upgradeDeviceNum = 0;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    public Integer deviceType = null;

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setSupportDevice(String supportDevice) {
        this.supportDevice = supportDevice;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public void setUpgradeDeviceNum(Integer upgradeDeviceNum) {
        this.upgradeDeviceNum = upgradeDeviceNum;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UploadUpgradePackageResponse)) {
            return false;
        }
        UploadUpgradePackageResponse other = (UploadUpgradePackageResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$scheduleType = getScheduleType();
        Object other$scheduleType = other.getScheduleType();
        if (this$scheduleType == null) {
            if (other$scheduleType != null) {
                return false;
            }
        } else if (!this$scheduleType.equals(other$scheduleType)) {
            return false;
        }
        Object this$versionCode = getVersionCode();
        Object other$versionCode = other.getVersionCode();
        if (this$versionCode == null) {
            if (other$versionCode != null) {
                return false;
            }
        } else if (!this$versionCode.equals(other$versionCode)) {
            return false;
        }
        Object this$releaseTime = getReleaseTime();
        Object other$releaseTime = other.getReleaseTime();
        if (this$releaseTime == null) {
            if (other$releaseTime != null) {
                return false;
            }
        } else if (!this$releaseTime.equals(other$releaseTime)) {
            return false;
        }
        Object this$supportDevice = getSupportDevice();
        Object other$supportDevice = other.getSupportDevice();
        if (this$supportDevice == null) {
            if (other$supportDevice != null) {
                return false;
            }
        } else if (!this$supportDevice.equals(other$supportDevice)) {
            return false;
        }
        Object this$releaseNote = getReleaseNote();
        Object other$releaseNote = other.getReleaseNote();
        if (this$releaseNote == null) {
            if (other$releaseNote != null) {
                return false;
            }
        } else if (!this$releaseNote.equals(other$releaseNote)) {
            return false;
        }
        Object this$packagePath = getPackagePath();
        Object other$packagePath = other.getPackagePath();
        if (this$packagePath == null) {
            if (other$packagePath != null) {
                return false;
            }
        } else if (!this$packagePath.equals(other$packagePath)) {
            return false;
        }
        Object this$upgradeDeviceNum = getUpgradeDeviceNum();
        Object other$upgradeDeviceNum = other.getUpgradeDeviceNum();
        if (this$upgradeDeviceNum == null) {
            if (other$upgradeDeviceNum != null) {
                return false;
            }
        } else if (!this$upgradeDeviceNum.equals(other$upgradeDeviceNum)) {
            return false;
        }
        Object this$deviceType = getDeviceType();
        Object other$deviceType = other.getDeviceType();
        return this$deviceType == null ? other$deviceType == null : this$deviceType.equals(other$deviceType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploadUpgradePackageResponse;
    }

    public int hashCode() {
        Object $scheduleType = getScheduleType();
        int result = (1 * 59) + ($scheduleType == null ? 43 : $scheduleType.hashCode());
        Object $versionCode = getVersionCode();
        int result2 = (result * 59) + ($versionCode == null ? 43 : $versionCode.hashCode());
        Object $releaseTime = getReleaseTime();
        int result3 = (result2 * 59) + ($releaseTime == null ? 43 : $releaseTime.hashCode());
        Object $supportDevice = getSupportDevice();
        int result4 = (result3 * 59) + ($supportDevice == null ? 43 : $supportDevice.hashCode());
        Object $releaseNote = getReleaseNote();
        int result5 = (result4 * 59) + ($releaseNote == null ? 43 : $releaseNote.hashCode());
        Object $packagePath = getPackagePath();
        int result6 = (result5 * 59) + ($packagePath == null ? 43 : $packagePath.hashCode());
        Object $upgradeDeviceNum = getUpgradeDeviceNum();
        int result7 = (result6 * 59) + ($upgradeDeviceNum == null ? 43 : $upgradeDeviceNum.hashCode());
        Object $deviceType = getDeviceType();
        return (result7 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
    }

    public String toString() {
        return "UploadUpgradePackageResponse(scheduleType=" + getScheduleType() + ", versionCode=" + getVersionCode() + ", releaseTime=" + getReleaseTime() + ", supportDevice=" + getSupportDevice() + ", releaseNote=" + getReleaseNote() + ", packagePath=" + getPackagePath() + ", upgradeDeviceNum=" + getUpgradeDeviceNum() + ", deviceType=" + getDeviceType() + ")";
    }

    public String getScheduleType() {
        return this.scheduleType;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public String getReleaseTime() {
        return this.releaseTime;
    }

    public String getSupportDevice() {
        return this.supportDevice;
    }

    public String getReleaseNote() {
        return this.releaseNote;
    }

    public String getPackagePath() {
        return this.packagePath;
    }

    public Integer getUpgradeDeviceNum() {
        return this.upgradeDeviceNum;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }
}

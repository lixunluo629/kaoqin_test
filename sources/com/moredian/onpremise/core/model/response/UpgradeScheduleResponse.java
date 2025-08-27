package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "升级任务列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/UpgradeScheduleResponse.class */
public class UpgradeScheduleResponse implements Serializable {
    private static final long serialVersionUID = 1438983394512071989L;

    @ApiModelProperty(name = "upgradeScheduleId", value = "任务id")
    private Long upgradeScheduleId;

    @ApiModelProperty(name = "upgradeDeviceNum", value = "预计升级设备数量")
    private Integer upgradeDeviceNum;

    @ApiModelProperty(name = "upgradeType", value = "升级类型：1-固件升级，2-app升级")
    private Integer upgradeType;

    @ApiModelProperty(name = "upgradeTime", value = "预计升级时间")
    private Date upgradeTime;

    @ApiModelProperty(name = "upgradeStatus", value = "升级状态：1-等待中，2-升级中，3-已完成，4-已取消")
    private Integer upgradeStatus;

    @ApiModelProperty(name = "upgradeVersion", value = "本次升级包版本")
    private String upgradeVersion;

    @ApiModelProperty(name = "upgradeResult", value = "本次升级结果")
    private String upgradeResult;

    public void setUpgradeScheduleId(Long upgradeScheduleId) {
        this.upgradeScheduleId = upgradeScheduleId;
    }

    public void setUpgradeDeviceNum(Integer upgradeDeviceNum) {
        this.upgradeDeviceNum = upgradeDeviceNum;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }

    public void setUpgradeTime(Date upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public void setUpgradeResult(String upgradeResult) {
        this.upgradeResult = upgradeResult;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpgradeScheduleResponse)) {
            return false;
        }
        UpgradeScheduleResponse other = (UpgradeScheduleResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$upgradeDeviceNum = getUpgradeDeviceNum();
        Object other$upgradeDeviceNum = other.getUpgradeDeviceNum();
        if (this$upgradeDeviceNum == null) {
            if (other$upgradeDeviceNum != null) {
                return false;
            }
        } else if (!this$upgradeDeviceNum.equals(other$upgradeDeviceNum)) {
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
        Object this$upgradeTime = getUpgradeTime();
        Object other$upgradeTime = other.getUpgradeTime();
        if (this$upgradeTime == null) {
            if (other$upgradeTime != null) {
                return false;
            }
        } else if (!this$upgradeTime.equals(other$upgradeTime)) {
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
        Object this$upgradeVersion = getUpgradeVersion();
        Object other$upgradeVersion = other.getUpgradeVersion();
        if (this$upgradeVersion == null) {
            if (other$upgradeVersion != null) {
                return false;
            }
        } else if (!this$upgradeVersion.equals(other$upgradeVersion)) {
            return false;
        }
        Object this$upgradeResult = getUpgradeResult();
        Object other$upgradeResult = other.getUpgradeResult();
        return this$upgradeResult == null ? other$upgradeResult == null : this$upgradeResult.equals(other$upgradeResult);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpgradeScheduleResponse;
    }

    public int hashCode() {
        Object $upgradeScheduleId = getUpgradeScheduleId();
        int result = (1 * 59) + ($upgradeScheduleId == null ? 43 : $upgradeScheduleId.hashCode());
        Object $upgradeDeviceNum = getUpgradeDeviceNum();
        int result2 = (result * 59) + ($upgradeDeviceNum == null ? 43 : $upgradeDeviceNum.hashCode());
        Object $upgradeType = getUpgradeType();
        int result3 = (result2 * 59) + ($upgradeType == null ? 43 : $upgradeType.hashCode());
        Object $upgradeTime = getUpgradeTime();
        int result4 = (result3 * 59) + ($upgradeTime == null ? 43 : $upgradeTime.hashCode());
        Object $upgradeStatus = getUpgradeStatus();
        int result5 = (result4 * 59) + ($upgradeStatus == null ? 43 : $upgradeStatus.hashCode());
        Object $upgradeVersion = getUpgradeVersion();
        int result6 = (result5 * 59) + ($upgradeVersion == null ? 43 : $upgradeVersion.hashCode());
        Object $upgradeResult = getUpgradeResult();
        return (result6 * 59) + ($upgradeResult == null ? 43 : $upgradeResult.hashCode());
    }

    public String toString() {
        return "UpgradeScheduleResponse(upgradeScheduleId=" + getUpgradeScheduleId() + ", upgradeDeviceNum=" + getUpgradeDeviceNum() + ", upgradeType=" + getUpgradeType() + ", upgradeTime=" + getUpgradeTime() + ", upgradeStatus=" + getUpgradeStatus() + ", upgradeVersion=" + getUpgradeVersion() + ", upgradeResult=" + getUpgradeResult() + ")";
    }

    public Long getUpgradeScheduleId() {
        return this.upgradeScheduleId;
    }

    public Integer getUpgradeDeviceNum() {
        return this.upgradeDeviceNum;
    }

    public Integer getUpgradeType() {
        return this.upgradeType;
    }

    public Date getUpgradeTime() {
        return this.upgradeTime;
    }

    public Integer getUpgradeStatus() {
        return this.upgradeStatus;
    }

    public String getUpgradeVersion() {
        return this.upgradeVersion;
    }

    public String getUpgradeResult() {
        return this.upgradeResult;
    }
}

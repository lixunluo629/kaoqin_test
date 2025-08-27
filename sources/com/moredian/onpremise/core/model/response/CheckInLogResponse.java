package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "签到记录信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInLogResponse.class */
public class CheckInLogResponse implements Serializable {

    @ApiModelProperty(name = "id", value = "签到记录id")
    private Long id;

    @ApiModelProperty(name = "memberName", value = "签到人")
    private String memberName;

    @ApiModelProperty(name = "taskName", value = "签到任务")
    private String taskName;

    @ApiModelProperty(name = "statusName", value = "签到状态")
    private String statusName;

    @ApiModelProperty(name = "deviceSn", value = "签到设备")
    private String deviceSn;

    @ApiModelProperty(name = "checkInTimeStr", value = "签到时间")
    private String checkInTimeStr;

    @ApiModelProperty(name = "faceUrl", value = "签到图片")
    private String faceUrl;

    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setCheckInTimeStr(String checkInTimeStr) {
        this.checkInTimeStr = checkInTimeStr;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInLogResponse)) {
            return false;
        }
        CheckInLogResponse other = (CheckInLogResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
            return false;
        }
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
            return false;
        }
        Object this$taskName = getTaskName();
        Object other$taskName = other.getTaskName();
        if (this$taskName == null) {
            if (other$taskName != null) {
                return false;
            }
        } else if (!this$taskName.equals(other$taskName)) {
            return false;
        }
        Object this$statusName = getStatusName();
        Object other$statusName = other.getStatusName();
        if (this$statusName == null) {
            if (other$statusName != null) {
                return false;
            }
        } else if (!this$statusName.equals(other$statusName)) {
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
        Object this$checkInTimeStr = getCheckInTimeStr();
        Object other$checkInTimeStr = other.getCheckInTimeStr();
        if (this$checkInTimeStr == null) {
            if (other$checkInTimeStr != null) {
                return false;
            }
        } else if (!this$checkInTimeStr.equals(other$checkInTimeStr)) {
            return false;
        }
        Object this$faceUrl = getFaceUrl();
        Object other$faceUrl = other.getFaceUrl();
        return this$faceUrl == null ? other$faceUrl == null : this$faceUrl.equals(other$faceUrl);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInLogResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $taskName = getTaskName();
        int result3 = (result2 * 59) + ($taskName == null ? 43 : $taskName.hashCode());
        Object $statusName = getStatusName();
        int result4 = (result3 * 59) + ($statusName == null ? 43 : $statusName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result5 = (result4 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $checkInTimeStr = getCheckInTimeStr();
        int result6 = (result5 * 59) + ($checkInTimeStr == null ? 43 : $checkInTimeStr.hashCode());
        Object $faceUrl = getFaceUrl();
        return (result6 * 59) + ($faceUrl == null ? 43 : $faceUrl.hashCode());
    }

    public String toString() {
        return "CheckInLogResponse(id=" + getId() + ", memberName=" + getMemberName() + ", taskName=" + getTaskName() + ", statusName=" + getStatusName() + ", deviceSn=" + getDeviceSn() + ", checkInTimeStr=" + getCheckInTimeStr() + ", faceUrl=" + getFaceUrl() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getCheckInTimeStr() {
        return this.checkInTimeStr;
    }

    public String getFaceUrl() {
        return this.faceUrl;
    }
}

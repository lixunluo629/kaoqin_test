package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "签到记录列表信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInLogListResponse.class */
public class CheckInLogListResponse implements Serializable {

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "checkInTaskId", value = "签到任务id")
    private Long checkInTaskId;

    @ApiModelProperty(name = "checkInTaskName", value = "签到任务名称")
    private String checkInTaskName;

    @ApiModelProperty(name = "checkInMemberId", value = "签到成员id")
    private Long checkInMemberId;

    @ApiModelProperty(name = "checkInMemberName", value = "签到成员名称")
    private String checkInMemberName;

    @ApiModelProperty(name = "checkInStatus", value = "签到状态")
    private Integer checkInStatus;

    @ApiModelProperty(name = "checkInStatusName", value = "签到状态名称")
    private String checkInStatusName;

    @ApiModelProperty(name = "checkInDeviceId", value = "签到设备id")
    private Long checkInDeviceId;

    @ApiModelProperty(name = "checkInDeviceSn", value = "签到设备sn")
    private String checkInDeviceSn;

    @ApiModelProperty(name = "checkInDeviceName", value = "签到设备名称")
    private String checkInDeviceName;

    @ApiModelProperty(name = "checkInTime", value = "签到时间")
    private Long checkInTime;

    @ApiModelProperty(name = "checkInDate", value = "设置签到日期")
    private String checkInDate;

    @ApiModelProperty(name = "checkInSnapFaceUrl", value = "签到抓拍头像")
    private String checkInSnapFaceUrl;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setCheckInTaskId(Long checkInTaskId) {
        this.checkInTaskId = checkInTaskId;
    }

    public void setCheckInTaskName(String checkInTaskName) {
        this.checkInTaskName = checkInTaskName;
    }

    public void setCheckInMemberId(Long checkInMemberId) {
        this.checkInMemberId = checkInMemberId;
    }

    public void setCheckInMemberName(String checkInMemberName) {
        this.checkInMemberName = checkInMemberName;
    }

    public void setCheckInStatus(Integer checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public void setCheckInStatusName(String checkInStatusName) {
        this.checkInStatusName = checkInStatusName;
    }

    public void setCheckInDeviceId(Long checkInDeviceId) {
        this.checkInDeviceId = checkInDeviceId;
    }

    public void setCheckInDeviceSn(String checkInDeviceSn) {
        this.checkInDeviceSn = checkInDeviceSn;
    }

    public void setCheckInDeviceName(String checkInDeviceName) {
        this.checkInDeviceName = checkInDeviceName;
    }

    public void setCheckInTime(Long checkInTime) {
        this.checkInTime = checkInTime;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckInSnapFaceUrl(String checkInSnapFaceUrl) {
        this.checkInSnapFaceUrl = checkInSnapFaceUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInLogListResponse)) {
            return false;
        }
        CheckInLogListResponse other = (CheckInLogListResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        Object this$checkInTaskId = getCheckInTaskId();
        Object other$checkInTaskId = other.getCheckInTaskId();
        if (this$checkInTaskId == null) {
            if (other$checkInTaskId != null) {
                return false;
            }
        } else if (!this$checkInTaskId.equals(other$checkInTaskId)) {
            return false;
        }
        Object this$checkInTaskName = getCheckInTaskName();
        Object other$checkInTaskName = other.getCheckInTaskName();
        if (this$checkInTaskName == null) {
            if (other$checkInTaskName != null) {
                return false;
            }
        } else if (!this$checkInTaskName.equals(other$checkInTaskName)) {
            return false;
        }
        Object this$checkInMemberId = getCheckInMemberId();
        Object other$checkInMemberId = other.getCheckInMemberId();
        if (this$checkInMemberId == null) {
            if (other$checkInMemberId != null) {
                return false;
            }
        } else if (!this$checkInMemberId.equals(other$checkInMemberId)) {
            return false;
        }
        Object this$checkInMemberName = getCheckInMemberName();
        Object other$checkInMemberName = other.getCheckInMemberName();
        if (this$checkInMemberName == null) {
            if (other$checkInMemberName != null) {
                return false;
            }
        } else if (!this$checkInMemberName.equals(other$checkInMemberName)) {
            return false;
        }
        Object this$checkInStatus = getCheckInStatus();
        Object other$checkInStatus = other.getCheckInStatus();
        if (this$checkInStatus == null) {
            if (other$checkInStatus != null) {
                return false;
            }
        } else if (!this$checkInStatus.equals(other$checkInStatus)) {
            return false;
        }
        Object this$checkInStatusName = getCheckInStatusName();
        Object other$checkInStatusName = other.getCheckInStatusName();
        if (this$checkInStatusName == null) {
            if (other$checkInStatusName != null) {
                return false;
            }
        } else if (!this$checkInStatusName.equals(other$checkInStatusName)) {
            return false;
        }
        Object this$checkInDeviceId = getCheckInDeviceId();
        Object other$checkInDeviceId = other.getCheckInDeviceId();
        if (this$checkInDeviceId == null) {
            if (other$checkInDeviceId != null) {
                return false;
            }
        } else if (!this$checkInDeviceId.equals(other$checkInDeviceId)) {
            return false;
        }
        Object this$checkInDeviceSn = getCheckInDeviceSn();
        Object other$checkInDeviceSn = other.getCheckInDeviceSn();
        if (this$checkInDeviceSn == null) {
            if (other$checkInDeviceSn != null) {
                return false;
            }
        } else if (!this$checkInDeviceSn.equals(other$checkInDeviceSn)) {
            return false;
        }
        Object this$checkInDeviceName = getCheckInDeviceName();
        Object other$checkInDeviceName = other.getCheckInDeviceName();
        if (this$checkInDeviceName == null) {
            if (other$checkInDeviceName != null) {
                return false;
            }
        } else if (!this$checkInDeviceName.equals(other$checkInDeviceName)) {
            return false;
        }
        Object this$checkInTime = getCheckInTime();
        Object other$checkInTime = other.getCheckInTime();
        if (this$checkInTime == null) {
            if (other$checkInTime != null) {
                return false;
            }
        } else if (!this$checkInTime.equals(other$checkInTime)) {
            return false;
        }
        Object this$checkInDate = getCheckInDate();
        Object other$checkInDate = other.getCheckInDate();
        if (this$checkInDate == null) {
            if (other$checkInDate != null) {
                return false;
            }
        } else if (!this$checkInDate.equals(other$checkInDate)) {
            return false;
        }
        Object this$checkInSnapFaceUrl = getCheckInSnapFaceUrl();
        Object other$checkInSnapFaceUrl = other.getCheckInSnapFaceUrl();
        return this$checkInSnapFaceUrl == null ? other$checkInSnapFaceUrl == null : this$checkInSnapFaceUrl.equals(other$checkInSnapFaceUrl);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInLogListResponse;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $checkInTaskId = getCheckInTaskId();
        int result2 = (result * 59) + ($checkInTaskId == null ? 43 : $checkInTaskId.hashCode());
        Object $checkInTaskName = getCheckInTaskName();
        int result3 = (result2 * 59) + ($checkInTaskName == null ? 43 : $checkInTaskName.hashCode());
        Object $checkInMemberId = getCheckInMemberId();
        int result4 = (result3 * 59) + ($checkInMemberId == null ? 43 : $checkInMemberId.hashCode());
        Object $checkInMemberName = getCheckInMemberName();
        int result5 = (result4 * 59) + ($checkInMemberName == null ? 43 : $checkInMemberName.hashCode());
        Object $checkInStatus = getCheckInStatus();
        int result6 = (result5 * 59) + ($checkInStatus == null ? 43 : $checkInStatus.hashCode());
        Object $checkInStatusName = getCheckInStatusName();
        int result7 = (result6 * 59) + ($checkInStatusName == null ? 43 : $checkInStatusName.hashCode());
        Object $checkInDeviceId = getCheckInDeviceId();
        int result8 = (result7 * 59) + ($checkInDeviceId == null ? 43 : $checkInDeviceId.hashCode());
        Object $checkInDeviceSn = getCheckInDeviceSn();
        int result9 = (result8 * 59) + ($checkInDeviceSn == null ? 43 : $checkInDeviceSn.hashCode());
        Object $checkInDeviceName = getCheckInDeviceName();
        int result10 = (result9 * 59) + ($checkInDeviceName == null ? 43 : $checkInDeviceName.hashCode());
        Object $checkInTime = getCheckInTime();
        int result11 = (result10 * 59) + ($checkInTime == null ? 43 : $checkInTime.hashCode());
        Object $checkInDate = getCheckInDate();
        int result12 = (result11 * 59) + ($checkInDate == null ? 43 : $checkInDate.hashCode());
        Object $checkInSnapFaceUrl = getCheckInSnapFaceUrl();
        return (result12 * 59) + ($checkInSnapFaceUrl == null ? 43 : $checkInSnapFaceUrl.hashCode());
    }

    public String toString() {
        return "CheckInLogListResponse(orgId=" + getOrgId() + ", checkInTaskId=" + getCheckInTaskId() + ", checkInTaskName=" + getCheckInTaskName() + ", checkInMemberId=" + getCheckInMemberId() + ", checkInMemberName=" + getCheckInMemberName() + ", checkInStatus=" + getCheckInStatus() + ", checkInStatusName=" + getCheckInStatusName() + ", checkInDeviceId=" + getCheckInDeviceId() + ", checkInDeviceSn=" + getCheckInDeviceSn() + ", checkInDeviceName=" + getCheckInDeviceName() + ", checkInTime=" + getCheckInTime() + ", checkInDate=" + getCheckInDate() + ", checkInSnapFaceUrl=" + getCheckInSnapFaceUrl() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Long getCheckInTaskId() {
        return this.checkInTaskId;
    }

    public String getCheckInTaskName() {
        return this.checkInTaskName;
    }

    public Long getCheckInMemberId() {
        return this.checkInMemberId;
    }

    public String getCheckInMemberName() {
        return this.checkInMemberName;
    }

    public Integer getCheckInStatus() {
        return this.checkInStatus;
    }

    public String getCheckInStatusName() {
        return this.checkInStatusName;
    }

    public Long getCheckInDeviceId() {
        return this.checkInDeviceId;
    }

    public String getCheckInDeviceSn() {
        return this.checkInDeviceSn;
    }

    public String getCheckInDeviceName() {
        return this.checkInDeviceName;
    }

    public Long getCheckInTime() {
        return this.checkInTime;
    }

    public String getCheckInDate() {
        return this.checkInDate;
    }

    public String getCheckInSnapFaceUrl() {
        return this.checkInSnapFaceUrl;
    }
}

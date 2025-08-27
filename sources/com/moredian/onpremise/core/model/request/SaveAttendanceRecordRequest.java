package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存考勤记录")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveAttendanceRecordRequest.class */
public class SaveAttendanceRecordRequest implements Serializable {
    private static final long serialVersionUID = 7583016752934220211L;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private String deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "attendancePictureUrl", value = "考勤抓拍照地址")
    private String attendancePictureUrl;

    @ApiModelProperty(name = "attendanceTime", value = "实际打卡时间")
    private Long attendanceTime;
    private Long deviceId;

    @ApiModelProperty(name = "deviceSn", value = "设备Sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setAttendancePictureUrl(String attendancePictureUrl) {
        this.attendancePictureUrl = attendancePictureUrl;
    }

    public void setAttendanceTime(Long attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAttendanceRecordRequest)) {
            return false;
        }
        SaveAttendanceRecordRequest other = (SaveAttendanceRecordRequest) o;
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
            return false;
        }
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        if (this$attendanceGroupId == null) {
            if (other$attendanceGroupId != null) {
                return false;
            }
        } else if (!this$attendanceGroupId.equals(other$attendanceGroupId)) {
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
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
            return false;
        }
        Object this$deptName = getDeptName();
        Object other$deptName = other.getDeptName();
        if (this$deptName == null) {
            if (other$deptName != null) {
                return false;
            }
        } else if (!this$deptName.equals(other$deptName)) {
            return false;
        }
        Object this$attendancePictureUrl = getAttendancePictureUrl();
        Object other$attendancePictureUrl = other.getAttendancePictureUrl();
        if (this$attendancePictureUrl == null) {
            if (other$attendancePictureUrl != null) {
                return false;
            }
        } else if (!this$attendancePictureUrl.equals(other$attendancePictureUrl)) {
            return false;
        }
        Object this$attendanceTime = getAttendanceTime();
        Object other$attendanceTime = other.getAttendanceTime();
        if (this$attendanceTime == null) {
            if (other$attendanceTime != null) {
                return false;
            }
        } else if (!this$attendanceTime.equals(other$attendanceTime)) {
            return false;
        }
        Object this$deviceId = getDeviceId();
        Object other$deviceId = other.getDeviceId();
        if (this$deviceId == null) {
            if (other$deviceId != null) {
                return false;
            }
        } else if (!this$deviceId.equals(other$deviceId)) {
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
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        return this$deviceName == null ? other$deviceName == null : this$deviceName.equals(other$deviceName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveAttendanceRecordRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $memberId = getMemberId();
        int result2 = (result * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result3 = (result2 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $memberName = getMemberName();
        int result4 = (result3 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result5 = (result4 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $deptId = getDeptId();
        int result6 = (result5 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result7 = (result6 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $attendancePictureUrl = getAttendancePictureUrl();
        int result8 = (result7 * 59) + ($attendancePictureUrl == null ? 43 : $attendancePictureUrl.hashCode());
        Object $attendanceTime = getAttendanceTime();
        int result9 = (result8 * 59) + ($attendanceTime == null ? 43 : $attendanceTime.hashCode());
        Object $deviceId = getDeviceId();
        int result10 = (result9 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result11 = (result10 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        return (result11 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
    }

    public String toString() {
        return "SaveAttendanceRecordRequest(orgId=" + getOrgId() + ", memberId=" + getMemberId() + ", attendanceGroupId=" + getAttendanceGroupId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", attendancePictureUrl=" + getAttendancePictureUrl() + ", attendanceTime=" + getAttendanceTime() + ", deviceId=" + getDeviceId() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getAttendancePictureUrl() {
        return this.attendancePictureUrl;
    }

    public Long getAttendanceTime() {
        return this.attendanceTime;
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}

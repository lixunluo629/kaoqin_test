package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤记录列表信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListAttendanceRecordResponse.class */
public class ListAttendanceRecordResponse implements Serializable {
    private static final long serialVersionUID = 5077259651081654949L;

    @ApiModelProperty(name = "attendanceRecordId", value = "考勤记录id")
    private Long attendanceRecordId;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "deviceSn", value = "设备Sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "recordType", value = "打卡类型：1-上班，2-下班")
    private Integer recordType;

    @ApiModelProperty(name = "attendancePictureUrl", value = "考勤抓拍照")
    private String attendancePictureUrl;

    @ApiModelProperty(name = "attendanceDay", value = "实际打卡日")
    private Integer attendanceDay;

    @ApiModelProperty(name = "attendanceTime", value = "实际打卡时间")
    private Long attendanceTime;

    @ApiModelProperty(name = "workTime", value = "工作时长，单位秒")
    private Long workTime;

    @ApiModelProperty(name = "ruleTime", value = "规定考勤时间")
    private Long ruleTime;

    @ApiModelProperty(name = "attendanceResult", value = "打卡结果：1-打卡成功，2-迟到，3-早退，4-缺卡")
    private Integer attendanceResult;

    public void setAttendanceRecordId(Long attendanceRecordId) {
        this.attendanceRecordId = attendanceRecordId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public void setAttendancePictureUrl(String attendancePictureUrl) {
        this.attendancePictureUrl = attendancePictureUrl;
    }

    public void setAttendanceDay(Integer attendanceDay) {
        this.attendanceDay = attendanceDay;
    }

    public void setAttendanceTime(Long attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public void setRuleTime(Long ruleTime) {
        this.ruleTime = ruleTime;
    }

    public void setAttendanceResult(Integer attendanceResult) {
        this.attendanceResult = attendanceResult;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListAttendanceRecordResponse)) {
            return false;
        }
        ListAttendanceRecordResponse other = (ListAttendanceRecordResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceRecordId = getAttendanceRecordId();
        Object other$attendanceRecordId = other.getAttendanceRecordId();
        if (this$attendanceRecordId == null) {
            if (other$attendanceRecordId != null) {
                return false;
            }
        } else if (!this$attendanceRecordId.equals(other$attendanceRecordId)) {
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
        Object this$deptName = getDeptName();
        Object other$deptName = other.getDeptName();
        if (this$deptName == null) {
            if (other$deptName != null) {
                return false;
            }
        } else if (!this$deptName.equals(other$deptName)) {
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
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
            return false;
        }
        Object this$recordType = getRecordType();
        Object other$recordType = other.getRecordType();
        if (this$recordType == null) {
            if (other$recordType != null) {
                return false;
            }
        } else if (!this$recordType.equals(other$recordType)) {
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
        Object this$attendanceDay = getAttendanceDay();
        Object other$attendanceDay = other.getAttendanceDay();
        if (this$attendanceDay == null) {
            if (other$attendanceDay != null) {
                return false;
            }
        } else if (!this$attendanceDay.equals(other$attendanceDay)) {
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
        Object this$workTime = getWorkTime();
        Object other$workTime = other.getWorkTime();
        if (this$workTime == null) {
            if (other$workTime != null) {
                return false;
            }
        } else if (!this$workTime.equals(other$workTime)) {
            return false;
        }
        Object this$ruleTime = getRuleTime();
        Object other$ruleTime = other.getRuleTime();
        if (this$ruleTime == null) {
            if (other$ruleTime != null) {
                return false;
            }
        } else if (!this$ruleTime.equals(other$ruleTime)) {
            return false;
        }
        Object this$attendanceResult = getAttendanceResult();
        Object other$attendanceResult = other.getAttendanceResult();
        return this$attendanceResult == null ? other$attendanceResult == null : this$attendanceResult.equals(other$attendanceResult);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListAttendanceRecordResponse;
    }

    public int hashCode() {
        Object $attendanceRecordId = getAttendanceRecordId();
        int result = (1 * 59) + ($attendanceRecordId == null ? 43 : $attendanceRecordId.hashCode());
        Object $memberId = getMemberId();
        int result2 = (result * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result3 = (result2 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result4 = (result3 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $deptName = getDeptName();
        int result5 = (result4 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result6 = (result5 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result7 = (result6 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $recordType = getRecordType();
        int result8 = (result7 * 59) + ($recordType == null ? 43 : $recordType.hashCode());
        Object $attendancePictureUrl = getAttendancePictureUrl();
        int result9 = (result8 * 59) + ($attendancePictureUrl == null ? 43 : $attendancePictureUrl.hashCode());
        Object $attendanceDay = getAttendanceDay();
        int result10 = (result9 * 59) + ($attendanceDay == null ? 43 : $attendanceDay.hashCode());
        Object $attendanceTime = getAttendanceTime();
        int result11 = (result10 * 59) + ($attendanceTime == null ? 43 : $attendanceTime.hashCode());
        Object $workTime = getWorkTime();
        int result12 = (result11 * 59) + ($workTime == null ? 43 : $workTime.hashCode());
        Object $ruleTime = getRuleTime();
        int result13 = (result12 * 59) + ($ruleTime == null ? 43 : $ruleTime.hashCode());
        Object $attendanceResult = getAttendanceResult();
        return (result13 * 59) + ($attendanceResult == null ? 43 : $attendanceResult.hashCode());
    }

    public String toString() {
        return "ListAttendanceRecordResponse(attendanceRecordId=" + getAttendanceRecordId() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", deptName=" + getDeptName() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", recordType=" + getRecordType() + ", attendancePictureUrl=" + getAttendancePictureUrl() + ", attendanceDay=" + getAttendanceDay() + ", attendanceTime=" + getAttendanceTime() + ", workTime=" + getWorkTime() + ", ruleTime=" + getRuleTime() + ", attendanceResult=" + getAttendanceResult() + ")";
    }

    public Long getAttendanceRecordId() {
        return this.attendanceRecordId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public String getAttendancePictureUrl() {
        return this.attendancePictureUrl;
    }

    public Integer getAttendanceDay() {
        return this.attendanceDay;
    }

    public Long getAttendanceTime() {
        return this.attendanceTime;
    }

    public Long getWorkTime() {
        return this.workTime;
    }

    public Long getRuleTime() {
        return this.ruleTime;
    }

    public Integer getAttendanceResult() {
        return this.attendanceResult;
    }
}

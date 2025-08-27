package com.moredian.onpremise.model;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeviceHeartBeatResponse.class */
public class DeviceHeartBeatResponse extends NettyBaseResponse {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_HEART_BEAT_RESPONSE;
    private Long groupLastModifyTime;
    private Long memberLastModifyTime;
    private Long checkInLastModifyTime;
    private Long checkInTaskMemberLastModifyTime;
    private Long attendanceGroupLastModifyTime;
    private Long attendanceHolidayLastModifyTime;
    private Long canteenLastModifyTime;
    private Long canteenMemberLastModifyTime;
    private Long visitConfigLastModifyTime;
    private Long externalContactLastModifyTime;
    private Long accountLastModifyTime;
    private Long deviceConfigLastModifyTime;
    private Long systemCurrentTime;
    private String timeZone;

    public void setGroupLastModifyTime(Long groupLastModifyTime) {
        this.groupLastModifyTime = groupLastModifyTime;
    }

    public void setMemberLastModifyTime(Long memberLastModifyTime) {
        this.memberLastModifyTime = memberLastModifyTime;
    }

    public void setCheckInLastModifyTime(Long checkInLastModifyTime) {
        this.checkInLastModifyTime = checkInLastModifyTime;
    }

    public void setCheckInTaskMemberLastModifyTime(Long checkInTaskMemberLastModifyTime) {
        this.checkInTaskMemberLastModifyTime = checkInTaskMemberLastModifyTime;
    }

    public void setAttendanceGroupLastModifyTime(Long attendanceGroupLastModifyTime) {
        this.attendanceGroupLastModifyTime = attendanceGroupLastModifyTime;
    }

    public void setAttendanceHolidayLastModifyTime(Long attendanceHolidayLastModifyTime) {
        this.attendanceHolidayLastModifyTime = attendanceHolidayLastModifyTime;
    }

    public void setCanteenLastModifyTime(Long canteenLastModifyTime) {
        this.canteenLastModifyTime = canteenLastModifyTime;
    }

    public void setCanteenMemberLastModifyTime(Long canteenMemberLastModifyTime) {
        this.canteenMemberLastModifyTime = canteenMemberLastModifyTime;
    }

    public void setVisitConfigLastModifyTime(Long visitConfigLastModifyTime) {
        this.visitConfigLastModifyTime = visitConfigLastModifyTime;
    }

    public void setExternalContactLastModifyTime(Long externalContactLastModifyTime) {
        this.externalContactLastModifyTime = externalContactLastModifyTime;
    }

    public void setAccountLastModifyTime(Long accountLastModifyTime) {
        this.accountLastModifyTime = accountLastModifyTime;
    }

    public void setDeviceConfigLastModifyTime(Long deviceConfigLastModifyTime) {
        this.deviceConfigLastModifyTime = deviceConfigLastModifyTime;
    }

    public void setSystemCurrentTime(Long systemCurrentTime) {
        this.systemCurrentTime = systemCurrentTime;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceHeartBeatResponse)) {
            return false;
        }
        DeviceHeartBeatResponse other = (DeviceHeartBeatResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupLastModifyTime = getGroupLastModifyTime();
        Object other$groupLastModifyTime = other.getGroupLastModifyTime();
        if (this$groupLastModifyTime == null) {
            if (other$groupLastModifyTime != null) {
                return false;
            }
        } else if (!this$groupLastModifyTime.equals(other$groupLastModifyTime)) {
            return false;
        }
        Object this$memberLastModifyTime = getMemberLastModifyTime();
        Object other$memberLastModifyTime = other.getMemberLastModifyTime();
        if (this$memberLastModifyTime == null) {
            if (other$memberLastModifyTime != null) {
                return false;
            }
        } else if (!this$memberLastModifyTime.equals(other$memberLastModifyTime)) {
            return false;
        }
        Object this$checkInLastModifyTime = getCheckInLastModifyTime();
        Object other$checkInLastModifyTime = other.getCheckInLastModifyTime();
        if (this$checkInLastModifyTime == null) {
            if (other$checkInLastModifyTime != null) {
                return false;
            }
        } else if (!this$checkInLastModifyTime.equals(other$checkInLastModifyTime)) {
            return false;
        }
        Object this$checkInTaskMemberLastModifyTime = getCheckInTaskMemberLastModifyTime();
        Object other$checkInTaskMemberLastModifyTime = other.getCheckInTaskMemberLastModifyTime();
        if (this$checkInTaskMemberLastModifyTime == null) {
            if (other$checkInTaskMemberLastModifyTime != null) {
                return false;
            }
        } else if (!this$checkInTaskMemberLastModifyTime.equals(other$checkInTaskMemberLastModifyTime)) {
            return false;
        }
        Object this$attendanceGroupLastModifyTime = getAttendanceGroupLastModifyTime();
        Object other$attendanceGroupLastModifyTime = other.getAttendanceGroupLastModifyTime();
        if (this$attendanceGroupLastModifyTime == null) {
            if (other$attendanceGroupLastModifyTime != null) {
                return false;
            }
        } else if (!this$attendanceGroupLastModifyTime.equals(other$attendanceGroupLastModifyTime)) {
            return false;
        }
        Object this$attendanceHolidayLastModifyTime = getAttendanceHolidayLastModifyTime();
        Object other$attendanceHolidayLastModifyTime = other.getAttendanceHolidayLastModifyTime();
        if (this$attendanceHolidayLastModifyTime == null) {
            if (other$attendanceHolidayLastModifyTime != null) {
                return false;
            }
        } else if (!this$attendanceHolidayLastModifyTime.equals(other$attendanceHolidayLastModifyTime)) {
            return false;
        }
        Object this$canteenLastModifyTime = getCanteenLastModifyTime();
        Object other$canteenLastModifyTime = other.getCanteenLastModifyTime();
        if (this$canteenLastModifyTime == null) {
            if (other$canteenLastModifyTime != null) {
                return false;
            }
        } else if (!this$canteenLastModifyTime.equals(other$canteenLastModifyTime)) {
            return false;
        }
        Object this$canteenMemberLastModifyTime = getCanteenMemberLastModifyTime();
        Object other$canteenMemberLastModifyTime = other.getCanteenMemberLastModifyTime();
        if (this$canteenMemberLastModifyTime == null) {
            if (other$canteenMemberLastModifyTime != null) {
                return false;
            }
        } else if (!this$canteenMemberLastModifyTime.equals(other$canteenMemberLastModifyTime)) {
            return false;
        }
        Object this$visitConfigLastModifyTime = getVisitConfigLastModifyTime();
        Object other$visitConfigLastModifyTime = other.getVisitConfigLastModifyTime();
        if (this$visitConfigLastModifyTime == null) {
            if (other$visitConfigLastModifyTime != null) {
                return false;
            }
        } else if (!this$visitConfigLastModifyTime.equals(other$visitConfigLastModifyTime)) {
            return false;
        }
        Object this$externalContactLastModifyTime = getExternalContactLastModifyTime();
        Object other$externalContactLastModifyTime = other.getExternalContactLastModifyTime();
        if (this$externalContactLastModifyTime == null) {
            if (other$externalContactLastModifyTime != null) {
                return false;
            }
        } else if (!this$externalContactLastModifyTime.equals(other$externalContactLastModifyTime)) {
            return false;
        }
        Object this$accountLastModifyTime = getAccountLastModifyTime();
        Object other$accountLastModifyTime = other.getAccountLastModifyTime();
        if (this$accountLastModifyTime == null) {
            if (other$accountLastModifyTime != null) {
                return false;
            }
        } else if (!this$accountLastModifyTime.equals(other$accountLastModifyTime)) {
            return false;
        }
        Object this$deviceConfigLastModifyTime = getDeviceConfigLastModifyTime();
        Object other$deviceConfigLastModifyTime = other.getDeviceConfigLastModifyTime();
        if (this$deviceConfigLastModifyTime == null) {
            if (other$deviceConfigLastModifyTime != null) {
                return false;
            }
        } else if (!this$deviceConfigLastModifyTime.equals(other$deviceConfigLastModifyTime)) {
            return false;
        }
        Object this$systemCurrentTime = getSystemCurrentTime();
        Object other$systemCurrentTime = other.getSystemCurrentTime();
        if (this$systemCurrentTime == null) {
            if (other$systemCurrentTime != null) {
                return false;
            }
        } else if (!this$systemCurrentTime.equals(other$systemCurrentTime)) {
            return false;
        }
        Object this$timeZone = getTimeZone();
        Object other$timeZone = other.getTimeZone();
        return this$timeZone == null ? other$timeZone == null : this$timeZone.equals(other$timeZone);
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    protected boolean canEqual(Object other) {
        return other instanceof DeviceHeartBeatResponse;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public int hashCode() {
        Object $groupLastModifyTime = getGroupLastModifyTime();
        int result = (1 * 59) + ($groupLastModifyTime == null ? 43 : $groupLastModifyTime.hashCode());
        Object $memberLastModifyTime = getMemberLastModifyTime();
        int result2 = (result * 59) + ($memberLastModifyTime == null ? 43 : $memberLastModifyTime.hashCode());
        Object $checkInLastModifyTime = getCheckInLastModifyTime();
        int result3 = (result2 * 59) + ($checkInLastModifyTime == null ? 43 : $checkInLastModifyTime.hashCode());
        Object $checkInTaskMemberLastModifyTime = getCheckInTaskMemberLastModifyTime();
        int result4 = (result3 * 59) + ($checkInTaskMemberLastModifyTime == null ? 43 : $checkInTaskMemberLastModifyTime.hashCode());
        Object $attendanceGroupLastModifyTime = getAttendanceGroupLastModifyTime();
        int result5 = (result4 * 59) + ($attendanceGroupLastModifyTime == null ? 43 : $attendanceGroupLastModifyTime.hashCode());
        Object $attendanceHolidayLastModifyTime = getAttendanceHolidayLastModifyTime();
        int result6 = (result5 * 59) + ($attendanceHolidayLastModifyTime == null ? 43 : $attendanceHolidayLastModifyTime.hashCode());
        Object $canteenLastModifyTime = getCanteenLastModifyTime();
        int result7 = (result6 * 59) + ($canteenLastModifyTime == null ? 43 : $canteenLastModifyTime.hashCode());
        Object $canteenMemberLastModifyTime = getCanteenMemberLastModifyTime();
        int result8 = (result7 * 59) + ($canteenMemberLastModifyTime == null ? 43 : $canteenMemberLastModifyTime.hashCode());
        Object $visitConfigLastModifyTime = getVisitConfigLastModifyTime();
        int result9 = (result8 * 59) + ($visitConfigLastModifyTime == null ? 43 : $visitConfigLastModifyTime.hashCode());
        Object $externalContactLastModifyTime = getExternalContactLastModifyTime();
        int result10 = (result9 * 59) + ($externalContactLastModifyTime == null ? 43 : $externalContactLastModifyTime.hashCode());
        Object $accountLastModifyTime = getAccountLastModifyTime();
        int result11 = (result10 * 59) + ($accountLastModifyTime == null ? 43 : $accountLastModifyTime.hashCode());
        Object $deviceConfigLastModifyTime = getDeviceConfigLastModifyTime();
        int result12 = (result11 * 59) + ($deviceConfigLastModifyTime == null ? 43 : $deviceConfigLastModifyTime.hashCode());
        Object $systemCurrentTime = getSystemCurrentTime();
        int result13 = (result12 * 59) + ($systemCurrentTime == null ? 43 : $systemCurrentTime.hashCode());
        Object $timeZone = getTimeZone();
        return (result13 * 59) + ($timeZone == null ? 43 : $timeZone.hashCode());
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public String toString() {
        return "DeviceHeartBeatResponse(groupLastModifyTime=" + getGroupLastModifyTime() + ", memberLastModifyTime=" + getMemberLastModifyTime() + ", checkInLastModifyTime=" + getCheckInLastModifyTime() + ", checkInTaskMemberLastModifyTime=" + getCheckInTaskMemberLastModifyTime() + ", attendanceGroupLastModifyTime=" + getAttendanceGroupLastModifyTime() + ", attendanceHolidayLastModifyTime=" + getAttendanceHolidayLastModifyTime() + ", canteenLastModifyTime=" + getCanteenLastModifyTime() + ", canteenMemberLastModifyTime=" + getCanteenMemberLastModifyTime() + ", visitConfigLastModifyTime=" + getVisitConfigLastModifyTime() + ", externalContactLastModifyTime=" + getExternalContactLastModifyTime() + ", accountLastModifyTime=" + getAccountLastModifyTime() + ", deviceConfigLastModifyTime=" + getDeviceConfigLastModifyTime() + ", systemCurrentTime=" + getSystemCurrentTime() + ", timeZone=" + getTimeZone() + ")";
    }

    public Long getGroupLastModifyTime() {
        return this.groupLastModifyTime;
    }

    public Long getMemberLastModifyTime() {
        return this.memberLastModifyTime;
    }

    public Long getCheckInLastModifyTime() {
        return this.checkInLastModifyTime;
    }

    public Long getCheckInTaskMemberLastModifyTime() {
        return this.checkInTaskMemberLastModifyTime;
    }

    public Long getAttendanceGroupLastModifyTime() {
        return this.attendanceGroupLastModifyTime;
    }

    public Long getAttendanceHolidayLastModifyTime() {
        return this.attendanceHolidayLastModifyTime;
    }

    public Long getCanteenLastModifyTime() {
        return this.canteenLastModifyTime;
    }

    public Long getCanteenMemberLastModifyTime() {
        return this.canteenMemberLastModifyTime;
    }

    public Long getVisitConfigLastModifyTime() {
        return this.visitConfigLastModifyTime;
    }

    public Long getExternalContactLastModifyTime() {
        return this.externalContactLastModifyTime;
    }

    public Long getAccountLastModifyTime() {
        return this.accountLastModifyTime;
    }

    public Long getDeviceConfigLastModifyTime() {
        return this.deviceConfigLastModifyTime;
    }

    public Long getSystemCurrentTime() {
        return this.systemCurrentTime;
    }

    public String getTimeZone() {
        return this.timeZone;
    }
}

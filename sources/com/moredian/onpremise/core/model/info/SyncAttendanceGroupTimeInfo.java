package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "同步考勤组时间信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/SyncAttendanceGroupTimeInfo.class */
public class SyncAttendanceGroupTimeInfo implements Serializable {
    private static final long serialVersionUID = 2342452675735506816L;

    @ApiModelProperty(name = "startDate", value = "开始日期")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "结束日期")
    private String endDate;

    @ApiModelProperty(name = "timeScope", value = "打卡周期：周日对应数字1，依次类推，逗号分隔，例：1,2,3,5")
    private String timeScope;

    @ApiModelProperty(name = "attendanceBeginTime", value = "上班时间，格式： 小时:分钟，例 20:20")
    private String attendanceBeginTime;

    @ApiModelProperty(name = "attendanceEndTime", value = "下班时间，格式： 小时:分钟，例 20:20")
    private String attendanceEndTime;

    @ApiModelProperty(name = "timeType", value = "时间类型：1-上班，2-下班")
    private Integer timeType;

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTimeScope(String timeScope) {
        this.timeScope = timeScope;
    }

    public void setAttendanceBeginTime(String attendanceBeginTime) {
        this.attendanceBeginTime = attendanceBeginTime;
    }

    public void setAttendanceEndTime(String attendanceEndTime) {
        this.attendanceEndTime = attendanceEndTime;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncAttendanceGroupTimeInfo)) {
            return false;
        }
        SyncAttendanceGroupTimeInfo other = (SyncAttendanceGroupTimeInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$startDate = getStartDate();
        Object other$startDate = other.getStartDate();
        if (this$startDate == null) {
            if (other$startDate != null) {
                return false;
            }
        } else if (!this$startDate.equals(other$startDate)) {
            return false;
        }
        Object this$endDate = getEndDate();
        Object other$endDate = other.getEndDate();
        if (this$endDate == null) {
            if (other$endDate != null) {
                return false;
            }
        } else if (!this$endDate.equals(other$endDate)) {
            return false;
        }
        Object this$timeScope = getTimeScope();
        Object other$timeScope = other.getTimeScope();
        if (this$timeScope == null) {
            if (other$timeScope != null) {
                return false;
            }
        } else if (!this$timeScope.equals(other$timeScope)) {
            return false;
        }
        Object this$attendanceBeginTime = getAttendanceBeginTime();
        Object other$attendanceBeginTime = other.getAttendanceBeginTime();
        if (this$attendanceBeginTime == null) {
            if (other$attendanceBeginTime != null) {
                return false;
            }
        } else if (!this$attendanceBeginTime.equals(other$attendanceBeginTime)) {
            return false;
        }
        Object this$attendanceEndTime = getAttendanceEndTime();
        Object other$attendanceEndTime = other.getAttendanceEndTime();
        if (this$attendanceEndTime == null) {
            if (other$attendanceEndTime != null) {
                return false;
            }
        } else if (!this$attendanceEndTime.equals(other$attendanceEndTime)) {
            return false;
        }
        Object this$timeType = getTimeType();
        Object other$timeType = other.getTimeType();
        return this$timeType == null ? other$timeType == null : this$timeType.equals(other$timeType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncAttendanceGroupTimeInfo;
    }

    public int hashCode() {
        Object $startDate = getStartDate();
        int result = (1 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result2 = (result * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $timeScope = getTimeScope();
        int result3 = (result2 * 59) + ($timeScope == null ? 43 : $timeScope.hashCode());
        Object $attendanceBeginTime = getAttendanceBeginTime();
        int result4 = (result3 * 59) + ($attendanceBeginTime == null ? 43 : $attendanceBeginTime.hashCode());
        Object $attendanceEndTime = getAttendanceEndTime();
        int result5 = (result4 * 59) + ($attendanceEndTime == null ? 43 : $attendanceEndTime.hashCode());
        Object $timeType = getTimeType();
        return (result5 * 59) + ($timeType == null ? 43 : $timeType.hashCode());
    }

    public String toString() {
        return "SyncAttendanceGroupTimeInfo(startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", timeScope=" + getTimeScope() + ", attendanceBeginTime=" + getAttendanceBeginTime() + ", attendanceEndTime=" + getAttendanceEndTime() + ", timeType=" + getTimeType() + ")";
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getTimeScope() {
        return this.timeScope;
    }

    public String getAttendanceBeginTime() {
        return this.attendanceBeginTime;
    }

    public String getAttendanceEndTime() {
        return this.attendanceEndTime;
    }

    public Integer getTimeType() {
        return this.timeType;
    }
}

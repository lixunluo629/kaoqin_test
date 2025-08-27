package com.moredian.onpremise.core.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "考勤组打卡时间信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/AttendanceGroupTimeDto.class */
public class AttendanceGroupTimeDto implements Serializable {
    private static final long serialVersionUID = 8767365784321461852L;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id", hidden = true)
    private Long attendanceGroupId;

    @ApiModelProperty(name = "startDate", value = "开始日期")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "结束日期")
    private String endDate;

    @ApiModelProperty(name = "timeScope", value = "打卡周期：周日对应数字1，依次类推，逗号分隔，例：1,2,3,5")
    private String timeScope;

    @ApiModelProperty(name = "attendanceBeginTime", value = "上班时间，格式： 小时:分钟，例 20:20")
    private String attendanceBeginTime;

    @ApiModelProperty(name = "attendanceBeginBefore", value = "上班打卡可提前时间")
    private Integer attendanceBeginBefore;

    @ApiModelProperty(name = "attendanceBeginAfter", value = "上班打卡可延后时间")
    private Integer attendanceBeginAfter;

    @ApiModelProperty(name = "attendanceEndTime", value = "下班时间，格式： 小时:分钟，例 20:20")
    private String attendanceEndTime;

    @ApiModelProperty(name = "attendanceEndBefore", value = "下班打卡可提前时间")
    private Integer attendanceEndBefore;

    @ApiModelProperty(name = "attendanceEndAfter", value = "下班打卡可延后时间")
    private Integer attendanceEndAfter;

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

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

    public void setAttendanceBeginBefore(Integer attendanceBeginBefore) {
        this.attendanceBeginBefore = attendanceBeginBefore;
    }

    public void setAttendanceBeginAfter(Integer attendanceBeginAfter) {
        this.attendanceBeginAfter = attendanceBeginAfter;
    }

    public void setAttendanceEndTime(String attendanceEndTime) {
        this.attendanceEndTime = attendanceEndTime;
    }

    public void setAttendanceEndBefore(Integer attendanceEndBefore) {
        this.attendanceEndBefore = attendanceEndBefore;
    }

    public void setAttendanceEndAfter(Integer attendanceEndAfter) {
        this.attendanceEndAfter = attendanceEndAfter;
    }

    public String toString() {
        return "AttendanceGroupTimeDto(attendanceGroupId=" + getAttendanceGroupId() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", timeScope=" + getTimeScope() + ", attendanceBeginTime=" + getAttendanceBeginTime() + ", attendanceBeginBefore=" + getAttendanceBeginBefore() + ", attendanceBeginAfter=" + getAttendanceBeginAfter() + ", attendanceEndTime=" + getAttendanceEndTime() + ", attendanceEndBefore=" + getAttendanceEndBefore() + ", attendanceEndAfter=" + getAttendanceEndAfter() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
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

    public Integer getAttendanceBeginBefore() {
        return this.attendanceBeginBefore;
    }

    public Integer getAttendanceBeginAfter() {
        return this.attendanceBeginAfter;
    }

    public String getAttendanceEndTime() {
        return this.attendanceEndTime;
    }

    public Integer getAttendanceEndBefore() {
        return this.attendanceEndBefore;
    }

    public Integer getAttendanceEndAfter() {
        return this.attendanceEndAfter;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttendanceGroupTimeDto dto = (AttendanceGroupTimeDto) o;
        return Objects.equals(this.startDate, dto.startDate) && Objects.equals(this.endDate, dto.endDate) && Objects.equals(this.timeScope, dto.timeScope) && Objects.equals(this.attendanceBeginTime, dto.attendanceBeginTime) && Objects.equals(this.attendanceBeginBefore, dto.attendanceBeginBefore) && Objects.equals(this.attendanceBeginAfter, dto.attendanceBeginAfter) && Objects.equals(this.attendanceEndTime, dto.attendanceEndTime) && Objects.equals(this.attendanceEndBefore, dto.attendanceEndBefore) && Objects.equals(this.attendanceEndAfter, dto.attendanceEndAfter);
    }

    public int hashCode() {
        return Objects.hash(this.startDate, this.endDate, this.timeScope, this.attendanceBeginTime, this.attendanceBeginBefore, this.attendanceBeginAfter, this.attendanceEndTime, this.attendanceEndBefore, this.attendanceEndAfter);
    }
}

package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤事件-加班")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/AttendanceEventOvertimeDto.class */
public class AttendanceEventOvertimeDto implements Serializable {

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "开始时间,格式 yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty(name = "startFrame", value = "班次，1-上午，2-下午")
    private Integer startFrame;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "结束时间,格式 yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty(name = "endFrame", value = "班次，1-上午，2-下午")
    private Integer endFrame;

    @ApiModelProperty(name = "overtimeDays", value = "加班天数")
    private String overtimeDays;

    @ApiModelProperty(name = "overtimeReason", value = "加班原因")
    private String overtimeReason;

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setStartFrame(Integer startFrame) {
        this.startFrame = startFrame;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setEndFrame(Integer endFrame) {
        this.endFrame = endFrame;
    }

    public void setOvertimeDays(String overtimeDays) {
        this.overtimeDays = overtimeDays;
    }

    public void setOvertimeReason(String overtimeReason) {
        this.overtimeReason = overtimeReason;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventOvertimeDto)) {
            return false;
        }
        AttendanceEventOvertimeDto other = (AttendanceEventOvertimeDto) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$startTime = getStartTime();
        Object other$startTime = other.getStartTime();
        if (this$startTime == null) {
            if (other$startTime != null) {
                return false;
            }
        } else if (!this$startTime.equals(other$startTime)) {
            return false;
        }
        Object this$startFrame = getStartFrame();
        Object other$startFrame = other.getStartFrame();
        if (this$startFrame == null) {
            if (other$startFrame != null) {
                return false;
            }
        } else if (!this$startFrame.equals(other$startFrame)) {
            return false;
        }
        Object this$endTime = getEndTime();
        Object other$endTime = other.getEndTime();
        if (this$endTime == null) {
            if (other$endTime != null) {
                return false;
            }
        } else if (!this$endTime.equals(other$endTime)) {
            return false;
        }
        Object this$endFrame = getEndFrame();
        Object other$endFrame = other.getEndFrame();
        if (this$endFrame == null) {
            if (other$endFrame != null) {
                return false;
            }
        } else if (!this$endFrame.equals(other$endFrame)) {
            return false;
        }
        Object this$overtimeDays = getOvertimeDays();
        Object other$overtimeDays = other.getOvertimeDays();
        if (this$overtimeDays == null) {
            if (other$overtimeDays != null) {
                return false;
            }
        } else if (!this$overtimeDays.equals(other$overtimeDays)) {
            return false;
        }
        Object this$overtimeReason = getOvertimeReason();
        Object other$overtimeReason = other.getOvertimeReason();
        return this$overtimeReason == null ? other$overtimeReason == null : this$overtimeReason.equals(other$overtimeReason);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventOvertimeDto;
    }

    public int hashCode() {
        Object $startTime = getStartTime();
        int result = (1 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $startFrame = getStartFrame();
        int result2 = (result * 59) + ($startFrame == null ? 43 : $startFrame.hashCode());
        Object $endTime = getEndTime();
        int result3 = (result2 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $endFrame = getEndFrame();
        int result4 = (result3 * 59) + ($endFrame == null ? 43 : $endFrame.hashCode());
        Object $overtimeDays = getOvertimeDays();
        int result5 = (result4 * 59) + ($overtimeDays == null ? 43 : $overtimeDays.hashCode());
        Object $overtimeReason = getOvertimeReason();
        return (result5 * 59) + ($overtimeReason == null ? 43 : $overtimeReason.hashCode());
    }

    public String toString() {
        return "AttendanceEventOvertimeDto(startTime=" + getStartTime() + ", startFrame=" + getStartFrame() + ", endTime=" + getEndTime() + ", endFrame=" + getEndFrame() + ", overtimeDays=" + getOvertimeDays() + ", overtimeReason=" + getOvertimeReason() + ")";
    }

    public String getStartTime() {
        return this.startTime;
    }

    public Integer getStartFrame() {
        return this.startFrame;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public Integer getEndFrame() {
        return this.endFrame;
    }

    public String getOvertimeDays() {
        return this.overtimeDays;
    }

    public String getOvertimeReason() {
        return this.overtimeReason;
    }
}

package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤事件-请假")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/AttendanceEventLeaveDto.class */
public class AttendanceEventLeaveDto implements Serializable {

    @ApiModelProperty(name = "type", value = "请假类型，1-年假，2-病假，3-事假，4-婚假，5-丧假，6-产假，7-陪产假，8-调休，9-其他")
    private Integer type;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "开始时间 ,格式 yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty(name = "startFrame", value = "区间，1-上午，2-下午")
    private Integer startFrame;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "结束时间 ,格式 yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty(name = "endFrame", value = "区间，1-上午，2-下午")
    private Integer endFrame;

    @ApiModelProperty(name = "leaveDays", value = "请假天数")
    private String leaveDays;

    @ApiModelProperty(name = "leaveReason", value = "请假原因")
    private String leaveReason;

    public void setType(Integer type) {
        this.type = type;
    }

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

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventLeaveDto)) {
            return false;
        }
        AttendanceEventLeaveDto other = (AttendanceEventLeaveDto) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
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
        Object this$leaveDays = getLeaveDays();
        Object other$leaveDays = other.getLeaveDays();
        if (this$leaveDays == null) {
            if (other$leaveDays != null) {
                return false;
            }
        } else if (!this$leaveDays.equals(other$leaveDays)) {
            return false;
        }
        Object this$leaveReason = getLeaveReason();
        Object other$leaveReason = other.getLeaveReason();
        return this$leaveReason == null ? other$leaveReason == null : this$leaveReason.equals(other$leaveReason);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventLeaveDto;
    }

    public int hashCode() {
        Object $type = getType();
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $startTime = getStartTime();
        int result2 = (result * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $startFrame = getStartFrame();
        int result3 = (result2 * 59) + ($startFrame == null ? 43 : $startFrame.hashCode());
        Object $endTime = getEndTime();
        int result4 = (result3 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $endFrame = getEndFrame();
        int result5 = (result4 * 59) + ($endFrame == null ? 43 : $endFrame.hashCode());
        Object $leaveDays = getLeaveDays();
        int result6 = (result5 * 59) + ($leaveDays == null ? 43 : $leaveDays.hashCode());
        Object $leaveReason = getLeaveReason();
        return (result6 * 59) + ($leaveReason == null ? 43 : $leaveReason.hashCode());
    }

    public String toString() {
        return "AttendanceEventLeaveDto(type=" + getType() + ", startTime=" + getStartTime() + ", startFrame=" + getStartFrame() + ", endTime=" + getEndTime() + ", endFrame=" + getEndFrame() + ", leaveDays=" + getLeaveDays() + ", leaveReason=" + getLeaveReason() + ")";
    }

    public Integer getType() {
        return this.type;
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

    public String getLeaveDays() {
        return this.leaveDays;
    }

    public String getLeaveReason() {
        return this.leaveReason;
    }
}

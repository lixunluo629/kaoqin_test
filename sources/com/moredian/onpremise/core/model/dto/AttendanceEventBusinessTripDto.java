package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤事件-出差")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/AttendanceEventBusinessTripDto.class */
public class AttendanceEventBusinessTripDto implements Serializable {

    @ApiModelProperty(name = RtspHeaders.Values.DESTINATION, value = "出差目的地")
    private String destination;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "开始时间,格式 yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty(name = "startFrame", value = "班次，1-上午，2-下午")
    private Integer startFrame;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "结束时间,格式 yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty(name = "endFrame", value = "班次，1-上午，2-下午")
    private Integer endFrame;

    @ApiModelProperty(name = "tripDays", value = "出差天数")
    private String tripDays;

    @ApiModelProperty(name = "tripReason", value = "出差原因")
    private String tripReason;

    public void setDestination(String destination) {
        this.destination = destination;
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

    public void setTripDays(String tripDays) {
        this.tripDays = tripDays;
    }

    public void setTripReason(String tripReason) {
        this.tripReason = tripReason;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventBusinessTripDto)) {
            return false;
        }
        AttendanceEventBusinessTripDto other = (AttendanceEventBusinessTripDto) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$destination = getDestination();
        Object other$destination = other.getDestination();
        if (this$destination == null) {
            if (other$destination != null) {
                return false;
            }
        } else if (!this$destination.equals(other$destination)) {
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
        Object this$tripDays = getTripDays();
        Object other$tripDays = other.getTripDays();
        if (this$tripDays == null) {
            if (other$tripDays != null) {
                return false;
            }
        } else if (!this$tripDays.equals(other$tripDays)) {
            return false;
        }
        Object this$tripReason = getTripReason();
        Object other$tripReason = other.getTripReason();
        return this$tripReason == null ? other$tripReason == null : this$tripReason.equals(other$tripReason);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventBusinessTripDto;
    }

    public int hashCode() {
        Object $destination = getDestination();
        int result = (1 * 59) + ($destination == null ? 43 : $destination.hashCode());
        Object $startTime = getStartTime();
        int result2 = (result * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $startFrame = getStartFrame();
        int result3 = (result2 * 59) + ($startFrame == null ? 43 : $startFrame.hashCode());
        Object $endTime = getEndTime();
        int result4 = (result3 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $endFrame = getEndFrame();
        int result5 = (result4 * 59) + ($endFrame == null ? 43 : $endFrame.hashCode());
        Object $tripDays = getTripDays();
        int result6 = (result5 * 59) + ($tripDays == null ? 43 : $tripDays.hashCode());
        Object $tripReason = getTripReason();
        return (result6 * 59) + ($tripReason == null ? 43 : $tripReason.hashCode());
    }

    public String toString() {
        return "AttendanceEventBusinessTripDto(destination=" + getDestination() + ", startTime=" + getStartTime() + ", startFrame=" + getStartFrame() + ", endTime=" + getEndTime() + ", endFrame=" + getEndFrame() + ", tripDays=" + getTripDays() + ", tripReason=" + getTripReason() + ")";
    }

    public String getDestination() {
        return this.destination;
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

    public String getTripDays() {
        return this.tripDays;
    }

    public String getTripReason() {
        return this.tripReason;
    }
}

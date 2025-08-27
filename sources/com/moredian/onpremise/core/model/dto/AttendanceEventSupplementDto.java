package com.moredian.onpremise.core.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤事件-补卡")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/AttendanceEventSupplementDto.class */
public class AttendanceEventSupplementDto implements Serializable {

    @ApiModelProperty(name = "type", value = "请假类型，1-上班卡，2-下班卡")
    private Integer type;

    @ApiModelProperty(name = "supplementTime", value = "补卡时间 ,格式：yyyy-MM-dd hh:mm:ss")
    private String supplementTime;

    @ApiModelProperty(name = "supplementReason", value = "补卡原因")
    private String supplementReason;

    public void setType(Integer type) {
        this.type = type;
    }

    public void setSupplementTime(String supplementTime) {
        this.supplementTime = supplementTime;
    }

    public void setSupplementReason(String supplementReason) {
        this.supplementReason = supplementReason;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventSupplementDto)) {
            return false;
        }
        AttendanceEventSupplementDto other = (AttendanceEventSupplementDto) o;
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
        Object this$supplementTime = getSupplementTime();
        Object other$supplementTime = other.getSupplementTime();
        if (this$supplementTime == null) {
            if (other$supplementTime != null) {
                return false;
            }
        } else if (!this$supplementTime.equals(other$supplementTime)) {
            return false;
        }
        Object this$supplementReason = getSupplementReason();
        Object other$supplementReason = other.getSupplementReason();
        return this$supplementReason == null ? other$supplementReason == null : this$supplementReason.equals(other$supplementReason);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventSupplementDto;
    }

    public int hashCode() {
        Object $type = getType();
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $supplementTime = getSupplementTime();
        int result2 = (result * 59) + ($supplementTime == null ? 43 : $supplementTime.hashCode());
        Object $supplementReason = getSupplementReason();
        return (result2 * 59) + ($supplementReason == null ? 43 : $supplementReason.hashCode());
    }

    public String toString() {
        return "AttendanceEventSupplementDto(type=" + getType() + ", supplementTime=" + getSupplementTime() + ", supplementReason=" + getSupplementReason() + ")";
    }

    public Integer getType() {
        return this.type;
    }

    public String getSupplementTime() {
        return this.supplementTime;
    }

    public String getSupplementReason() {
        return this.supplementReason;
    }
}

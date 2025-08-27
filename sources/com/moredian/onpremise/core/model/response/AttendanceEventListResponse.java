package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤事件分页查询响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AttendanceEventListResponse.class */
public class AttendanceEventListResponse implements Serializable {

    @ApiModelProperty(name = "orgId", value = "orgId", hidden = true)
    private Long orgId;

    @ApiModelProperty(name = "attendanceEventId", value = "考勤事件id")
    private Long attendanceEventId;

    @ApiModelProperty(name = "eventType", value = "事件类型 ：1-请假，2-出差，3-外出，4-加班，5-补卡")
    private Integer eventType;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "applyTime", value = "申请时间")
    private Long applyTime;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setAttendanceEventId(Long attendanceEventId) {
        this.attendanceEventId = attendanceEventId;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventListResponse)) {
            return false;
        }
        AttendanceEventListResponse other = (AttendanceEventListResponse) o;
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
        Object this$attendanceEventId = getAttendanceEventId();
        Object other$attendanceEventId = other.getAttendanceEventId();
        if (this$attendanceEventId == null) {
            if (other$attendanceEventId != null) {
                return false;
            }
        } else if (!this$attendanceEventId.equals(other$attendanceEventId)) {
            return false;
        }
        Object this$eventType = getEventType();
        Object other$eventType = other.getEventType();
        if (this$eventType == null) {
            if (other$eventType != null) {
                return false;
            }
        } else if (!this$eventType.equals(other$eventType)) {
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
        Object this$applyTime = getApplyTime();
        Object other$applyTime = other.getApplyTime();
        return this$applyTime == null ? other$applyTime == null : this$applyTime.equals(other$applyTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventListResponse;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $attendanceEventId = getAttendanceEventId();
        int result2 = (result * 59) + ($attendanceEventId == null ? 43 : $attendanceEventId.hashCode());
        Object $eventType = getEventType();
        int result3 = (result2 * 59) + ($eventType == null ? 43 : $eventType.hashCode());
        Object $memberId = getMemberId();
        int result4 = (result3 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result5 = (result4 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $applyTime = getApplyTime();
        return (result5 * 59) + ($applyTime == null ? 43 : $applyTime.hashCode());
    }

    public String toString() {
        return "AttendanceEventListResponse(orgId=" + getOrgId() + ", attendanceEventId=" + getAttendanceEventId() + ", eventType=" + getEventType() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", applyTime=" + getApplyTime() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Long getAttendanceEventId() {
        return this.attendanceEventId;
    }

    public Integer getEventType() {
        return this.eventType;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Long getApplyTime() {
        return this.applyTime;
    }
}

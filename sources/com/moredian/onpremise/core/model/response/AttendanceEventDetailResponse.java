package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.model.dto.AttendanceEventBusinessOutDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventBusinessTripDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventLeaveDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventOvertimeDto;
import com.moredian.onpremise.core.model.dto.AttendanceEventSupplementDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤事件详情")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AttendanceEventDetailResponse.class */
public class AttendanceEventDetailResponse implements Serializable {
    private static final long serialVersionUID = -3797044616643645814L;

    @ApiModelProperty(name = "attendanceEventId", value = "考勤事件id")
    private Long attendanceEventId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "eventType", value = "事件类型：1-请假，2-出差，3-外出，4-加班，5-补卡")
    private Integer eventType;

    @ApiModelProperty(name = "leave", value = "请假")
    private AttendanceEventLeaveDto leave;

    @ApiModelProperty(name = "businessTrip", value = "出差")
    private AttendanceEventBusinessTripDto businessTrip;

    @ApiModelProperty(name = "businessOut", value = "外出")
    private AttendanceEventBusinessOutDto businessOut;

    @ApiModelProperty(name = "overtime", value = "加班")
    private AttendanceEventOvertimeDto overtime;

    @ApiModelProperty(name = "supplement", value = "补卡")
    private AttendanceEventSupplementDto supplement;

    public void setAttendanceEventId(Long attendanceEventId) {
        this.attendanceEventId = attendanceEventId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public void setLeave(AttendanceEventLeaveDto leave) {
        this.leave = leave;
    }

    public void setBusinessTrip(AttendanceEventBusinessTripDto businessTrip) {
        this.businessTrip = businessTrip;
    }

    public void setBusinessOut(AttendanceEventBusinessOutDto businessOut) {
        this.businessOut = businessOut;
    }

    public void setOvertime(AttendanceEventOvertimeDto overtime) {
        this.overtime = overtime;
    }

    public void setSupplement(AttendanceEventSupplementDto supplement) {
        this.supplement = supplement;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceEventDetailResponse)) {
            return false;
        }
        AttendanceEventDetailResponse other = (AttendanceEventDetailResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
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
        Object this$leave = getLeave();
        Object other$leave = other.getLeave();
        if (this$leave == null) {
            if (other$leave != null) {
                return false;
            }
        } else if (!this$leave.equals(other$leave)) {
            return false;
        }
        Object this$businessTrip = getBusinessTrip();
        Object other$businessTrip = other.getBusinessTrip();
        if (this$businessTrip == null) {
            if (other$businessTrip != null) {
                return false;
            }
        } else if (!this$businessTrip.equals(other$businessTrip)) {
            return false;
        }
        Object this$businessOut = getBusinessOut();
        Object other$businessOut = other.getBusinessOut();
        if (this$businessOut == null) {
            if (other$businessOut != null) {
                return false;
            }
        } else if (!this$businessOut.equals(other$businessOut)) {
            return false;
        }
        Object this$overtime = getOvertime();
        Object other$overtime = other.getOvertime();
        if (this$overtime == null) {
            if (other$overtime != null) {
                return false;
            }
        } else if (!this$overtime.equals(other$overtime)) {
            return false;
        }
        Object this$supplement = getSupplement();
        Object other$supplement = other.getSupplement();
        return this$supplement == null ? other$supplement == null : this$supplement.equals(other$supplement);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceEventDetailResponse;
    }

    public int hashCode() {
        Object $attendanceEventId = getAttendanceEventId();
        int result = (1 * 59) + ($attendanceEventId == null ? 43 : $attendanceEventId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $memberId = getMemberId();
        int result3 = (result2 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result4 = (result3 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $eventType = getEventType();
        int result5 = (result4 * 59) + ($eventType == null ? 43 : $eventType.hashCode());
        Object $leave = getLeave();
        int result6 = (result5 * 59) + ($leave == null ? 43 : $leave.hashCode());
        Object $businessTrip = getBusinessTrip();
        int result7 = (result6 * 59) + ($businessTrip == null ? 43 : $businessTrip.hashCode());
        Object $businessOut = getBusinessOut();
        int result8 = (result7 * 59) + ($businessOut == null ? 43 : $businessOut.hashCode());
        Object $overtime = getOvertime();
        int result9 = (result8 * 59) + ($overtime == null ? 43 : $overtime.hashCode());
        Object $supplement = getSupplement();
        return (result9 * 59) + ($supplement == null ? 43 : $supplement.hashCode());
    }

    public String toString() {
        return "AttendanceEventDetailResponse(attendanceEventId=" + getAttendanceEventId() + ", orgId=" + getOrgId() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", eventType=" + getEventType() + ", leave=" + getLeave() + ", businessTrip=" + getBusinessTrip() + ", businessOut=" + getBusinessOut() + ", overtime=" + getOvertime() + ", supplement=" + getSupplement() + ")";
    }

    public Long getAttendanceEventId() {
        return this.attendanceEventId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Integer getEventType() {
        return this.eventType;
    }

    public AttendanceEventLeaveDto getLeave() {
        return this.leave;
    }

    public AttendanceEventBusinessTripDto getBusinessTrip() {
        return this.businessTrip;
    }

    public AttendanceEventBusinessOutDto getBusinessOut() {
        return this.businessOut;
    }

    public AttendanceEventOvertimeDto getOvertime() {
        return this.overtime;
    }

    public AttendanceEventSupplementDto getSupplement() {
        return this.supplement;
    }
}

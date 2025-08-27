package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除考勤事件")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteAttendanceEventRequest.class */
public class DeleteAttendanceEventRequest extends BaseRequest {
    private static final long serialVersionUID = -8972421569645711614L;

    @ApiModelProperty(name = "attendanceEventId", value = "考勤事件id")
    private Long attendanceEventId;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    public void setAttendanceEventId(Long attendanceEventId) {
        this.attendanceEventId = attendanceEventId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteAttendanceEventRequest)) {
            return false;
        }
        DeleteAttendanceEventRequest other = (DeleteAttendanceEventRequest) o;
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        return this$memberId == null ? other$memberId == null : this$memberId.equals(other$memberId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteAttendanceEventRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $attendanceEventId = getAttendanceEventId();
        int result = (1 * 59) + ($attendanceEventId == null ? 43 : $attendanceEventId.hashCode());
        Object $memberId = getMemberId();
        return (result * 59) + ($memberId == null ? 43 : $memberId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteAttendanceEventRequest(attendanceEventId=" + getAttendanceEventId() + ", memberId=" + getMemberId() + ")";
    }

    public Long getAttendanceEventId() {
        return this.attendanceEventId;
    }

    public Long getMemberId() {
        return this.memberId;
    }
}

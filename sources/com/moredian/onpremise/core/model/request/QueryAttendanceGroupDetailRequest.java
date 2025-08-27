package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查询考勤组详情")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryAttendanceGroupDetailRequest.class */
public class QueryAttendanceGroupDetailRequest extends BaseRequest {
    private static final long serialVersionUID = -1519977574177879906L;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryAttendanceGroupDetailRequest)) {
            return false;
        }
        QueryAttendanceGroupDetailRequest other = (QueryAttendanceGroupDetailRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        return this$attendanceGroupId == null ? other$attendanceGroupId == null : this$attendanceGroupId.equals(other$attendanceGroupId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryAttendanceGroupDetailRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $attendanceGroupId = getAttendanceGroupId();
        int result = (1 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryAttendanceGroupDetailRequest(attendanceGroupId=" + getAttendanceGroupId() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }
}

package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查询节假日")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryAttendanceHolidayRequest.class */
public class QueryAttendanceHolidayRequest extends BaseRequest {
    private static final long serialVersionUID = -3536461731343910776L;

    @ApiModelProperty(name = "attendanceHolidayId", value = "节假日id")
    private Long attendanceHolidayId;

    public void setAttendanceHolidayId(Long attendanceHolidayId) {
        this.attendanceHolidayId = attendanceHolidayId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryAttendanceHolidayRequest)) {
            return false;
        }
        QueryAttendanceHolidayRequest other = (QueryAttendanceHolidayRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceHolidayId = getAttendanceHolidayId();
        Object other$attendanceHolidayId = other.getAttendanceHolidayId();
        return this$attendanceHolidayId == null ? other$attendanceHolidayId == null : this$attendanceHolidayId.equals(other$attendanceHolidayId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryAttendanceHolidayRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $attendanceHolidayId = getAttendanceHolidayId();
        int result = (1 * 59) + ($attendanceHolidayId == null ? 43 : $attendanceHolidayId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryAttendanceHolidayRequest(attendanceHolidayId=" + getAttendanceHolidayId() + ")";
    }

    public Long getAttendanceHolidayId() {
        return this.attendanceHolidayId;
    }
}

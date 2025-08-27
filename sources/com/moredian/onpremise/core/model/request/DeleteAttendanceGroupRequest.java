package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除考勤组请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteAttendanceGroupRequest.class */
public class DeleteAttendanceGroupRequest extends BaseRequest {
    private static final long serialVersionUID = 2281216793166118101L;

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
        if (!(o instanceof DeleteAttendanceGroupRequest)) {
            return false;
        }
        DeleteAttendanceGroupRequest other = (DeleteAttendanceGroupRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        return this$attendanceGroupId == null ? other$attendanceGroupId == null : this$attendanceGroupId.equals(other$attendanceGroupId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteAttendanceGroupRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $attendanceGroupId = getAttendanceGroupId();
        int result = (1 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteAttendanceGroupRequest(attendanceGroupId=" + getAttendanceGroupId() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }
}

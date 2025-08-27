package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存考勤组响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/SaveAttendanceGroupResponse.class */
public class SaveAttendanceGroupResponse implements Serializable {
    private static final long serialVersionUID = -4598366622730649676L;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAttendanceGroupResponse)) {
            return false;
        }
        SaveAttendanceGroupResponse other = (SaveAttendanceGroupResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        return this$attendanceGroupId == null ? other$attendanceGroupId == null : this$attendanceGroupId.equals(other$attendanceGroupId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveAttendanceGroupResponse;
    }

    public int hashCode() {
        Object $attendanceGroupId = getAttendanceGroupId();
        int result = (1 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        return result;
    }

    public String toString() {
        return "SaveAttendanceGroupResponse(attendanceGroupId=" + getAttendanceGroupId() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public SaveAttendanceGroupResponse() {
    }

    public SaveAttendanceGroupResponse(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }
}

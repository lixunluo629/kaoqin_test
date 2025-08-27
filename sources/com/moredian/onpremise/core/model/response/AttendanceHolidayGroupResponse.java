package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "查询考勤节假日-关联考勤组响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AttendanceHolidayGroupResponse.class */
public class AttendanceHolidayGroupResponse implements Serializable {

    @ApiModelProperty(name = "attendanceGroupId", value = "关联的考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "groupName", value = "关联的考勤组名称")
    private String groupName;

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceHolidayGroupResponse)) {
            return false;
        }
        AttendanceHolidayGroupResponse other = (AttendanceHolidayGroupResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        if (this$attendanceGroupId == null) {
            if (other$attendanceGroupId != null) {
                return false;
            }
        } else if (!this$attendanceGroupId.equals(other$attendanceGroupId)) {
            return false;
        }
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        return this$groupName == null ? other$groupName == null : this$groupName.equals(other$groupName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceHolidayGroupResponse;
    }

    public int hashCode() {
        Object $attendanceGroupId = getAttendanceGroupId();
        int result = (1 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $groupName = getGroupName();
        return (result * 59) + ($groupName == null ? 43 : $groupName.hashCode());
    }

    public String toString() {
        return "AttendanceHolidayGroupResponse(attendanceGroupId=" + getAttendanceGroupId() + ", groupName=" + getGroupName() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getGroupName() {
        return this.groupName;
    }
}

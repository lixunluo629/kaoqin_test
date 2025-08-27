package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "查询考勤节假日分页响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AttendanceHolidayListResponse.class */
public class AttendanceHolidayListResponse implements Serializable {

    @ApiModelProperty(name = "attendanceHolidayId", value = "节假日id")
    private Long attendanceHolidayId;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private String attendanceGroupId;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    @ApiModelProperty(name = "holidayName", value = "节假日名称")
    private String holidayName;

    @ApiModelProperty(name = "startDate", value = "放假时间开始")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "放假时间结束")
    private String endDate;

    @ApiModelProperty(name = "groupNames", value = "关联考勤组")
    private String groupNames;

    public void setAttendanceHolidayId(Long attendanceHolidayId) {
        this.attendanceHolidayId = attendanceHolidayId;
    }

    public void setAttendanceGroupId(String attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceHolidayListResponse)) {
            return false;
        }
        AttendanceHolidayListResponse other = (AttendanceHolidayListResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$attendanceHolidayId = getAttendanceHolidayId();
        Object other$attendanceHolidayId = other.getAttendanceHolidayId();
        if (this$attendanceHolidayId == null) {
            if (other$attendanceHolidayId != null) {
                return false;
            }
        } else if (!this$attendanceHolidayId.equals(other$attendanceHolidayId)) {
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        Object this$holidayName = getHolidayName();
        Object other$holidayName = other.getHolidayName();
        if (this$holidayName == null) {
            if (other$holidayName != null) {
                return false;
            }
        } else if (!this$holidayName.equals(other$holidayName)) {
            return false;
        }
        Object this$startDate = getStartDate();
        Object other$startDate = other.getStartDate();
        if (this$startDate == null) {
            if (other$startDate != null) {
                return false;
            }
        } else if (!this$startDate.equals(other$startDate)) {
            return false;
        }
        Object this$endDate = getEndDate();
        Object other$endDate = other.getEndDate();
        if (this$endDate == null) {
            if (other$endDate != null) {
                return false;
            }
        } else if (!this$endDate.equals(other$endDate)) {
            return false;
        }
        Object this$groupNames = getGroupNames();
        Object other$groupNames = other.getGroupNames();
        return this$groupNames == null ? other$groupNames == null : this$groupNames.equals(other$groupNames);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceHolidayListResponse;
    }

    public int hashCode() {
        Object $attendanceHolidayId = getAttendanceHolidayId();
        int result = (1 * 59) + ($attendanceHolidayId == null ? 43 : $attendanceHolidayId.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result2 = (result * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $orgId = getOrgId();
        int result3 = (result2 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $holidayName = getHolidayName();
        int result4 = (result3 * 59) + ($holidayName == null ? 43 : $holidayName.hashCode());
        Object $startDate = getStartDate();
        int result5 = (result4 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result6 = (result5 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $groupNames = getGroupNames();
        return (result6 * 59) + ($groupNames == null ? 43 : $groupNames.hashCode());
    }

    public String toString() {
        return "AttendanceHolidayListResponse(attendanceHolidayId=" + getAttendanceHolidayId() + ", attendanceGroupId=" + getAttendanceGroupId() + ", orgId=" + getOrgId() + ", holidayName=" + getHolidayName() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", groupNames=" + getGroupNames() + ")";
    }

    public Long getAttendanceHolidayId() {
        return this.attendanceHolidayId;
    }

    public String getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getHolidayName() {
        return this.holidayName;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getGroupNames() {
        return this.groupNames;
    }
}

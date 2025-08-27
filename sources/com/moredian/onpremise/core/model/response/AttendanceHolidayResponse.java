package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.model.dto.AttendanceGroupDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "查询考勤节假日响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AttendanceHolidayResponse.class */
public class AttendanceHolidayResponse implements Serializable {

    @ApiModelProperty(name = "attendanceHolidayId", value = "节假日id，更新时必填")
    private Long attendanceHolidayId;

    @ApiModelProperty(name = "holidayName", value = "节假日名称，必填")
    private String holidayName;

    @ApiModelProperty(name = "startDate", value = "放假时间开始")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "放假时间结束")
    private String endDate;

    @ApiModelProperty(name = "repayWorkDates", value = "补班日期，例['2019-04-28','2019-04-29']")
    private List<String> repayWorkDates;

    @ApiModelProperty(name = "attendanceGroupResponses", value = "关联考勤组，例[1,2]")
    private List<AttendanceGroupDto> attendanceGroupResponses;

    public void setAttendanceHolidayId(Long attendanceHolidayId) {
        this.attendanceHolidayId = attendanceHolidayId;
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

    public void setRepayWorkDates(List<String> repayWorkDates) {
        this.repayWorkDates = repayWorkDates;
    }

    public void setAttendanceGroupResponses(List<AttendanceGroupDto> attendanceGroupResponses) {
        this.attendanceGroupResponses = attendanceGroupResponses;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceHolidayResponse)) {
            return false;
        }
        AttendanceHolidayResponse other = (AttendanceHolidayResponse) o;
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
        Object this$repayWorkDates = getRepayWorkDates();
        Object other$repayWorkDates = other.getRepayWorkDates();
        if (this$repayWorkDates == null) {
            if (other$repayWorkDates != null) {
                return false;
            }
        } else if (!this$repayWorkDates.equals(other$repayWorkDates)) {
            return false;
        }
        Object this$attendanceGroupResponses = getAttendanceGroupResponses();
        Object other$attendanceGroupResponses = other.getAttendanceGroupResponses();
        return this$attendanceGroupResponses == null ? other$attendanceGroupResponses == null : this$attendanceGroupResponses.equals(other$attendanceGroupResponses);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceHolidayResponse;
    }

    public int hashCode() {
        Object $attendanceHolidayId = getAttendanceHolidayId();
        int result = (1 * 59) + ($attendanceHolidayId == null ? 43 : $attendanceHolidayId.hashCode());
        Object $holidayName = getHolidayName();
        int result2 = (result * 59) + ($holidayName == null ? 43 : $holidayName.hashCode());
        Object $startDate = getStartDate();
        int result3 = (result2 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result4 = (result3 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $repayWorkDates = getRepayWorkDates();
        int result5 = (result4 * 59) + ($repayWorkDates == null ? 43 : $repayWorkDates.hashCode());
        Object $attendanceGroupResponses = getAttendanceGroupResponses();
        return (result5 * 59) + ($attendanceGroupResponses == null ? 43 : $attendanceGroupResponses.hashCode());
    }

    public String toString() {
        return "AttendanceHolidayResponse(attendanceHolidayId=" + getAttendanceHolidayId() + ", holidayName=" + getHolidayName() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", repayWorkDates=" + getRepayWorkDates() + ", attendanceGroupResponses=" + getAttendanceGroupResponses() + ")";
    }

    public Long getAttendanceHolidayId() {
        return this.attendanceHolidayId;
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

    public List<String> getRepayWorkDates() {
        return this.repayWorkDates;
    }

    public List<AttendanceGroupDto> getAttendanceGroupResponses() {
        return this.attendanceGroupResponses;
    }
}

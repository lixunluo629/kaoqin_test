package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "保存考勤节假日")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveAttendanceHolidayRequest.class */
public class SaveAttendanceHolidayRequest extends BaseRequest {

    @ApiModelProperty(name = "attendanceHolidayId", value = "节假日id，更新时必填")
    private Long attendanceHolidayId;

    @ApiModelProperty(name = "holidayName", value = "节假日名称，必填")
    private String holidayName;

    @ApiModelProperty(name = "startDate", value = "放假开始时间,格式yyyy-MM-dd ,例2019-04-28")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "放假结束时间，格式yyyy-MM-dd ,例2019-04-28")
    private String endDate;

    @ApiModelProperty(name = "repayWorkDates", value = "补班日期，例['2019-04-28','2019-04-29']")
    private List<String> repayWorkDates;

    @ApiModelProperty(name = "attendanceGroupIds", value = "关联考勤组，例[1,2]")
    private List<Long> attendanceGroupIds;

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

    public void setAttendanceGroupIds(List<Long> attendanceGroupIds) {
        this.attendanceGroupIds = attendanceGroupIds;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAttendanceHolidayRequest)) {
            return false;
        }
        SaveAttendanceHolidayRequest other = (SaveAttendanceHolidayRequest) o;
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
        Object this$attendanceGroupIds = getAttendanceGroupIds();
        Object other$attendanceGroupIds = other.getAttendanceGroupIds();
        return this$attendanceGroupIds == null ? other$attendanceGroupIds == null : this$attendanceGroupIds.equals(other$attendanceGroupIds);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveAttendanceHolidayRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
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
        Object $attendanceGroupIds = getAttendanceGroupIds();
        return (result5 * 59) + ($attendanceGroupIds == null ? 43 : $attendanceGroupIds.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveAttendanceHolidayRequest(attendanceHolidayId=" + getAttendanceHolidayId() + ", holidayName=" + getHolidayName() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", repayWorkDates=" + getRepayWorkDates() + ", attendanceGroupIds=" + getAttendanceGroupIds() + ")";
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

    public List<Long> getAttendanceGroupIds() {
        return this.attendanceGroupIds;
    }
}

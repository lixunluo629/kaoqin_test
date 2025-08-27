package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(description = "终端同步权限组响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncAttendanceHolidayResponse.class */
public class TerminalSyncAttendanceHolidayResponse implements Serializable {

    @ApiModelProperty(name = "attendanceGroupId", value = "节假日id")
    private Long attendanceHolidayId;

    @ApiModelProperty(name = "holidayName", value = "节假日名称")
    private String holidayName;

    @ApiModelProperty(name = "startDate", value = "放假时间开始")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "放假时间结束")
    private String endDate;

    @ApiModelProperty(name = "repayWorkDates", value = "补班日期，例['2019-04-28','2019-04-29']")
    private List<String> repayWorkDates;

    @ApiModelProperty(name = "attendanceGroupId", value = "关联考勤组，例[1,2]")
    private List<Long> attendanceGroupId;

    @ApiModelProperty(name = "deleteOrNot", value = "是否删除：1-删除，0-保留", hidden = true)
    private Integer deleteOrNot;

    @ApiModelProperty(name = "gmtCreate", value = "创建时间", hidden = true)
    private Date gmtCreate;

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

    public void setAttendanceGroupId(List<Long> attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncAttendanceHolidayResponse)) {
            return false;
        }
        TerminalSyncAttendanceHolidayResponse other = (TerminalSyncAttendanceHolidayResponse) o;
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
        Object this$attendanceGroupId = getAttendanceGroupId();
        Object other$attendanceGroupId = other.getAttendanceGroupId();
        if (this$attendanceGroupId == null) {
            if (other$attendanceGroupId != null) {
                return false;
            }
        } else if (!this$attendanceGroupId.equals(other$attendanceGroupId)) {
            return false;
        }
        Object this$deleteOrNot = getDeleteOrNot();
        Object other$deleteOrNot = other.getDeleteOrNot();
        if (this$deleteOrNot == null) {
            if (other$deleteOrNot != null) {
                return false;
            }
        } else if (!this$deleteOrNot.equals(other$deleteOrNot)) {
            return false;
        }
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        return this$gmtCreate == null ? other$gmtCreate == null : this$gmtCreate.equals(other$gmtCreate);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncAttendanceHolidayResponse;
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
        Object $attendanceGroupId = getAttendanceGroupId();
        int result6 = (result5 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $deleteOrNot = getDeleteOrNot();
        int result7 = (result6 * 59) + ($deleteOrNot == null ? 43 : $deleteOrNot.hashCode());
        Object $gmtCreate = getGmtCreate();
        return (result7 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
    }

    public String toString() {
        return "TerminalSyncAttendanceHolidayResponse(attendanceHolidayId=" + getAttendanceHolidayId() + ", holidayName=" + getHolidayName() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", repayWorkDates=" + getRepayWorkDates() + ", attendanceGroupId=" + getAttendanceGroupId() + ", deleteOrNot=" + getDeleteOrNot() + ", gmtCreate=" + getGmtCreate() + ")";
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

    public List<Long> getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }
}

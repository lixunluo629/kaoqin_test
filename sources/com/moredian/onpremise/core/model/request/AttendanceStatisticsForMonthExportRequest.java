package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "月报表考勤信息请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/AttendanceStatisticsForMonthExportRequest.class */
public class AttendanceStatisticsForMonthExportRequest extends BaseRequest {
    private static final long serialVersionUID = -7531442570198955032L;

    @ApiModelProperty(name = "startDay", value = "查询开始日期 ，格式:yyyyMMdd,例：20190606")
    private String startDay;

    @ApiModelProperty(name = "endDay", value = "查询结束日期 ，格式:yyyyMMdd,例：20190606")
    private String endDay;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "keywords", value = "成员名称或工号")
    private String keywords;

    @ApiModelProperty(name = "memberIds", value = "成员id列表", hidden = true)
    private List<Long> memberIds;

    @ApiModelProperty(name = "memberId", value = "成员id", hidden = true)
    private Long memberId;

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceStatisticsForMonthExportRequest)) {
            return false;
        }
        AttendanceStatisticsForMonthExportRequest other = (AttendanceStatisticsForMonthExportRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$startDay = getStartDay();
        Object other$startDay = other.getStartDay();
        if (this$startDay == null) {
            if (other$startDay != null) {
                return false;
            }
        } else if (!this$startDay.equals(other$startDay)) {
            return false;
        }
        Object this$endDay = getEndDay();
        Object other$endDay = other.getEndDay();
        if (this$endDay == null) {
            if (other$endDay != null) {
                return false;
            }
        } else if (!this$endDay.equals(other$endDay)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
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
        Object this$keywords = getKeywords();
        Object other$keywords = other.getKeywords();
        if (this$keywords == null) {
            if (other$keywords != null) {
                return false;
            }
        } else if (!this$keywords.equals(other$keywords)) {
            return false;
        }
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        if (this$memberIds == null) {
            if (other$memberIds != null) {
                return false;
            }
        } else if (!this$memberIds.equals(other$memberIds)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        return this$memberId == null ? other$memberId == null : this$memberId.equals(other$memberId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof AttendanceStatisticsForMonthExportRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $startDay = getStartDay();
        int result = (1 * 59) + ($startDay == null ? 43 : $startDay.hashCode());
        Object $endDay = getEndDay();
        int result2 = (result * 59) + ($endDay == null ? 43 : $endDay.hashCode());
        Object $deptId = getDeptId();
        int result3 = (result2 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result4 = (result3 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $keywords = getKeywords();
        int result5 = (result4 * 59) + ($keywords == null ? 43 : $keywords.hashCode());
        Object $memberIds = getMemberIds();
        int result6 = (result5 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $memberId = getMemberId();
        return (result6 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "AttendanceStatisticsForMonthExportRequest(startDay=" + getStartDay() + ", endDay=" + getEndDay() + ", deptId=" + getDeptId() + ", attendanceGroupId=" + getAttendanceGroupId() + ", keywords=" + getKeywords() + ", memberIds=" + getMemberIds() + ", memberId=" + getMemberId() + ")";
    }

    public String getStartDay() {
        return this.startDay;
    }

    public String getEndDay() {
        return this.endDay;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }

    public Long getMemberId() {
        return this.memberId;
    }
}

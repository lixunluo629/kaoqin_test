package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "考勤记录请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListAttendanceRecordRequest.class */
public class ListAttendanceRecordRequest extends BaseRequest {
    private static final long serialVersionUID = -5343599976199224506L;

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    @ApiModelProperty(name = "deptIds", value = "部门id集合")
    private List<Long> deptIds;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "name", value = "成员名称或成员工号")
    private String name;

    @ApiModelProperty(name = "queryDate", value = "日期，例：20191010", hidden = true)
    private Integer queryDate;

    @ApiModelProperty(name = "startDate", value = "开始日期")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "结束日期")
    private String endDate;

    @ApiModelProperty(name = "managerDeptIds", value = "部门id集合", hidden = true)
    private List<String> managerDeptIds;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQueryDate(Integer queryDate) {
        this.queryDate = queryDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setManagerDeptIds(List<String> managerDeptIds) {
        this.managerDeptIds = managerDeptIds;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListAttendanceRecordRequest)) {
            return false;
        }
        ListAttendanceRecordRequest other = (ListAttendanceRecordRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
            return false;
        }
        Object this$deptIds = getDeptIds();
        Object other$deptIds = other.getDeptIds();
        if (this$deptIds == null) {
            if (other$deptIds != null) {
                return false;
            }
        } else if (!this$deptIds.equals(other$deptIds)) {
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
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$queryDate = getQueryDate();
        Object other$queryDate = other.getQueryDate();
        if (this$queryDate == null) {
            if (other$queryDate != null) {
                return false;
            }
        } else if (!this$queryDate.equals(other$queryDate)) {
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
        Object this$managerDeptIds = getManagerDeptIds();
        Object other$managerDeptIds = other.getManagerDeptIds();
        if (this$managerDeptIds == null) {
            if (other$managerDeptIds != null) {
                return false;
            }
        } else if (!this$managerDeptIds.equals(other$managerDeptIds)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        return this$memberId == null ? other$memberId == null : this$memberId.equals(other$memberId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListAttendanceRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $deptIds = getDeptIds();
        int result2 = (result * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result3 = (result2 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $name = getName();
        int result4 = (result3 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $queryDate = getQueryDate();
        int result5 = (result4 * 59) + ($queryDate == null ? 43 : $queryDate.hashCode());
        Object $startDate = getStartDate();
        int result6 = (result5 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result7 = (result6 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $managerDeptIds = getManagerDeptIds();
        int result8 = (result7 * 59) + ($managerDeptIds == null ? 43 : $managerDeptIds.hashCode());
        Object $memberId = getMemberId();
        return (result8 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListAttendanceRecordRequest(paginator=" + getPaginator() + ", deptIds=" + getDeptIds() + ", attendanceGroupId=" + getAttendanceGroupId() + ", name=" + getName() + ", queryDate=" + getQueryDate() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", managerDeptIds=" + getManagerDeptIds() + ", memberId=" + getMemberId() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public List<Long> getDeptIds() {
        return this.deptIds;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getName() {
        return this.name;
    }

    public Integer getQueryDate() {
        return this.queryDate;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public List<String> getManagerDeptIds() {
        return this.managerDeptIds;
    }

    public Long getMemberId() {
        return this.memberId;
    }
}

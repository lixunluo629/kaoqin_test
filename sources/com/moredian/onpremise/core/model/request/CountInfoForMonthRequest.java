package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "每月统计考勤信息请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CountInfoForMonthRequest.class */
public class CountInfoForMonthRequest extends BaseRequest {
    private static final long serialVersionUID = -2741589501325565241L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "deptIdLists", value = "部门id集合")
    private List<Long> deptIdLists;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "name", value = "成员名称或工号")
    private String name;

    @ApiModelProperty(name = "queryStartDate", value = "查询日期 ，格式:yyyy-MM-dd,例：2019-06-01")
    private String queryStartDate;

    @ApiModelProperty(name = "queryEndDate", value = "查询日期 ，格式:yyyy-MM-dd,例：2019-06-30")
    private String queryEndDate;

    @ApiModelProperty(name = "deptIds", value = "部门id列表", hidden = true)
    private List<String> deptIds;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setDeptIdLists(List<Long> deptIdLists) {
        this.deptIdLists = deptIdLists;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQueryStartDate(String queryStartDate) {
        this.queryStartDate = queryStartDate;
    }

    public void setQueryEndDate(String queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    public void setDeptIds(List<String> deptIds) {
        this.deptIds = deptIds;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountInfoForMonthRequest)) {
            return false;
        }
        CountInfoForMonthRequest other = (CountInfoForMonthRequest) o;
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
        Object this$deptIdLists = getDeptIdLists();
        Object other$deptIdLists = other.getDeptIdLists();
        if (this$deptIdLists == null) {
            if (other$deptIdLists != null) {
                return false;
            }
        } else if (!this$deptIdLists.equals(other$deptIdLists)) {
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
        Object this$queryStartDate = getQueryStartDate();
        Object other$queryStartDate = other.getQueryStartDate();
        if (this$queryStartDate == null) {
            if (other$queryStartDate != null) {
                return false;
            }
        } else if (!this$queryStartDate.equals(other$queryStartDate)) {
            return false;
        }
        Object this$queryEndDate = getQueryEndDate();
        Object other$queryEndDate = other.getQueryEndDate();
        if (this$queryEndDate == null) {
            if (other$queryEndDate != null) {
                return false;
            }
        } else if (!this$queryEndDate.equals(other$queryEndDate)) {
            return false;
        }
        Object this$deptIds = getDeptIds();
        Object other$deptIds = other.getDeptIds();
        return this$deptIds == null ? other$deptIds == null : this$deptIds.equals(other$deptIds);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CountInfoForMonthRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $deptIdLists = getDeptIdLists();
        int result2 = (result * 59) + ($deptIdLists == null ? 43 : $deptIdLists.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result3 = (result2 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $name = getName();
        int result4 = (result3 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $queryStartDate = getQueryStartDate();
        int result5 = (result4 * 59) + ($queryStartDate == null ? 43 : $queryStartDate.hashCode());
        Object $queryEndDate = getQueryEndDate();
        int result6 = (result5 * 59) + ($queryEndDate == null ? 43 : $queryEndDate.hashCode());
        Object $deptIds = getDeptIds();
        return (result6 * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CountInfoForMonthRequest(paginator=" + getPaginator() + ", deptIdLists=" + getDeptIdLists() + ", attendanceGroupId=" + getAttendanceGroupId() + ", name=" + getName() + ", queryStartDate=" + getQueryStartDate() + ", queryEndDate=" + getQueryEndDate() + ", deptIds=" + getDeptIds() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public List<Long> getDeptIdLists() {
        return this.deptIdLists;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getName() {
        return this.name;
    }

    public String getQueryStartDate() {
        return this.queryStartDate;
    }

    public String getQueryEndDate() {
        return this.queryEndDate;
    }

    public List<String> getDeptIds() {
        return this.deptIds;
    }
}

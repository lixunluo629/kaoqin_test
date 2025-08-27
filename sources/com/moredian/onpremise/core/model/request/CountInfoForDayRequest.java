package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "每日统计考勤信息请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CountInfoForDayRequest.class */
public class CountInfoForDayRequest extends BaseRequest {
    private static final long serialVersionUID = -2741589501325565241L;

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "deptIdLists", value = "部门id列表")
    private List<Long> deptIdLists;

    @ApiModelProperty(name = "queryDate", value = "查询日期 ，格式:yyyy-MM-dd,例：2019-06-06")
    private String queryDate;

    @ApiModelProperty(name = "deptIds", value = "部门id列表", hidden = true)
    private List<String> deptIds;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "name", value = "成员名称或工号", hidden = true)
    private String name;

    @ApiModelProperty(name = "memberIds", value = "成员id列表", hidden = true)
    private List<Long> memberIds;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setDeptIdLists(List<Long> deptIdLists) {
        this.deptIdLists = deptIdLists;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public void setDeptIds(List<String> deptIds) {
        this.deptIds = deptIds;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountInfoForDayRequest)) {
            return false;
        }
        CountInfoForDayRequest other = (CountInfoForDayRequest) o;
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
        Object this$queryDate = getQueryDate();
        Object other$queryDate = other.getQueryDate();
        if (this$queryDate == null) {
            if (other$queryDate != null) {
                return false;
            }
        } else if (!this$queryDate.equals(other$queryDate)) {
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
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        return this$memberIds == null ? other$memberIds == null : this$memberIds.equals(other$memberIds);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CountInfoForDayRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $deptIdLists = getDeptIdLists();
        int result2 = (result * 59) + ($deptIdLists == null ? 43 : $deptIdLists.hashCode());
        Object $queryDate = getQueryDate();
        int result3 = (result2 * 59) + ($queryDate == null ? 43 : $queryDate.hashCode());
        Object $deptIds = getDeptIds();
        int result4 = (result3 * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $deptId = getDeptId();
        int result5 = (result4 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result6 = (result5 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $name = getName();
        int result7 = (result6 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $memberIds = getMemberIds();
        return (result7 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CountInfoForDayRequest(paginator=" + getPaginator() + ", deptIdLists=" + getDeptIdLists() + ", queryDate=" + getQueryDate() + ", deptIds=" + getDeptIds() + ", deptId=" + getDeptId() + ", attendanceGroupId=" + getAttendanceGroupId() + ", name=" + getName() + ", memberIds=" + getMemberIds() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public List<Long> getDeptIdLists() {
        return this.deptIdLists;
    }

    public String getQueryDate() {
        return this.queryDate;
    }

    public List<String> getDeptIds() {
        return this.deptIds;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getName() {
        return this.name;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }
}

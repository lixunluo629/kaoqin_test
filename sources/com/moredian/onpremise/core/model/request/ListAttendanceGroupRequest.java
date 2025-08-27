package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "考勤组列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListAttendanceGroupRequest.class */
public class ListAttendanceGroupRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "groupName", value = "考勤组名称")
    private String groupName;

    @ApiModelProperty(name = "groupType", value = "考勤组类型：1-固定排班，2-手动排班，3-自由打卡")
    private Integer groupType;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListAttendanceGroupRequest)) {
            return false;
        }
        ListAttendanceGroupRequest other = (ListAttendanceGroupRequest) o;
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
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        if (this$groupName == null) {
            if (other$groupName != null) {
                return false;
            }
        } else if (!this$groupName.equals(other$groupName)) {
            return false;
        }
        Object this$groupType = getGroupType();
        Object other$groupType = other.getGroupType();
        return this$groupType == null ? other$groupType == null : this$groupType.equals(other$groupType);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListAttendanceGroupRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $groupName = getGroupName();
        int result2 = (result * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $groupType = getGroupType();
        return (result2 * 59) + ($groupType == null ? 43 : $groupType.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListAttendanceGroupRequest(paginator=" + getPaginator() + ", groupName=" + getGroupName() + ", groupType=" + getGroupType() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Integer getGroupType() {
        return this.groupType;
    }
}

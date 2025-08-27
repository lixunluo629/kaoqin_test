package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "检查人员是否已经分配考勤组请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CheckMemberHasAttendanceGroupRequest.class */
public class CheckMemberHasAttendanceGroupRequest extends BaseRequest {

    @ApiModelProperty(name = "memberIds", value = "查询成员id列表")
    private List<Long> memberIds;

    @ApiModelProperty(name = "deptIds", value = "查询部门id列表")
    private List<Long> deptIds;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id，新增时不传，修改时必填，")
    private Long attendanceGroupId;

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckMemberHasAttendanceGroupRequest)) {
            return false;
        }
        CheckMemberHasAttendanceGroupRequest other = (CheckMemberHasAttendanceGroupRequest) o;
        if (!other.canEqual(this)) {
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
        return this$attendanceGroupId == null ? other$attendanceGroupId == null : this$attendanceGroupId.equals(other$attendanceGroupId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CheckMemberHasAttendanceGroupRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberIds = getMemberIds();
        int result = (1 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $deptIds = getDeptIds();
        int result2 = (result * 59) + ($deptIds == null ? 43 : $deptIds.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        return (result2 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CheckMemberHasAttendanceGroupRequest(memberIds=" + getMemberIds() + ", deptIds=" + getDeptIds() + ", attendanceGroupId=" + getAttendanceGroupId() + ")";
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }

    public List<Long> getDeptIds() {
        return this.deptIds;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }
}

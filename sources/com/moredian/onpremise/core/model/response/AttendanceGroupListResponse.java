package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "考勤组列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AttendanceGroupListResponse.class */
public class AttendanceGroupListResponse implements Serializable {

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "groupName", value = "考勤组名称")
    private String groupName;

    @ApiModelProperty(name = "memberNums", value = "考勤组人员数")
    private Integer memberNums;

    @ApiModelProperty(name = "groupType", value = "考勤组类型：1-固定排班，2-手动排班，3-自由打卡")
    private Integer groupType;

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMemberNums(Integer memberNums) {
        this.memberNums = memberNums;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceGroupListResponse)) {
            return false;
        }
        AttendanceGroupListResponse other = (AttendanceGroupListResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        if (this$groupName == null) {
            if (other$groupName != null) {
                return false;
            }
        } else if (!this$groupName.equals(other$groupName)) {
            return false;
        }
        Object this$memberNums = getMemberNums();
        Object other$memberNums = other.getMemberNums();
        if (this$memberNums == null) {
            if (other$memberNums != null) {
                return false;
            }
        } else if (!this$memberNums.equals(other$memberNums)) {
            return false;
        }
        Object this$groupType = getGroupType();
        Object other$groupType = other.getGroupType();
        return this$groupType == null ? other$groupType == null : this$groupType.equals(other$groupType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceGroupListResponse;
    }

    public int hashCode() {
        Object $attendanceGroupId = getAttendanceGroupId();
        int result = (1 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $groupName = getGroupName();
        int result2 = (result * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $memberNums = getMemberNums();
        int result3 = (result2 * 59) + ($memberNums == null ? 43 : $memberNums.hashCode());
        Object $groupType = getGroupType();
        return (result3 * 59) + ($groupType == null ? 43 : $groupType.hashCode());
    }

    public String toString() {
        return "AttendanceGroupListResponse(attendanceGroupId=" + getAttendanceGroupId() + ", groupName=" + getGroupName() + ", memberNums=" + getMemberNums() + ", groupType=" + getGroupType() + ")";
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Integer getMemberNums() {
        return this.memberNums;
    }

    public Integer getGroupType() {
        return this.groupType;
    }
}

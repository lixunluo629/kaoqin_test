package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/AttendanceGroupMemberInfo.class */
public class AttendanceGroupMemberInfo implements Serializable {

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "attendanceGroupId", value = "考勤组id")
    private Long attendanceGroupId;

    @ApiModelProperty(name = "attendanceGroupName", value = "考勤组名称")
    private String attendanceGroupName;

    @ApiModelProperty(name = "type", value = "考勤组成员类型：1-部门，2-成员")
    private Integer type;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "groupType", value = "考勤组类型：1-固定排班，2-手动排班，3-自由打卡")
    private Integer groupType;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setAttendanceGroupId(Long attendanceGroupId) {
        this.attendanceGroupId = attendanceGroupId;
    }

    public void setAttendanceGroupName(String attendanceGroupName) {
        this.attendanceGroupName = attendanceGroupName;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttendanceGroupMemberInfo)) {
            return false;
        }
        AttendanceGroupMemberInfo other = (AttendanceGroupMemberInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
            return false;
        }
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
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
        Object this$attendanceGroupName = getAttendanceGroupName();
        Object other$attendanceGroupName = other.getAttendanceGroupName();
        if (this$attendanceGroupName == null) {
            if (other$attendanceGroupName != null) {
                return false;
            }
        } else if (!this$attendanceGroupName.equals(other$attendanceGroupName)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
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
        Object this$deptName = getDeptName();
        Object other$deptName = other.getDeptName();
        if (this$deptName == null) {
            if (other$deptName != null) {
                return false;
            }
        } else if (!this$deptName.equals(other$deptName)) {
            return false;
        }
        Object this$groupType = getGroupType();
        Object other$groupType = other.getGroupType();
        return this$groupType == null ? other$groupType == null : this$groupType.equals(other$groupType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AttendanceGroupMemberInfo;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $attendanceGroupId = getAttendanceGroupId();
        int result3 = (result2 * 59) + ($attendanceGroupId == null ? 43 : $attendanceGroupId.hashCode());
        Object $attendanceGroupName = getAttendanceGroupName();
        int result4 = (result3 * 59) + ($attendanceGroupName == null ? 43 : $attendanceGroupName.hashCode());
        Object $type = getType();
        int result5 = (result4 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $deptId = getDeptId();
        int result6 = (result5 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result7 = (result6 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $groupType = getGroupType();
        return (result7 * 59) + ($groupType == null ? 43 : $groupType.hashCode());
    }

    public String toString() {
        return "AttendanceGroupMemberInfo(memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", attendanceGroupId=" + getAttendanceGroupId() + ", attendanceGroupName=" + getAttendanceGroupName() + ", type=" + getType() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", groupType=" + getGroupType() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Long getAttendanceGroupId() {
        return this.attendanceGroupId;
    }

    public String getAttendanceGroupName() {
        return this.attendanceGroupName;
    }

    public Integer getType() {
        return this.type;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Integer getGroupType() {
        return this.groupType;
    }
}

package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步权限组成员关系信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalGroupMemberSyncResponse.class */
public class TerminalGroupMemberSyncResponse implements Serializable {

    @ApiModelProperty(name = "groupId", value = "权限组id", hidden = true)
    private Long groupId;

    @ApiModelProperty(name = "type", value = "权限组成员类型：1-部门，2-成员")
    private Integer type;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "memberId", value = "memberId")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalGroupMemberSyncResponse)) {
            return false;
        }
        TerminalGroupMemberSyncResponse other = (TerminalGroupMemberSyncResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupId = getGroupId();
        Object other$groupId = other.getGroupId();
        if (this$groupId == null) {
            if (other$groupId != null) {
                return false;
            }
        } else if (!this$groupId.equals(other$groupId)) {
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
        return this$memberName == null ? other$memberName == null : this$memberName.equals(other$memberName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalGroupMemberSyncResponse;
    }

    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $type = getType();
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $deptId = getDeptId();
        int result3 = (result2 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result4 = (result3 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $memberId = getMemberId();
        int result5 = (result4 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        return (result5 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
    }

    public String toString() {
        return "TerminalGroupMemberSyncResponse(groupId=" + getGroupId() + ", type=" + getType() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
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

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }
}

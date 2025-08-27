package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步权限组关联成员响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncGroupMemberV2Response.class */
public class TerminalSyncGroupMemberV2Response implements Serializable {
    private static final long serialVersionUID = 404159526221793094L;

    @ApiModelProperty(name = "groupId", value = "权限组id")
    private Long groupId;

    @ApiModelProperty(name = "type", value = "权限组成员类型：1-部门，2-成员")
    private Integer type;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncGroupMemberV2Response)) {
            return false;
        }
        TerminalSyncGroupMemberV2Response other = (TerminalSyncGroupMemberV2Response) o;
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        return this$deptId == null ? other$deptId == null : this$deptId.equals(other$deptId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncGroupMemberV2Response;
    }

    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $type = getType();
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $memberId = getMemberId();
        int result3 = (result2 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $deptId = getDeptId();
        return (result3 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
    }

    public String toString() {
        return "TerminalSyncGroupMemberV2Response(groupId=" + getGroupId() + ", type=" + getType() + ", memberId=" + getMemberId() + ", deptId=" + getDeptId() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public Integer getType() {
        return this.type;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getDeptId() {
        return this.deptId;
    }
}

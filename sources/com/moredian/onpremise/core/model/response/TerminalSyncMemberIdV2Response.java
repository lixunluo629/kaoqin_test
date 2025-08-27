package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步成员id和部门id关系表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncMemberIdV2Response.class */
public class TerminalSyncMemberIdV2Response implements Serializable {
    private static final long serialVersionUID = 7725203202355184168L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long id;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncMemberIdV2Response)) {
            return false;
        }
        TerminalSyncMemberIdV2Response other = (TerminalSyncMemberIdV2Response) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        return this$deptId == null ? other$deptId == null : this$deptId.equals(other$deptId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncMemberIdV2Response;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $deptId = getDeptId();
        return (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
    }

    public String toString() {
        return "TerminalSyncMemberIdV2Response(id=" + getId() + ", deptId=" + getDeptId() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public Long getDeptId() {
        return this.deptId;
    }
}

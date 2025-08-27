package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "同步部门")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncDeptV2Response.class */
public class TerminalSyncDeptV2Response implements Serializable {
    private static final long serialVersionUID = 4081574597842939646L;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Integer deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "superDeptId", value = "上级部门id")
    private Integer superDeptId;

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setSuperDeptId(Integer superDeptId) {
        this.superDeptId = superDeptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncDeptV2Response)) {
            return false;
        }
        TerminalSyncDeptV2Response other = (TerminalSyncDeptV2Response) o;
        if (!other.canEqual(this)) {
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
        Object this$superDeptId = getSuperDeptId();
        Object other$superDeptId = other.getSuperDeptId();
        return this$superDeptId == null ? other$superDeptId == null : this$superDeptId.equals(other$superDeptId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncDeptV2Response;
    }

    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result2 = (result * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $superDeptId = getSuperDeptId();
        return (result2 * 59) + ($superDeptId == null ? 43 : $superDeptId.hashCode());
    }

    public String toString() {
        return "TerminalSyncDeptV2Response(deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", superDeptId=" + getSuperDeptId() + ")";
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Integer getSuperDeptId() {
        return this.superDeptId;
    }
}

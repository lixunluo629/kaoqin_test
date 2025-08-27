package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "部门信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/MemberDetailsDeptResponse.class */
public class MemberDetailsDeptResponse implements Serializable {

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberDetailsDeptResponse)) {
            return false;
        }
        MemberDetailsDeptResponse other = (MemberDetailsDeptResponse) o;
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
        return this$deptName == null ? other$deptName == null : this$deptName.equals(other$deptName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MemberDetailsDeptResponse;
    }

    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        return (result * 59) + ($deptName == null ? 43 : $deptName.hashCode());
    }

    public String toString() {
        return "MemberDetailsDeptResponse(deptId=" + getDeptId() + ", deptName=" + getDeptName() + ")";
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }
}

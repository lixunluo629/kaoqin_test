package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "不包含组织架构部门列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListDeptNoConstructureResponse.class */
public class ListDeptNoConstructureResponse implements Serializable {

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListDeptNoConstructureResponse)) {
            return false;
        }
        ListDeptNoConstructureResponse other = (ListDeptNoConstructureResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        return this$deptId == null ? other$deptId == null : this$deptId.equals(other$deptId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListDeptNoConstructureResponse;
    }

    public int hashCode() {
        Object $deptName = getDeptName();
        int result = (1 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $deptId = getDeptId();
        return (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
    }

    public String toString() {
        return "ListDeptNoConstructureResponse(deptName=" + getDeptName() + ", deptId=" + getDeptId() + ")";
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Long getDeptId() {
        return this.deptId;
    }
}

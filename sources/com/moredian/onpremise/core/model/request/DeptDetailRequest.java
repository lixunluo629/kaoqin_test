package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "获取部门详情参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeptDetailRequest.class */
public class DeptDetailRequest extends BaseRequest {

    @ApiModelProperty(name = "deptId", value = "部门id", required = true)
    private Long deptId;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeptDetailRequest)) {
            return false;
        }
        DeptDetailRequest other = (DeptDetailRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        return this$deptId == null ? other$deptId == null : this$deptId.equals(other$deptId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeptDetailRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeptDetailRequest(deptId=" + getDeptId() + ")";
    }

    public Long getDeptId() {
        return this.deptId;
    }
}

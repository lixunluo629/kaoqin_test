package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "获取直属子部门列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListChildImmediateDeptRequest.class */
public class ListChildImmediateDeptRequest extends BaseRequest {

    @ApiModelProperty(name = "superDeptId", value = "需要查询的部门id")
    private Long deptId;

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListChildImmediateDeptRequest)) {
            return false;
        }
        ListChildImmediateDeptRequest other = (ListChildImmediateDeptRequest) o;
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
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        return this$paginator == null ? other$paginator == null : this$paginator.equals(other$paginator);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListChildImmediateDeptRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deptId = getDeptId();
        int result = (1 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $paginator = getPaginator();
        return (result * 59) + ($paginator == null ? 43 : $paginator.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListChildImmediateDeptRequest(deptId=" + getDeptId() + ", paginator=" + getPaginator() + ")";
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public Paginator getPaginator() {
        return this.paginator;
    }
}

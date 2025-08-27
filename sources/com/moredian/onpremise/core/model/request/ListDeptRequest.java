package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查询含部门结构的列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListDeptRequest.class */
public class ListDeptRequest extends BaseRequest {

    @ApiModelProperty(name = "needMember", value = "是否需要查询部门下成员列表：1-根部门需要，2-不需要", hidden = true)
    private Integer needMember;

    public void setNeedMember(Integer needMember) {
        this.needMember = needMember;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListDeptRequest)) {
            return false;
        }
        ListDeptRequest other = (ListDeptRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$needMember = getNeedMember();
        Object other$needMember = other.getNeedMember();
        return this$needMember == null ? other$needMember == null : this$needMember.equals(other$needMember);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListDeptRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $needMember = getNeedMember();
        int result = (1 * 59) + ($needMember == null ? 43 : $needMember.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListDeptRequest(needMember=" + getNeedMember() + ")";
    }

    public Integer getNeedMember() {
        return this.needMember;
    }
}

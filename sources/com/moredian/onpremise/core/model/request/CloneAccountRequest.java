package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "克隆账户信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CloneAccountRequest.class */
public class CloneAccountRequest extends BaseRequest {
    private static final long serialVersionUID = -1686994869271469601L;

    @ApiModelProperty(name = "cloneAccountId", value = "克隆账户id")
    private Long cloneAccountId;

    public void setCloneAccountId(Long cloneAccountId) {
        this.cloneAccountId = cloneAccountId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CloneAccountRequest)) {
            return false;
        }
        CloneAccountRequest other = (CloneAccountRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$cloneAccountId = getCloneAccountId();
        Object other$cloneAccountId = other.getCloneAccountId();
        return this$cloneAccountId == null ? other$cloneAccountId == null : this$cloneAccountId.equals(other$cloneAccountId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CloneAccountRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $cloneAccountId = getCloneAccountId();
        int result = (1 * 59) + ($cloneAccountId == null ? 43 : $cloneAccountId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CloneAccountRequest(cloneAccountId=" + getCloneAccountId() + ")";
    }

    public Long getCloneAccountId() {
        return this.cloneAccountId;
    }
}

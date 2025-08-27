package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "解绑就餐卡请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UnbindMealCardRequest.class */
public class UnbindMealCardRequest extends BaseRequest {
    private static final long serialVersionUID = 2524406546398535147L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UnbindMealCardRequest)) {
            return false;
        }
        UnbindMealCardRequest other = (UnbindMealCardRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        return this$memberId == null ? other$memberId == null : this$memberId.equals(other$memberId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UnbindMealCardRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UnbindMealCardRequest(memberId=" + getMemberId() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }
}

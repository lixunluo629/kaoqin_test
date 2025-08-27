package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "绑定就餐卡请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BindMealCardRequest.class */
public class BindMealCardRequest extends BaseRequest {
    private static final long serialVersionUID = -7963383442122406673L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "mealCardId", value = "就餐卡id")
    private Long mealCardId;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BindMealCardRequest)) {
            return false;
        }
        BindMealCardRequest other = (BindMealCardRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$mealCardId = getMealCardId();
        Object other$mealCardId = other.getMealCardId();
        return this$mealCardId == null ? other$mealCardId == null : this$mealCardId.equals(other$mealCardId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof BindMealCardRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $mealCardId = getMealCardId();
        return (result * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "BindMealCardRequest(memberId=" + getMemberId() + ", mealCardId=" + getMealCardId() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }
}

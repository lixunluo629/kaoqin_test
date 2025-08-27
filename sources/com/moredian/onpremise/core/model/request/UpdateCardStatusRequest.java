package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "修改卡状态接口")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UpdateCardStatusRequest.class */
public class UpdateCardStatusRequest extends BaseRequest {
    private static final long serialVersionUID = -6965905932390321866L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "cardStatus", value = "卡状态：1-启用，2-禁用")
    private Integer cardStatus;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateCardStatusRequest)) {
            return false;
        }
        UpdateCardStatusRequest other = (UpdateCardStatusRequest) o;
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
        Object this$cardStatus = getCardStatus();
        Object other$cardStatus = other.getCardStatus();
        return this$cardStatus == null ? other$cardStatus == null : this$cardStatus.equals(other$cardStatus);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UpdateCardStatusRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $cardStatus = getCardStatus();
        return (result * 59) + ($cardStatus == null ? 43 : $cardStatus.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UpdateCardStatusRequest(memberId=" + getMemberId() + ", cardStatus=" + getCardStatus() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Integer getCardStatus() {
        return this.cardStatus;
    }
}

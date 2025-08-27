package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "批量修改卡状态接口")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BatchUpdateCardStatusRequest.class */
public class BatchUpdateCardStatusRequest extends BaseRequest {
    private static final long serialVersionUID = -7513361066358542112L;

    @ApiModelProperty(name = "memberIds", value = "操作成员id列表")
    private List<Long> memberIds;

    @ApiModelProperty(name = "mealCardId", value = "就餐卡id，传0代表解绑卡，不传或者传null则不执行绑定解绑操作")
    private Long mealCardId;

    @ApiModelProperty(name = "cardStatus", value = "卡状态：1-启用，2-禁用")
    private Integer cardStatus;

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchUpdateCardStatusRequest)) {
            return false;
        }
        BatchUpdateCardStatusRequest other = (BatchUpdateCardStatusRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        if (this$memberIds == null) {
            if (other$memberIds != null) {
                return false;
            }
        } else if (!this$memberIds.equals(other$memberIds)) {
            return false;
        }
        Object this$mealCardId = getMealCardId();
        Object other$mealCardId = other.getMealCardId();
        if (this$mealCardId == null) {
            if (other$mealCardId != null) {
                return false;
            }
        } else if (!this$mealCardId.equals(other$mealCardId)) {
            return false;
        }
        Object this$cardStatus = getCardStatus();
        Object other$cardStatus = other.getCardStatus();
        return this$cardStatus == null ? other$cardStatus == null : this$cardStatus.equals(other$cardStatus);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof BatchUpdateCardStatusRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberIds = getMemberIds();
        int result = (1 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $mealCardId = getMealCardId();
        int result2 = (result * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        Object $cardStatus = getCardStatus();
        return (result2 * 59) + ($cardStatus == null ? 43 : $cardStatus.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "BatchUpdateCardStatusRequest(memberIds=" + getMemberIds() + ", mealCardId=" + getMealCardId() + ", cardStatus=" + getCardStatus() + ")";
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }

    public Integer getCardStatus() {
        return this.cardStatus;
    }
}

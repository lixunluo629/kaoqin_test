package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "检查人员是否已经分配就餐卡响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckMemberHasCardResponse.class */
public class CheckMemberHasCardResponse implements Serializable {
    private static final long serialVersionUID = -6369002336666662270L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "mealCardId", value = "就餐卡id")
    private Long mealCardId;

    @ApiModelProperty(name = "cardName", value = "就餐卡名称")
    private String cardName;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckMemberHasCardResponse)) {
            return false;
        }
        CheckMemberHasCardResponse other = (CheckMemberHasCardResponse) o;
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
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
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
        Object this$cardName = getCardName();
        Object other$cardName = other.getCardName();
        return this$cardName == null ? other$cardName == null : this$cardName.equals(other$cardName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckMemberHasCardResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $mealCardId = getMealCardId();
        int result3 = (result2 * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        Object $cardName = getCardName();
        return (result3 * 59) + ($cardName == null ? 43 : $cardName.hashCode());
    }

    public String toString() {
        return "CheckMemberHasCardResponse(memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", mealCardId=" + getMealCardId() + ", cardName=" + getCardName() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }

    public String getCardName() {
        return this.cardName;
    }
}

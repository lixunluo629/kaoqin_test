package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "根据人员id查询与卡绑定信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/MealCardMemberInfo.class */
public class MealCardMemberInfo implements Serializable {

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "mealCardId", value = "就餐卡id")
    private Long mealCardId;

    @ApiModelProperty(name = "cardName", value = "就餐卡名称")
    private String cardName;

    @ApiModelProperty(name = "cardType", value = "卡类型")
    private Integer cardType;

    @ApiModelProperty(name = "cardStatus", value = "卡状态：1-使用中，2-已禁用，3-无效卡")
    private Integer cardStatus;

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

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MealCardMemberInfo)) {
            return false;
        }
        MealCardMemberInfo other = (MealCardMemberInfo) o;
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
        if (this$cardName == null) {
            if (other$cardName != null) {
                return false;
            }
        } else if (!this$cardName.equals(other$cardName)) {
            return false;
        }
        Object this$cardType = getCardType();
        Object other$cardType = other.getCardType();
        if (this$cardType == null) {
            if (other$cardType != null) {
                return false;
            }
        } else if (!this$cardType.equals(other$cardType)) {
            return false;
        }
        Object this$cardStatus = getCardStatus();
        Object other$cardStatus = other.getCardStatus();
        return this$cardStatus == null ? other$cardStatus == null : this$cardStatus.equals(other$cardStatus);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MealCardMemberInfo;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $mealCardId = getMealCardId();
        int result3 = (result2 * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        Object $cardName = getCardName();
        int result4 = (result3 * 59) + ($cardName == null ? 43 : $cardName.hashCode());
        Object $cardType = getCardType();
        int result5 = (result4 * 59) + ($cardType == null ? 43 : $cardType.hashCode());
        Object $cardStatus = getCardStatus();
        return (result5 * 59) + ($cardStatus == null ? 43 : $cardStatus.hashCode());
    }

    public String toString() {
        return "MealCardMemberInfo(memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", mealCardId=" + getMealCardId() + ", cardName=" + getCardName() + ", cardType=" + getCardType() + ", cardStatus=" + getCardStatus() + ")";
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

    public Integer getCardType() {
        return this.cardType;
    }

    public Integer getCardStatus() {
        return this.cardStatus;
    }
}

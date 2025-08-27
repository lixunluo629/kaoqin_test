package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "团餐卡列表信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListSearchMealCardResponse.class */
public class ListSearchMealCardResponse implements Serializable {

    @ApiModelProperty(name = "mealCardId", value = "id")
    private Long mealCardId;

    @ApiModelProperty(name = "cardName", value = "卡名称")
    private String cardName;

    @ApiModelProperty(name = "cardType", value = "卡类型：1-员工卡，2-值班卡，3-vip卡，4-临时卡")
    private Integer cardType;

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListSearchMealCardResponse)) {
            return false;
        }
        ListSearchMealCardResponse other = (ListSearchMealCardResponse) o;
        if (!other.canEqual(this)) {
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
        return this$cardType == null ? other$cardType == null : this$cardType.equals(other$cardType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListSearchMealCardResponse;
    }

    public int hashCode() {
        Object $mealCardId = getMealCardId();
        int result = (1 * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        Object $cardName = getCardName();
        int result2 = (result * 59) + ($cardName == null ? 43 : $cardName.hashCode());
        Object $cardType = getCardType();
        return (result2 * 59) + ($cardType == null ? 43 : $cardType.hashCode());
    }

    public String toString() {
        return "ListSearchMealCardResponse(mealCardId=" + getMealCardId() + ", cardName=" + getCardName() + ", cardType=" + getCardType() + ")";
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
}

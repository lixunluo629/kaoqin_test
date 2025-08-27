package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.MemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "成员团餐卡发放请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SendCardToMemberRequest.class */
public class SendCardToMemberRequest extends BaseRequest {
    private static final long serialVersionUID = -6101623180197142226L;

    @ApiModelProperty(name = "mealCardId", value = "id")
    private Long mealCardId;

    @ApiModelProperty(name = "cardMembers", value = "卡关联人员")
    private List<MemberDto> cardMembers;

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    public void setCardMembers(List<MemberDto> cardMembers) {
        this.cardMembers = cardMembers;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SendCardToMemberRequest)) {
            return false;
        }
        SendCardToMemberRequest other = (SendCardToMemberRequest) o;
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
        Object this$cardMembers = getCardMembers();
        Object other$cardMembers = other.getCardMembers();
        return this$cardMembers == null ? other$cardMembers == null : this$cardMembers.equals(other$cardMembers);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SendCardToMemberRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $mealCardId = getMealCardId();
        int result = (1 * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        Object $cardMembers = getCardMembers();
        return (result * 59) + ($cardMembers == null ? 43 : $cardMembers.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SendCardToMemberRequest(mealCardId=" + getMealCardId() + ", cardMembers=" + getCardMembers() + ")";
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }

    public List<MemberDto> getCardMembers() {
        return this.cardMembers;
    }
}

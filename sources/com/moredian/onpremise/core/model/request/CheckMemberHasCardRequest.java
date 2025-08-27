package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.MemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "检查人员是否已经分配就餐卡请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CheckMemberHasCardRequest.class */
public class CheckMemberHasCardRequest extends BaseRequest {
    private static final long serialVersionUID = 2604716027283560798L;

    @ApiModelProperty(name = "memberIds", value = "查询成员id列表")
    private List<MemberDto> memberIds;

    @ApiModelProperty(name = "mealCardId", value = "就餐卡id，新增时不传，修改时必填，")
    private Long mealCardId;

    public void setMemberIds(List<MemberDto> memberIds) {
        this.memberIds = memberIds;
    }

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckMemberHasCardRequest)) {
            return false;
        }
        CheckMemberHasCardRequest other = (CheckMemberHasCardRequest) o;
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
        return this$mealCardId == null ? other$mealCardId == null : this$mealCardId.equals(other$mealCardId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CheckMemberHasCardRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberIds = getMemberIds();
        int result = (1 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $mealCardId = getMealCardId();
        return (result * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CheckMemberHasCardRequest(memberIds=" + getMemberIds() + ", mealCardId=" + getMealCardId() + ")";
    }

    public List<MemberDto> getMemberIds() {
        return this.memberIds;
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }
}

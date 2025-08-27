package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "获取成员详情请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/MemberDetailsRequest.class */
public class MemberDetailsRequest extends BaseRequest {

    @ApiModelProperty(name = "memberId", value = "成员id", required = true)
    private Long memberId;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberDetailsRequest)) {
            return false;
        }
        MemberDetailsRequest other = (MemberDetailsRequest) o;
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
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        return this$memberJobNum == null ? other$memberJobNum == null : this$memberJobNum.equals(other$memberJobNum);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof MemberDetailsRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberJobNum = getMemberJobNum();
        return (result * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "MemberDetailsRequest(memberId=" + getMemberId() + ", memberJobNum=" + getMemberJobNum() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }
}

package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "成员更改人脸记录列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/MemberInputFaceRecordListResponse.class */
public class MemberInputFaceRecordListResponse implements Serializable {
    private static final long serialVersionUID = -4190536019709353139L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @ApiModelProperty(name = "memberName", value = "成员身份证    ")
    private String identityCard;

    @ApiModelProperty(name = "newVerifyFaceUrl", value = "新成员底库照片")
    private String newVerifyFaceUrl;

    @ApiModelProperty(name = "gmtCreate", value = "采集时间")
    private Date gmtCreate;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setNewVerifyFaceUrl(String newVerifyFaceUrl) {
        this.newVerifyFaceUrl = newVerifyFaceUrl;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberInputFaceRecordListResponse)) {
            return false;
        }
        MemberInputFaceRecordListResponse other = (MemberInputFaceRecordListResponse) o;
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
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
            return false;
        }
        Object this$identityCard = getIdentityCard();
        Object other$identityCard = other.getIdentityCard();
        if (this$identityCard == null) {
            if (other$identityCard != null) {
                return false;
            }
        } else if (!this$identityCard.equals(other$identityCard)) {
            return false;
        }
        Object this$newVerifyFaceUrl = getNewVerifyFaceUrl();
        Object other$newVerifyFaceUrl = other.getNewVerifyFaceUrl();
        if (this$newVerifyFaceUrl == null) {
            if (other$newVerifyFaceUrl != null) {
                return false;
            }
        } else if (!this$newVerifyFaceUrl.equals(other$newVerifyFaceUrl)) {
            return false;
        }
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        return this$gmtCreate == null ? other$gmtCreate == null : this$gmtCreate.equals(other$gmtCreate);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MemberInputFaceRecordListResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result3 = (result2 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $identityCard = getIdentityCard();
        int result4 = (result3 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
        Object $newVerifyFaceUrl = getNewVerifyFaceUrl();
        int result5 = (result4 * 59) + ($newVerifyFaceUrl == null ? 43 : $newVerifyFaceUrl.hashCode());
        Object $gmtCreate = getGmtCreate();
        return (result5 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
    }

    public String toString() {
        return "MemberInputFaceRecordListResponse(memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", identityCard=" + getIdentityCard() + ", newVerifyFaceUrl=" + getNewVerifyFaceUrl() + ", gmtCreate=" + getGmtCreate() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public String getNewVerifyFaceUrl() {
        return this.newVerifyFaceUrl;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }
}

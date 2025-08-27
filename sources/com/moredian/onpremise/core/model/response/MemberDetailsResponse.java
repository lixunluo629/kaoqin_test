package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "成员详情")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/MemberDetailsResponse.class */
public class MemberDetailsResponse implements Serializable {

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "deptId", value = "所属部门，多个之间逗号隔开")
    private String deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称，多个之前逗号隔开，同dept_id")
    private String deptName;

    @ApiModelProperty(name = "verifyFaceUrl", value = "成员底库照")
    private String verifyFaceUrl;

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "memberMobilePre", value = "成员手机号前缀，国际化")
    private String memberMobilePre;

    @ApiModelProperty(name = "memberMobile", value = "成员手机号")
    private String memberMobile;

    @ApiModelProperty(name = "memberEmail", value = "成员邮箱")
    private String memberEmail;

    @ApiModelProperty(name = "memberTelphone", value = "成员分机号")
    private String memberTelphone;

    @ApiModelProperty(name = "memberPosition", value = "职位")
    private String memberPosition;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(name = "memberJoinTime", value = "入职时间")
    private String memberJoinTime;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "memberGender", value = "性别：1-男，2-女，3-未选中")
    private Integer memberGender;

    @ApiModelProperty(name = "identityCard", value = "证件号")
    private String identityCard;

    @ApiModelProperty(name = "memberCardNum", value = "卡号")
    private String memberCardNum;

    @ApiModelProperty(name = "healthCode", value = "健康码")
    private String healthCode;

    @ApiModelProperty(name = "healthCodeModifyTimestamp", value = "健康码最后更新时间")
    private Long healthCodeModifyTimestamp;

    @ApiModelProperty(name = "deptList", value = "部门列表")
    private List<MemberDetailsDeptResponse> deptList;

    @ApiModelProperty(name = "groupList", value = "权限组列表")
    private List<MemberDetailsGroupResponse> groupList;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberMobilePre(String memberMobilePre) {
        this.memberMobilePre = memberMobilePre;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public void setMemberTelphone(String memberTelphone) {
        this.memberTelphone = memberTelphone;
    }

    public void setMemberPosition(String memberPosition) {
        this.memberPosition = memberPosition;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setMemberJoinTime(String memberJoinTime) {
        this.memberJoinTime = memberJoinTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setMemberCardNum(String memberCardNum) {
        this.memberCardNum = memberCardNum;
    }

    public void setHealthCode(String healthCode) {
        this.healthCode = healthCode;
    }

    public void setHealthCodeModifyTimestamp(Long healthCodeModifyTimestamp) {
        this.healthCodeModifyTimestamp = healthCodeModifyTimestamp;
    }

    public void setDeptList(List<MemberDetailsDeptResponse> deptList) {
        this.deptList = deptList;
    }

    public void setGroupList(List<MemberDetailsGroupResponse> groupList) {
        this.groupList = groupList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberDetailsResponse)) {
            return false;
        }
        MemberDetailsResponse other = (MemberDetailsResponse) o;
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
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
            return false;
        }
        Object this$deptName = getDeptName();
        Object other$deptName = other.getDeptName();
        if (this$deptName == null) {
            if (other$deptName != null) {
                return false;
            }
        } else if (!this$deptName.equals(other$deptName)) {
            return false;
        }
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        if (this$verifyFaceUrl == null) {
            if (other$verifyFaceUrl != null) {
                return false;
            }
        } else if (!this$verifyFaceUrl.equals(other$verifyFaceUrl)) {
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
        Object this$memberMobilePre = getMemberMobilePre();
        Object other$memberMobilePre = other.getMemberMobilePre();
        if (this$memberMobilePre == null) {
            if (other$memberMobilePre != null) {
                return false;
            }
        } else if (!this$memberMobilePre.equals(other$memberMobilePre)) {
            return false;
        }
        Object this$memberMobile = getMemberMobile();
        Object other$memberMobile = other.getMemberMobile();
        if (this$memberMobile == null) {
            if (other$memberMobile != null) {
                return false;
            }
        } else if (!this$memberMobile.equals(other$memberMobile)) {
            return false;
        }
        Object this$memberEmail = getMemberEmail();
        Object other$memberEmail = other.getMemberEmail();
        if (this$memberEmail == null) {
            if (other$memberEmail != null) {
                return false;
            }
        } else if (!this$memberEmail.equals(other$memberEmail)) {
            return false;
        }
        Object this$memberTelphone = getMemberTelphone();
        Object other$memberTelphone = other.getMemberTelphone();
        if (this$memberTelphone == null) {
            if (other$memberTelphone != null) {
                return false;
            }
        } else if (!this$memberTelphone.equals(other$memberTelphone)) {
            return false;
        }
        Object this$memberPosition = getMemberPosition();
        Object other$memberPosition = other.getMemberPosition();
        if (this$memberPosition == null) {
            if (other$memberPosition != null) {
                return false;
            }
        } else if (!this$memberPosition.equals(other$memberPosition)) {
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
        Object this$memberJoinTime = getMemberJoinTime();
        Object other$memberJoinTime = other.getMemberJoinTime();
        if (this$memberJoinTime == null) {
            if (other$memberJoinTime != null) {
                return false;
            }
        } else if (!this$memberJoinTime.equals(other$memberJoinTime)) {
            return false;
        }
        Object this$remark = getRemark();
        Object other$remark = other.getRemark();
        if (this$remark == null) {
            if (other$remark != null) {
                return false;
            }
        } else if (!this$remark.equals(other$remark)) {
            return false;
        }
        Object this$memberGender = getMemberGender();
        Object other$memberGender = other.getMemberGender();
        if (this$memberGender == null) {
            if (other$memberGender != null) {
                return false;
            }
        } else if (!this$memberGender.equals(other$memberGender)) {
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
        Object this$memberCardNum = getMemberCardNum();
        Object other$memberCardNum = other.getMemberCardNum();
        if (this$memberCardNum == null) {
            if (other$memberCardNum != null) {
                return false;
            }
        } else if (!this$memberCardNum.equals(other$memberCardNum)) {
            return false;
        }
        Object this$healthCode = getHealthCode();
        Object other$healthCode = other.getHealthCode();
        if (this$healthCode == null) {
            if (other$healthCode != null) {
                return false;
            }
        } else if (!this$healthCode.equals(other$healthCode)) {
            return false;
        }
        Object this$healthCodeModifyTimestamp = getHealthCodeModifyTimestamp();
        Object other$healthCodeModifyTimestamp = other.getHealthCodeModifyTimestamp();
        if (this$healthCodeModifyTimestamp == null) {
            if (other$healthCodeModifyTimestamp != null) {
                return false;
            }
        } else if (!this$healthCodeModifyTimestamp.equals(other$healthCodeModifyTimestamp)) {
            return false;
        }
        Object this$deptList = getDeptList();
        Object other$deptList = other.getDeptList();
        if (this$deptList == null) {
            if (other$deptList != null) {
                return false;
            }
        } else if (!this$deptList.equals(other$deptList)) {
            return false;
        }
        Object this$groupList = getGroupList();
        Object other$groupList = other.getGroupList();
        return this$groupList == null ? other$groupList == null : this$groupList.equals(other$groupList);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MemberDetailsResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result3 = (result2 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result4 = (result3 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $memberName = getMemberName();
        int result5 = (result4 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberMobilePre = getMemberMobilePre();
        int result6 = (result5 * 59) + ($memberMobilePre == null ? 43 : $memberMobilePre.hashCode());
        Object $memberMobile = getMemberMobile();
        int result7 = (result6 * 59) + ($memberMobile == null ? 43 : $memberMobile.hashCode());
        Object $memberEmail = getMemberEmail();
        int result8 = (result7 * 59) + ($memberEmail == null ? 43 : $memberEmail.hashCode());
        Object $memberTelphone = getMemberTelphone();
        int result9 = (result8 * 59) + ($memberTelphone == null ? 43 : $memberTelphone.hashCode());
        Object $memberPosition = getMemberPosition();
        int result10 = (result9 * 59) + ($memberPosition == null ? 43 : $memberPosition.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result11 = (result10 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberJoinTime = getMemberJoinTime();
        int result12 = (result11 * 59) + ($memberJoinTime == null ? 43 : $memberJoinTime.hashCode());
        Object $remark = getRemark();
        int result13 = (result12 * 59) + ($remark == null ? 43 : $remark.hashCode());
        Object $memberGender = getMemberGender();
        int result14 = (result13 * 59) + ($memberGender == null ? 43 : $memberGender.hashCode());
        Object $identityCard = getIdentityCard();
        int result15 = (result14 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
        Object $memberCardNum = getMemberCardNum();
        int result16 = (result15 * 59) + ($memberCardNum == null ? 43 : $memberCardNum.hashCode());
        Object $healthCode = getHealthCode();
        int result17 = (result16 * 59) + ($healthCode == null ? 43 : $healthCode.hashCode());
        Object $healthCodeModifyTimestamp = getHealthCodeModifyTimestamp();
        int result18 = (result17 * 59) + ($healthCodeModifyTimestamp == null ? 43 : $healthCodeModifyTimestamp.hashCode());
        Object $deptList = getDeptList();
        int result19 = (result18 * 59) + ($deptList == null ? 43 : $deptList.hashCode());
        Object $groupList = getGroupList();
        return (result19 * 59) + ($groupList == null ? 43 : $groupList.hashCode());
    }

    public String toString() {
        return "MemberDetailsResponse(memberId=" + getMemberId() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberName=" + getMemberName() + ", memberMobilePre=" + getMemberMobilePre() + ", memberMobile=" + getMemberMobile() + ", memberEmail=" + getMemberEmail() + ", memberTelphone=" + getMemberTelphone() + ", memberPosition=" + getMemberPosition() + ", memberJobNum=" + getMemberJobNum() + ", memberJoinTime=" + getMemberJoinTime() + ", remark=" + getRemark() + ", memberGender=" + getMemberGender() + ", identityCard=" + getIdentityCard() + ", memberCardNum=" + getMemberCardNum() + ", healthCode=" + getHealthCode() + ", healthCodeModifyTimestamp=" + getHealthCodeModifyTimestamp() + ", deptList=" + getDeptList() + ", groupList=" + getGroupList() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberMobilePre() {
        return this.memberMobilePre;
    }

    public String getMemberMobile() {
        return this.memberMobile;
    }

    public String getMemberEmail() {
        return this.memberEmail;
    }

    public String getMemberTelphone() {
        return this.memberTelphone;
    }

    public String getMemberPosition() {
        return this.memberPosition;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getMemberJoinTime() {
        return this.memberJoinTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getMemberGender() {
        return this.memberGender;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public String getMemberCardNum() {
        return this.memberCardNum;
    }

    public String getHealthCode() {
        return this.healthCode;
    }

    public Long getHealthCodeModifyTimestamp() {
        return this.healthCodeModifyTimestamp;
    }

    public List<MemberDetailsDeptResponse> getDeptList() {
        return this.deptList;
    }

    public List<MemberDetailsGroupResponse> getGroupList() {
        return this.groupList;
    }
}

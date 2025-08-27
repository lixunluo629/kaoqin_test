package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存成员返回对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/SaveMemberResponse.class */
public class SaveMemberResponse implements Serializable {
    private static final long serialVersionUID = -1169871454501627110L;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "deptId", value = "所属部门，多个之间逗号隔开")
    private String deptId;

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

    @ApiModelProperty(name = "jobNum", value = "任务序列号")
    private String jobNum;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveMemberResponse)) {
            return false;
        }
        SaveMemberResponse other = (SaveMemberResponse) o;
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
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        return this$jobNum == null ? other$jobNum == null : this$jobNum.equals(other$jobNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SaveMemberResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result3 = (result2 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $memberName = getMemberName();
        int result4 = (result3 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberMobilePre = getMemberMobilePre();
        int result5 = (result4 * 59) + ($memberMobilePre == null ? 43 : $memberMobilePre.hashCode());
        Object $memberMobile = getMemberMobile();
        int result6 = (result5 * 59) + ($memberMobile == null ? 43 : $memberMobile.hashCode());
        Object $memberEmail = getMemberEmail();
        int result7 = (result6 * 59) + ($memberEmail == null ? 43 : $memberEmail.hashCode());
        Object $memberTelphone = getMemberTelphone();
        int result8 = (result7 * 59) + ($memberTelphone == null ? 43 : $memberTelphone.hashCode());
        Object $memberPosition = getMemberPosition();
        int result9 = (result8 * 59) + ($memberPosition == null ? 43 : $memberPosition.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result10 = (result9 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberJoinTime = getMemberJoinTime();
        int result11 = (result10 * 59) + ($memberJoinTime == null ? 43 : $memberJoinTime.hashCode());
        Object $remark = getRemark();
        int result12 = (result11 * 59) + ($remark == null ? 43 : $remark.hashCode());
        Object $jobNum = getJobNum();
        return (result12 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
    }

    public String toString() {
        return "SaveMemberResponse(memberId=" + getMemberId() + ", deptId=" + getDeptId() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberName=" + getMemberName() + ", memberMobilePre=" + getMemberMobilePre() + ", memberMobile=" + getMemberMobile() + ", memberEmail=" + getMemberEmail() + ", memberTelphone=" + getMemberTelphone() + ", memberPosition=" + getMemberPosition() + ", memberJobNum=" + getMemberJobNum() + ", memberJoinTime=" + getMemberJoinTime() + ", remark=" + getRemark() + ", jobNum=" + getJobNum() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getDeptId() {
        return this.deptId;
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

    public String getJobNum() {
        return this.jobNum;
    }
}

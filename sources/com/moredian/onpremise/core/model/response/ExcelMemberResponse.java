package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "导出成员列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ExcelMemberResponse.class */
public class ExcelMemberResponse implements Serializable {

    @ApiModelProperty(name = "memberName", value = "姓名时间")
    private String memberName;

    @ApiModelProperty(name = "memberMobilePre", value = "手机号前缀，国际化")
    private String memberMobilePre;

    @ApiModelProperty(name = "memberMobile", value = "手机号")
    private String memberMobile;

    @ApiModelProperty(name = "memberTelphone", value = "分机号")
    private String memberTelphone;

    @ApiModelProperty(name = "deptName", value = "部门名称，多个之间逗号分隔")
    private String deptName;

    @ApiModelProperty(name = "deptId", value = "部门id，多个之间逗号分隔")
    private String deptId;

    @ApiModelProperty(name = "memberPosition", value = "职位")
    private String memberPosition;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(name = "memberJoinTime", value = "入职时间")
    private String memberJoinTime;

    @ApiModelProperty(name = "memberEmail", value = "邮箱地址")
    private String memberEmail;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "eigenvalueValue", value = "特征值")
    private String eigenvalueValue;

    @ApiModelProperty(name = "memberGender", value = "性别：1-男，2-女，3-未选中")
    private Integer memberGender;

    @ApiModelProperty(name = "identityCard", value = "证件号")
    private String identityCard;

    @ApiModelProperty(name = "memberCardNum", value = "卡号")
    private String memberCardNum;

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberMobilePre(String memberMobilePre) {
        this.memberMobilePre = memberMobilePre;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public void setMemberTelphone(String memberTelphone) {
        this.memberTelphone = memberTelphone;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExcelMemberResponse)) {
            return false;
        }
        ExcelMemberResponse other = (ExcelMemberResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$memberTelphone = getMemberTelphone();
        Object other$memberTelphone = other.getMemberTelphone();
        if (this$memberTelphone == null) {
            if (other$memberTelphone != null) {
                return false;
            }
        } else if (!this$memberTelphone.equals(other$memberTelphone)) {
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
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
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
        Object this$memberEmail = getMemberEmail();
        Object other$memberEmail = other.getMemberEmail();
        if (this$memberEmail == null) {
            if (other$memberEmail != null) {
                return false;
            }
        } else if (!this$memberEmail.equals(other$memberEmail)) {
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
        Object this$eigenvalueValue = getEigenvalueValue();
        Object other$eigenvalueValue = other.getEigenvalueValue();
        if (this$eigenvalueValue == null) {
            if (other$eigenvalueValue != null) {
                return false;
            }
        } else if (!this$eigenvalueValue.equals(other$eigenvalueValue)) {
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
        return this$memberCardNum == null ? other$memberCardNum == null : this$memberCardNum.equals(other$memberCardNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExcelMemberResponse;
    }

    public int hashCode() {
        Object $memberName = getMemberName();
        int result = (1 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberMobilePre = getMemberMobilePre();
        int result2 = (result * 59) + ($memberMobilePre == null ? 43 : $memberMobilePre.hashCode());
        Object $memberMobile = getMemberMobile();
        int result3 = (result2 * 59) + ($memberMobile == null ? 43 : $memberMobile.hashCode());
        Object $memberTelphone = getMemberTelphone();
        int result4 = (result3 * 59) + ($memberTelphone == null ? 43 : $memberTelphone.hashCode());
        Object $deptName = getDeptName();
        int result5 = (result4 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $deptId = getDeptId();
        int result6 = (result5 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $memberPosition = getMemberPosition();
        int result7 = (result6 * 59) + ($memberPosition == null ? 43 : $memberPosition.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result8 = (result7 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberJoinTime = getMemberJoinTime();
        int result9 = (result8 * 59) + ($memberJoinTime == null ? 43 : $memberJoinTime.hashCode());
        Object $memberEmail = getMemberEmail();
        int result10 = (result9 * 59) + ($memberEmail == null ? 43 : $memberEmail.hashCode());
        Object $remark = getRemark();
        int result11 = (result10 * 59) + ($remark == null ? 43 : $remark.hashCode());
        Object $eigenvalueValue = getEigenvalueValue();
        int result12 = (result11 * 59) + ($eigenvalueValue == null ? 43 : $eigenvalueValue.hashCode());
        Object $memberGender = getMemberGender();
        int result13 = (result12 * 59) + ($memberGender == null ? 43 : $memberGender.hashCode());
        Object $identityCard = getIdentityCard();
        int result14 = (result13 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
        Object $memberCardNum = getMemberCardNum();
        return (result14 * 59) + ($memberCardNum == null ? 43 : $memberCardNum.hashCode());
    }

    public String toString() {
        return "ExcelMemberResponse(memberName=" + getMemberName() + ", memberMobilePre=" + getMemberMobilePre() + ", memberMobile=" + getMemberMobile() + ", memberTelphone=" + getMemberTelphone() + ", deptName=" + getDeptName() + ", deptId=" + getDeptId() + ", memberPosition=" + getMemberPosition() + ", memberJobNum=" + getMemberJobNum() + ", memberJoinTime=" + getMemberJoinTime() + ", memberEmail=" + getMemberEmail() + ", remark=" + getRemark() + ", eigenvalueValue=" + getEigenvalueValue() + ", memberGender=" + getMemberGender() + ", identityCard=" + getIdentityCard() + ", memberCardNum=" + getMemberCardNum() + ")";
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

    public String getMemberTelphone() {
        return this.memberTelphone;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getDeptId() {
        return this.deptId;
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

    public String getMemberEmail() {
        return this.memberEmail;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
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
}

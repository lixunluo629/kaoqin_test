package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "团餐临时工成员保存请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveMealMemberRequest.class */
public class SaveMealMemberRequest extends BaseRequest {

    @ApiModelProperty(name = "memberId", value = "成员id，新增时不填，修改时必填")
    private Long memberId;

    @ApiModelProperty(name = "deptId", value = "所属部门，多个之间逗号隔开")
    private String deptId;

    @ApiModelProperty(name = "verifyFaceUrl", value = "成员底库照")
    private String verifyFaceUrl;

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "memberJobNum", value = "魔点工号")
    private String memberJobNum;

    @ApiModelProperty(name = "memberJoinTime", value = "入职时间，格式yyyy-MM-dd")
    private String memberJoinTime;

    @ApiModelProperty(name = "memberRetireTime", value = "离职时间，格式yyyy-MM-dd")
    private String memberRetireTime;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "jobNum", value = "任务序列号")
    private String jobNum;

    @ApiModelProperty(name = "memberWorkNum", value = "团餐工号")
    private String memberWorkNum;

    @ApiModelProperty(name = "memberGender", value = "性别：1-男，2-女，3-未选中")
    private Integer memberGender;

    @ApiModelProperty(name = "roomStatus", value = "住宿：1-是，0-否")
    private Integer roomStatus;

    @ApiModelProperty(name = "shiftStatus", value = "班次：1-白班，2-夜班")
    private Integer shiftStatus;

    @ApiModelProperty(name = "memberType", value = "人员类型：1-正式员工，2-临时员工")
    private Integer memberType;

    @ApiModelProperty(name = "enableStatus", value = "启用状态：1-启用，0-禁用")
    private Integer enableStatus;

    @ApiModelProperty(name = "cardId", value = "饭卡id")
    private Long cardId;

    @ApiModelProperty(name = "identityCard", value = "证件号")
    private String identityCard;

    @ApiModelProperty(name = "mealDeptName", value = "组织机构名")
    private String mealDeptName;

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

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setMemberJoinTime(String memberJoinTime) {
        this.memberJoinTime = memberJoinTime;
    }

    public void setMemberRetireTime(String memberRetireTime) {
        this.memberRetireTime = memberRetireTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setMemberWorkNum(String memberWorkNum) {
        this.memberWorkNum = memberWorkNum;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public void setShiftStatus(Integer shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setMealDeptName(String mealDeptName) {
        this.mealDeptName = mealDeptName;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveMealMemberRequest)) {
            return false;
        }
        SaveMealMemberRequest other = (SaveMealMemberRequest) o;
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
        Object this$memberRetireTime = getMemberRetireTime();
        Object other$memberRetireTime = other.getMemberRetireTime();
        if (this$memberRetireTime == null) {
            if (other$memberRetireTime != null) {
                return false;
            }
        } else if (!this$memberRetireTime.equals(other$memberRetireTime)) {
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
        if (this$jobNum == null) {
            if (other$jobNum != null) {
                return false;
            }
        } else if (!this$jobNum.equals(other$jobNum)) {
            return false;
        }
        Object this$memberWorkNum = getMemberWorkNum();
        Object other$memberWorkNum = other.getMemberWorkNum();
        if (this$memberWorkNum == null) {
            if (other$memberWorkNum != null) {
                return false;
            }
        } else if (!this$memberWorkNum.equals(other$memberWorkNum)) {
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
        Object this$roomStatus = getRoomStatus();
        Object other$roomStatus = other.getRoomStatus();
        if (this$roomStatus == null) {
            if (other$roomStatus != null) {
                return false;
            }
        } else if (!this$roomStatus.equals(other$roomStatus)) {
            return false;
        }
        Object this$shiftStatus = getShiftStatus();
        Object other$shiftStatus = other.getShiftStatus();
        if (this$shiftStatus == null) {
            if (other$shiftStatus != null) {
                return false;
            }
        } else if (!this$shiftStatus.equals(other$shiftStatus)) {
            return false;
        }
        Object this$memberType = getMemberType();
        Object other$memberType = other.getMemberType();
        if (this$memberType == null) {
            if (other$memberType != null) {
                return false;
            }
        } else if (!this$memberType.equals(other$memberType)) {
            return false;
        }
        Object this$enableStatus = getEnableStatus();
        Object other$enableStatus = other.getEnableStatus();
        if (this$enableStatus == null) {
            if (other$enableStatus != null) {
                return false;
            }
        } else if (!this$enableStatus.equals(other$enableStatus)) {
            return false;
        }
        Object this$cardId = getCardId();
        Object other$cardId = other.getCardId();
        if (this$cardId == null) {
            if (other$cardId != null) {
                return false;
            }
        } else if (!this$cardId.equals(other$cardId)) {
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
        Object this$mealDeptName = getMealDeptName();
        Object other$mealDeptName = other.getMealDeptName();
        return this$mealDeptName == null ? other$mealDeptName == null : this$mealDeptName.equals(other$mealDeptName);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveMealMemberRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $deptId = getDeptId();
        int result2 = (result * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result3 = (result2 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $memberName = getMemberName();
        int result4 = (result3 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result5 = (result4 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberJoinTime = getMemberJoinTime();
        int result6 = (result5 * 59) + ($memberJoinTime == null ? 43 : $memberJoinTime.hashCode());
        Object $memberRetireTime = getMemberRetireTime();
        int result7 = (result6 * 59) + ($memberRetireTime == null ? 43 : $memberRetireTime.hashCode());
        Object $remark = getRemark();
        int result8 = (result7 * 59) + ($remark == null ? 43 : $remark.hashCode());
        Object $jobNum = getJobNum();
        int result9 = (result8 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $memberWorkNum = getMemberWorkNum();
        int result10 = (result9 * 59) + ($memberWorkNum == null ? 43 : $memberWorkNum.hashCode());
        Object $memberGender = getMemberGender();
        int result11 = (result10 * 59) + ($memberGender == null ? 43 : $memberGender.hashCode());
        Object $roomStatus = getRoomStatus();
        int result12 = (result11 * 59) + ($roomStatus == null ? 43 : $roomStatus.hashCode());
        Object $shiftStatus = getShiftStatus();
        int result13 = (result12 * 59) + ($shiftStatus == null ? 43 : $shiftStatus.hashCode());
        Object $memberType = getMemberType();
        int result14 = (result13 * 59) + ($memberType == null ? 43 : $memberType.hashCode());
        Object $enableStatus = getEnableStatus();
        int result15 = (result14 * 59) + ($enableStatus == null ? 43 : $enableStatus.hashCode());
        Object $cardId = getCardId();
        int result16 = (result15 * 59) + ($cardId == null ? 43 : $cardId.hashCode());
        Object $identityCard = getIdentityCard();
        int result17 = (result16 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
        Object $mealDeptName = getMealDeptName();
        return (result17 * 59) + ($mealDeptName == null ? 43 : $mealDeptName.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveMealMemberRequest(memberId=" + getMemberId() + ", deptId=" + getDeptId() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", memberJoinTime=" + getMemberJoinTime() + ", memberRetireTime=" + getMemberRetireTime() + ", remark=" + getRemark() + ", jobNum=" + getJobNum() + ", memberWorkNum=" + getMemberWorkNum() + ", memberGender=" + getMemberGender() + ", roomStatus=" + getRoomStatus() + ", shiftStatus=" + getShiftStatus() + ", memberType=" + getMemberType() + ", enableStatus=" + getEnableStatus() + ", cardId=" + getCardId() + ", identityCard=" + getIdentityCard() + ", mealDeptName=" + getMealDeptName() + ")";
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

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getMemberJoinTime() {
        return this.memberJoinTime;
    }

    public String getMemberRetireTime() {
        return this.memberRetireTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public String getMemberWorkNum() {
        return this.memberWorkNum;
    }

    public Integer getMemberGender() {
        return this.memberGender;
    }

    public Integer getRoomStatus() {
        return this.roomStatus;
    }

    public Integer getShiftStatus() {
        return this.shiftStatus;
    }

    public Integer getMemberType() {
        return this.memberType;
    }

    public Integer getEnableStatus() {
        return this.enableStatus;
    }

    public Long getCardId() {
        return this.cardId;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public String getMealDeptName() {
        return this.mealDeptName;
    }
}

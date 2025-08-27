package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "团餐临时工成员保存请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/MealMemberDetailResponse.class */
public class MealMemberDetailResponse implements Serializable {

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

    @ApiModelProperty(name = "memberJobNum", value = "魔点工号")
    private String memberJobNum;

    @ApiModelProperty(name = "memberJoinTime", value = "入职时间")
    private String memberJoinTime;

    @ApiModelProperty(name = "memberRetireTime", value = "离职时间")
    private String memberRetireTime;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "memberWorkNum", value = "工号")
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

    @ApiModelProperty(name = "deptList", value = "部门列表")
    private List<MemberDetailsDeptResponse> deptList;

    @ApiModelProperty(name = "mealDeptName", value = "组织机构名")
    private String mealDeptName;

    @ApiModelProperty(name = "identityCard", value = "证件号")
    private String identityCard;

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

    public void setDeptList(List<MemberDetailsDeptResponse> deptList) {
        this.deptList = deptList;
    }

    public void setMealDeptName(String mealDeptName) {
        this.mealDeptName = mealDeptName;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MealMemberDetailResponse)) {
            return false;
        }
        MealMemberDetailResponse other = (MealMemberDetailResponse) o;
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
        Object this$deptList = getDeptList();
        Object other$deptList = other.getDeptList();
        if (this$deptList == null) {
            if (other$deptList != null) {
                return false;
            }
        } else if (!this$deptList.equals(other$deptList)) {
            return false;
        }
        Object this$mealDeptName = getMealDeptName();
        Object other$mealDeptName = other.getMealDeptName();
        if (this$mealDeptName == null) {
            if (other$mealDeptName != null) {
                return false;
            }
        } else if (!this$mealDeptName.equals(other$mealDeptName)) {
            return false;
        }
        Object this$identityCard = getIdentityCard();
        Object other$identityCard = other.getIdentityCard();
        return this$identityCard == null ? other$identityCard == null : this$identityCard.equals(other$identityCard);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MealMemberDetailResponse;
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
        Object $memberJobNum = getMemberJobNum();
        int result6 = (result5 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberJoinTime = getMemberJoinTime();
        int result7 = (result6 * 59) + ($memberJoinTime == null ? 43 : $memberJoinTime.hashCode());
        Object $memberRetireTime = getMemberRetireTime();
        int result8 = (result7 * 59) + ($memberRetireTime == null ? 43 : $memberRetireTime.hashCode());
        Object $remark = getRemark();
        int result9 = (result8 * 59) + ($remark == null ? 43 : $remark.hashCode());
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
        Object $deptList = getDeptList();
        int result17 = (result16 * 59) + ($deptList == null ? 43 : $deptList.hashCode());
        Object $mealDeptName = getMealDeptName();
        int result18 = (result17 * 59) + ($mealDeptName == null ? 43 : $mealDeptName.hashCode());
        Object $identityCard = getIdentityCard();
        return (result18 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
    }

    public String toString() {
        return "MealMemberDetailResponse(memberId=" + getMemberId() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberName=" + getMemberName() + ", memberJobNum=" + getMemberJobNum() + ", memberJoinTime=" + getMemberJoinTime() + ", memberRetireTime=" + getMemberRetireTime() + ", remark=" + getRemark() + ", memberWorkNum=" + getMemberWorkNum() + ", memberGender=" + getMemberGender() + ", roomStatus=" + getRoomStatus() + ", shiftStatus=" + getShiftStatus() + ", memberType=" + getMemberType() + ", enableStatus=" + getEnableStatus() + ", cardId=" + getCardId() + ", deptList=" + getDeptList() + ", mealDeptName=" + getMealDeptName() + ", identityCard=" + getIdentityCard() + ")";
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

    public List<MemberDetailsDeptResponse> getDeptList() {
        return this.deptList;
    }

    public String getMealDeptName() {
        return this.mealDeptName;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }
}

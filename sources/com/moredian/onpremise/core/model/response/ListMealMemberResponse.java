package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "就餐人员列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListMealMemberResponse.class */
public class ListMealMemberResponse implements Serializable {

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private String deptId;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "mealDeptName", value = "组织机构名")
    private String mealDeptName;

    @ApiModelProperty(name = "cardName", value = "卡名称")
    private String cardName;

    @ApiModelProperty(name = "cardStatus", value = "卡状态：1-使用中，2-已禁用，3-无效卡，4-未绑卡")
    private Integer cardStatus;

    @ApiModelProperty(name = "mealCardId", value = "卡id")
    private Long mealCardId;

    @ApiModelProperty(name = "verifyFaceUrl", value = "成员底库照")
    private String verifyFaceUrl;

    @ApiModelProperty(name = "memberType", value = "人员类型：1-正式员工，2-临时员工")
    private Integer memberType;

    @ApiModelProperty(name = "memberJoinTime", value = "入职时间")
    private String memberJoinTime;

    @ApiModelProperty(name = "memberRetireTime", value = "离职时间")
    private String memberRetireTime;

    @ApiModelProperty(name = "memberWorkNum", value = "团餐工号")
    private String memberWorkNum;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setMealDeptName(String mealDeptName) {
        this.mealDeptName = mealDeptName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public void setMemberJoinTime(String memberJoinTime) {
        this.memberJoinTime = memberJoinTime;
    }

    public void setMemberRetireTime(String memberRetireTime) {
        this.memberRetireTime = memberRetireTime;
    }

    public void setMemberWorkNum(String memberWorkNum) {
        this.memberWorkNum = memberWorkNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListMealMemberResponse)) {
            return false;
        }
        ListMealMemberResponse other = (ListMealMemberResponse) o;
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
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
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
        Object this$mealDeptName = getMealDeptName();
        Object other$mealDeptName = other.getMealDeptName();
        if (this$mealDeptName == null) {
            if (other$mealDeptName != null) {
                return false;
            }
        } else if (!this$mealDeptName.equals(other$mealDeptName)) {
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
        Object this$cardStatus = getCardStatus();
        Object other$cardStatus = other.getCardStatus();
        if (this$cardStatus == null) {
            if (other$cardStatus != null) {
                return false;
            }
        } else if (!this$cardStatus.equals(other$cardStatus)) {
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
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        if (this$verifyFaceUrl == null) {
            if (other$verifyFaceUrl != null) {
                return false;
            }
        } else if (!this$verifyFaceUrl.equals(other$verifyFaceUrl)) {
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
        Object this$memberWorkNum = getMemberWorkNum();
        Object other$memberWorkNum = other.getMemberWorkNum();
        return this$memberWorkNum == null ? other$memberWorkNum == null : this$memberWorkNum.equals(other$memberWorkNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListMealMemberResponse;
    }

    public int hashCode() {
        Object $memberId = getMemberId();
        int result = (1 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result2 = (result * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberName = getMemberName();
        int result3 = (result2 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $deptId = getDeptId();
        int result4 = (result3 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $deptName = getDeptName();
        int result5 = (result4 * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $mealDeptName = getMealDeptName();
        int result6 = (result5 * 59) + ($mealDeptName == null ? 43 : $mealDeptName.hashCode());
        Object $cardName = getCardName();
        int result7 = (result6 * 59) + ($cardName == null ? 43 : $cardName.hashCode());
        Object $cardStatus = getCardStatus();
        int result8 = (result7 * 59) + ($cardStatus == null ? 43 : $cardStatus.hashCode());
        Object $mealCardId = getMealCardId();
        int result9 = (result8 * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result10 = (result9 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $memberType = getMemberType();
        int result11 = (result10 * 59) + ($memberType == null ? 43 : $memberType.hashCode());
        Object $memberJoinTime = getMemberJoinTime();
        int result12 = (result11 * 59) + ($memberJoinTime == null ? 43 : $memberJoinTime.hashCode());
        Object $memberRetireTime = getMemberRetireTime();
        int result13 = (result12 * 59) + ($memberRetireTime == null ? 43 : $memberRetireTime.hashCode());
        Object $memberWorkNum = getMemberWorkNum();
        return (result13 * 59) + ($memberWorkNum == null ? 43 : $memberWorkNum.hashCode());
    }

    public String toString() {
        return "ListMealMemberResponse(memberId=" + getMemberId() + ", memberJobNum=" + getMemberJobNum() + ", memberName=" + getMemberName() + ", deptId=" + getDeptId() + ", deptName=" + getDeptName() + ", mealDeptName=" + getMealDeptName() + ", cardName=" + getCardName() + ", cardStatus=" + getCardStatus() + ", mealCardId=" + getMealCardId() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberType=" + getMemberType() + ", memberJoinTime=" + getMemberJoinTime() + ", memberRetireTime=" + getMemberRetireTime() + ", memberWorkNum=" + getMemberWorkNum() + ")";
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getMealDeptName() {
        return this.mealDeptName;
    }

    public String getCardName() {
        return this.cardName;
    }

    public Integer getCardStatus() {
        return this.cardStatus;
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public Integer getMemberType() {
        return this.memberType;
    }

    public String getMemberJoinTime() {
        return this.memberJoinTime;
    }

    public String getMemberRetireTime() {
        return this.memberRetireTime;
    }

    public String getMemberWorkNum() {
        return this.memberWorkNum;
    }
}

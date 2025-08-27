package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "统计团餐饭卡记录报表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CountMealCardResponse.class */
public class CountMealCardResponse implements Serializable {
    private static final long serialVersionUID = 2681843471313804590L;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称")
    private String canteenName;

    @ApiModelProperty(name = "cardNo", value = "饭卡卡号")
    private String cardNo;

    @ApiModelProperty(name = "cardName", value = "饭卡名称")
    private String cardName;
    private Long memberId;

    @ApiModelProperty(name = "memberJobNum", value = "员工工号")
    private String memberJobNum;

    @ApiModelProperty(name = "memberName", value = "员工名称")
    private String memberName;

    @ApiModelProperty(name = "deptId", value = "部门ID")
    private String deptId;

    @ApiModelProperty(name = "recordDate", value = "操作日期")
    private Long recordDate;

    @ApiModelProperty(name = "recordType", value = "操作类型：1-新办，2-退卡，3-状态修改")
    private Integer recordType;

    @ApiModelProperty(name = "memberJoinTime", value = "入职时间")
    private String memberJoinTime;

    @ApiModelProperty(name = "memberRetireTime", value = "离职时间")
    private String memberRetireTime;

    @ApiModelProperty(name = "roomStatus", value = "住宿：1-是，0-否")
    private Integer roomStatus;

    @ApiModelProperty(name = "shiftStatus", value = "班次：1-白班，2-夜班")
    private Integer shiftStatus;

    @ApiModelProperty(name = "cardCreateTime", value = "开卡时间")
    private String cardCreateTime;

    @ApiModelProperty(name = "mealDeptName", value = "机构名")
    private String mealDeptName;

    @ApiModelProperty(name = "memberType", value = "员工类型")
    private String memberType;

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

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

    public void setRecordDate(Long recordDate) {
        this.recordDate = recordDate;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public void setMemberJoinTime(String memberJoinTime) {
        this.memberJoinTime = memberJoinTime;
    }

    public void setMemberRetireTime(String memberRetireTime) {
        this.memberRetireTime = memberRetireTime;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public void setShiftStatus(Integer shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public void setCardCreateTime(String cardCreateTime) {
        this.cardCreateTime = cardCreateTime;
    }

    public void setMealDeptName(String mealDeptName) {
        this.mealDeptName = mealDeptName;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountMealCardResponse)) {
            return false;
        }
        CountMealCardResponse other = (CountMealCardResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$canteenName = getCanteenName();
        Object other$canteenName = other.getCanteenName();
        if (this$canteenName == null) {
            if (other$canteenName != null) {
                return false;
            }
        } else if (!this$canteenName.equals(other$canteenName)) {
            return false;
        }
        Object this$cardNo = getCardNo();
        Object other$cardNo = other.getCardNo();
        if (this$cardNo == null) {
            if (other$cardNo != null) {
                return false;
            }
        } else if (!this$cardNo.equals(other$cardNo)) {
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
        Object this$recordDate = getRecordDate();
        Object other$recordDate = other.getRecordDate();
        if (this$recordDate == null) {
            if (other$recordDate != null) {
                return false;
            }
        } else if (!this$recordDate.equals(other$recordDate)) {
            return false;
        }
        Object this$recordType = getRecordType();
        Object other$recordType = other.getRecordType();
        if (this$recordType == null) {
            if (other$recordType != null) {
                return false;
            }
        } else if (!this$recordType.equals(other$recordType)) {
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
        Object this$cardCreateTime = getCardCreateTime();
        Object other$cardCreateTime = other.getCardCreateTime();
        if (this$cardCreateTime == null) {
            if (other$cardCreateTime != null) {
                return false;
            }
        } else if (!this$cardCreateTime.equals(other$cardCreateTime)) {
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
        Object this$memberType = getMemberType();
        Object other$memberType = other.getMemberType();
        return this$memberType == null ? other$memberType == null : this$memberType.equals(other$memberType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CountMealCardResponse;
    }

    public int hashCode() {
        Object $canteenName = getCanteenName();
        int result = (1 * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $cardNo = getCardNo();
        int result2 = (result * 59) + ($cardNo == null ? 43 : $cardNo.hashCode());
        Object $cardName = getCardName();
        int result3 = (result2 * 59) + ($cardName == null ? 43 : $cardName.hashCode());
        Object $memberId = getMemberId();
        int result4 = (result3 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result5 = (result4 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $memberName = getMemberName();
        int result6 = (result5 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $deptId = getDeptId();
        int result7 = (result6 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $recordDate = getRecordDate();
        int result8 = (result7 * 59) + ($recordDate == null ? 43 : $recordDate.hashCode());
        Object $recordType = getRecordType();
        int result9 = (result8 * 59) + ($recordType == null ? 43 : $recordType.hashCode());
        Object $memberJoinTime = getMemberJoinTime();
        int result10 = (result9 * 59) + ($memberJoinTime == null ? 43 : $memberJoinTime.hashCode());
        Object $memberRetireTime = getMemberRetireTime();
        int result11 = (result10 * 59) + ($memberRetireTime == null ? 43 : $memberRetireTime.hashCode());
        Object $roomStatus = getRoomStatus();
        int result12 = (result11 * 59) + ($roomStatus == null ? 43 : $roomStatus.hashCode());
        Object $shiftStatus = getShiftStatus();
        int result13 = (result12 * 59) + ($shiftStatus == null ? 43 : $shiftStatus.hashCode());
        Object $cardCreateTime = getCardCreateTime();
        int result14 = (result13 * 59) + ($cardCreateTime == null ? 43 : $cardCreateTime.hashCode());
        Object $mealDeptName = getMealDeptName();
        int result15 = (result14 * 59) + ($mealDeptName == null ? 43 : $mealDeptName.hashCode());
        Object $memberType = getMemberType();
        return (result15 * 59) + ($memberType == null ? 43 : $memberType.hashCode());
    }

    public String toString() {
        return "CountMealCardResponse(canteenName=" + getCanteenName() + ", cardNo=" + getCardNo() + ", cardName=" + getCardName() + ", memberId=" + getMemberId() + ", memberJobNum=" + getMemberJobNum() + ", memberName=" + getMemberName() + ", deptId=" + getDeptId() + ", recordDate=" + getRecordDate() + ", recordType=" + getRecordType() + ", memberJoinTime=" + getMemberJoinTime() + ", memberRetireTime=" + getMemberRetireTime() + ", roomStatus=" + getRoomStatus() + ", shiftStatus=" + getShiftStatus() + ", cardCreateTime=" + getCardCreateTime() + ", mealDeptName=" + getMealDeptName() + ", memberType=" + getMemberType() + ")";
    }

    public String getCanteenName() {
        return this.canteenName;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public String getCardName() {
        return this.cardName;
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

    public Long getRecordDate() {
        return this.recordDate;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public String getMemberJoinTime() {
        return this.memberJoinTime;
    }

    public String getMemberRetireTime() {
        return this.memberRetireTime;
    }

    public Integer getRoomStatus() {
        return this.roomStatus;
    }

    public Integer getShiftStatus() {
        return this.shiftStatus;
    }

    public String getCardCreateTime() {
        return this.cardCreateTime;
    }

    public String getMealDeptName() {
        return this.mealDeptName;
    }

    public String getMemberType() {
        return this.memberType;
    }
}

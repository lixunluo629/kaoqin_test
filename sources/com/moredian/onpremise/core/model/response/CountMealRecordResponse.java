package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "统计就餐记录报表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CountMealRecordResponse.class */
public class CountMealRecordResponse implements Serializable {
    private static final long serialVersionUID = 4392369563138542215L;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称")
    private String canteenName;

    @ApiModelProperty(name = "deptName", value = "部门名称")
    private String deptName;

    @ApiModelProperty(name = "memberName", value = "人员名称")
    private String memberName;

    @ApiModelProperty(name = "recordType", value = "就餐类型：1-早餐，2-午餐，3-晚餐，4-宵夜")
    private Integer recordType;

    @ApiModelProperty(name = "mealTime", value = "就餐时间戳")
    private Long mealTime;

    @ApiModelProperty(name = "mealDate", value = "就餐日期")
    private Long mealDate;

    @ApiModelProperty(name = "mealMemberNum", value = "就餐人数")
    private Integer mealMemberNum;

    @ApiModelProperty(name = "breakfastNum", value = "早餐就餐人数")
    private Integer breakfastNum;

    @ApiModelProperty(name = "lunchNum", value = "中餐就餐人数")
    private Integer lunchNum;

    @ApiModelProperty(name = "supperNum", value = "晚餐就餐人数")
    private Integer supperNum;

    @ApiModelProperty(name = "midnightNum", value = "宵夜就餐人数")
    private Integer midnightNum;

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public void setMealTime(Long mealTime) {
        this.mealTime = mealTime;
    }

    public void setMealDate(Long mealDate) {
        this.mealDate = mealDate;
    }

    public void setMealMemberNum(Integer mealMemberNum) {
        this.mealMemberNum = mealMemberNum;
    }

    public void setBreakfastNum(Integer breakfastNum) {
        this.breakfastNum = breakfastNum;
    }

    public void setLunchNum(Integer lunchNum) {
        this.lunchNum = lunchNum;
    }

    public void setSupperNum(Integer supperNum) {
        this.supperNum = supperNum;
    }

    public void setMidnightNum(Integer midnightNum) {
        this.midnightNum = midnightNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountMealRecordResponse)) {
            return false;
        }
        CountMealRecordResponse other = (CountMealRecordResponse) o;
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
        Object this$deptName = getDeptName();
        Object other$deptName = other.getDeptName();
        if (this$deptName == null) {
            if (other$deptName != null) {
                return false;
            }
        } else if (!this$deptName.equals(other$deptName)) {
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
        Object this$recordType = getRecordType();
        Object other$recordType = other.getRecordType();
        if (this$recordType == null) {
            if (other$recordType != null) {
                return false;
            }
        } else if (!this$recordType.equals(other$recordType)) {
            return false;
        }
        Object this$mealTime = getMealTime();
        Object other$mealTime = other.getMealTime();
        if (this$mealTime == null) {
            if (other$mealTime != null) {
                return false;
            }
        } else if (!this$mealTime.equals(other$mealTime)) {
            return false;
        }
        Object this$mealDate = getMealDate();
        Object other$mealDate = other.getMealDate();
        if (this$mealDate == null) {
            if (other$mealDate != null) {
                return false;
            }
        } else if (!this$mealDate.equals(other$mealDate)) {
            return false;
        }
        Object this$mealMemberNum = getMealMemberNum();
        Object other$mealMemberNum = other.getMealMemberNum();
        if (this$mealMemberNum == null) {
            if (other$mealMemberNum != null) {
                return false;
            }
        } else if (!this$mealMemberNum.equals(other$mealMemberNum)) {
            return false;
        }
        Object this$breakfastNum = getBreakfastNum();
        Object other$breakfastNum = other.getBreakfastNum();
        if (this$breakfastNum == null) {
            if (other$breakfastNum != null) {
                return false;
            }
        } else if (!this$breakfastNum.equals(other$breakfastNum)) {
            return false;
        }
        Object this$lunchNum = getLunchNum();
        Object other$lunchNum = other.getLunchNum();
        if (this$lunchNum == null) {
            if (other$lunchNum != null) {
                return false;
            }
        } else if (!this$lunchNum.equals(other$lunchNum)) {
            return false;
        }
        Object this$supperNum = getSupperNum();
        Object other$supperNum = other.getSupperNum();
        if (this$supperNum == null) {
            if (other$supperNum != null) {
                return false;
            }
        } else if (!this$supperNum.equals(other$supperNum)) {
            return false;
        }
        Object this$midnightNum = getMidnightNum();
        Object other$midnightNum = other.getMidnightNum();
        return this$midnightNum == null ? other$midnightNum == null : this$midnightNum.equals(other$midnightNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CountMealRecordResponse;
    }

    public int hashCode() {
        Object $canteenName = getCanteenName();
        int result = (1 * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $deptName = getDeptName();
        int result2 = (result * 59) + ($deptName == null ? 43 : $deptName.hashCode());
        Object $memberName = getMemberName();
        int result3 = (result2 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $recordType = getRecordType();
        int result4 = (result3 * 59) + ($recordType == null ? 43 : $recordType.hashCode());
        Object $mealTime = getMealTime();
        int result5 = (result4 * 59) + ($mealTime == null ? 43 : $mealTime.hashCode());
        Object $mealDate = getMealDate();
        int result6 = (result5 * 59) + ($mealDate == null ? 43 : $mealDate.hashCode());
        Object $mealMemberNum = getMealMemberNum();
        int result7 = (result6 * 59) + ($mealMemberNum == null ? 43 : $mealMemberNum.hashCode());
        Object $breakfastNum = getBreakfastNum();
        int result8 = (result7 * 59) + ($breakfastNum == null ? 43 : $breakfastNum.hashCode());
        Object $lunchNum = getLunchNum();
        int result9 = (result8 * 59) + ($lunchNum == null ? 43 : $lunchNum.hashCode());
        Object $supperNum = getSupperNum();
        int result10 = (result9 * 59) + ($supperNum == null ? 43 : $supperNum.hashCode());
        Object $midnightNum = getMidnightNum();
        return (result10 * 59) + ($midnightNum == null ? 43 : $midnightNum.hashCode());
    }

    public String toString() {
        return "CountMealRecordResponse(canteenName=" + getCanteenName() + ", deptName=" + getDeptName() + ", memberName=" + getMemberName() + ", recordType=" + getRecordType() + ", mealTime=" + getMealTime() + ", mealDate=" + getMealDate() + ", mealMemberNum=" + getMealMemberNum() + ", breakfastNum=" + getBreakfastNum() + ", lunchNum=" + getLunchNum() + ", supperNum=" + getSupperNum() + ", midnightNum=" + getMidnightNum() + ")";
    }

    public String getCanteenName() {
        return this.canteenName;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public Long getMealTime() {
        return this.mealTime;
    }

    public Long getMealDate() {
        return this.mealDate;
    }

    public Integer getMealMemberNum() {
        return this.mealMemberNum;
    }

    public Integer getBreakfastNum() {
        return this.breakfastNum;
    }

    public Integer getLunchNum() {
        return this.lunchNum;
    }

    public Integer getSupperNum() {
        return this.supperNum;
    }

    public Integer getMidnightNum() {
        return this.midnightNum;
    }
}

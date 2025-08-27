package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.MemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "保存团餐卡信息请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveMealCardRequest.class */
public class SaveMealCardRequest extends BaseRequest {
    private static final long serialVersionUID = 8450635160941756820L;

    @ApiModelProperty(name = "mealCardId", value = "卡id，新增时不传，修改时必填")
    private Long mealCardId;

    @ApiModelProperty(name = "cardName", value = "卡名称")
    private String cardName;

    @ApiModelProperty(name = "cardType", value = "卡类型：1-员工卡，2-值班卡，3-vip卡，4-临时卡")
    private Integer cardType;

    @ApiModelProperty(name = "breakfastTimes", value = "早餐可消费次数")
    private Integer breakfastTimes;

    @ApiModelProperty(name = "lunchTimes", value = "午餐可消费次数")
    private Integer lunchTimes;

    @ApiModelProperty(name = "dinnerTimes", value = "晚餐可消费次数")
    private Integer dinnerTimes;

    @ApiModelProperty(name = "midnightSnackTimes", value = "宵夜可消费次数")
    private Integer midnightSnackTimes;

    @ApiModelProperty(name = "totalLimitTimes", value = "单日限制次数，0为不限制")
    private Integer totalLimitTimes;

    @ApiModelProperty(name = "cardMembers", value = "卡关联人员id列表")
    private List<MemberDto> cardMembers;

    public void setMealCardId(Long mealCardId) {
        this.mealCardId = mealCardId;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public void setBreakfastTimes(Integer breakfastTimes) {
        this.breakfastTimes = breakfastTimes;
    }

    public void setLunchTimes(Integer lunchTimes) {
        this.lunchTimes = lunchTimes;
    }

    public void setDinnerTimes(Integer dinnerTimes) {
        this.dinnerTimes = dinnerTimes;
    }

    public void setMidnightSnackTimes(Integer midnightSnackTimes) {
        this.midnightSnackTimes = midnightSnackTimes;
    }

    public void setTotalLimitTimes(Integer totalLimitTimes) {
        this.totalLimitTimes = totalLimitTimes;
    }

    public void setCardMembers(List<MemberDto> cardMembers) {
        this.cardMembers = cardMembers;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveMealCardRequest)) {
            return false;
        }
        SaveMealCardRequest other = (SaveMealCardRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$cardName = getCardName();
        Object other$cardName = other.getCardName();
        if (this$cardName == null) {
            if (other$cardName != null) {
                return false;
            }
        } else if (!this$cardName.equals(other$cardName)) {
            return false;
        }
        Object this$cardType = getCardType();
        Object other$cardType = other.getCardType();
        if (this$cardType == null) {
            if (other$cardType != null) {
                return false;
            }
        } else if (!this$cardType.equals(other$cardType)) {
            return false;
        }
        Object this$breakfastTimes = getBreakfastTimes();
        Object other$breakfastTimes = other.getBreakfastTimes();
        if (this$breakfastTimes == null) {
            if (other$breakfastTimes != null) {
                return false;
            }
        } else if (!this$breakfastTimes.equals(other$breakfastTimes)) {
            return false;
        }
        Object this$lunchTimes = getLunchTimes();
        Object other$lunchTimes = other.getLunchTimes();
        if (this$lunchTimes == null) {
            if (other$lunchTimes != null) {
                return false;
            }
        } else if (!this$lunchTimes.equals(other$lunchTimes)) {
            return false;
        }
        Object this$dinnerTimes = getDinnerTimes();
        Object other$dinnerTimes = other.getDinnerTimes();
        if (this$dinnerTimes == null) {
            if (other$dinnerTimes != null) {
                return false;
            }
        } else if (!this$dinnerTimes.equals(other$dinnerTimes)) {
            return false;
        }
        Object this$midnightSnackTimes = getMidnightSnackTimes();
        Object other$midnightSnackTimes = other.getMidnightSnackTimes();
        if (this$midnightSnackTimes == null) {
            if (other$midnightSnackTimes != null) {
                return false;
            }
        } else if (!this$midnightSnackTimes.equals(other$midnightSnackTimes)) {
            return false;
        }
        Object this$totalLimitTimes = getTotalLimitTimes();
        Object other$totalLimitTimes = other.getTotalLimitTimes();
        if (this$totalLimitTimes == null) {
            if (other$totalLimitTimes != null) {
                return false;
            }
        } else if (!this$totalLimitTimes.equals(other$totalLimitTimes)) {
            return false;
        }
        Object this$cardMembers = getCardMembers();
        Object other$cardMembers = other.getCardMembers();
        return this$cardMembers == null ? other$cardMembers == null : this$cardMembers.equals(other$cardMembers);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveMealCardRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $mealCardId = getMealCardId();
        int result = (1 * 59) + ($mealCardId == null ? 43 : $mealCardId.hashCode());
        Object $cardName = getCardName();
        int result2 = (result * 59) + ($cardName == null ? 43 : $cardName.hashCode());
        Object $cardType = getCardType();
        int result3 = (result2 * 59) + ($cardType == null ? 43 : $cardType.hashCode());
        Object $breakfastTimes = getBreakfastTimes();
        int result4 = (result3 * 59) + ($breakfastTimes == null ? 43 : $breakfastTimes.hashCode());
        Object $lunchTimes = getLunchTimes();
        int result5 = (result4 * 59) + ($lunchTimes == null ? 43 : $lunchTimes.hashCode());
        Object $dinnerTimes = getDinnerTimes();
        int result6 = (result5 * 59) + ($dinnerTimes == null ? 43 : $dinnerTimes.hashCode());
        Object $midnightSnackTimes = getMidnightSnackTimes();
        int result7 = (result6 * 59) + ($midnightSnackTimes == null ? 43 : $midnightSnackTimes.hashCode());
        Object $totalLimitTimes = getTotalLimitTimes();
        int result8 = (result7 * 59) + ($totalLimitTimes == null ? 43 : $totalLimitTimes.hashCode());
        Object $cardMembers = getCardMembers();
        return (result8 * 59) + ($cardMembers == null ? 43 : $cardMembers.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveMealCardRequest(mealCardId=" + getMealCardId() + ", cardName=" + getCardName() + ", cardType=" + getCardType() + ", breakfastTimes=" + getBreakfastTimes() + ", lunchTimes=" + getLunchTimes() + ", dinnerTimes=" + getDinnerTimes() + ", midnightSnackTimes=" + getMidnightSnackTimes() + ", totalLimitTimes=" + getTotalLimitTimes() + ", cardMembers=" + getCardMembers() + ")";
    }

    public Long getMealCardId() {
        return this.mealCardId;
    }

    public String getCardName() {
        return this.cardName;
    }

    public Integer getCardType() {
        return this.cardType;
    }

    public Integer getBreakfastTimes() {
        return this.breakfastTimes;
    }

    public Integer getLunchTimes() {
        return this.lunchTimes;
    }

    public Integer getDinnerTimes() {
        return this.dinnerTimes;
    }

    public Integer getMidnightSnackTimes() {
        return this.midnightSnackTimes;
    }

    public Integer getTotalLimitTimes() {
        return this.totalLimitTimes;
    }

    public List<MemberDto> getCardMembers() {
        return this.cardMembers;
    }
}

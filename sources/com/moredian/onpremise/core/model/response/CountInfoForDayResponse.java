package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "统计每日考勤信息响应结果")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CountInfoForDayResponse.class */
public class CountInfoForDayResponse implements Serializable {

    @ApiModelProperty(name = "memberTotalNum", value = "总人数")
    private Integer memberTotalNum;

    @ApiModelProperty(name = "memberNormalNum", value = "正常考勤人数")
    private Integer memberNormalNum;

    @ApiModelProperty(name = "memberLaterNum", value = "迟到人数")
    private Integer memberLaterNum;

    @ApiModelProperty(name = "memberEarlyNum", value = "早退人数")
    private Integer memberEarlyNum;

    @ApiModelProperty(name = "memberBeginLackNum", value = "上班缺卡人数")
    private Integer memberBeginLackNum;

    @ApiModelProperty(name = "memberEndLackNum", value = "下班缺卡人数")
    private Integer memberEndLackNum;

    @ApiModelProperty(name = "totalWorkHours", value = "总工时数")
    private Integer totalWorkHours;

    @ApiModelProperty(name = "avgWorkHours", value = "平均工时数")
    private Integer avgWorkHours;

    public void setMemberTotalNum(Integer memberTotalNum) {
        this.memberTotalNum = memberTotalNum;
    }

    public void setMemberNormalNum(Integer memberNormalNum) {
        this.memberNormalNum = memberNormalNum;
    }

    public void setMemberLaterNum(Integer memberLaterNum) {
        this.memberLaterNum = memberLaterNum;
    }

    public void setMemberEarlyNum(Integer memberEarlyNum) {
        this.memberEarlyNum = memberEarlyNum;
    }

    public void setMemberBeginLackNum(Integer memberBeginLackNum) {
        this.memberBeginLackNum = memberBeginLackNum;
    }

    public void setMemberEndLackNum(Integer memberEndLackNum) {
        this.memberEndLackNum = memberEndLackNum;
    }

    public void setTotalWorkHours(Integer totalWorkHours) {
        this.totalWorkHours = totalWorkHours;
    }

    public void setAvgWorkHours(Integer avgWorkHours) {
        this.avgWorkHours = avgWorkHours;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountInfoForDayResponse)) {
            return false;
        }
        CountInfoForDayResponse other = (CountInfoForDayResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$memberTotalNum = getMemberTotalNum();
        Object other$memberTotalNum = other.getMemberTotalNum();
        if (this$memberTotalNum == null) {
            if (other$memberTotalNum != null) {
                return false;
            }
        } else if (!this$memberTotalNum.equals(other$memberTotalNum)) {
            return false;
        }
        Object this$memberNormalNum = getMemberNormalNum();
        Object other$memberNormalNum = other.getMemberNormalNum();
        if (this$memberNormalNum == null) {
            if (other$memberNormalNum != null) {
                return false;
            }
        } else if (!this$memberNormalNum.equals(other$memberNormalNum)) {
            return false;
        }
        Object this$memberLaterNum = getMemberLaterNum();
        Object other$memberLaterNum = other.getMemberLaterNum();
        if (this$memberLaterNum == null) {
            if (other$memberLaterNum != null) {
                return false;
            }
        } else if (!this$memberLaterNum.equals(other$memberLaterNum)) {
            return false;
        }
        Object this$memberEarlyNum = getMemberEarlyNum();
        Object other$memberEarlyNum = other.getMemberEarlyNum();
        if (this$memberEarlyNum == null) {
            if (other$memberEarlyNum != null) {
                return false;
            }
        } else if (!this$memberEarlyNum.equals(other$memberEarlyNum)) {
            return false;
        }
        Object this$memberBeginLackNum = getMemberBeginLackNum();
        Object other$memberBeginLackNum = other.getMemberBeginLackNum();
        if (this$memberBeginLackNum == null) {
            if (other$memberBeginLackNum != null) {
                return false;
            }
        } else if (!this$memberBeginLackNum.equals(other$memberBeginLackNum)) {
            return false;
        }
        Object this$memberEndLackNum = getMemberEndLackNum();
        Object other$memberEndLackNum = other.getMemberEndLackNum();
        if (this$memberEndLackNum == null) {
            if (other$memberEndLackNum != null) {
                return false;
            }
        } else if (!this$memberEndLackNum.equals(other$memberEndLackNum)) {
            return false;
        }
        Object this$totalWorkHours = getTotalWorkHours();
        Object other$totalWorkHours = other.getTotalWorkHours();
        if (this$totalWorkHours == null) {
            if (other$totalWorkHours != null) {
                return false;
            }
        } else if (!this$totalWorkHours.equals(other$totalWorkHours)) {
            return false;
        }
        Object this$avgWorkHours = getAvgWorkHours();
        Object other$avgWorkHours = other.getAvgWorkHours();
        return this$avgWorkHours == null ? other$avgWorkHours == null : this$avgWorkHours.equals(other$avgWorkHours);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CountInfoForDayResponse;
    }

    public int hashCode() {
        Object $memberTotalNum = getMemberTotalNum();
        int result = (1 * 59) + ($memberTotalNum == null ? 43 : $memberTotalNum.hashCode());
        Object $memberNormalNum = getMemberNormalNum();
        int result2 = (result * 59) + ($memberNormalNum == null ? 43 : $memberNormalNum.hashCode());
        Object $memberLaterNum = getMemberLaterNum();
        int result3 = (result2 * 59) + ($memberLaterNum == null ? 43 : $memberLaterNum.hashCode());
        Object $memberEarlyNum = getMemberEarlyNum();
        int result4 = (result3 * 59) + ($memberEarlyNum == null ? 43 : $memberEarlyNum.hashCode());
        Object $memberBeginLackNum = getMemberBeginLackNum();
        int result5 = (result4 * 59) + ($memberBeginLackNum == null ? 43 : $memberBeginLackNum.hashCode());
        Object $memberEndLackNum = getMemberEndLackNum();
        int result6 = (result5 * 59) + ($memberEndLackNum == null ? 43 : $memberEndLackNum.hashCode());
        Object $totalWorkHours = getTotalWorkHours();
        int result7 = (result6 * 59) + ($totalWorkHours == null ? 43 : $totalWorkHours.hashCode());
        Object $avgWorkHours = getAvgWorkHours();
        return (result7 * 59) + ($avgWorkHours == null ? 43 : $avgWorkHours.hashCode());
    }

    public String toString() {
        return "CountInfoForDayResponse(memberTotalNum=" + getMemberTotalNum() + ", memberNormalNum=" + getMemberNormalNum() + ", memberLaterNum=" + getMemberLaterNum() + ", memberEarlyNum=" + getMemberEarlyNum() + ", memberBeginLackNum=" + getMemberBeginLackNum() + ", memberEndLackNum=" + getMemberEndLackNum() + ", totalWorkHours=" + getTotalWorkHours() + ", avgWorkHours=" + getAvgWorkHours() + ")";
    }

    public Integer getMemberTotalNum() {
        return this.memberTotalNum;
    }

    public Integer getMemberNormalNum() {
        return this.memberNormalNum;
    }

    public Integer getMemberLaterNum() {
        return this.memberLaterNum;
    }

    public Integer getMemberEarlyNum() {
        return this.memberEarlyNum;
    }

    public Integer getMemberBeginLackNum() {
        return this.memberBeginLackNum;
    }

    public Integer getMemberEndLackNum() {
        return this.memberEndLackNum;
    }

    public Integer getTotalWorkHours() {
        return this.totalWorkHours;
    }

    public Integer getAvgWorkHours() {
        return this.avgWorkHours;
    }
}

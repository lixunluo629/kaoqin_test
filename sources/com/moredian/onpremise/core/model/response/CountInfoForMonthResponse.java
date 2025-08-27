package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "每月统计列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CountInfoForMonthResponse.class */
public class CountInfoForMonthResponse implements Serializable {

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "memberJobNum", value = "成员工号")
    private String memberJobNum;

    @ApiModelProperty(name = "workDays", value = "工作天数")
    private Integer workDays;

    @ApiModelProperty(name = "restDays", value = "休息天数")
    private Integer restDays;

    @ApiModelProperty(name = "workTime", value = "工作时长，单位秒")
    private Long workTime;

    @ApiModelProperty(name = "lateTimes", value = "迟到次数")
    private Integer lateTimes;

    @ApiModelProperty(name = "lateTime", value = "迟到时长，单位秒")
    private Long lateTime;

    @ApiModelProperty(name = "earlyTimes", value = "早退次数")
    private Integer earlyTimes;

    @ApiModelProperty(name = "earlyTime", value = "早退时长，单位秒")
    private Long earlyTime;

    @ApiModelProperty(name = "beginWorkLackTimes", value = "上班缺卡次数")
    private Integer beginWorkLackTimes;

    @ApiModelProperty(name = "endWorkLackTimes", value = "下班缺卡次数")
    private Integer endWorkLackTimes;

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setWorkDays(Integer workDays) {
        this.workDays = workDays;
    }

    public void setRestDays(Integer restDays) {
        this.restDays = restDays;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public void setLateTimes(Integer lateTimes) {
        this.lateTimes = lateTimes;
    }

    public void setLateTime(Long lateTime) {
        this.lateTime = lateTime;
    }

    public void setEarlyTimes(Integer earlyTimes) {
        this.earlyTimes = earlyTimes;
    }

    public void setEarlyTime(Long earlyTime) {
        this.earlyTime = earlyTime;
    }

    public void setBeginWorkLackTimes(Integer beginWorkLackTimes) {
        this.beginWorkLackTimes = beginWorkLackTimes;
    }

    public void setEndWorkLackTimes(Integer endWorkLackTimes) {
        this.endWorkLackTimes = endWorkLackTimes;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CountInfoForMonthResponse)) {
            return false;
        }
        CountInfoForMonthResponse other = (CountInfoForMonthResponse) o;
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
        Object this$workDays = getWorkDays();
        Object other$workDays = other.getWorkDays();
        if (this$workDays == null) {
            if (other$workDays != null) {
                return false;
            }
        } else if (!this$workDays.equals(other$workDays)) {
            return false;
        }
        Object this$restDays = getRestDays();
        Object other$restDays = other.getRestDays();
        if (this$restDays == null) {
            if (other$restDays != null) {
                return false;
            }
        } else if (!this$restDays.equals(other$restDays)) {
            return false;
        }
        Object this$workTime = getWorkTime();
        Object other$workTime = other.getWorkTime();
        if (this$workTime == null) {
            if (other$workTime != null) {
                return false;
            }
        } else if (!this$workTime.equals(other$workTime)) {
            return false;
        }
        Object this$lateTimes = getLateTimes();
        Object other$lateTimes = other.getLateTimes();
        if (this$lateTimes == null) {
            if (other$lateTimes != null) {
                return false;
            }
        } else if (!this$lateTimes.equals(other$lateTimes)) {
            return false;
        }
        Object this$lateTime = getLateTime();
        Object other$lateTime = other.getLateTime();
        if (this$lateTime == null) {
            if (other$lateTime != null) {
                return false;
            }
        } else if (!this$lateTime.equals(other$lateTime)) {
            return false;
        }
        Object this$earlyTimes = getEarlyTimes();
        Object other$earlyTimes = other.getEarlyTimes();
        if (this$earlyTimes == null) {
            if (other$earlyTimes != null) {
                return false;
            }
        } else if (!this$earlyTimes.equals(other$earlyTimes)) {
            return false;
        }
        Object this$earlyTime = getEarlyTime();
        Object other$earlyTime = other.getEarlyTime();
        if (this$earlyTime == null) {
            if (other$earlyTime != null) {
                return false;
            }
        } else if (!this$earlyTime.equals(other$earlyTime)) {
            return false;
        }
        Object this$beginWorkLackTimes = getBeginWorkLackTimes();
        Object other$beginWorkLackTimes = other.getBeginWorkLackTimes();
        if (this$beginWorkLackTimes == null) {
            if (other$beginWorkLackTimes != null) {
                return false;
            }
        } else if (!this$beginWorkLackTimes.equals(other$beginWorkLackTimes)) {
            return false;
        }
        Object this$endWorkLackTimes = getEndWorkLackTimes();
        Object other$endWorkLackTimes = other.getEndWorkLackTimes();
        return this$endWorkLackTimes == null ? other$endWorkLackTimes == null : this$endWorkLackTimes.equals(other$endWorkLackTimes);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CountInfoForMonthResponse;
    }

    public int hashCode() {
        Object $memberName = getMemberName();
        int result = (1 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberId = getMemberId();
        int result2 = (result * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result3 = (result2 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $workDays = getWorkDays();
        int result4 = (result3 * 59) + ($workDays == null ? 43 : $workDays.hashCode());
        Object $restDays = getRestDays();
        int result5 = (result4 * 59) + ($restDays == null ? 43 : $restDays.hashCode());
        Object $workTime = getWorkTime();
        int result6 = (result5 * 59) + ($workTime == null ? 43 : $workTime.hashCode());
        Object $lateTimes = getLateTimes();
        int result7 = (result6 * 59) + ($lateTimes == null ? 43 : $lateTimes.hashCode());
        Object $lateTime = getLateTime();
        int result8 = (result7 * 59) + ($lateTime == null ? 43 : $lateTime.hashCode());
        Object $earlyTimes = getEarlyTimes();
        int result9 = (result8 * 59) + ($earlyTimes == null ? 43 : $earlyTimes.hashCode());
        Object $earlyTime = getEarlyTime();
        int result10 = (result9 * 59) + ($earlyTime == null ? 43 : $earlyTime.hashCode());
        Object $beginWorkLackTimes = getBeginWorkLackTimes();
        int result11 = (result10 * 59) + ($beginWorkLackTimes == null ? 43 : $beginWorkLackTimes.hashCode());
        Object $endWorkLackTimes = getEndWorkLackTimes();
        return (result11 * 59) + ($endWorkLackTimes == null ? 43 : $endWorkLackTimes.hashCode());
    }

    public String toString() {
        return "CountInfoForMonthResponse(memberName=" + getMemberName() + ", memberId=" + getMemberId() + ", memberJobNum=" + getMemberJobNum() + ", workDays=" + getWorkDays() + ", restDays=" + getRestDays() + ", workTime=" + getWorkTime() + ", lateTimes=" + getLateTimes() + ", lateTime=" + getLateTime() + ", earlyTimes=" + getEarlyTimes() + ", earlyTime=" + getEarlyTime() + ", beginWorkLackTimes=" + getBeginWorkLackTimes() + ", endWorkLackTimes=" + getEndWorkLackTimes() + ")";
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public Integer getWorkDays() {
        return this.workDays;
    }

    public Integer getRestDays() {
        return this.restDays;
    }

    public Long getWorkTime() {
        return this.workTime;
    }

    public Integer getLateTimes() {
        return this.lateTimes;
    }

    public Long getLateTime() {
        return this.lateTime;
    }

    public Integer getEarlyTimes() {
        return this.earlyTimes;
    }

    public Long getEarlyTime() {
        return this.earlyTime;
    }

    public Integer getBeginWorkLackTimes() {
        return this.beginWorkLackTimes;
    }

    public Integer getEndWorkLackTimes() {
        return this.endWorkLackTimes;
    }
}

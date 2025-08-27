package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "终端同步餐厅响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncCanteenResponse.class */
public class TerminalSyncCanteenResponse implements Serializable {

    @ApiModelProperty(name = "mealCanteenId", value = "餐厅id")
    private Long mealCanteenId;

    @ApiModelProperty(name = "canteenName", value = "餐厅名称")
    private String canteenName;

    @ApiModelProperty(name = "canteenScope", value = "[1,2,3,4,5,6,7]")
    private String canteenScope;

    @ApiModelProperty(name = "times", value = "用餐时间段")
    private List<CanteenTime> times;

    @ApiModelProperty(name = "syncTime", value = "同步时间")
    private Long syncTime;

    public void setMealCanteenId(Long mealCanteenId) {
        this.mealCanteenId = mealCanteenId;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public void setCanteenScope(String canteenScope) {
        this.canteenScope = canteenScope;
    }

    public void setTimes(List<CanteenTime> times) {
        this.times = times;
    }

    public void setSyncTime(Long syncTime) {
        this.syncTime = syncTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncCanteenResponse)) {
            return false;
        }
        TerminalSyncCanteenResponse other = (TerminalSyncCanteenResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$mealCanteenId = getMealCanteenId();
        Object other$mealCanteenId = other.getMealCanteenId();
        if (this$mealCanteenId == null) {
            if (other$mealCanteenId != null) {
                return false;
            }
        } else if (!this$mealCanteenId.equals(other$mealCanteenId)) {
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
        Object this$canteenScope = getCanteenScope();
        Object other$canteenScope = other.getCanteenScope();
        if (this$canteenScope == null) {
            if (other$canteenScope != null) {
                return false;
            }
        } else if (!this$canteenScope.equals(other$canteenScope)) {
            return false;
        }
        Object this$times = getTimes();
        Object other$times = other.getTimes();
        if (this$times == null) {
            if (other$times != null) {
                return false;
            }
        } else if (!this$times.equals(other$times)) {
            return false;
        }
        Object this$syncTime = getSyncTime();
        Object other$syncTime = other.getSyncTime();
        return this$syncTime == null ? other$syncTime == null : this$syncTime.equals(other$syncTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncCanteenResponse;
    }

    public int hashCode() {
        Object $mealCanteenId = getMealCanteenId();
        int result = (1 * 59) + ($mealCanteenId == null ? 43 : $mealCanteenId.hashCode());
        Object $canteenName = getCanteenName();
        int result2 = (result * 59) + ($canteenName == null ? 43 : $canteenName.hashCode());
        Object $canteenScope = getCanteenScope();
        int result3 = (result2 * 59) + ($canteenScope == null ? 43 : $canteenScope.hashCode());
        Object $times = getTimes();
        int result4 = (result3 * 59) + ($times == null ? 43 : $times.hashCode());
        Object $syncTime = getSyncTime();
        return (result4 * 59) + ($syncTime == null ? 43 : $syncTime.hashCode());
    }

    public String toString() {
        return "TerminalSyncCanteenResponse(mealCanteenId=" + getMealCanteenId() + ", canteenName=" + getCanteenName() + ", canteenScope=" + getCanteenScope() + ", times=" + getTimes() + ", syncTime=" + getSyncTime() + ")";
    }

    public Long getMealCanteenId() {
        return this.mealCanteenId;
    }

    public String getCanteenName() {
        return this.canteenName;
    }

    public String getCanteenScope() {
        return this.canteenScope;
    }

    public List<CanteenTime> getTimes() {
        return this.times;
    }

    /* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncCanteenResponse$CanteenTime.class */
    public static class CanteenTime implements Serializable {

        @ApiModelProperty(name = "timeType", value = "就餐时间类型：1-早餐，2-午餐，3-晚餐，4-宵夜")
        private Integer timeType;

        @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "用餐开始时间")
        private String startTime;

        @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "用餐结束时间")
        private String endTime;

        public void setTimeType(Integer timeType) {
            this.timeType = timeType;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CanteenTime)) {
                return false;
            }
            CanteenTime other = (CanteenTime) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$timeType = getTimeType();
            Object other$timeType = other.getTimeType();
            if (this$timeType == null) {
                if (other$timeType != null) {
                    return false;
                }
            } else if (!this$timeType.equals(other$timeType)) {
                return false;
            }
            Object this$startTime = getStartTime();
            Object other$startTime = other.getStartTime();
            if (this$startTime == null) {
                if (other$startTime != null) {
                    return false;
                }
            } else if (!this$startTime.equals(other$startTime)) {
                return false;
            }
            Object this$endTime = getEndTime();
            Object other$endTime = other.getEndTime();
            return this$endTime == null ? other$endTime == null : this$endTime.equals(other$endTime);
        }

        protected boolean canEqual(Object other) {
            return other instanceof CanteenTime;
        }

        public int hashCode() {
            Object $timeType = getTimeType();
            int result = (1 * 59) + ($timeType == null ? 43 : $timeType.hashCode());
            Object $startTime = getStartTime();
            int result2 = (result * 59) + ($startTime == null ? 43 : $startTime.hashCode());
            Object $endTime = getEndTime();
            return (result2 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        }

        public String toString() {
            return "TerminalSyncCanteenResponse.CanteenTime(timeType=" + getTimeType() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ")";
        }

        public Integer getTimeType() {
            return this.timeType;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public String getEndTime() {
            return this.endTime;
        }
    }

    public Long getSyncTime() {
        return this.syncTime;
    }
}

package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "识别记录分析请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/StatisticsVerifyScoreRequest.class */
public class StatisticsVerifyScoreRequest extends BaseRequest {

    @ApiModelProperty(name = "startTimeStr", value = "开始时间字符串，1.5.7后废弃，请及时用startTimestamp代替")
    private String startTimeStr;

    @ApiModelProperty(name = "endTimeStr", value = "结束时间字符串，1.5.7后废弃，请及时用startTimestamp代替")
    private String endTimeStr;

    @ApiModelProperty(name = "startTimestamp", value = "开始时间戳例：1586500125121")
    private Long startTimestamp;

    @ApiModelProperty(name = "endTimestamp", value = "结束时间戳例：1586500125121")
    private Long endTimestamp;

    @ApiModelProperty(name = "startDate", value = "开始时间日期例：20200410")
    private Integer startDate;

    @ApiModelProperty(name = "endDate", value = "结束时间日期例：20200410")
    private Integer endDate;

    @ApiModelProperty(name = "deviceSn", value = "设备号")
    private String deviceSn;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_APP_TYPE_KEY, value = "业务：1-魔点门禁，2-魔点考勤，3-魔点签到，4-魔点访客，5-魔点团餐")
    private Integer appType;

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StatisticsVerifyScoreRequest)) {
            return false;
        }
        StatisticsVerifyScoreRequest other = (StatisticsVerifyScoreRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$startTimeStr = getStartTimeStr();
        Object other$startTimeStr = other.getStartTimeStr();
        if (this$startTimeStr == null) {
            if (other$startTimeStr != null) {
                return false;
            }
        } else if (!this$startTimeStr.equals(other$startTimeStr)) {
            return false;
        }
        Object this$endTimeStr = getEndTimeStr();
        Object other$endTimeStr = other.getEndTimeStr();
        if (this$endTimeStr == null) {
            if (other$endTimeStr != null) {
                return false;
            }
        } else if (!this$endTimeStr.equals(other$endTimeStr)) {
            return false;
        }
        Object this$startTimestamp = getStartTimestamp();
        Object other$startTimestamp = other.getStartTimestamp();
        if (this$startTimestamp == null) {
            if (other$startTimestamp != null) {
                return false;
            }
        } else if (!this$startTimestamp.equals(other$startTimestamp)) {
            return false;
        }
        Object this$endTimestamp = getEndTimestamp();
        Object other$endTimestamp = other.getEndTimestamp();
        if (this$endTimestamp == null) {
            if (other$endTimestamp != null) {
                return false;
            }
        } else if (!this$endTimestamp.equals(other$endTimestamp)) {
            return false;
        }
        Object this$startDate = getStartDate();
        Object other$startDate = other.getStartDate();
        if (this$startDate == null) {
            if (other$startDate != null) {
                return false;
            }
        } else if (!this$startDate.equals(other$startDate)) {
            return false;
        }
        Object this$endDate = getEndDate();
        Object other$endDate = other.getEndDate();
        if (this$endDate == null) {
            if (other$endDate != null) {
                return false;
            }
        } else if (!this$endDate.equals(other$endDate)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        return this$appType == null ? other$appType == null : this$appType.equals(other$appType);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof StatisticsVerifyScoreRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $startTimeStr = getStartTimeStr();
        int result = (1 * 59) + ($startTimeStr == null ? 43 : $startTimeStr.hashCode());
        Object $endTimeStr = getEndTimeStr();
        int result2 = (result * 59) + ($endTimeStr == null ? 43 : $endTimeStr.hashCode());
        Object $startTimestamp = getStartTimestamp();
        int result3 = (result2 * 59) + ($startTimestamp == null ? 43 : $startTimestamp.hashCode());
        Object $endTimestamp = getEndTimestamp();
        int result4 = (result3 * 59) + ($endTimestamp == null ? 43 : $endTimestamp.hashCode());
        Object $startDate = getStartDate();
        int result5 = (result4 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result6 = (result5 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $deviceSn = getDeviceSn();
        int result7 = (result6 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $appType = getAppType();
        return (result7 * 59) + ($appType == null ? 43 : $appType.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "StatisticsVerifyScoreRequest(startTimeStr=" + getStartTimeStr() + ", endTimeStr=" + getEndTimeStr() + ", startTimestamp=" + getStartTimestamp() + ", endTimestamp=" + getEndTimestamp() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", deviceSn=" + getDeviceSn() + ", appType=" + getAppType() + ")";
    }

    public String getStartTimeStr() {
        return this.startTimeStr;
    }

    public String getEndTimeStr() {
        return this.endTimeStr;
    }

    public Long getStartTimestamp() {
        return this.startTimestamp;
    }

    public Long getEndTimestamp() {
        return this.endTimestamp;
    }

    public Integer getStartDate() {
        return this.startDate;
    }

    public Integer getEndDate() {
        return this.endDate;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getAppType() {
        return this.appType;
    }
}

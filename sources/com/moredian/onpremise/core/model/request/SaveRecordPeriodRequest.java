package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存记录存储周期请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveRecordPeriodRequest.class */
public class SaveRecordPeriodRequest extends BaseRequest {
    private static final long serialVersionUID = 3297946243804776891L;

    @ApiModelProperty(name = "monthNum", value = "记录存储月份数")
    private String monthNum;

    @ApiModelProperty(name = "dayNum", value = "记录存储天份数")
    private String dayNum;

    public void setMonthNum(String monthNum) {
        this.monthNum = monthNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveRecordPeriodRequest)) {
            return false;
        }
        SaveRecordPeriodRequest other = (SaveRecordPeriodRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$monthNum = getMonthNum();
        Object other$monthNum = other.getMonthNum();
        if (this$monthNum == null) {
            if (other$monthNum != null) {
                return false;
            }
        } else if (!this$monthNum.equals(other$monthNum)) {
            return false;
        }
        Object this$dayNum = getDayNum();
        Object other$dayNum = other.getDayNum();
        return this$dayNum == null ? other$dayNum == null : this$dayNum.equals(other$dayNum);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveRecordPeriodRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $monthNum = getMonthNum();
        int result = (1 * 59) + ($monthNum == null ? 43 : $monthNum.hashCode());
        Object $dayNum = getDayNum();
        return (result * 59) + ($dayNum == null ? 43 : $dayNum.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveRecordPeriodRequest(monthNum=" + getMonthNum() + ", dayNum=" + getDayNum() + ")";
    }

    public String getMonthNum() {
        return this.monthNum;
    }

    public String getDayNum() {
        return this.dayNum;
    }
}

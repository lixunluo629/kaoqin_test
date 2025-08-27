package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "查询考勤组日期时间范围响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryAttendanceGroupDateFrameResponse.class */
public class QueryAttendanceGroupDateFrameResponse implements Serializable {

    @ApiModelProperty(name = "startDate", value = "开始日期")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "结束日期")
    private String endDate;

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryAttendanceGroupDateFrameResponse)) {
            return false;
        }
        QueryAttendanceGroupDateFrameResponse other = (QueryAttendanceGroupDateFrameResponse) o;
        if (!other.canEqual(this)) {
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
        return this$endDate == null ? other$endDate == null : this$endDate.equals(other$endDate);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryAttendanceGroupDateFrameResponse;
    }

    public int hashCode() {
        Object $startDate = getStartDate();
        int result = (1 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        return (result * 59) + ($endDate == null ? 43 : $endDate.hashCode());
    }

    public String toString() {
        return "QueryAttendanceGroupDateFrameResponse(startDate=" + getStartDate() + ", endDate=" + getEndDate() + ")";
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }
}

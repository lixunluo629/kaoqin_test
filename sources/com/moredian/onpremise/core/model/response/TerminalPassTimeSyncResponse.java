package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端通行时间段同步响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalPassTimeSyncResponse.class */
public class TerminalPassTimeSyncResponse implements Serializable {
    private static final long serialVersionUID = 6364797531250542246L;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "权限开始时间")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "权限结束时间")
    private String endTime;

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
        if (!(o instanceof TerminalPassTimeSyncResponse)) {
            return false;
        }
        TerminalPassTimeSyncResponse other = (TerminalPassTimeSyncResponse) o;
        if (!other.canEqual(this)) {
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
        return other instanceof TerminalPassTimeSyncResponse;
    }

    public int hashCode() {
        Object $startTime = getStartTime();
        int result = (1 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        return (result * 59) + ($endTime == null ? 43 : $endTime.hashCode());
    }

    public String toString() {
        return "TerminalPassTimeSyncResponse(startTime=" + getStartTime() + ", endTime=" + getEndTime() + ")";
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }
}

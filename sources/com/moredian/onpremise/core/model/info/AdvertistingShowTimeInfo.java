package com.moredian.onpremise.core.model.info;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "广告播放时间列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/AdvertistingShowTimeInfo.class */
public class AdvertistingShowTimeInfo implements Serializable {
    private static final long serialVersionUID = 8079844228063563965L;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "广告开始时间，例：HH:mm")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "广告结束时间，例：HH:mm")
    private String endTime;

    @ApiModelProperty(name = "scope", value = "广告播放重复周期")
    private String scope;

    @ApiModelProperty(name = "startDate", value = "广告开始日期，例：yyyy-MM-dd")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "广告结束日期，例：yyyy-MM-dd")
    private String endDate;

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

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
        if (!(o instanceof AdvertistingShowTimeInfo)) {
            return false;
        }
        AdvertistingShowTimeInfo other = (AdvertistingShowTimeInfo) o;
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
        if (this$endTime == null) {
            if (other$endTime != null) {
                return false;
            }
        } else if (!this$endTime.equals(other$endTime)) {
            return false;
        }
        Object this$scope = getScope();
        Object other$scope = other.getScope();
        if (this$scope == null) {
            if (other$scope != null) {
                return false;
            }
        } else if (!this$scope.equals(other$scope)) {
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
        return other instanceof AdvertistingShowTimeInfo;
    }

    public int hashCode() {
        Object $startTime = getStartTime();
        int result = (1 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result2 = (result * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $scope = getScope();
        int result3 = (result2 * 59) + ($scope == null ? 43 : $scope.hashCode());
        Object $startDate = getStartDate();
        int result4 = (result3 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        return (result4 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
    }

    public String toString() {
        return "AdvertistingShowTimeInfo(startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", scope=" + getScope() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ")";
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getScope() {
        return this.scope;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }
}

package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "终端权限组权限范围同步响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalGroupAuthSyncResponse.class */
public class TerminalGroupAuthSyncResponse implements Serializable {
    private static final long serialVersionUID = -4579548086648861942L;

    @ApiModelProperty(name = "groupId", value = "权限组id", hidden = true)
    private Long groupId;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "1.5.x版本后将废弃，请及时用passTimeList替代，权限开始时间")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "1.5.x版本后将废弃，请及时用passTimeList替代，权限结束时间")
    private String endTime;

    @ApiModelProperty(name = "scope", value = "开放权限重复日期")
    private String scope;

    @ApiModelProperty(name = "startDate", value = "权限开始日期，例：2019-10-01 12:12:12")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "权限结束日期，例：2019-10-01 12:12:12")
    private String endDate;

    @ApiModelProperty(name = "passTimeList", value = "允许通行时间段，例[{\"startTime\":\"09:21\",\"endTime\":\"10:11\"}]")
    private List<TerminalPassTimeSyncResponse> passTimeList;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

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

    public void setPassTimeList(List<TerminalPassTimeSyncResponse> passTimeList) {
        this.passTimeList = passTimeList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalGroupAuthSyncResponse)) {
            return false;
        }
        TerminalGroupAuthSyncResponse other = (TerminalGroupAuthSyncResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupId = getGroupId();
        Object other$groupId = other.getGroupId();
        if (this$groupId == null) {
            if (other$groupId != null) {
                return false;
            }
        } else if (!this$groupId.equals(other$groupId)) {
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
        if (this$endDate == null) {
            if (other$endDate != null) {
                return false;
            }
        } else if (!this$endDate.equals(other$endDate)) {
            return false;
        }
        Object this$passTimeList = getPassTimeList();
        Object other$passTimeList = other.getPassTimeList();
        return this$passTimeList == null ? other$passTimeList == null : this$passTimeList.equals(other$passTimeList);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalGroupAuthSyncResponse;
    }

    public int hashCode() {
        Object $groupId = getGroupId();
        int result = (1 * 59) + ($groupId == null ? 43 : $groupId.hashCode());
        Object $startTime = getStartTime();
        int result2 = (result * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result3 = (result2 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $scope = getScope();
        int result4 = (result3 * 59) + ($scope == null ? 43 : $scope.hashCode());
        Object $startDate = getStartDate();
        int result5 = (result4 * 59) + ($startDate == null ? 43 : $startDate.hashCode());
        Object $endDate = getEndDate();
        int result6 = (result5 * 59) + ($endDate == null ? 43 : $endDate.hashCode());
        Object $passTimeList = getPassTimeList();
        return (result6 * 59) + ($passTimeList == null ? 43 : $passTimeList.hashCode());
    }

    public String toString() {
        return "TerminalGroupAuthSyncResponse(groupId=" + getGroupId() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", scope=" + getScope() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + ", passTimeList=" + getPassTimeList() + ")";
    }

    public Long getGroupId() {
        return this.groupId;
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

    public List<TerminalPassTimeSyncResponse> getPassTimeList() {
        return this.passTimeList;
    }
}

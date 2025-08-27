package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "签到记录列表查询参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CheckInLogListRequest.class */
public class CheckInLogListRequest extends BaseRequest {

    @ApiModelProperty(name = "paginator", value = "分页器对象")
    private Paginator paginator;

    @ApiModelProperty(name = "checkInTaskLogId", value = "任务日志id，选填")
    private Long checkInTaskLogId;

    @ApiModelProperty(name = "checkInTaskId", value = "任务id，选填")
    private Long checkInTaskId;

    @ApiModelProperty(name = "checkInTaskIds", value = "任务列表，选填")
    private List<Long> checkInTaskIds;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "列表类型, 0:全部；1：已签到；2：未签到，选填")
    private Integer status;

    @ApiModelProperty(name = "checkInMemberId", value = "成员，选填")
    private Long checkInMemberId;

    @ApiModelProperty(name = "checkInMemberId", value = "成员列表，选填")
    private List<Long> checkInMemberIds;

    @ApiModelProperty(name = "checkInDeptIds", value = "部门列表，选填")
    private List<Long> checkInDeptIds;

    @ApiModelProperty(name = "checkInTaskTimeStr", value = "日期，选填")
    private String checkInTaskTimeStr;

    @ApiModelProperty(name = "checkInTaskName", value = "任务名称，选填")
    private String checkInTaskName;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "开始时间，选填")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "结束时间，选填")
    private String endTime;

    @ApiModelProperty(name = "checkInMemberName", value = "成员名称，选填")
    private String checkInMemberName;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setCheckInTaskLogId(Long checkInTaskLogId) {
        this.checkInTaskLogId = checkInTaskLogId;
    }

    public void setCheckInTaskId(Long checkInTaskId) {
        this.checkInTaskId = checkInTaskId;
    }

    public void setCheckInTaskIds(List<Long> checkInTaskIds) {
        this.checkInTaskIds = checkInTaskIds;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCheckInMemberId(Long checkInMemberId) {
        this.checkInMemberId = checkInMemberId;
    }

    public void setCheckInMemberIds(List<Long> checkInMemberIds) {
        this.checkInMemberIds = checkInMemberIds;
    }

    public void setCheckInDeptIds(List<Long> checkInDeptIds) {
        this.checkInDeptIds = checkInDeptIds;
    }

    public void setCheckInTaskTimeStr(String checkInTaskTimeStr) {
        this.checkInTaskTimeStr = checkInTaskTimeStr;
    }

    public void setCheckInTaskName(String checkInTaskName) {
        this.checkInTaskName = checkInTaskName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCheckInMemberName(String checkInMemberName) {
        this.checkInMemberName = checkInMemberName;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInLogListRequest)) {
            return false;
        }
        CheckInLogListRequest other = (CheckInLogListRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
            return false;
        }
        Object this$checkInTaskLogId = getCheckInTaskLogId();
        Object other$checkInTaskLogId = other.getCheckInTaskLogId();
        if (this$checkInTaskLogId == null) {
            if (other$checkInTaskLogId != null) {
                return false;
            }
        } else if (!this$checkInTaskLogId.equals(other$checkInTaskLogId)) {
            return false;
        }
        Object this$checkInTaskId = getCheckInTaskId();
        Object other$checkInTaskId = other.getCheckInTaskId();
        if (this$checkInTaskId == null) {
            if (other$checkInTaskId != null) {
                return false;
            }
        } else if (!this$checkInTaskId.equals(other$checkInTaskId)) {
            return false;
        }
        Object this$checkInTaskIds = getCheckInTaskIds();
        Object other$checkInTaskIds = other.getCheckInTaskIds();
        if (this$checkInTaskIds == null) {
            if (other$checkInTaskIds != null) {
                return false;
            }
        } else if (!this$checkInTaskIds.equals(other$checkInTaskIds)) {
            return false;
        }
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        if (this$status == null) {
            if (other$status != null) {
                return false;
            }
        } else if (!this$status.equals(other$status)) {
            return false;
        }
        Object this$checkInMemberId = getCheckInMemberId();
        Object other$checkInMemberId = other.getCheckInMemberId();
        if (this$checkInMemberId == null) {
            if (other$checkInMemberId != null) {
                return false;
            }
        } else if (!this$checkInMemberId.equals(other$checkInMemberId)) {
            return false;
        }
        Object this$checkInMemberIds = getCheckInMemberIds();
        Object other$checkInMemberIds = other.getCheckInMemberIds();
        if (this$checkInMemberIds == null) {
            if (other$checkInMemberIds != null) {
                return false;
            }
        } else if (!this$checkInMemberIds.equals(other$checkInMemberIds)) {
            return false;
        }
        Object this$checkInDeptIds = getCheckInDeptIds();
        Object other$checkInDeptIds = other.getCheckInDeptIds();
        if (this$checkInDeptIds == null) {
            if (other$checkInDeptIds != null) {
                return false;
            }
        } else if (!this$checkInDeptIds.equals(other$checkInDeptIds)) {
            return false;
        }
        Object this$checkInTaskTimeStr = getCheckInTaskTimeStr();
        Object other$checkInTaskTimeStr = other.getCheckInTaskTimeStr();
        if (this$checkInTaskTimeStr == null) {
            if (other$checkInTaskTimeStr != null) {
                return false;
            }
        } else if (!this$checkInTaskTimeStr.equals(other$checkInTaskTimeStr)) {
            return false;
        }
        Object this$checkInTaskName = getCheckInTaskName();
        Object other$checkInTaskName = other.getCheckInTaskName();
        if (this$checkInTaskName == null) {
            if (other$checkInTaskName != null) {
                return false;
            }
        } else if (!this$checkInTaskName.equals(other$checkInTaskName)) {
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
        Object this$checkInMemberName = getCheckInMemberName();
        Object other$checkInMemberName = other.getCheckInMemberName();
        return this$checkInMemberName == null ? other$checkInMemberName == null : this$checkInMemberName.equals(other$checkInMemberName);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CheckInLogListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $checkInTaskLogId = getCheckInTaskLogId();
        int result2 = (result * 59) + ($checkInTaskLogId == null ? 43 : $checkInTaskLogId.hashCode());
        Object $checkInTaskId = getCheckInTaskId();
        int result3 = (result2 * 59) + ($checkInTaskId == null ? 43 : $checkInTaskId.hashCode());
        Object $checkInTaskIds = getCheckInTaskIds();
        int result4 = (result3 * 59) + ($checkInTaskIds == null ? 43 : $checkInTaskIds.hashCode());
        Object $status = getStatus();
        int result5 = (result4 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $checkInMemberId = getCheckInMemberId();
        int result6 = (result5 * 59) + ($checkInMemberId == null ? 43 : $checkInMemberId.hashCode());
        Object $checkInMemberIds = getCheckInMemberIds();
        int result7 = (result6 * 59) + ($checkInMemberIds == null ? 43 : $checkInMemberIds.hashCode());
        Object $checkInDeptIds = getCheckInDeptIds();
        int result8 = (result7 * 59) + ($checkInDeptIds == null ? 43 : $checkInDeptIds.hashCode());
        Object $checkInTaskTimeStr = getCheckInTaskTimeStr();
        int result9 = (result8 * 59) + ($checkInTaskTimeStr == null ? 43 : $checkInTaskTimeStr.hashCode());
        Object $checkInTaskName = getCheckInTaskName();
        int result10 = (result9 * 59) + ($checkInTaskName == null ? 43 : $checkInTaskName.hashCode());
        Object $startTime = getStartTime();
        int result11 = (result10 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result12 = (result11 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $checkInMemberName = getCheckInMemberName();
        return (result12 * 59) + ($checkInMemberName == null ? 43 : $checkInMemberName.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CheckInLogListRequest(paginator=" + getPaginator() + ", checkInTaskLogId=" + getCheckInTaskLogId() + ", checkInTaskId=" + getCheckInTaskId() + ", checkInTaskIds=" + getCheckInTaskIds() + ", status=" + getStatus() + ", checkInMemberId=" + getCheckInMemberId() + ", checkInMemberIds=" + getCheckInMemberIds() + ", checkInDeptIds=" + getCheckInDeptIds() + ", checkInTaskTimeStr=" + getCheckInTaskTimeStr() + ", checkInTaskName=" + getCheckInTaskName() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", checkInMemberName=" + getCheckInMemberName() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Long getCheckInTaskLogId() {
        return this.checkInTaskLogId;
    }

    public Long getCheckInTaskId() {
        return this.checkInTaskId;
    }

    public List<Long> getCheckInTaskIds() {
        return this.checkInTaskIds;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Long getCheckInMemberId() {
        return this.checkInMemberId;
    }

    public List<Long> getCheckInMemberIds() {
        return this.checkInMemberIds;
    }

    public List<Long> getCheckInDeptIds() {
        return this.checkInDeptIds;
    }

    public String getCheckInTaskTimeStr() {
        return this.checkInTaskTimeStr;
    }

    public String getCheckInTaskName() {
        return this.checkInTaskName;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getCheckInMemberName() {
        return this.checkInMemberName;
    }
}

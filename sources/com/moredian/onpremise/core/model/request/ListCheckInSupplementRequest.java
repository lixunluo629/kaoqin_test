package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "签到补签记录")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListCheckInSupplementRequest.class */
public class ListCheckInSupplementRequest extends BaseRequest {
    private static final long serialVersionUID = -4006730507606205788L;

    @ApiModelProperty(name = "taskId", value = "任务ID")
    private Long taskId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "查询开始时间戳")
    private Long startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "查询结束时间戳")
    private Long endTime;

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListCheckInSupplementRequest)) {
            return false;
        }
        ListCheckInSupplementRequest other = (ListCheckInSupplementRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$taskId = getTaskId();
        Object other$taskId = other.getTaskId();
        if (this$taskId == null) {
            if (other$taskId != null) {
                return false;
            }
        } else if (!this$taskId.equals(other$taskId)) {
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
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        return this$paginator == null ? other$paginator == null : this$paginator.equals(other$paginator);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListCheckInSupplementRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $taskId = getTaskId();
        int result = (1 * 59) + ($taskId == null ? 43 : $taskId.hashCode());
        Object $memberName = getMemberName();
        int result2 = (result * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $startTime = getStartTime();
        int result3 = (result2 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result4 = (result3 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $paginator = getPaginator();
        return (result4 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListCheckInSupplementRequest(taskId=" + getTaskId() + ", memberName=" + getMemberName() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", paginator=" + getPaginator() + ")";
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public Paginator getPaginator() {
        return this.paginator;
    }
}

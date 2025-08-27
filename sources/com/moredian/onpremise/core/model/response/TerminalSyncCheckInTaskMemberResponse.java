package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "终端同步签到任务成员响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncCheckInTaskMemberResponse.class */
public class TerminalSyncCheckInTaskMemberResponse implements Serializable {

    @ApiModelProperty(name = "taskId", value = "签到任务id")
    private Long taskId;

    @ApiModelProperty(name = "type", value = "类型，1：部门；2：成员")
    private Integer type;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "不需签到，1：是；2：否，标记为是的成员，对当前签到任务不需要签到")
    private Integer status;

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncCheckInTaskMemberResponse)) {
            return false;
        }
        TerminalSyncCheckInTaskMemberResponse other = (TerminalSyncCheckInTaskMemberResponse) o;
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
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$deptId = getDeptId();
        Object other$deptId = other.getDeptId();
        if (this$deptId == null) {
            if (other$deptId != null) {
                return false;
            }
        } else if (!this$deptId.equals(other$deptId)) {
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
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        return this$status == null ? other$status == null : this$status.equals(other$status);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncCheckInTaskMemberResponse;
    }

    public int hashCode() {
        Object $taskId = getTaskId();
        int result = (1 * 59) + ($taskId == null ? 43 : $taskId.hashCode());
        Object $type = getType();
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $deptId = getDeptId();
        int result3 = (result2 * 59) + ($deptId == null ? 43 : $deptId.hashCode());
        Object $memberId = getMemberId();
        int result4 = (result3 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $status = getStatus();
        return (result4 * 59) + ($status == null ? 43 : $status.hashCode());
    }

    public String toString() {
        return "TerminalSyncCheckInTaskMemberResponse(taskId=" + getTaskId() + ", type=" + getType() + ", deptId=" + getDeptId() + ", memberId=" + getMemberId() + ", status=" + getStatus() + ")";
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public Integer getType() {
        return this.type;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Integer getStatus() {
        return this.status;
    }
}

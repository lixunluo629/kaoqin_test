package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "终端同步签到任务数据请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSyncCheckInTaskRequest.class */
public class TerminalSyncCheckInTaskRequest extends TerminalSyncRequest {

    @ApiModelProperty(name = "taskId", value = "任务id")
    private Long taskId;

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncCheckInTaskRequest)) {
            return false;
        }
        TerminalSyncCheckInTaskRequest other = (TerminalSyncCheckInTaskRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$taskId = getTaskId();
        Object other$taskId = other.getTaskId();
        return this$taskId == null ? other$taskId == null : this$taskId.equals(other$taskId);
    }

    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncCheckInTaskRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    public int hashCode() {
        Object $taskId = getTaskId();
        int result = (1 * 59) + ($taskId == null ? 43 : $taskId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.TerminalSyncRequest
    public String toString() {
        return "TerminalSyncCheckInTaskRequest(taskId=" + getTaskId() + ")";
    }

    public Long getTaskId() {
        return this.taskId;
    }
}

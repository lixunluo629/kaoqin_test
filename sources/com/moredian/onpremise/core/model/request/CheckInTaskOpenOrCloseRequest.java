package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "禁用或启用签到任务请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CheckInTaskOpenOrCloseRequest.class */
public class CheckInTaskOpenOrCloseRequest extends BaseRequest {
    private static final long serialVersionUID = -7043246486709793706L;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "状态1：开启；2：关闭，默认开启")
    private Integer status;

    @ApiModelProperty(name = "checkTaskId", value = "签到任务id")
    private Long checkTaskId;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCheckTaskId(Long checkTaskId) {
        this.checkTaskId = checkTaskId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInTaskOpenOrCloseRequest)) {
            return false;
        }
        CheckInTaskOpenOrCloseRequest other = (CheckInTaskOpenOrCloseRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$checkTaskId = getCheckTaskId();
        Object other$checkTaskId = other.getCheckTaskId();
        return this$checkTaskId == null ? other$checkTaskId == null : this$checkTaskId.equals(other$checkTaskId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CheckInTaskOpenOrCloseRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $status = getStatus();
        int result = (1 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $checkTaskId = getCheckTaskId();
        return (result * 59) + ($checkTaskId == null ? 43 : $checkTaskId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CheckInTaskOpenOrCloseRequest(status=" + getStatus() + ", checkTaskId=" + getCheckTaskId() + ")";
    }

    public Integer getStatus() {
        return this.status;
    }

    public Long getCheckTaskId() {
        return this.checkTaskId;
    }
}

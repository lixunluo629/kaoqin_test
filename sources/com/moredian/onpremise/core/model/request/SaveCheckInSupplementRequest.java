package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "签到补签记录")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveCheckInSupplementRequest.class */
public class SaveCheckInSupplementRequest extends BaseRequest {
    private static final long serialVersionUID = -7884232712376289731L;

    @ApiModelProperty(name = "supplementId", value = "补签记录ID")
    private Long supplementId;

    @ApiModelProperty(name = "taskId", value = "任务ID")
    private Long taskId;

    @ApiModelProperty(name = "memberIds", value = "成员id，多个id用逗号分隔")
    private List<Long> memberIds;

    @ApiModelProperty(name = "supplementTime", value = "签到时间")
    private Long supplementTime;

    @ApiModelProperty(name = "supplementReason", value = "签到事由")
    private String supplementReason;

    public void setSupplementId(Long supplementId) {
        this.supplementId = supplementId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public void setSupplementTime(Long supplementTime) {
        this.supplementTime = supplementTime;
    }

    public void setSupplementReason(String supplementReason) {
        this.supplementReason = supplementReason;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveCheckInSupplementRequest)) {
            return false;
        }
        SaveCheckInSupplementRequest other = (SaveCheckInSupplementRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$supplementId = getSupplementId();
        Object other$supplementId = other.getSupplementId();
        if (this$supplementId == null) {
            if (other$supplementId != null) {
                return false;
            }
        } else if (!this$supplementId.equals(other$supplementId)) {
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
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        if (this$memberIds == null) {
            if (other$memberIds != null) {
                return false;
            }
        } else if (!this$memberIds.equals(other$memberIds)) {
            return false;
        }
        Object this$supplementTime = getSupplementTime();
        Object other$supplementTime = other.getSupplementTime();
        if (this$supplementTime == null) {
            if (other$supplementTime != null) {
                return false;
            }
        } else if (!this$supplementTime.equals(other$supplementTime)) {
            return false;
        }
        Object this$supplementReason = getSupplementReason();
        Object other$supplementReason = other.getSupplementReason();
        return this$supplementReason == null ? other$supplementReason == null : this$supplementReason.equals(other$supplementReason);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveCheckInSupplementRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $supplementId = getSupplementId();
        int result = (1 * 59) + ($supplementId == null ? 43 : $supplementId.hashCode());
        Object $taskId = getTaskId();
        int result2 = (result * 59) + ($taskId == null ? 43 : $taskId.hashCode());
        Object $memberIds = getMemberIds();
        int result3 = (result2 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $supplementTime = getSupplementTime();
        int result4 = (result3 * 59) + ($supplementTime == null ? 43 : $supplementTime.hashCode());
        Object $supplementReason = getSupplementReason();
        return (result4 * 59) + ($supplementReason == null ? 43 : $supplementReason.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveCheckInSupplementRequest(supplementId=" + getSupplementId() + ", taskId=" + getTaskId() + ", memberIds=" + getMemberIds() + ", supplementTime=" + getSupplementTime() + ", supplementReason=" + getSupplementReason() + ")";
    }

    public Long getSupplementId() {
        return this.supplementId;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }

    public Long getSupplementTime() {
        return this.supplementTime;
    }

    public String getSupplementReason() {
        return this.supplementReason;
    }
}

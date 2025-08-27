package com.moredian.onpremise.core.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "签到补签信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInSupplementResponse.class */
public class CheckInSupplementResponse implements Serializable {
    private static final long serialVersionUID = -8093666431690648541L;

    @ApiModelProperty(name = "supplementId", value = "补签记录ID")
    private Long supplementId;

    @ApiModelProperty(name = "taskId", value = "任务ID")
    private Long taskId;

    @ApiModelProperty(name = "taskName", value = "任务名称")
    private String taskName;

    @ApiModelProperty(name = "memberIds", value = "成员id，多个id用逗号分隔")
    private List<Long> memberIds;

    @JsonIgnore
    private Long memberId;

    @ApiModelProperty(name = "memberName", value = "成员名称")
    private String memberName;

    @ApiModelProperty(name = "supplementTime", value = "补签时间")
    private Long supplementTime;

    @ApiModelProperty(name = "supplementReason", value = "签到事由")
    private String supplementReason;

    public void setSupplementId(Long supplementId) {
        this.supplementId = supplementId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setSupplementTime(Long supplementTime) {
        this.supplementTime = supplementTime;
    }

    public void setSupplementReason(String supplementReason) {
        this.supplementReason = supplementReason;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInSupplementResponse)) {
            return false;
        }
        CheckInSupplementResponse other = (CheckInSupplementResponse) o;
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
        Object this$taskName = getTaskName();
        Object other$taskName = other.getTaskName();
        if (this$taskName == null) {
            if (other$taskName != null) {
                return false;
            }
        } else if (!this$taskName.equals(other$taskName)) {
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
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

    protected boolean canEqual(Object other) {
        return other instanceof CheckInSupplementResponse;
    }

    public int hashCode() {
        Object $supplementId = getSupplementId();
        int result = (1 * 59) + ($supplementId == null ? 43 : $supplementId.hashCode());
        Object $taskId = getTaskId();
        int result2 = (result * 59) + ($taskId == null ? 43 : $taskId.hashCode());
        Object $taskName = getTaskName();
        int result3 = (result2 * 59) + ($taskName == null ? 43 : $taskName.hashCode());
        Object $memberIds = getMemberIds();
        int result4 = (result3 * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
        Object $memberId = getMemberId();
        int result5 = (result4 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $memberName = getMemberName();
        int result6 = (result5 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $supplementTime = getSupplementTime();
        int result7 = (result6 * 59) + ($supplementTime == null ? 43 : $supplementTime.hashCode());
        Object $supplementReason = getSupplementReason();
        return (result7 * 59) + ($supplementReason == null ? 43 : $supplementReason.hashCode());
    }

    public String toString() {
        return "CheckInSupplementResponse(supplementId=" + getSupplementId() + ", taskId=" + getTaskId() + ", taskName=" + getTaskName() + ", memberIds=" + getMemberIds() + ", memberId=" + getMemberId() + ", memberName=" + getMemberName() + ", supplementTime=" + getSupplementTime() + ", supplementReason=" + getSupplementReason() + ")";
    }

    public Long getSupplementId() {
        return this.supplementId;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public Long getSupplementTime() {
        return this.supplementTime;
    }

    public String getSupplementReason() {
        return this.supplementReason;
    }
}

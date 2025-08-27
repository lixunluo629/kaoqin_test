package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "签到报表-个人记录列表信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInLogMemberListResponse.class */
public class CheckInLogMemberListResponse implements Serializable {

    @ApiModelProperty(name = "checkInTaskId", value = "签到任务id")
    private Long checkInTaskId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "checkInTaskName", value = "签到任务名称")
    private String checkInTaskName;

    @ApiModelProperty(name = "checkInMemberId", value = "签到成员id")
    private Long checkInMemberId;

    @ApiModelProperty(name = "checkInMemberName", value = "签到成员名称")
    private String checkInMemberName;

    @ApiModelProperty(name = "totalCheckInTaskCount", value = "成员对应签到任务应签到次数")
    private Integer totalCheckInTaskCount;

    @ApiModelProperty(name = "checkedInTaskCount", value = "成员对应签到任务已签到次数")
    private Integer checkedInTaskCount;

    @ApiModelProperty(name = "unCheckedInTaskCount", value = "成员对应签到任务未签到次数")
    private Integer unCheckedInTaskCount;

    public void setCheckInTaskId(Long checkInTaskId) {
        this.checkInTaskId = checkInTaskId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setCheckInTaskName(String checkInTaskName) {
        this.checkInTaskName = checkInTaskName;
    }

    public void setCheckInMemberId(Long checkInMemberId) {
        this.checkInMemberId = checkInMemberId;
    }

    public void setCheckInMemberName(String checkInMemberName) {
        this.checkInMemberName = checkInMemberName;
    }

    public void setTotalCheckInTaskCount(Integer totalCheckInTaskCount) {
        this.totalCheckInTaskCount = totalCheckInTaskCount;
    }

    public void setCheckedInTaskCount(Integer checkedInTaskCount) {
        this.checkedInTaskCount = checkedInTaskCount;
    }

    public void setUnCheckedInTaskCount(Integer unCheckedInTaskCount) {
        this.unCheckedInTaskCount = unCheckedInTaskCount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInLogMemberListResponse)) {
            return false;
        }
        CheckInLogMemberListResponse other = (CheckInLogMemberListResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
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
        Object this$checkInMemberId = getCheckInMemberId();
        Object other$checkInMemberId = other.getCheckInMemberId();
        if (this$checkInMemberId == null) {
            if (other$checkInMemberId != null) {
                return false;
            }
        } else if (!this$checkInMemberId.equals(other$checkInMemberId)) {
            return false;
        }
        Object this$checkInMemberName = getCheckInMemberName();
        Object other$checkInMemberName = other.getCheckInMemberName();
        if (this$checkInMemberName == null) {
            if (other$checkInMemberName != null) {
                return false;
            }
        } else if (!this$checkInMemberName.equals(other$checkInMemberName)) {
            return false;
        }
        Object this$totalCheckInTaskCount = getTotalCheckInTaskCount();
        Object other$totalCheckInTaskCount = other.getTotalCheckInTaskCount();
        if (this$totalCheckInTaskCount == null) {
            if (other$totalCheckInTaskCount != null) {
                return false;
            }
        } else if (!this$totalCheckInTaskCount.equals(other$totalCheckInTaskCount)) {
            return false;
        }
        Object this$checkedInTaskCount = getCheckedInTaskCount();
        Object other$checkedInTaskCount = other.getCheckedInTaskCount();
        if (this$checkedInTaskCount == null) {
            if (other$checkedInTaskCount != null) {
                return false;
            }
        } else if (!this$checkedInTaskCount.equals(other$checkedInTaskCount)) {
            return false;
        }
        Object this$unCheckedInTaskCount = getUnCheckedInTaskCount();
        Object other$unCheckedInTaskCount = other.getUnCheckedInTaskCount();
        return this$unCheckedInTaskCount == null ? other$unCheckedInTaskCount == null : this$unCheckedInTaskCount.equals(other$unCheckedInTaskCount);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInLogMemberListResponse;
    }

    public int hashCode() {
        Object $checkInTaskId = getCheckInTaskId();
        int result = (1 * 59) + ($checkInTaskId == null ? 43 : $checkInTaskId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $checkInTaskName = getCheckInTaskName();
        int result3 = (result2 * 59) + ($checkInTaskName == null ? 43 : $checkInTaskName.hashCode());
        Object $checkInMemberId = getCheckInMemberId();
        int result4 = (result3 * 59) + ($checkInMemberId == null ? 43 : $checkInMemberId.hashCode());
        Object $checkInMemberName = getCheckInMemberName();
        int result5 = (result4 * 59) + ($checkInMemberName == null ? 43 : $checkInMemberName.hashCode());
        Object $totalCheckInTaskCount = getTotalCheckInTaskCount();
        int result6 = (result5 * 59) + ($totalCheckInTaskCount == null ? 43 : $totalCheckInTaskCount.hashCode());
        Object $checkedInTaskCount = getCheckedInTaskCount();
        int result7 = (result6 * 59) + ($checkedInTaskCount == null ? 43 : $checkedInTaskCount.hashCode());
        Object $unCheckedInTaskCount = getUnCheckedInTaskCount();
        return (result7 * 59) + ($unCheckedInTaskCount == null ? 43 : $unCheckedInTaskCount.hashCode());
    }

    public String toString() {
        return "CheckInLogMemberListResponse(checkInTaskId=" + getCheckInTaskId() + ", orgId=" + getOrgId() + ", checkInTaskName=" + getCheckInTaskName() + ", checkInMemberId=" + getCheckInMemberId() + ", checkInMemberName=" + getCheckInMemberName() + ", totalCheckInTaskCount=" + getTotalCheckInTaskCount() + ", checkedInTaskCount=" + getCheckedInTaskCount() + ", unCheckedInTaskCount=" + getUnCheckedInTaskCount() + ")";
    }

    public Long getCheckInTaskId() {
        return this.checkInTaskId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getCheckInTaskName() {
        return this.checkInTaskName;
    }

    public Long getCheckInMemberId() {
        return this.checkInMemberId;
    }

    public String getCheckInMemberName() {
        return this.checkInMemberName;
    }

    public Integer getTotalCheckInTaskCount() {
        return this.totalCheckInTaskCount;
    }

    public Integer getCheckedInTaskCount() {
        return this.checkedInTaskCount;
    }

    public Integer getUnCheckedInTaskCount() {
        return this.unCheckedInTaskCount;
    }
}

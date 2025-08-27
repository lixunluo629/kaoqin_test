package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "签到报表-任务列表信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInLogDayListResponse.class */
public class CheckInLogDayListResponse implements Serializable {

    @ApiModelProperty(name = "checkInTaskLogId", value = "签到任务日志id，点击查看详情传入该id")
    private Long checkInTaskLogId;

    @ApiModelProperty(name = "checkInTaskId", value = "签到任务id")
    private Long checkInTaskId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "checkInTaskName", value = "签到任务名称")
    private String checkInTaskName;

    @ApiModelProperty(name = "totalCheckInMemberCount", value = "应签到总人数")
    private Integer totalCheckInMemberCount;

    @ApiModelProperty(name = "checkedInMemberCount", value = "已签到人数")
    private Integer checkedInMemberCount;

    @ApiModelProperty(name = "unCheckedInMemberCount", value = "未签到人数")
    private Integer unCheckedInMemberCount;

    @ApiModelProperty(name = "checkInTaskTime", value = "签到任务日期")
    private Date checkInTaskTime;

    @ApiModelProperty(name = "checkInTaskTimeStr", value = "签到任务日期")
    private String checkInTaskTimeStr;

    @ApiModelProperty(name = "checkInTaskStartTime", value = "签到任务开始时间")
    private Date checkInTaskStartTime;

    @ApiModelProperty(name = "checkInTaskStartTimeStr", value = "签到任务开始时间")
    private String checkInTaskStartTimeStr;

    @ApiModelProperty(name = "checkInTaskEndTime", value = "签到任务结束时间")
    private Date checkInTaskEndTime;

    @ApiModelProperty(name = "checkInTaskEndTimeStr", value = "签到任务结束时间")
    private String checkInTaskEndTimeStr;

    public void setCheckInTaskLogId(Long checkInTaskLogId) {
        this.checkInTaskLogId = checkInTaskLogId;
    }

    public void setCheckInTaskId(Long checkInTaskId) {
        this.checkInTaskId = checkInTaskId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setCheckInTaskName(String checkInTaskName) {
        this.checkInTaskName = checkInTaskName;
    }

    public void setTotalCheckInMemberCount(Integer totalCheckInMemberCount) {
        this.totalCheckInMemberCount = totalCheckInMemberCount;
    }

    public void setCheckedInMemberCount(Integer checkedInMemberCount) {
        this.checkedInMemberCount = checkedInMemberCount;
    }

    public void setUnCheckedInMemberCount(Integer unCheckedInMemberCount) {
        this.unCheckedInMemberCount = unCheckedInMemberCount;
    }

    public void setCheckInTaskTime(Date checkInTaskTime) {
        this.checkInTaskTime = checkInTaskTime;
    }

    public void setCheckInTaskTimeStr(String checkInTaskTimeStr) {
        this.checkInTaskTimeStr = checkInTaskTimeStr;
    }

    public void setCheckInTaskStartTime(Date checkInTaskStartTime) {
        this.checkInTaskStartTime = checkInTaskStartTime;
    }

    public void setCheckInTaskStartTimeStr(String checkInTaskStartTimeStr) {
        this.checkInTaskStartTimeStr = checkInTaskStartTimeStr;
    }

    public void setCheckInTaskEndTime(Date checkInTaskEndTime) {
        this.checkInTaskEndTime = checkInTaskEndTime;
    }

    public void setCheckInTaskEndTimeStr(String checkInTaskEndTimeStr) {
        this.checkInTaskEndTimeStr = checkInTaskEndTimeStr;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInLogDayListResponse)) {
            return false;
        }
        CheckInLogDayListResponse other = (CheckInLogDayListResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$totalCheckInMemberCount = getTotalCheckInMemberCount();
        Object other$totalCheckInMemberCount = other.getTotalCheckInMemberCount();
        if (this$totalCheckInMemberCount == null) {
            if (other$totalCheckInMemberCount != null) {
                return false;
            }
        } else if (!this$totalCheckInMemberCount.equals(other$totalCheckInMemberCount)) {
            return false;
        }
        Object this$checkedInMemberCount = getCheckedInMemberCount();
        Object other$checkedInMemberCount = other.getCheckedInMemberCount();
        if (this$checkedInMemberCount == null) {
            if (other$checkedInMemberCount != null) {
                return false;
            }
        } else if (!this$checkedInMemberCount.equals(other$checkedInMemberCount)) {
            return false;
        }
        Object this$unCheckedInMemberCount = getUnCheckedInMemberCount();
        Object other$unCheckedInMemberCount = other.getUnCheckedInMemberCount();
        if (this$unCheckedInMemberCount == null) {
            if (other$unCheckedInMemberCount != null) {
                return false;
            }
        } else if (!this$unCheckedInMemberCount.equals(other$unCheckedInMemberCount)) {
            return false;
        }
        Object this$checkInTaskTime = getCheckInTaskTime();
        Object other$checkInTaskTime = other.getCheckInTaskTime();
        if (this$checkInTaskTime == null) {
            if (other$checkInTaskTime != null) {
                return false;
            }
        } else if (!this$checkInTaskTime.equals(other$checkInTaskTime)) {
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
        Object this$checkInTaskStartTime = getCheckInTaskStartTime();
        Object other$checkInTaskStartTime = other.getCheckInTaskStartTime();
        if (this$checkInTaskStartTime == null) {
            if (other$checkInTaskStartTime != null) {
                return false;
            }
        } else if (!this$checkInTaskStartTime.equals(other$checkInTaskStartTime)) {
            return false;
        }
        Object this$checkInTaskStartTimeStr = getCheckInTaskStartTimeStr();
        Object other$checkInTaskStartTimeStr = other.getCheckInTaskStartTimeStr();
        if (this$checkInTaskStartTimeStr == null) {
            if (other$checkInTaskStartTimeStr != null) {
                return false;
            }
        } else if (!this$checkInTaskStartTimeStr.equals(other$checkInTaskStartTimeStr)) {
            return false;
        }
        Object this$checkInTaskEndTime = getCheckInTaskEndTime();
        Object other$checkInTaskEndTime = other.getCheckInTaskEndTime();
        if (this$checkInTaskEndTime == null) {
            if (other$checkInTaskEndTime != null) {
                return false;
            }
        } else if (!this$checkInTaskEndTime.equals(other$checkInTaskEndTime)) {
            return false;
        }
        Object this$checkInTaskEndTimeStr = getCheckInTaskEndTimeStr();
        Object other$checkInTaskEndTimeStr = other.getCheckInTaskEndTimeStr();
        return this$checkInTaskEndTimeStr == null ? other$checkInTaskEndTimeStr == null : this$checkInTaskEndTimeStr.equals(other$checkInTaskEndTimeStr);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInLogDayListResponse;
    }

    public int hashCode() {
        Object $checkInTaskLogId = getCheckInTaskLogId();
        int result = (1 * 59) + ($checkInTaskLogId == null ? 43 : $checkInTaskLogId.hashCode());
        Object $checkInTaskId = getCheckInTaskId();
        int result2 = (result * 59) + ($checkInTaskId == null ? 43 : $checkInTaskId.hashCode());
        Object $orgId = getOrgId();
        int result3 = (result2 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $checkInTaskName = getCheckInTaskName();
        int result4 = (result3 * 59) + ($checkInTaskName == null ? 43 : $checkInTaskName.hashCode());
        Object $totalCheckInMemberCount = getTotalCheckInMemberCount();
        int result5 = (result4 * 59) + ($totalCheckInMemberCount == null ? 43 : $totalCheckInMemberCount.hashCode());
        Object $checkedInMemberCount = getCheckedInMemberCount();
        int result6 = (result5 * 59) + ($checkedInMemberCount == null ? 43 : $checkedInMemberCount.hashCode());
        Object $unCheckedInMemberCount = getUnCheckedInMemberCount();
        int result7 = (result6 * 59) + ($unCheckedInMemberCount == null ? 43 : $unCheckedInMemberCount.hashCode());
        Object $checkInTaskTime = getCheckInTaskTime();
        int result8 = (result7 * 59) + ($checkInTaskTime == null ? 43 : $checkInTaskTime.hashCode());
        Object $checkInTaskTimeStr = getCheckInTaskTimeStr();
        int result9 = (result8 * 59) + ($checkInTaskTimeStr == null ? 43 : $checkInTaskTimeStr.hashCode());
        Object $checkInTaskStartTime = getCheckInTaskStartTime();
        int result10 = (result9 * 59) + ($checkInTaskStartTime == null ? 43 : $checkInTaskStartTime.hashCode());
        Object $checkInTaskStartTimeStr = getCheckInTaskStartTimeStr();
        int result11 = (result10 * 59) + ($checkInTaskStartTimeStr == null ? 43 : $checkInTaskStartTimeStr.hashCode());
        Object $checkInTaskEndTime = getCheckInTaskEndTime();
        int result12 = (result11 * 59) + ($checkInTaskEndTime == null ? 43 : $checkInTaskEndTime.hashCode());
        Object $checkInTaskEndTimeStr = getCheckInTaskEndTimeStr();
        return (result12 * 59) + ($checkInTaskEndTimeStr == null ? 43 : $checkInTaskEndTimeStr.hashCode());
    }

    public String toString() {
        return "CheckInLogDayListResponse(checkInTaskLogId=" + getCheckInTaskLogId() + ", checkInTaskId=" + getCheckInTaskId() + ", orgId=" + getOrgId() + ", checkInTaskName=" + getCheckInTaskName() + ", totalCheckInMemberCount=" + getTotalCheckInMemberCount() + ", checkedInMemberCount=" + getCheckedInMemberCount() + ", unCheckedInMemberCount=" + getUnCheckedInMemberCount() + ", checkInTaskTime=" + getCheckInTaskTime() + ", checkInTaskTimeStr=" + getCheckInTaskTimeStr() + ", checkInTaskStartTime=" + getCheckInTaskStartTime() + ", checkInTaskStartTimeStr=" + getCheckInTaskStartTimeStr() + ", checkInTaskEndTime=" + getCheckInTaskEndTime() + ", checkInTaskEndTimeStr=" + getCheckInTaskEndTimeStr() + ")";
    }

    public Long getCheckInTaskLogId() {
        return this.checkInTaskLogId;
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

    public Integer getTotalCheckInMemberCount() {
        return this.totalCheckInMemberCount;
    }

    public Integer getCheckedInMemberCount() {
        return this.checkedInMemberCount;
    }

    public Integer getUnCheckedInMemberCount() {
        return this.unCheckedInMemberCount;
    }

    public Date getCheckInTaskTime() {
        return this.checkInTaskTime;
    }

    public String getCheckInTaskTimeStr() {
        return this.checkInTaskTimeStr;
    }

    public Date getCheckInTaskStartTime() {
        return this.checkInTaskStartTime;
    }

    public String getCheckInTaskStartTimeStr() {
        return this.checkInTaskStartTimeStr;
    }

    public Date getCheckInTaskEndTime() {
        return this.checkInTaskEndTime;
    }

    public String getCheckInTaskEndTimeStr() {
        return this.checkInTaskEndTimeStr;
    }
}

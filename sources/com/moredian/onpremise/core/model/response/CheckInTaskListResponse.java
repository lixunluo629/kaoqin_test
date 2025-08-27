package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "签到任务列表信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInTaskListResponse.class */
public class CheckInTaskListResponse implements Serializable {

    @ApiModelProperty(name = "id", value = "签到任务id")
    private Long id;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "name", value = "签到任务名称")
    private String name;

    @ApiModelProperty(name = "allUser", value = "是否全员，1：是；2：不是")
    private Integer allUser;

    @ApiModelProperty(name = "allUserName", value = "是否全员，是；不是")
    private String allUserName;

    @ApiModelProperty(name = "taskMemberCount", value = "签到任务成员数量")
    private Integer taskMemberCount;

    @ApiModelProperty(name = "allDay", value = "是否全天，1：是；2：不是")
    private Integer allDay;

    @ApiModelProperty(name = "allDayName", value = "是否全天，是；不是")
    private String allDayName;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "签到任务时间范围")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "签到任务时间范围")
    private String endTime;

    @ApiModelProperty(name = "timeStr", value = "签到任务时间范围")
    private String timeStr;

    @ApiModelProperty(name = "cycle", value = "签到任务周期")
    private Integer cycle;

    @ApiModelProperty(name = "cycleName", value = "签到任务周期名称")
    private String cycleName;

    @ApiModelProperty(name = "cycleExtra", value = "签到任务周期详情")
    private String cycleExtra;

    @ApiModelProperty(name = "taskDeviceCount", value = "签到任务设备数量")
    private Integer taskDeviceCount;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "签到任务状态")
    private Integer status;

    @ApiModelProperty(name = "statusName", value = "签到任务状态名称")
    private String statusName;

    @ApiModelProperty(name = "type", value = "类型1：普通；2：会议")
    private Integer type;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllUser(Integer allUser) {
        this.allUser = allUser;
    }

    public void setAllUserName(String allUserName) {
        this.allUserName = allUserName;
    }

    public void setTaskMemberCount(Integer taskMemberCount) {
        this.taskMemberCount = taskMemberCount;
    }

    public void setAllDay(Integer allDay) {
        this.allDay = allDay;
    }

    public void setAllDayName(String allDayName) {
        this.allDayName = allDayName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public void setCycleExtra(String cycleExtra) {
        this.cycleExtra = cycleExtra;
    }

    public void setTaskDeviceCount(Integer taskDeviceCount) {
        this.taskDeviceCount = taskDeviceCount;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInTaskListResponse)) {
            return false;
        }
        CheckInTaskListResponse other = (CheckInTaskListResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id == null) {
            if (other$id != null) {
                return false;
            }
        } else if (!this$id.equals(other$id)) {
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
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$allUser = getAllUser();
        Object other$allUser = other.getAllUser();
        if (this$allUser == null) {
            if (other$allUser != null) {
                return false;
            }
        } else if (!this$allUser.equals(other$allUser)) {
            return false;
        }
        Object this$allUserName = getAllUserName();
        Object other$allUserName = other.getAllUserName();
        if (this$allUserName == null) {
            if (other$allUserName != null) {
                return false;
            }
        } else if (!this$allUserName.equals(other$allUserName)) {
            return false;
        }
        Object this$taskMemberCount = getTaskMemberCount();
        Object other$taskMemberCount = other.getTaskMemberCount();
        if (this$taskMemberCount == null) {
            if (other$taskMemberCount != null) {
                return false;
            }
        } else if (!this$taskMemberCount.equals(other$taskMemberCount)) {
            return false;
        }
        Object this$allDay = getAllDay();
        Object other$allDay = other.getAllDay();
        if (this$allDay == null) {
            if (other$allDay != null) {
                return false;
            }
        } else if (!this$allDay.equals(other$allDay)) {
            return false;
        }
        Object this$allDayName = getAllDayName();
        Object other$allDayName = other.getAllDayName();
        if (this$allDayName == null) {
            if (other$allDayName != null) {
                return false;
            }
        } else if (!this$allDayName.equals(other$allDayName)) {
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
        Object this$timeStr = getTimeStr();
        Object other$timeStr = other.getTimeStr();
        if (this$timeStr == null) {
            if (other$timeStr != null) {
                return false;
            }
        } else if (!this$timeStr.equals(other$timeStr)) {
            return false;
        }
        Object this$cycle = getCycle();
        Object other$cycle = other.getCycle();
        if (this$cycle == null) {
            if (other$cycle != null) {
                return false;
            }
        } else if (!this$cycle.equals(other$cycle)) {
            return false;
        }
        Object this$cycleName = getCycleName();
        Object other$cycleName = other.getCycleName();
        if (this$cycleName == null) {
            if (other$cycleName != null) {
                return false;
            }
        } else if (!this$cycleName.equals(other$cycleName)) {
            return false;
        }
        Object this$cycleExtra = getCycleExtra();
        Object other$cycleExtra = other.getCycleExtra();
        if (this$cycleExtra == null) {
            if (other$cycleExtra != null) {
                return false;
            }
        } else if (!this$cycleExtra.equals(other$cycleExtra)) {
            return false;
        }
        Object this$taskDeviceCount = getTaskDeviceCount();
        Object other$taskDeviceCount = other.getTaskDeviceCount();
        if (this$taskDeviceCount == null) {
            if (other$taskDeviceCount != null) {
                return false;
            }
        } else if (!this$taskDeviceCount.equals(other$taskDeviceCount)) {
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
        Object this$statusName = getStatusName();
        Object other$statusName = other.getStatusName();
        if (this$statusName == null) {
            if (other$statusName != null) {
                return false;
            }
        } else if (!this$statusName.equals(other$statusName)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        return this$type == null ? other$type == null : this$type.equals(other$type);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInTaskListResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $name = getName();
        int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $allUser = getAllUser();
        int result4 = (result3 * 59) + ($allUser == null ? 43 : $allUser.hashCode());
        Object $allUserName = getAllUserName();
        int result5 = (result4 * 59) + ($allUserName == null ? 43 : $allUserName.hashCode());
        Object $taskMemberCount = getTaskMemberCount();
        int result6 = (result5 * 59) + ($taskMemberCount == null ? 43 : $taskMemberCount.hashCode());
        Object $allDay = getAllDay();
        int result7 = (result6 * 59) + ($allDay == null ? 43 : $allDay.hashCode());
        Object $allDayName = getAllDayName();
        int result8 = (result7 * 59) + ($allDayName == null ? 43 : $allDayName.hashCode());
        Object $startTime = getStartTime();
        int result9 = (result8 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result10 = (result9 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $timeStr = getTimeStr();
        int result11 = (result10 * 59) + ($timeStr == null ? 43 : $timeStr.hashCode());
        Object $cycle = getCycle();
        int result12 = (result11 * 59) + ($cycle == null ? 43 : $cycle.hashCode());
        Object $cycleName = getCycleName();
        int result13 = (result12 * 59) + ($cycleName == null ? 43 : $cycleName.hashCode());
        Object $cycleExtra = getCycleExtra();
        int result14 = (result13 * 59) + ($cycleExtra == null ? 43 : $cycleExtra.hashCode());
        Object $taskDeviceCount = getTaskDeviceCount();
        int result15 = (result14 * 59) + ($taskDeviceCount == null ? 43 : $taskDeviceCount.hashCode());
        Object $status = getStatus();
        int result16 = (result15 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $statusName = getStatusName();
        int result17 = (result16 * 59) + ($statusName == null ? 43 : $statusName.hashCode());
        Object $type = getType();
        return (result17 * 59) + ($type == null ? 43 : $type.hashCode());
    }

    public String toString() {
        return "CheckInTaskListResponse(id=" + getId() + ", orgId=" + getOrgId() + ", name=" + getName() + ", allUser=" + getAllUser() + ", allUserName=" + getAllUserName() + ", taskMemberCount=" + getTaskMemberCount() + ", allDay=" + getAllDay() + ", allDayName=" + getAllDayName() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", timeStr=" + getTimeStr() + ", cycle=" + getCycle() + ", cycleName=" + getCycleName() + ", cycleExtra=" + getCycleExtra() + ", taskDeviceCount=" + getTaskDeviceCount() + ", status=" + getStatus() + ", statusName=" + getStatusName() + ", type=" + getType() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getName() {
        return this.name;
    }

    public Integer getAllUser() {
        return this.allUser;
    }

    public String getAllUserName() {
        return this.allUserName;
    }

    public Integer getTaskMemberCount() {
        return this.taskMemberCount;
    }

    public Integer getAllDay() {
        return this.allDay;
    }

    public String getAllDayName() {
        return this.allDayName;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getTimeStr() {
        return this.timeStr;
    }

    public Integer getCycle() {
        return this.cycle;
    }

    public String getCycleName() {
        return this.cycleName;
    }

    public String getCycleExtra() {
        return this.cycleExtra;
    }

    public Integer getTaskDeviceCount() {
        return this.taskDeviceCount;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public Integer getType() {
        return this.type;
    }
}

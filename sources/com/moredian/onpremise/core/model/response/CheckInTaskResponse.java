package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.model.dto.CheckInDeviceDto;
import com.moredian.onpremise.core.model.dto.CheckInMemberDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "签到任务信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CheckInTaskResponse.class */
public class CheckInTaskResponse implements Serializable {

    @ApiModelProperty(name = "id", value = "签到任务id")
    private Long id;

    @ApiModelProperty(name = "name", value = "签到任务名称")
    private String name;

    @ApiModelProperty(name = "allUser", value = "是否全员，1：是；2：否")
    private Integer allUser;

    @ApiModelProperty(name = "taskMembers", value = "签到任务的成员列表")
    private List<CheckInMemberDto> taskMembers;

    @ApiModelProperty(name = "taskMembers", value = "不需签到的成员列表")
    private List<CheckInMemberDto> unTaskMembers;

    @ApiModelProperty(name = "allDay", value = "是否全天，1：是；2：否")
    private Integer allDay;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "开始时间")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "结束时间")
    private String endTime;

    @ApiModelProperty(name = "cycle", value = "周期类型，1：每天；2：指定日期；3：单次")
    private Integer cycle;

    @ApiModelProperty(name = "cycleExtra", value = "周期类型详情")
    private String cycleExtra;

    @ApiModelProperty(name = "tipsText", value = "签到成功，文字提示")
    private String tipsText;

    @ApiModelProperty(name = "tipsSpeech", value = "签到成功，语音播报内容")
    private String tipsSpeech;

    @ApiModelProperty(name = "taskDevices", value = "签到任务设备列表")
    private List<CheckInDeviceDto> taskDevices;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "状态，1：开启；2：关闭")
    private Integer status;

    @ApiModelProperty(name = "type", value = "类型1：普通；2：会议")
    private Integer type;

    @ApiModelProperty(name = "doorFlag", value = "签到成功是否开门，0-否；1-是")
    private Integer doorFlag;

    @ApiModelProperty(name = "voiceRemind", value = "语音提醒")
    private String voiceRemind;

    @ApiModelProperty(name = "voiceRemindAdvanceTime", value = "语音提醒提前时间，单位：秒")
    private Integer voiceRemindAdvanceTime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAllUser(Integer allUser) {
        this.allUser = allUser;
    }

    public void setTaskMembers(List<CheckInMemberDto> taskMembers) {
        this.taskMembers = taskMembers;
    }

    public void setUnTaskMembers(List<CheckInMemberDto> unTaskMembers) {
        this.unTaskMembers = unTaskMembers;
    }

    public void setAllDay(Integer allDay) {
        this.allDay = allDay;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public void setCycleExtra(String cycleExtra) {
        this.cycleExtra = cycleExtra;
    }

    public void setTipsText(String tipsText) {
        this.tipsText = tipsText;
    }

    public void setTipsSpeech(String tipsSpeech) {
        this.tipsSpeech = tipsSpeech;
    }

    public void setTaskDevices(List<CheckInDeviceDto> taskDevices) {
        this.taskDevices = taskDevices;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDoorFlag(Integer doorFlag) {
        this.doorFlag = doorFlag;
    }

    public void setVoiceRemind(String voiceRemind) {
        this.voiceRemind = voiceRemind;
    }

    public void setVoiceRemindAdvanceTime(Integer voiceRemindAdvanceTime) {
        this.voiceRemindAdvanceTime = voiceRemindAdvanceTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInTaskResponse)) {
            return false;
        }
        CheckInTaskResponse other = (CheckInTaskResponse) o;
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
        Object this$taskMembers = getTaskMembers();
        Object other$taskMembers = other.getTaskMembers();
        if (this$taskMembers == null) {
            if (other$taskMembers != null) {
                return false;
            }
        } else if (!this$taskMembers.equals(other$taskMembers)) {
            return false;
        }
        Object this$unTaskMembers = getUnTaskMembers();
        Object other$unTaskMembers = other.getUnTaskMembers();
        if (this$unTaskMembers == null) {
            if (other$unTaskMembers != null) {
                return false;
            }
        } else if (!this$unTaskMembers.equals(other$unTaskMembers)) {
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
        Object this$cycle = getCycle();
        Object other$cycle = other.getCycle();
        if (this$cycle == null) {
            if (other$cycle != null) {
                return false;
            }
        } else if (!this$cycle.equals(other$cycle)) {
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
        Object this$tipsText = getTipsText();
        Object other$tipsText = other.getTipsText();
        if (this$tipsText == null) {
            if (other$tipsText != null) {
                return false;
            }
        } else if (!this$tipsText.equals(other$tipsText)) {
            return false;
        }
        Object this$tipsSpeech = getTipsSpeech();
        Object other$tipsSpeech = other.getTipsSpeech();
        if (this$tipsSpeech == null) {
            if (other$tipsSpeech != null) {
                return false;
            }
        } else if (!this$tipsSpeech.equals(other$tipsSpeech)) {
            return false;
        }
        Object this$taskDevices = getTaskDevices();
        Object other$taskDevices = other.getTaskDevices();
        if (this$taskDevices == null) {
            if (other$taskDevices != null) {
                return false;
            }
        } else if (!this$taskDevices.equals(other$taskDevices)) {
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
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$doorFlag = getDoorFlag();
        Object other$doorFlag = other.getDoorFlag();
        if (this$doorFlag == null) {
            if (other$doorFlag != null) {
                return false;
            }
        } else if (!this$doorFlag.equals(other$doorFlag)) {
            return false;
        }
        Object this$voiceRemind = getVoiceRemind();
        Object other$voiceRemind = other.getVoiceRemind();
        if (this$voiceRemind == null) {
            if (other$voiceRemind != null) {
                return false;
            }
        } else if (!this$voiceRemind.equals(other$voiceRemind)) {
            return false;
        }
        Object this$voiceRemindAdvanceTime = getVoiceRemindAdvanceTime();
        Object other$voiceRemindAdvanceTime = other.getVoiceRemindAdvanceTime();
        return this$voiceRemindAdvanceTime == null ? other$voiceRemindAdvanceTime == null : this$voiceRemindAdvanceTime.equals(other$voiceRemindAdvanceTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInTaskResponse;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $allUser = getAllUser();
        int result3 = (result2 * 59) + ($allUser == null ? 43 : $allUser.hashCode());
        Object $taskMembers = getTaskMembers();
        int result4 = (result3 * 59) + ($taskMembers == null ? 43 : $taskMembers.hashCode());
        Object $unTaskMembers = getUnTaskMembers();
        int result5 = (result4 * 59) + ($unTaskMembers == null ? 43 : $unTaskMembers.hashCode());
        Object $allDay = getAllDay();
        int result6 = (result5 * 59) + ($allDay == null ? 43 : $allDay.hashCode());
        Object $startTime = getStartTime();
        int result7 = (result6 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result8 = (result7 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $cycle = getCycle();
        int result9 = (result8 * 59) + ($cycle == null ? 43 : $cycle.hashCode());
        Object $cycleExtra = getCycleExtra();
        int result10 = (result9 * 59) + ($cycleExtra == null ? 43 : $cycleExtra.hashCode());
        Object $tipsText = getTipsText();
        int result11 = (result10 * 59) + ($tipsText == null ? 43 : $tipsText.hashCode());
        Object $tipsSpeech = getTipsSpeech();
        int result12 = (result11 * 59) + ($tipsSpeech == null ? 43 : $tipsSpeech.hashCode());
        Object $taskDevices = getTaskDevices();
        int result13 = (result12 * 59) + ($taskDevices == null ? 43 : $taskDevices.hashCode());
        Object $status = getStatus();
        int result14 = (result13 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $type = getType();
        int result15 = (result14 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $doorFlag = getDoorFlag();
        int result16 = (result15 * 59) + ($doorFlag == null ? 43 : $doorFlag.hashCode());
        Object $voiceRemind = getVoiceRemind();
        int result17 = (result16 * 59) + ($voiceRemind == null ? 43 : $voiceRemind.hashCode());
        Object $voiceRemindAdvanceTime = getVoiceRemindAdvanceTime();
        return (result17 * 59) + ($voiceRemindAdvanceTime == null ? 43 : $voiceRemindAdvanceTime.hashCode());
    }

    public String toString() {
        return "CheckInTaskResponse(id=" + getId() + ", name=" + getName() + ", allUser=" + getAllUser() + ", taskMembers=" + getTaskMembers() + ", unTaskMembers=" + getUnTaskMembers() + ", allDay=" + getAllDay() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", cycle=" + getCycle() + ", cycleExtra=" + getCycleExtra() + ", tipsText=" + getTipsText() + ", tipsSpeech=" + getTipsSpeech() + ", taskDevices=" + getTaskDevices() + ", status=" + getStatus() + ", type=" + getType() + ", doorFlag=" + getDoorFlag() + ", voiceRemind=" + getVoiceRemind() + ", voiceRemindAdvanceTime=" + getVoiceRemindAdvanceTime() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getAllUser() {
        return this.allUser;
    }

    public List<CheckInMemberDto> getTaskMembers() {
        return this.taskMembers;
    }

    public List<CheckInMemberDto> getUnTaskMembers() {
        return this.unTaskMembers;
    }

    public Integer getAllDay() {
        return this.allDay;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public Integer getCycle() {
        return this.cycle;
    }

    public String getCycleExtra() {
        return this.cycleExtra;
    }

    public String getTipsText() {
        return this.tipsText;
    }

    public String getTipsSpeech() {
        return this.tipsSpeech;
    }

    public List<CheckInDeviceDto> getTaskDevices() {
        return this.taskDevices;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getDoorFlag() {
        return this.doorFlag;
    }

    public String getVoiceRemind() {
        return this.voiceRemind;
    }

    public Integer getVoiceRemindAdvanceTime() {
        return this.voiceRemindAdvanceTime;
    }
}

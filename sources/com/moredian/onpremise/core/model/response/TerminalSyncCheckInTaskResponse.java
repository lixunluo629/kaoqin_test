package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "终端同步签到任务响应")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncCheckInTaskResponse.class */
public class TerminalSyncCheckInTaskResponse implements Serializable {

    @ApiModelProperty(name = "checkInTaskId", value = "签到任务id")
    private Long checkInTaskId;

    @ApiModelProperty(name = "checkInTaskName", value = "签到任务名称")
    private String checkInTaskName;

    @ApiModelProperty(name = "allUser", value = "是否全员，1：是；2：否，全员签到任务不需要判断成员是否有签到任务，识别成功就调签到接口")
    private Integer allUser;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_START_TIME_KEY, value = "签到开始时间")
    private String startTime;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_END_TIME_KEY, value = "签到结束时间")
    private String endTime;

    @ApiModelProperty(name = "cycle", value = "周期，1：每天；2：指定日期；3：单次")
    private Integer cycle;

    @ApiModelProperty(name = "cycleExtra", value = "周期详情，cycle=2时：[1,2,3,4,5,6,7];cycle=3时：2019-04-19")
    private String cycleExtra;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "状态，1：开启；2：关闭，开启状态的才需要签到")
    private Integer status;

    @ApiModelProperty(name = "deleteFlag", value = "是否删除，1：是；2：否")
    private Integer deleteFlag;

    @ApiModelProperty(name = "tipsText", value = "文字提示")
    private String tipsText;

    @ApiModelProperty(name = "tipsSpeech", value = "语音提示")
    private String tipsSpeech;

    @ApiModelProperty(name = "type", value = "类型1：普通；2：会议")
    private Integer type;

    @ApiModelProperty(name = "doorFlag", value = "签到成功是否开门，0-否；1-是")
    private Integer doorFlag;

    @ApiModelProperty(name = "voiceRemind", value = "语音提醒")
    private String voiceRemind;

    @ApiModelProperty(name = "voiceRemindAdvanceTime", value = "语音提醒提前时间，单位：秒")
    private Integer voiceRemindAdvanceTime;

    public void setCheckInTaskId(Long checkInTaskId) {
        this.checkInTaskId = checkInTaskId;
    }

    public void setCheckInTaskName(String checkInTaskName) {
        this.checkInTaskName = checkInTaskName;
    }

    public void setAllUser(Integer allUser) {
        this.allUser = allUser;
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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setTipsText(String tipsText) {
        this.tipsText = tipsText;
    }

    public void setTipsSpeech(String tipsSpeech) {
        this.tipsSpeech = tipsSpeech;
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
        if (!(o instanceof TerminalSyncCheckInTaskResponse)) {
            return false;
        }
        TerminalSyncCheckInTaskResponse other = (TerminalSyncCheckInTaskResponse) o;
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
        Object this$checkInTaskName = getCheckInTaskName();
        Object other$checkInTaskName = other.getCheckInTaskName();
        if (this$checkInTaskName == null) {
            if (other$checkInTaskName != null) {
                return false;
            }
        } else if (!this$checkInTaskName.equals(other$checkInTaskName)) {
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
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        if (this$status == null) {
            if (other$status != null) {
                return false;
            }
        } else if (!this$status.equals(other$status)) {
            return false;
        }
        Object this$deleteFlag = getDeleteFlag();
        Object other$deleteFlag = other.getDeleteFlag();
        if (this$deleteFlag == null) {
            if (other$deleteFlag != null) {
                return false;
            }
        } else if (!this$deleteFlag.equals(other$deleteFlag)) {
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
        return other instanceof TerminalSyncCheckInTaskResponse;
    }

    public int hashCode() {
        Object $checkInTaskId = getCheckInTaskId();
        int result = (1 * 59) + ($checkInTaskId == null ? 43 : $checkInTaskId.hashCode());
        Object $checkInTaskName = getCheckInTaskName();
        int result2 = (result * 59) + ($checkInTaskName == null ? 43 : $checkInTaskName.hashCode());
        Object $allUser = getAllUser();
        int result3 = (result2 * 59) + ($allUser == null ? 43 : $allUser.hashCode());
        Object $startTime = getStartTime();
        int result4 = (result3 * 59) + ($startTime == null ? 43 : $startTime.hashCode());
        Object $endTime = getEndTime();
        int result5 = (result4 * 59) + ($endTime == null ? 43 : $endTime.hashCode());
        Object $cycle = getCycle();
        int result6 = (result5 * 59) + ($cycle == null ? 43 : $cycle.hashCode());
        Object $cycleExtra = getCycleExtra();
        int result7 = (result6 * 59) + ($cycleExtra == null ? 43 : $cycleExtra.hashCode());
        Object $status = getStatus();
        int result8 = (result7 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $deleteFlag = getDeleteFlag();
        int result9 = (result8 * 59) + ($deleteFlag == null ? 43 : $deleteFlag.hashCode());
        Object $tipsText = getTipsText();
        int result10 = (result9 * 59) + ($tipsText == null ? 43 : $tipsText.hashCode());
        Object $tipsSpeech = getTipsSpeech();
        int result11 = (result10 * 59) + ($tipsSpeech == null ? 43 : $tipsSpeech.hashCode());
        Object $type = getType();
        int result12 = (result11 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $doorFlag = getDoorFlag();
        int result13 = (result12 * 59) + ($doorFlag == null ? 43 : $doorFlag.hashCode());
        Object $voiceRemind = getVoiceRemind();
        int result14 = (result13 * 59) + ($voiceRemind == null ? 43 : $voiceRemind.hashCode());
        Object $voiceRemindAdvanceTime = getVoiceRemindAdvanceTime();
        return (result14 * 59) + ($voiceRemindAdvanceTime == null ? 43 : $voiceRemindAdvanceTime.hashCode());
    }

    public String toString() {
        return "TerminalSyncCheckInTaskResponse(checkInTaskId=" + getCheckInTaskId() + ", checkInTaskName=" + getCheckInTaskName() + ", allUser=" + getAllUser() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", cycle=" + getCycle() + ", cycleExtra=" + getCycleExtra() + ", status=" + getStatus() + ", deleteFlag=" + getDeleteFlag() + ", tipsText=" + getTipsText() + ", tipsSpeech=" + getTipsSpeech() + ", type=" + getType() + ", doorFlag=" + getDoorFlag() + ", voiceRemind=" + getVoiceRemind() + ", voiceRemindAdvanceTime=" + getVoiceRemindAdvanceTime() + ")";
    }

    public Long getCheckInTaskId() {
        return this.checkInTaskId;
    }

    public String getCheckInTaskName() {
        return this.checkInTaskName;
    }

    public Integer getAllUser() {
        return this.allUser;
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

    public Integer getStatus() {
        return this.status;
    }

    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public String getTipsText() {
        return this.tipsText;
    }

    public String getTipsSpeech() {
        return this.tipsSpeech;
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

package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步设备配置响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncDeviceConfigResponse.class */
public class TerminalSyncDeviceConfigResponse implements Serializable {
    private static final long serialVersionUID = -8602178192736537899L;

    @ApiModelProperty(name = "syncTime", value = "同步时间")
    private Long syncTime;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceVoice", value = "设备音量类型：1-标准模式，2-低音模式，3-静音模式")
    private Integer deviceVoice;

    @ApiModelProperty(name = "genType", value = "韦根协议类型：1：26-bit，2：34-bit")
    private Integer genType;

    @ApiModelProperty(name = "openingTime", value = "开门时长")
    private Integer openingTime;

    @ApiModelProperty(name = "identifyDistance", value = "识别距离 ，0-2米均分成10个档次，1-10每个档次递增0.2米")
    private Integer identifyDistance;

    @ApiModelProperty(name = "identifyModule", value = "识别模式：1-单人识别，2-多人识别")
    private Integer identifyModule;

    @ApiModelProperty(name = "thresholdScore", value = "识别相似度阈值")
    private Integer thresholdScore;

    @ApiModelProperty(name = "fastMode", value = "活检等级：0-快速；1-一般；2-严格")
    private Integer fastMode;

    @ApiModelProperty(name = "light", value = "补光阈值")
    private Integer light;

    @ApiModelProperty(name = "maskMode", value = "口罩模式：0-关闭；1-开启，默认关闭")
    private Integer maskMode;

    @ApiModelProperty(name = "ringMode", value = "铃声模式：1-水晶；2-丝绸")
    private Integer ringMode;

    @ApiModelProperty(name = "fontSize", value = "字号：1-超小；2-小；3-中；4-大；5-超大")
    private Integer fontSize;

    @ApiModelProperty(name = "showTime", value = "展示时长（毫秒）")
    private Integer showTime;

    @ApiModelProperty(name = "frame", value = "判断陌生人帧数")
    private Integer strangerFrame;

    @ApiModelProperty(name = "showReminderInfo", value = "陌生人文字提示")
    private String showReminderInfo;

    @ApiModelProperty(name = "speechReminderInfo", value = "陌生人语音提示")
    private String speechReminderInfo;

    @ApiModelProperty(name = "snapMode", value = "抓拍照模式，1-小图；2-大图")
    private Integer snapMode;

    @ApiModelProperty(name = "direction", value = "门禁方向：1-进；2-出")
    private Integer direction;

    @ApiModelProperty(name = "focus", value = "识别遮罩层显示：0-关闭；1-开启")
    private Integer focus;

    @ApiModelProperty(name = "maskRemindPicture", value = "未戴口罩图片提醒：0-关闭；1-开启")
    private Integer maskRemindPicture;

    @ApiModelProperty(name = "maskRemindVoice", value = "未戴口罩声音提醒：0-关闭；1-开启")
    private Integer maskRemindVoice;

    public void setSyncTime(Long syncTime) {
        this.syncTime = syncTime;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceVoice(Integer deviceVoice) {
        this.deviceVoice = deviceVoice;
    }

    public void setGenType(Integer genType) {
        this.genType = genType;
    }

    public void setOpeningTime(Integer openingTime) {
        this.openingTime = openingTime;
    }

    public void setIdentifyDistance(Integer identifyDistance) {
        this.identifyDistance = identifyDistance;
    }

    public void setIdentifyModule(Integer identifyModule) {
        this.identifyModule = identifyModule;
    }

    public void setThresholdScore(Integer thresholdScore) {
        this.thresholdScore = thresholdScore;
    }

    public void setFastMode(Integer fastMode) {
        this.fastMode = fastMode;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public void setMaskMode(Integer maskMode) {
        this.maskMode = maskMode;
    }

    public void setRingMode(Integer ringMode) {
        this.ringMode = ringMode;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public void setShowTime(Integer showTime) {
        this.showTime = showTime;
    }

    public void setStrangerFrame(Integer strangerFrame) {
        this.strangerFrame = strangerFrame;
    }

    public void setShowReminderInfo(String showReminderInfo) {
        this.showReminderInfo = showReminderInfo;
    }

    public void setSpeechReminderInfo(String speechReminderInfo) {
        this.speechReminderInfo = speechReminderInfo;
    }

    public void setSnapMode(Integer snapMode) {
        this.snapMode = snapMode;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public void setFocus(Integer focus) {
        this.focus = focus;
    }

    public void setMaskRemindPicture(Integer maskRemindPicture) {
        this.maskRemindPicture = maskRemindPicture;
    }

    public void setMaskRemindVoice(Integer maskRemindVoice) {
        this.maskRemindVoice = maskRemindVoice;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncDeviceConfigResponse)) {
            return false;
        }
        TerminalSyncDeviceConfigResponse other = (TerminalSyncDeviceConfigResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$syncTime = getSyncTime();
        Object other$syncTime = other.getSyncTime();
        if (this$syncTime == null) {
            if (other$syncTime != null) {
                return false;
            }
        } else if (!this$syncTime.equals(other$syncTime)) {
            return false;
        }
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
            return false;
        }
        Object this$deviceVoice = getDeviceVoice();
        Object other$deviceVoice = other.getDeviceVoice();
        if (this$deviceVoice == null) {
            if (other$deviceVoice != null) {
                return false;
            }
        } else if (!this$deviceVoice.equals(other$deviceVoice)) {
            return false;
        }
        Object this$genType = getGenType();
        Object other$genType = other.getGenType();
        if (this$genType == null) {
            if (other$genType != null) {
                return false;
            }
        } else if (!this$genType.equals(other$genType)) {
            return false;
        }
        Object this$openingTime = getOpeningTime();
        Object other$openingTime = other.getOpeningTime();
        if (this$openingTime == null) {
            if (other$openingTime != null) {
                return false;
            }
        } else if (!this$openingTime.equals(other$openingTime)) {
            return false;
        }
        Object this$identifyDistance = getIdentifyDistance();
        Object other$identifyDistance = other.getIdentifyDistance();
        if (this$identifyDistance == null) {
            if (other$identifyDistance != null) {
                return false;
            }
        } else if (!this$identifyDistance.equals(other$identifyDistance)) {
            return false;
        }
        Object this$identifyModule = getIdentifyModule();
        Object other$identifyModule = other.getIdentifyModule();
        if (this$identifyModule == null) {
            if (other$identifyModule != null) {
                return false;
            }
        } else if (!this$identifyModule.equals(other$identifyModule)) {
            return false;
        }
        Object this$thresholdScore = getThresholdScore();
        Object other$thresholdScore = other.getThresholdScore();
        if (this$thresholdScore == null) {
            if (other$thresholdScore != null) {
                return false;
            }
        } else if (!this$thresholdScore.equals(other$thresholdScore)) {
            return false;
        }
        Object this$fastMode = getFastMode();
        Object other$fastMode = other.getFastMode();
        if (this$fastMode == null) {
            if (other$fastMode != null) {
                return false;
            }
        } else if (!this$fastMode.equals(other$fastMode)) {
            return false;
        }
        Object this$light = getLight();
        Object other$light = other.getLight();
        if (this$light == null) {
            if (other$light != null) {
                return false;
            }
        } else if (!this$light.equals(other$light)) {
            return false;
        }
        Object this$maskMode = getMaskMode();
        Object other$maskMode = other.getMaskMode();
        if (this$maskMode == null) {
            if (other$maskMode != null) {
                return false;
            }
        } else if (!this$maskMode.equals(other$maskMode)) {
            return false;
        }
        Object this$ringMode = getRingMode();
        Object other$ringMode = other.getRingMode();
        if (this$ringMode == null) {
            if (other$ringMode != null) {
                return false;
            }
        } else if (!this$ringMode.equals(other$ringMode)) {
            return false;
        }
        Object this$fontSize = getFontSize();
        Object other$fontSize = other.getFontSize();
        if (this$fontSize == null) {
            if (other$fontSize != null) {
                return false;
            }
        } else if (!this$fontSize.equals(other$fontSize)) {
            return false;
        }
        Object this$showTime = getShowTime();
        Object other$showTime = other.getShowTime();
        if (this$showTime == null) {
            if (other$showTime != null) {
                return false;
            }
        } else if (!this$showTime.equals(other$showTime)) {
            return false;
        }
        Object this$strangerFrame = getStrangerFrame();
        Object other$strangerFrame = other.getStrangerFrame();
        if (this$strangerFrame == null) {
            if (other$strangerFrame != null) {
                return false;
            }
        } else if (!this$strangerFrame.equals(other$strangerFrame)) {
            return false;
        }
        Object this$showReminderInfo = getShowReminderInfo();
        Object other$showReminderInfo = other.getShowReminderInfo();
        if (this$showReminderInfo == null) {
            if (other$showReminderInfo != null) {
                return false;
            }
        } else if (!this$showReminderInfo.equals(other$showReminderInfo)) {
            return false;
        }
        Object this$speechReminderInfo = getSpeechReminderInfo();
        Object other$speechReminderInfo = other.getSpeechReminderInfo();
        if (this$speechReminderInfo == null) {
            if (other$speechReminderInfo != null) {
                return false;
            }
        } else if (!this$speechReminderInfo.equals(other$speechReminderInfo)) {
            return false;
        }
        Object this$snapMode = getSnapMode();
        Object other$snapMode = other.getSnapMode();
        if (this$snapMode == null) {
            if (other$snapMode != null) {
                return false;
            }
        } else if (!this$snapMode.equals(other$snapMode)) {
            return false;
        }
        Object this$direction = getDirection();
        Object other$direction = other.getDirection();
        if (this$direction == null) {
            if (other$direction != null) {
                return false;
            }
        } else if (!this$direction.equals(other$direction)) {
            return false;
        }
        Object this$focus = getFocus();
        Object other$focus = other.getFocus();
        if (this$focus == null) {
            if (other$focus != null) {
                return false;
            }
        } else if (!this$focus.equals(other$focus)) {
            return false;
        }
        Object this$maskRemindPicture = getMaskRemindPicture();
        Object other$maskRemindPicture = other.getMaskRemindPicture();
        if (this$maskRemindPicture == null) {
            if (other$maskRemindPicture != null) {
                return false;
            }
        } else if (!this$maskRemindPicture.equals(other$maskRemindPicture)) {
            return false;
        }
        Object this$maskRemindVoice = getMaskRemindVoice();
        Object other$maskRemindVoice = other.getMaskRemindVoice();
        return this$maskRemindVoice == null ? other$maskRemindVoice == null : this$maskRemindVoice.equals(other$maskRemindVoice);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncDeviceConfigResponse;
    }

    public int hashCode() {
        Object $syncTime = getSyncTime();
        int result = (1 * 59) + ($syncTime == null ? 43 : $syncTime.hashCode());
        Object $deviceName = getDeviceName();
        int result2 = (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceVoice = getDeviceVoice();
        int result3 = (result2 * 59) + ($deviceVoice == null ? 43 : $deviceVoice.hashCode());
        Object $genType = getGenType();
        int result4 = (result3 * 59) + ($genType == null ? 43 : $genType.hashCode());
        Object $openingTime = getOpeningTime();
        int result5 = (result4 * 59) + ($openingTime == null ? 43 : $openingTime.hashCode());
        Object $identifyDistance = getIdentifyDistance();
        int result6 = (result5 * 59) + ($identifyDistance == null ? 43 : $identifyDistance.hashCode());
        Object $identifyModule = getIdentifyModule();
        int result7 = (result6 * 59) + ($identifyModule == null ? 43 : $identifyModule.hashCode());
        Object $thresholdScore = getThresholdScore();
        int result8 = (result7 * 59) + ($thresholdScore == null ? 43 : $thresholdScore.hashCode());
        Object $fastMode = getFastMode();
        int result9 = (result8 * 59) + ($fastMode == null ? 43 : $fastMode.hashCode());
        Object $light = getLight();
        int result10 = (result9 * 59) + ($light == null ? 43 : $light.hashCode());
        Object $maskMode = getMaskMode();
        int result11 = (result10 * 59) + ($maskMode == null ? 43 : $maskMode.hashCode());
        Object $ringMode = getRingMode();
        int result12 = (result11 * 59) + ($ringMode == null ? 43 : $ringMode.hashCode());
        Object $fontSize = getFontSize();
        int result13 = (result12 * 59) + ($fontSize == null ? 43 : $fontSize.hashCode());
        Object $showTime = getShowTime();
        int result14 = (result13 * 59) + ($showTime == null ? 43 : $showTime.hashCode());
        Object $strangerFrame = getStrangerFrame();
        int result15 = (result14 * 59) + ($strangerFrame == null ? 43 : $strangerFrame.hashCode());
        Object $showReminderInfo = getShowReminderInfo();
        int result16 = (result15 * 59) + ($showReminderInfo == null ? 43 : $showReminderInfo.hashCode());
        Object $speechReminderInfo = getSpeechReminderInfo();
        int result17 = (result16 * 59) + ($speechReminderInfo == null ? 43 : $speechReminderInfo.hashCode());
        Object $snapMode = getSnapMode();
        int result18 = (result17 * 59) + ($snapMode == null ? 43 : $snapMode.hashCode());
        Object $direction = getDirection();
        int result19 = (result18 * 59) + ($direction == null ? 43 : $direction.hashCode());
        Object $focus = getFocus();
        int result20 = (result19 * 59) + ($focus == null ? 43 : $focus.hashCode());
        Object $maskRemindPicture = getMaskRemindPicture();
        int result21 = (result20 * 59) + ($maskRemindPicture == null ? 43 : $maskRemindPicture.hashCode());
        Object $maskRemindVoice = getMaskRemindVoice();
        return (result21 * 59) + ($maskRemindVoice == null ? 43 : $maskRemindVoice.hashCode());
    }

    public String toString() {
        return "TerminalSyncDeviceConfigResponse(syncTime=" + getSyncTime() + ", deviceName=" + getDeviceName() + ", deviceVoice=" + getDeviceVoice() + ", genType=" + getGenType() + ", openingTime=" + getOpeningTime() + ", identifyDistance=" + getIdentifyDistance() + ", identifyModule=" + getIdentifyModule() + ", thresholdScore=" + getThresholdScore() + ", fastMode=" + getFastMode() + ", light=" + getLight() + ", maskMode=" + getMaskMode() + ", ringMode=" + getRingMode() + ", fontSize=" + getFontSize() + ", showTime=" + getShowTime() + ", strangerFrame=" + getStrangerFrame() + ", showReminderInfo=" + getShowReminderInfo() + ", speechReminderInfo=" + getSpeechReminderInfo() + ", snapMode=" + getSnapMode() + ", direction=" + getDirection() + ", focus=" + getFocus() + ", maskRemindPicture=" + getMaskRemindPicture() + ", maskRemindVoice=" + getMaskRemindVoice() + ")";
    }

    public Long getSyncTime() {
        return this.syncTime;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Integer getDeviceVoice() {
        return this.deviceVoice;
    }

    public Integer getGenType() {
        return this.genType;
    }

    public Integer getOpeningTime() {
        return this.openingTime;
    }

    public Integer getIdentifyDistance() {
        return this.identifyDistance;
    }

    public Integer getIdentifyModule() {
        return this.identifyModule;
    }

    public Integer getThresholdScore() {
        return this.thresholdScore;
    }

    public Integer getFastMode() {
        return this.fastMode;
    }

    public Integer getLight() {
        return this.light;
    }

    public Integer getMaskMode() {
        return this.maskMode;
    }

    public Integer getRingMode() {
        return this.ringMode;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public Integer getShowTime() {
        return this.showTime;
    }

    public Integer getStrangerFrame() {
        return this.strangerFrame;
    }

    public String getShowReminderInfo() {
        return this.showReminderInfo;
    }

    public String getSpeechReminderInfo() {
        return this.speechReminderInfo;
    }

    public Integer getSnapMode() {
        return this.snapMode;
    }

    public Integer getDirection() {
        return this.direction;
    }

    public Integer getFocus() {
        return this.focus;
    }

    public Integer getMaskRemindPicture() {
        return this.maskRemindPicture;
    }

    public Integer getMaskRemindVoice() {
        return this.maskRemindVoice;
    }
}

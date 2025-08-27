package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeviceVoiceConfigRequest.class */
public class DeviceVoiceConfigRequest implements Serializable {
    private static final long serialVersionUID = -6347120152210471423L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_VOICE_CONFIG_REQUEST;
    private Long lastModifyTime;
    private String deviceSn;
    private String deviceName;
    private Integer deviceVoice;
    private Integer genType;
    private Integer openingTime;
    private Integer identifyDistance;
    private Integer identifyModule;
    private Integer thresholdScore;
    private Integer fastMode;
    private Integer light;
    private Integer maskMode;
    private Integer ringMode;
    private Integer fontSize;
    private Integer showTime;
    private Integer direction;
    private Integer focus;
    private Integer maskRemindPicture;
    private Integer maskRemindVoice;

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
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
        if (!(o instanceof DeviceVoiceConfigRequest)) {
            return false;
        }
        DeviceVoiceConfigRequest other = (DeviceVoiceConfigRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$lastModifyTime = getLastModifyTime();
        Object other$lastModifyTime = other.getLastModifyTime();
        if (this$lastModifyTime == null) {
            if (other$lastModifyTime != null) {
                return false;
            }
        } else if (!this$lastModifyTime.equals(other$lastModifyTime)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
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
        return other instanceof DeviceVoiceConfigRequest;
    }

    public int hashCode() {
        Object $lastModifyTime = getLastModifyTime();
        int result = (1 * 59) + ($lastModifyTime == null ? 43 : $lastModifyTime.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result3 = (result2 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceVoice = getDeviceVoice();
        int result4 = (result3 * 59) + ($deviceVoice == null ? 43 : $deviceVoice.hashCode());
        Object $genType = getGenType();
        int result5 = (result4 * 59) + ($genType == null ? 43 : $genType.hashCode());
        Object $openingTime = getOpeningTime();
        int result6 = (result5 * 59) + ($openingTime == null ? 43 : $openingTime.hashCode());
        Object $identifyDistance = getIdentifyDistance();
        int result7 = (result6 * 59) + ($identifyDistance == null ? 43 : $identifyDistance.hashCode());
        Object $identifyModule = getIdentifyModule();
        int result8 = (result7 * 59) + ($identifyModule == null ? 43 : $identifyModule.hashCode());
        Object $thresholdScore = getThresholdScore();
        int result9 = (result8 * 59) + ($thresholdScore == null ? 43 : $thresholdScore.hashCode());
        Object $fastMode = getFastMode();
        int result10 = (result9 * 59) + ($fastMode == null ? 43 : $fastMode.hashCode());
        Object $light = getLight();
        int result11 = (result10 * 59) + ($light == null ? 43 : $light.hashCode());
        Object $maskMode = getMaskMode();
        int result12 = (result11 * 59) + ($maskMode == null ? 43 : $maskMode.hashCode());
        Object $ringMode = getRingMode();
        int result13 = (result12 * 59) + ($ringMode == null ? 43 : $ringMode.hashCode());
        Object $fontSize = getFontSize();
        int result14 = (result13 * 59) + ($fontSize == null ? 43 : $fontSize.hashCode());
        Object $showTime = getShowTime();
        int result15 = (result14 * 59) + ($showTime == null ? 43 : $showTime.hashCode());
        Object $direction = getDirection();
        int result16 = (result15 * 59) + ($direction == null ? 43 : $direction.hashCode());
        Object $focus = getFocus();
        int result17 = (result16 * 59) + ($focus == null ? 43 : $focus.hashCode());
        Object $maskRemindPicture = getMaskRemindPicture();
        int result18 = (result17 * 59) + ($maskRemindPicture == null ? 43 : $maskRemindPicture.hashCode());
        Object $maskRemindVoice = getMaskRemindVoice();
        return (result18 * 59) + ($maskRemindVoice == null ? 43 : $maskRemindVoice.hashCode());
    }

    public String toString() {
        return "DeviceVoiceConfigRequest(lastModifyTime=" + getLastModifyTime() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", deviceVoice=" + getDeviceVoice() + ", genType=" + getGenType() + ", openingTime=" + getOpeningTime() + ", identifyDistance=" + getIdentifyDistance() + ", identifyModule=" + getIdentifyModule() + ", thresholdScore=" + getThresholdScore() + ", fastMode=" + getFastMode() + ", light=" + getLight() + ", maskMode=" + getMaskMode() + ", ringMode=" + getRingMode() + ", fontSize=" + getFontSize() + ", showTime=" + getShowTime() + ", direction=" + getDirection() + ", focus=" + getFocus() + ", maskRemindPicture=" + getMaskRemindPicture() + ", maskRemindVoice=" + getMaskRemindVoice() + ")";
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public String getDeviceSn() {
        return this.deviceSn;
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

    public DeviceVoiceConfigRequest() {
    }

    public DeviceVoiceConfigRequest(String deviceSn, Integer genType, String deviceName, Integer deviceVoice, Integer openingTime, Integer identifyDistance, Integer identifyModule, Integer thresholdScore, Integer fastMode, Integer light, Integer maskMode, Integer ringMode, Integer fontSize, Integer showTime, Integer direction, Integer focus, Integer maskRemindPicture, Integer maskRemindVoice, Long lastModifyTime) {
        this.deviceSn = deviceSn;
        this.deviceName = deviceName;
        this.deviceVoice = deviceVoice;
        if (genType == null || genType.intValue() == 0) {
            this.genType = 2;
        } else {
            this.genType = genType;
        }
        this.openingTime = openingTime;
        this.identifyDistance = identifyDistance;
        this.identifyModule = identifyModule;
        if (thresholdScore == null || thresholdScore.intValue() == 0) {
            this.thresholdScore = 72;
        } else {
            this.thresholdScore = thresholdScore;
        }
        this.fastMode = fastMode;
        this.light = light;
        this.maskMode = maskMode;
        this.ringMode = ringMode;
        this.fontSize = fontSize;
        this.showTime = showTime;
        this.direction = direction;
        this.focus = focus;
        this.maskRemindPicture = maskRemindPicture;
        this.maskRemindVoice = maskRemindVoice;
        this.lastModifyTime = lastModifyTime;
    }
}

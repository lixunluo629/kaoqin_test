package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "激活设备响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ActiveDeviceResponse.class */
public class ActiveDeviceResponse implements Serializable {

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Long deviceId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_ORG_CODE_KEY, value = "机构码")
    private String orgCode;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceType", value = "设备类型：1-门禁机，2-消费机-C，3-梯控机，4-自证机，5-消费机-A，6-消费机-W，7-销课机，8-温控机-A，9-温控机-B")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "token", value = "颁发给设备token")
    private String token;

    @ApiModelProperty(name = "speechReminderInfo", value = "陌生人刷脸语音提示")
    private String speechReminderInfo;

    @ApiModelProperty(name = "showReminderInfo", value = "陌生人刷脸界面文字提示")
    private String showReminderInfo;

    @ApiModelProperty(name = "genType", value = "韦根协议类型：1：26-bit;2：34-bit")
    private Integer genType;

    @ApiModelProperty(name = "openingTime", value = "开门时长")
    private Integer openingTime;

    @ApiModelProperty(name = "identifyDistance", value = "识别距离 ，0-2米均分成10个档次，1-10每个档次递增0.2米")
    private Integer identifyDistance;

    @ApiModelProperty(name = "identifyModule", value = "识别模式：1-单人识别，2-多人识别")
    private Integer identifyModule;

    @ApiModelProperty(name = "thresholdScore", value = "识别相似度阈值,百分制")
    private Integer thresholdScore;

    @ApiModelProperty(name = "fastMode", value = "快速模式：0-关闭；1-开启")
    private Integer fastMode;

    @ApiModelProperty(name = "frame", value = "帧数，判断陌生人")
    private Integer frame;

    @ApiModelProperty(name = "light", value = "补光阈值")
    private Integer light;

    @ApiModelProperty(name = "snapMode", value = "抓拍照模式，1-小图；2-大图")
    private Integer snapMode;

    @ApiModelProperty(name = "maskMode", value = "口罩模式，0-关闭；1-开启")
    private Integer maskMode;

    @ApiModelProperty(name = "ringMode", value = "铃声模式：1-水晶；2-丝绸")
    private Integer ringMode;

    @ApiModelProperty(name = "fontSize", value = "字号：1-超小；2-小；3-中；4-大；5-超大")
    private Integer fontSize;

    @ApiModelProperty(name = "showTime", value = "展示时长（毫秒）")
    private Integer showTime;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSpeechReminderInfo(String speechReminderInfo) {
        this.speechReminderInfo = speechReminderInfo;
    }

    public void setShowReminderInfo(String showReminderInfo) {
        this.showReminderInfo = showReminderInfo;
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

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public void setSnapMode(Integer snapMode) {
        this.snapMode = snapMode;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ActiveDeviceResponse)) {
            return false;
        }
        ActiveDeviceResponse other = (ActiveDeviceResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceId = getDeviceId();
        Object other$deviceId = other.getDeviceId();
        if (this$deviceId == null) {
            if (other$deviceId != null) {
                return false;
            }
        } else if (!this$deviceId.equals(other$deviceId)) {
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
        Object this$orgCode = getOrgCode();
        Object other$orgCode = other.getOrgCode();
        if (this$orgCode == null) {
            if (other$orgCode != null) {
                return false;
            }
        } else if (!this$orgCode.equals(other$orgCode)) {
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
        Object this$deviceType = getDeviceType();
        Object other$deviceType = other.getDeviceType();
        if (this$deviceType == null) {
            if (other$deviceType != null) {
                return false;
            }
        } else if (!this$deviceType.equals(other$deviceType)) {
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
        Object this$token = getToken();
        Object other$token = other.getToken();
        if (this$token == null) {
            if (other$token != null) {
                return false;
            }
        } else if (!this$token.equals(other$token)) {
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
        Object this$showReminderInfo = getShowReminderInfo();
        Object other$showReminderInfo = other.getShowReminderInfo();
        if (this$showReminderInfo == null) {
            if (other$showReminderInfo != null) {
                return false;
            }
        } else if (!this$showReminderInfo.equals(other$showReminderInfo)) {
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
        Object this$frame = getFrame();
        Object other$frame = other.getFrame();
        if (this$frame == null) {
            if (other$frame != null) {
                return false;
            }
        } else if (!this$frame.equals(other$frame)) {
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
        Object this$snapMode = getSnapMode();
        Object other$snapMode = other.getSnapMode();
        if (this$snapMode == null) {
            if (other$snapMode != null) {
                return false;
            }
        } else if (!this$snapMode.equals(other$snapMode)) {
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
        return this$showTime == null ? other$showTime == null : this$showTime.equals(other$showTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ActiveDeviceResponse;
    }

    public int hashCode() {
        Object $deviceId = getDeviceId();
        int result = (1 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $orgCode = getOrgCode();
        int result3 = (result2 * 59) + ($orgCode == null ? 43 : $orgCode.hashCode());
        Object $deviceName = getDeviceName();
        int result4 = (result3 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceType = getDeviceType();
        int result5 = (result4 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
        Object $deviceSn = getDeviceSn();
        int result6 = (result5 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $token = getToken();
        int result7 = (result6 * 59) + ($token == null ? 43 : $token.hashCode());
        Object $speechReminderInfo = getSpeechReminderInfo();
        int result8 = (result7 * 59) + ($speechReminderInfo == null ? 43 : $speechReminderInfo.hashCode());
        Object $showReminderInfo = getShowReminderInfo();
        int result9 = (result8 * 59) + ($showReminderInfo == null ? 43 : $showReminderInfo.hashCode());
        Object $genType = getGenType();
        int result10 = (result9 * 59) + ($genType == null ? 43 : $genType.hashCode());
        Object $openingTime = getOpeningTime();
        int result11 = (result10 * 59) + ($openingTime == null ? 43 : $openingTime.hashCode());
        Object $identifyDistance = getIdentifyDistance();
        int result12 = (result11 * 59) + ($identifyDistance == null ? 43 : $identifyDistance.hashCode());
        Object $identifyModule = getIdentifyModule();
        int result13 = (result12 * 59) + ($identifyModule == null ? 43 : $identifyModule.hashCode());
        Object $thresholdScore = getThresholdScore();
        int result14 = (result13 * 59) + ($thresholdScore == null ? 43 : $thresholdScore.hashCode());
        Object $fastMode = getFastMode();
        int result15 = (result14 * 59) + ($fastMode == null ? 43 : $fastMode.hashCode());
        Object $frame = getFrame();
        int result16 = (result15 * 59) + ($frame == null ? 43 : $frame.hashCode());
        Object $light = getLight();
        int result17 = (result16 * 59) + ($light == null ? 43 : $light.hashCode());
        Object $snapMode = getSnapMode();
        int result18 = (result17 * 59) + ($snapMode == null ? 43 : $snapMode.hashCode());
        Object $maskMode = getMaskMode();
        int result19 = (result18 * 59) + ($maskMode == null ? 43 : $maskMode.hashCode());
        Object $ringMode = getRingMode();
        int result20 = (result19 * 59) + ($ringMode == null ? 43 : $ringMode.hashCode());
        Object $fontSize = getFontSize();
        int result21 = (result20 * 59) + ($fontSize == null ? 43 : $fontSize.hashCode());
        Object $showTime = getShowTime();
        return (result21 * 59) + ($showTime == null ? 43 : $showTime.hashCode());
    }

    public String toString() {
        return "ActiveDeviceResponse(deviceId=" + getDeviceId() + ", orgId=" + getOrgId() + ", orgCode=" + getOrgCode() + ", deviceName=" + getDeviceName() + ", deviceType=" + getDeviceType() + ", deviceSn=" + getDeviceSn() + ", token=" + getToken() + ", speechReminderInfo=" + getSpeechReminderInfo() + ", showReminderInfo=" + getShowReminderInfo() + ", genType=" + getGenType() + ", openingTime=" + getOpeningTime() + ", identifyDistance=" + getIdentifyDistance() + ", identifyModule=" + getIdentifyModule() + ", thresholdScore=" + getThresholdScore() + ", fastMode=" + getFastMode() + ", frame=" + getFrame() + ", light=" + getLight() + ", snapMode=" + getSnapMode() + ", maskMode=" + getMaskMode() + ", ringMode=" + getRingMode() + ", fontSize=" + getFontSize() + ", showTime=" + getShowTime() + ")";
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getToken() {
        return this.token;
    }

    public String getSpeechReminderInfo() {
        return this.speechReminderInfo;
    }

    public String getShowReminderInfo() {
        return this.showReminderInfo;
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

    public Integer getFrame() {
        return this.frame;
    }

    public Integer getLight() {
        return this.light;
    }

    public Integer getSnapMode() {
        return this.snapMode;
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
}

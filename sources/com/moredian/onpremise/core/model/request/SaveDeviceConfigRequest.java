package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.model.dto.GroupDeviceGroupDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

@ApiModel(description = "保存设备设置")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveDeviceConfigRequest.class */
public class SaveDeviceConfigRequest extends BaseRequest {

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceVoice", value = "设备音量类型：1-标准模式，2-低音模式，3-静音模式")
    private Integer deviceVoice;

    @ApiModelProperty(name = ConstraintHelper.GROUPS, value = "设备群组")
    private List<GroupDeviceGroupDto> groups;

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

    @ApiModelProperty(name = "fastMode", value = "急速模式：0-快速，1-普通，2-严格；3-户外")
    private Integer fastMode;

    @ApiModelProperty(name = "light", value = "补光阈值")
    private Integer light;

    @ApiModelProperty(name = "maskMode", value = "口罩模式：0-关闭；1-开启")
    private Integer maskMode;

    @ApiModelProperty(name = "ringMode", value = "铃声模式：1-水晶；2-丝绸")
    private Integer ringMode;

    @ApiModelProperty(name = "fontSize", value = "字号：1-超小；2-小；3-中；4-大；5-超大")
    private Integer fontSize;

    @ApiModelProperty(name = "showTime", value = "展示时长（毫秒）")
    private Integer showTime;

    @ApiModelProperty(name = "syncMode", value = "同步模式：1-定时；2-实时")
    private Integer syncMode;

    @ApiModelProperty(name = "direction", value = "门禁方向：1-进；2-出")
    private Integer direction;

    @ApiModelProperty(name = "focus", value = "识别遮罩层显示：0-关闭；1-开启")
    private Integer focus;

    @ApiModelProperty(name = "maskRemindPicture", value = "未戴口罩图片提醒：0-关闭；1-开启")
    private Integer maskRemindPicture;

    @ApiModelProperty(name = "maskRemindVoice", value = "未戴口罩声音提醒：0-关闭；1-开启")
    private Integer maskRemindVoice;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceVoice(Integer deviceVoice) {
        this.deviceVoice = deviceVoice;
    }

    public void setGroups(List<GroupDeviceGroupDto> groups) {
        this.groups = groups;
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

    public void setSyncMode(Integer syncMode) {
        this.syncMode = syncMode;
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

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveDeviceConfigRequest)) {
            return false;
        }
        SaveDeviceConfigRequest other = (SaveDeviceConfigRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
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
        Object this$groups = getGroups();
        Object other$groups = other.getGroups();
        if (this$groups == null) {
            if (other$groups != null) {
                return false;
            }
        } else if (!this$groups.equals(other$groups)) {
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
        Object this$syncMode = getSyncMode();
        Object other$syncMode = other.getSyncMode();
        if (this$syncMode == null) {
            if (other$syncMode != null) {
                return false;
            }
        } else if (!this$syncMode.equals(other$syncMode)) {
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

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveDeviceConfigRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deviceName = getDeviceName();
        int result = (1 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceVoice = getDeviceVoice();
        int result3 = (result2 * 59) + ($deviceVoice == null ? 43 : $deviceVoice.hashCode());
        Object $groups = getGroups();
        int result4 = (result3 * 59) + ($groups == null ? 43 : $groups.hashCode());
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
        Object $syncMode = getSyncMode();
        int result16 = (result15 * 59) + ($syncMode == null ? 43 : $syncMode.hashCode());
        Object $direction = getDirection();
        int result17 = (result16 * 59) + ($direction == null ? 43 : $direction.hashCode());
        Object $focus = getFocus();
        int result18 = (result17 * 59) + ($focus == null ? 43 : $focus.hashCode());
        Object $maskRemindPicture = getMaskRemindPicture();
        int result19 = (result18 * 59) + ($maskRemindPicture == null ? 43 : $maskRemindPicture.hashCode());
        Object $maskRemindVoice = getMaskRemindVoice();
        return (result19 * 59) + ($maskRemindVoice == null ? 43 : $maskRemindVoice.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveDeviceConfigRequest(deviceName=" + getDeviceName() + ", deviceSn=" + getDeviceSn() + ", deviceVoice=" + getDeviceVoice() + ", groups=" + getGroups() + ", genType=" + getGenType() + ", openingTime=" + getOpeningTime() + ", identifyDistance=" + getIdentifyDistance() + ", identifyModule=" + getIdentifyModule() + ", thresholdScore=" + getThresholdScore() + ", fastMode=" + getFastMode() + ", light=" + getLight() + ", maskMode=" + getMaskMode() + ", ringMode=" + getRingMode() + ", fontSize=" + getFontSize() + ", showTime=" + getShowTime() + ", syncMode=" + getSyncMode() + ", direction=" + getDirection() + ", focus=" + getFocus() + ", maskRemindPicture=" + getMaskRemindPicture() + ", maskRemindVoice=" + getMaskRemindVoice() + ")";
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getDeviceVoice() {
        return this.deviceVoice;
    }

    public List<GroupDeviceGroupDto> getGroups() {
        return this.groups;
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

    public Integer getSyncMode() {
        return this.syncMode;
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

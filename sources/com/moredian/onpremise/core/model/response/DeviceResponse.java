package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "设备响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/DeviceResponse.class */
public class DeviceResponse implements Serializable {

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Long deviceId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "deviceType", value = "设备类型：1-门禁机，2-消费机-C，3-梯控机，4-自证机，5-消费机-A，6-消费机-W，7-销课机，8-温控机-A，9-温控机-B")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceModel", value = "设备型号，例：D2,D1,MS3")
    private String deviceModel;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "netType", value = "网络信息：1-无线网络，2-有线网络")
    private Integer netType;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "onlineStatus", value = "状态：1-在线，2-离线")
    private Integer onlineStatus;

    @ApiModelProperty(name = "deviceStatus", value = "设备状态：1-已激活，2-未激活")
    private Integer deviceStatus;

    @ApiModelProperty(name = "macAddress", value = "mac地址")
    private String macAddress;

    @ApiModelProperty(name = "version", value = "软件版本")
    private String version;

    @ApiModelProperty(name = "romVersion", value = "rom版本")
    private String romVersion;

    @ApiModelProperty(name = "deviceVoice", value = "设备音量类型：1-标准模式，2-低音模式，3-静音模式")
    private Integer deviceVoice;

    @ApiModelProperty(name = "deviceIp", value = "设备安装ip")
    private String deviceIp;

    @ApiModelProperty(name = "genType", value = "韦根协议类型：1：26-bit，2：34-bit")
    private Integer genType;

    @ApiModelProperty(name = "openingTime", value = "开门时长")
    private Integer openingTime;

    @ApiModelProperty(name = "identifyDistance", value = "识别距离 ，0-2米均分成10个档次，1-10每个档次递增0.2米")
    private Integer identifyDistance;

    @ApiModelProperty(name = "identifyModule", value = "识别模式：1-单人识别，2-多人识别")
    private Integer identifyModule;

    @Deprecated
    @ApiModelProperty(name = "groupIds", value = "设备所在权限组id列表", hidden = true)
    private List<Long> groupIds;

    @ApiModelProperty(name = "thresholdScore", value = "识别相似度阈值,百分制")
    private Integer thresholdScore;

    @ApiModelProperty(name = "fastMode", value = "活检等级：0-快速；1-一般；2-严格；3-户外")
    private Integer fastMode;

    @ApiModelProperty(name = "light", value = "补光阈值")
    private Integer light;

    @ApiModelProperty(name = "maskMode", value = "口罩模式：0-关闭；1-开启")
    private Integer maskMode;

    @ApiModelProperty(name = "ringMode", value = "铃声模式：1-水晶；2-丝绸")
    private Integer ringMode;

    @ApiModelProperty(name = "upgradeStatus", value = "设备状态：1-火警；2-可升级；3-离线；4-升级中；5-无状态")
    private Integer upgradeStatus;

    @ApiModelProperty(name = "groupName", value = "设备所在权限组名称")
    private String groupName = "";

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

    @ApiModelProperty(name = "deviceGroupId", value = "设备组id")
    private Long deviceGroupId;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }

    public void setDeviceVoice(Integer deviceVoice) {
        this.deviceVoice = deviceVoice;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
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

    @Deprecated
    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
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

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceResponse)) {
            return false;
        }
        DeviceResponse other = (DeviceResponse) o;
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
        Object this$deviceType = getDeviceType();
        Object other$deviceType = other.getDeviceType();
        if (this$deviceType == null) {
            if (other$deviceType != null) {
                return false;
            }
        } else if (!this$deviceType.equals(other$deviceType)) {
            return false;
        }
        Object this$deviceModel = getDeviceModel();
        Object other$deviceModel = other.getDeviceModel();
        if (this$deviceModel == null) {
            if (other$deviceModel != null) {
                return false;
            }
        } else if (!this$deviceModel.equals(other$deviceModel)) {
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
        Object this$netType = getNetType();
        Object other$netType = other.getNetType();
        if (this$netType == null) {
            if (other$netType != null) {
                return false;
            }
        } else if (!this$netType.equals(other$netType)) {
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
        Object this$onlineStatus = getOnlineStatus();
        Object other$onlineStatus = other.getOnlineStatus();
        if (this$onlineStatus == null) {
            if (other$onlineStatus != null) {
                return false;
            }
        } else if (!this$onlineStatus.equals(other$onlineStatus)) {
            return false;
        }
        Object this$deviceStatus = getDeviceStatus();
        Object other$deviceStatus = other.getDeviceStatus();
        if (this$deviceStatus == null) {
            if (other$deviceStatus != null) {
                return false;
            }
        } else if (!this$deviceStatus.equals(other$deviceStatus)) {
            return false;
        }
        Object this$macAddress = getMacAddress();
        Object other$macAddress = other.getMacAddress();
        if (this$macAddress == null) {
            if (other$macAddress != null) {
                return false;
            }
        } else if (!this$macAddress.equals(other$macAddress)) {
            return false;
        }
        Object this$version = getVersion();
        Object other$version = other.getVersion();
        if (this$version == null) {
            if (other$version != null) {
                return false;
            }
        } else if (!this$version.equals(other$version)) {
            return false;
        }
        Object this$romVersion = getRomVersion();
        Object other$romVersion = other.getRomVersion();
        if (this$romVersion == null) {
            if (other$romVersion != null) {
                return false;
            }
        } else if (!this$romVersion.equals(other$romVersion)) {
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
        Object this$deviceIp = getDeviceIp();
        Object other$deviceIp = other.getDeviceIp();
        if (this$deviceIp == null) {
            if (other$deviceIp != null) {
                return false;
            }
        } else if (!this$deviceIp.equals(other$deviceIp)) {
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
        Object this$groupIds = getGroupIds();
        Object other$groupIds = other.getGroupIds();
        if (this$groupIds == null) {
            if (other$groupIds != null) {
                return false;
            }
        } else if (!this$groupIds.equals(other$groupIds)) {
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
        Object this$upgradeStatus = getUpgradeStatus();
        Object other$upgradeStatus = other.getUpgradeStatus();
        if (this$upgradeStatus == null) {
            if (other$upgradeStatus != null) {
                return false;
            }
        } else if (!this$upgradeStatus.equals(other$upgradeStatus)) {
            return false;
        }
        Object this$groupName = getGroupName();
        Object other$groupName = other.getGroupName();
        if (this$groupName == null) {
            if (other$groupName != null) {
                return false;
            }
        } else if (!this$groupName.equals(other$groupName)) {
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
        if (this$maskRemindVoice == null) {
            if (other$maskRemindVoice != null) {
                return false;
            }
        } else if (!this$maskRemindVoice.equals(other$maskRemindVoice)) {
            return false;
        }
        Object this$deviceGroupId = getDeviceGroupId();
        Object other$deviceGroupId = other.getDeviceGroupId();
        return this$deviceGroupId == null ? other$deviceGroupId == null : this$deviceGroupId.equals(other$deviceGroupId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceResponse;
    }

    public int hashCode() {
        Object $deviceId = getDeviceId();
        int result = (1 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deviceType = getDeviceType();
        int result3 = (result2 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
        Object $deviceModel = getDeviceModel();
        int result4 = (result3 * 59) + ($deviceModel == null ? 43 : $deviceModel.hashCode());
        Object $deviceName = getDeviceName();
        int result5 = (result4 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $netType = getNetType();
        int result6 = (result5 * 59) + ($netType == null ? 43 : $netType.hashCode());
        Object $deviceSn = getDeviceSn();
        int result7 = (result6 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $onlineStatus = getOnlineStatus();
        int result8 = (result7 * 59) + ($onlineStatus == null ? 43 : $onlineStatus.hashCode());
        Object $deviceStatus = getDeviceStatus();
        int result9 = (result8 * 59) + ($deviceStatus == null ? 43 : $deviceStatus.hashCode());
        Object $macAddress = getMacAddress();
        int result10 = (result9 * 59) + ($macAddress == null ? 43 : $macAddress.hashCode());
        Object $version = getVersion();
        int result11 = (result10 * 59) + ($version == null ? 43 : $version.hashCode());
        Object $romVersion = getRomVersion();
        int result12 = (result11 * 59) + ($romVersion == null ? 43 : $romVersion.hashCode());
        Object $deviceVoice = getDeviceVoice();
        int result13 = (result12 * 59) + ($deviceVoice == null ? 43 : $deviceVoice.hashCode());
        Object $deviceIp = getDeviceIp();
        int result14 = (result13 * 59) + ($deviceIp == null ? 43 : $deviceIp.hashCode());
        Object $genType = getGenType();
        int result15 = (result14 * 59) + ($genType == null ? 43 : $genType.hashCode());
        Object $openingTime = getOpeningTime();
        int result16 = (result15 * 59) + ($openingTime == null ? 43 : $openingTime.hashCode());
        Object $identifyDistance = getIdentifyDistance();
        int result17 = (result16 * 59) + ($identifyDistance == null ? 43 : $identifyDistance.hashCode());
        Object $identifyModule = getIdentifyModule();
        int result18 = (result17 * 59) + ($identifyModule == null ? 43 : $identifyModule.hashCode());
        Object $groupIds = getGroupIds();
        int result19 = (result18 * 59) + ($groupIds == null ? 43 : $groupIds.hashCode());
        Object $thresholdScore = getThresholdScore();
        int result20 = (result19 * 59) + ($thresholdScore == null ? 43 : $thresholdScore.hashCode());
        Object $fastMode = getFastMode();
        int result21 = (result20 * 59) + ($fastMode == null ? 43 : $fastMode.hashCode());
        Object $light = getLight();
        int result22 = (result21 * 59) + ($light == null ? 43 : $light.hashCode());
        Object $maskMode = getMaskMode();
        int result23 = (result22 * 59) + ($maskMode == null ? 43 : $maskMode.hashCode());
        Object $ringMode = getRingMode();
        int result24 = (result23 * 59) + ($ringMode == null ? 43 : $ringMode.hashCode());
        Object $upgradeStatus = getUpgradeStatus();
        int result25 = (result24 * 59) + ($upgradeStatus == null ? 43 : $upgradeStatus.hashCode());
        Object $groupName = getGroupName();
        int result26 = (result25 * 59) + ($groupName == null ? 43 : $groupName.hashCode());
        Object $fontSize = getFontSize();
        int result27 = (result26 * 59) + ($fontSize == null ? 43 : $fontSize.hashCode());
        Object $showTime = getShowTime();
        int result28 = (result27 * 59) + ($showTime == null ? 43 : $showTime.hashCode());
        Object $syncMode = getSyncMode();
        int result29 = (result28 * 59) + ($syncMode == null ? 43 : $syncMode.hashCode());
        Object $direction = getDirection();
        int result30 = (result29 * 59) + ($direction == null ? 43 : $direction.hashCode());
        Object $focus = getFocus();
        int result31 = (result30 * 59) + ($focus == null ? 43 : $focus.hashCode());
        Object $maskRemindPicture = getMaskRemindPicture();
        int result32 = (result31 * 59) + ($maskRemindPicture == null ? 43 : $maskRemindPicture.hashCode());
        Object $maskRemindVoice = getMaskRemindVoice();
        int result33 = (result32 * 59) + ($maskRemindVoice == null ? 43 : $maskRemindVoice.hashCode());
        Object $deviceGroupId = getDeviceGroupId();
        return (result33 * 59) + ($deviceGroupId == null ? 43 : $deviceGroupId.hashCode());
    }

    public String toString() {
        return "DeviceResponse(deviceId=" + getDeviceId() + ", orgId=" + getOrgId() + ", deviceType=" + getDeviceType() + ", deviceModel=" + getDeviceModel() + ", deviceName=" + getDeviceName() + ", netType=" + getNetType() + ", deviceSn=" + getDeviceSn() + ", onlineStatus=" + getOnlineStatus() + ", deviceStatus=" + getDeviceStatus() + ", macAddress=" + getMacAddress() + ", version=" + getVersion() + ", romVersion=" + getRomVersion() + ", deviceVoice=" + getDeviceVoice() + ", deviceIp=" + getDeviceIp() + ", genType=" + getGenType() + ", openingTime=" + getOpeningTime() + ", identifyDistance=" + getIdentifyDistance() + ", identifyModule=" + getIdentifyModule() + ", groupIds=" + getGroupIds() + ", thresholdScore=" + getThresholdScore() + ", fastMode=" + getFastMode() + ", light=" + getLight() + ", maskMode=" + getMaskMode() + ", ringMode=" + getRingMode() + ", upgradeStatus=" + getUpgradeStatus() + ", groupName=" + getGroupName() + ", fontSize=" + getFontSize() + ", showTime=" + getShowTime() + ", syncMode=" + getSyncMode() + ", direction=" + getDirection() + ", focus=" + getFocus() + ", maskRemindPicture=" + getMaskRemindPicture() + ", maskRemindVoice=" + getMaskRemindVoice() + ", deviceGroupId=" + getDeviceGroupId() + ")";
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Integer getNetType() {
        return this.netType;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getOnlineStatus() {
        return this.onlineStatus;
    }

    public Integer getDeviceStatus() {
        return this.deviceStatus;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getVersion() {
        return this.version;
    }

    public String getRomVersion() {
        return this.romVersion;
    }

    public Integer getDeviceVoice() {
        return this.deviceVoice;
    }

    public String getDeviceIp() {
        return this.deviceIp;
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

    @Deprecated
    public List<Long> getGroupIds() {
        return this.groupIds;
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

    public Integer getUpgradeStatus() {
        return this.upgradeStatus;
    }

    public String getGroupName() {
        return this.groupName;
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

    public Long getDeviceGroupId() {
        return this.deviceGroupId;
    }
}

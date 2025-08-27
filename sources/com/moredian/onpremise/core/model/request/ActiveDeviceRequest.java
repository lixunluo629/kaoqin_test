package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备激活参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ActiveDeviceRequest.class */
public class ActiveDeviceRequest implements Serializable {
    private static final long serialVersionUID = -422919235794332775L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "ipAddress", value = "设备ip地址")
    private String ipAddress;

    @ApiModelProperty(name = "version", value = "apk版本号")
    private String version;

    @ApiModelProperty(name = "romVersion", value = "rom版本号")
    private String romVersion;

    @ApiModelProperty(name = "deviceType", value = "设备类型：1-门禁机，2-消费机-C，3-梯控机，4-自证机，5-消费机-A，6-消费机-W，7-销课机，8-温控机-A，9-温控机-B")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceModel", value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(name = "netType", value = "网络信息：1-无线网络，2-有线网络")
    private Integer netType;

    @ApiModelProperty(name = "macAddress", value = "mac地址")
    private String macAddress;

    @ApiModelProperty(name = "secret", value = "秘钥")
    private String secret;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceGroupId", value = "设备组id")
    private Long deviceGroupId;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ActiveDeviceRequest)) {
            return false;
        }
        ActiveDeviceRequest other = (ActiveDeviceRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$ipAddress = getIpAddress();
        Object other$ipAddress = other.getIpAddress();
        if (this$ipAddress == null) {
            if (other$ipAddress != null) {
                return false;
            }
        } else if (!this$ipAddress.equals(other$ipAddress)) {
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
        Object this$netType = getNetType();
        Object other$netType = other.getNetType();
        if (this$netType == null) {
            if (other$netType != null) {
                return false;
            }
        } else if (!this$netType.equals(other$netType)) {
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
        Object this$secret = getSecret();
        Object other$secret = other.getSecret();
        if (this$secret == null) {
            if (other$secret != null) {
                return false;
            }
        } else if (!this$secret.equals(other$secret)) {
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
        Object this$deviceGroupId = getDeviceGroupId();
        Object other$deviceGroupId = other.getDeviceGroupId();
        return this$deviceGroupId == null ? other$deviceGroupId == null : this$deviceGroupId.equals(other$deviceGroupId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ActiveDeviceRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $ipAddress = getIpAddress();
        int result2 = (result * 59) + ($ipAddress == null ? 43 : $ipAddress.hashCode());
        Object $version = getVersion();
        int result3 = (result2 * 59) + ($version == null ? 43 : $version.hashCode());
        Object $romVersion = getRomVersion();
        int result4 = (result3 * 59) + ($romVersion == null ? 43 : $romVersion.hashCode());
        Object $deviceType = getDeviceType();
        int result5 = (result4 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
        Object $deviceModel = getDeviceModel();
        int result6 = (result5 * 59) + ($deviceModel == null ? 43 : $deviceModel.hashCode());
        Object $netType = getNetType();
        int result7 = (result6 * 59) + ($netType == null ? 43 : $netType.hashCode());
        Object $macAddress = getMacAddress();
        int result8 = (result7 * 59) + ($macAddress == null ? 43 : $macAddress.hashCode());
        Object $secret = getSecret();
        int result9 = (result8 * 59) + ($secret == null ? 43 : $secret.hashCode());
        Object $deviceName = getDeviceName();
        int result10 = (result9 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceGroupId = getDeviceGroupId();
        return (result10 * 59) + ($deviceGroupId == null ? 43 : $deviceGroupId.hashCode());
    }

    public String toString() {
        return "ActiveDeviceRequest(deviceSn=" + getDeviceSn() + ", ipAddress=" + getIpAddress() + ", version=" + getVersion() + ", romVersion=" + getRomVersion() + ", deviceType=" + getDeviceType() + ", deviceModel=" + getDeviceModel() + ", netType=" + getNetType() + ", macAddress=" + getMacAddress() + ", secret=" + getSecret() + ", deviceName=" + getDeviceName() + ", deviceGroupId=" + getDeviceGroupId() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getVersion() {
        return this.version;
    }

    public String getRomVersion() {
        return this.romVersion;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public Integer getNetType() {
        return this.netType;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Long getDeviceGroupId() {
        return this.deviceGroupId;
    }
}

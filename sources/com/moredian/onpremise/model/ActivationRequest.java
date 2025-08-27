package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/ActivationRequest.class */
public class ActivationRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.ACTIVATION_REQUEST;
    private String cpuId;
    private String ipAddress;
    private String version;
    private String romVersion;
    private Integer netType;
    private Integer equipmentType;
    private Integer appType;
    private String serialNumber;
    private String macAddress;
    private String secret;
    private String deviceModel;
    private String deviceName;

    public void setCpuId(String cpuId) {
        this.cpuId = cpuId;
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

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public void setEquipmentType(Integer equipmentType) {
        this.equipmentType = equipmentType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ActivationRequest)) {
            return false;
        }
        ActivationRequest other = (ActivationRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$cpuId = getCpuId();
        Object other$cpuId = other.getCpuId();
        if (this$cpuId == null) {
            if (other$cpuId != null) {
                return false;
            }
        } else if (!this$cpuId.equals(other$cpuId)) {
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
        Object this$netType = getNetType();
        Object other$netType = other.getNetType();
        if (this$netType == null) {
            if (other$netType != null) {
                return false;
            }
        } else if (!this$netType.equals(other$netType)) {
            return false;
        }
        Object this$equipmentType = getEquipmentType();
        Object other$equipmentType = other.getEquipmentType();
        if (this$equipmentType == null) {
            if (other$equipmentType != null) {
                return false;
            }
        } else if (!this$equipmentType.equals(other$equipmentType)) {
            return false;
        }
        Object this$appType = getAppType();
        Object other$appType = other.getAppType();
        if (this$appType == null) {
            if (other$appType != null) {
                return false;
            }
        } else if (!this$appType.equals(other$appType)) {
            return false;
        }
        Object this$serialNumber = getSerialNumber();
        Object other$serialNumber = other.getSerialNumber();
        if (this$serialNumber == null) {
            if (other$serialNumber != null) {
                return false;
            }
        } else if (!this$serialNumber.equals(other$serialNumber)) {
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
        return this$deviceName == null ? other$deviceName == null : this$deviceName.equals(other$deviceName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ActivationRequest;
    }

    public int hashCode() {
        Object $cpuId = getCpuId();
        int result = (1 * 59) + ($cpuId == null ? 43 : $cpuId.hashCode());
        Object $ipAddress = getIpAddress();
        int result2 = (result * 59) + ($ipAddress == null ? 43 : $ipAddress.hashCode());
        Object $version = getVersion();
        int result3 = (result2 * 59) + ($version == null ? 43 : $version.hashCode());
        Object $romVersion = getRomVersion();
        int result4 = (result3 * 59) + ($romVersion == null ? 43 : $romVersion.hashCode());
        Object $netType = getNetType();
        int result5 = (result4 * 59) + ($netType == null ? 43 : $netType.hashCode());
        Object $equipmentType = getEquipmentType();
        int result6 = (result5 * 59) + ($equipmentType == null ? 43 : $equipmentType.hashCode());
        Object $appType = getAppType();
        int result7 = (result6 * 59) + ($appType == null ? 43 : $appType.hashCode());
        Object $serialNumber = getSerialNumber();
        int result8 = (result7 * 59) + ($serialNumber == null ? 43 : $serialNumber.hashCode());
        Object $macAddress = getMacAddress();
        int result9 = (result8 * 59) + ($macAddress == null ? 43 : $macAddress.hashCode());
        Object $secret = getSecret();
        int result10 = (result9 * 59) + ($secret == null ? 43 : $secret.hashCode());
        Object $deviceModel = getDeviceModel();
        int result11 = (result10 * 59) + ($deviceModel == null ? 43 : $deviceModel.hashCode());
        Object $deviceName = getDeviceName();
        return (result11 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
    }

    public String toString() {
        return "ActivationRequest(cpuId=" + getCpuId() + ", ipAddress=" + getIpAddress() + ", version=" + getVersion() + ", romVersion=" + getRomVersion() + ", netType=" + getNetType() + ", equipmentType=" + getEquipmentType() + ", appType=" + getAppType() + ", serialNumber=" + getSerialNumber() + ", macAddress=" + getMacAddress() + ", secret=" + getSecret() + ", deviceModel=" + getDeviceModel() + ", deviceName=" + getDeviceName() + ")";
    }

    public String getCpuId() {
        return this.cpuId;
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

    public Integer getNetType() {
        return this.netType;
    }

    public Integer getEquipmentType() {
        return this.equipmentType;
    }

    public Integer getAppType() {
        return this.appType;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}

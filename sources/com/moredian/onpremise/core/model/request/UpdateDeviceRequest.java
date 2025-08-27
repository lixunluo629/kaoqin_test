package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "更新设备信息请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UpdateDeviceRequest.class */
public class UpdateDeviceRequest extends BaseRequest {
    private static final long serialVersionUID = -4108140007495188918L;

    @ApiModelProperty(name = "ipAddress", value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(name = "macAddress", value = "mac地址")
    private String macAddress;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "softVersion", value = "软件版本")
    private String softVersion;

    @ApiModelProperty(name = "romVersion", value = "rom版本")
    private String romVersion;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateDeviceRequest)) {
            return false;
        }
        UpdateDeviceRequest other = (UpdateDeviceRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$macAddress = getMacAddress();
        Object other$macAddress = other.getMacAddress();
        if (this$macAddress == null) {
            if (other$macAddress != null) {
                return false;
            }
        } else if (!this$macAddress.equals(other$macAddress)) {
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
        Object this$softVersion = getSoftVersion();
        Object other$softVersion = other.getSoftVersion();
        if (this$softVersion == null) {
            if (other$softVersion != null) {
                return false;
            }
        } else if (!this$softVersion.equals(other$softVersion)) {
            return false;
        }
        Object this$romVersion = getRomVersion();
        Object other$romVersion = other.getRomVersion();
        return this$romVersion == null ? other$romVersion == null : this$romVersion.equals(other$romVersion);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UpdateDeviceRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $ipAddress = getIpAddress();
        int result = (1 * 59) + ($ipAddress == null ? 43 : $ipAddress.hashCode());
        Object $macAddress = getMacAddress();
        int result2 = (result * 59) + ($macAddress == null ? 43 : $macAddress.hashCode());
        Object $deviceSn = getDeviceSn();
        int result3 = (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $softVersion = getSoftVersion();
        int result4 = (result3 * 59) + ($softVersion == null ? 43 : $softVersion.hashCode());
        Object $romVersion = getRomVersion();
        return (result4 * 59) + ($romVersion == null ? 43 : $romVersion.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UpdateDeviceRequest(ipAddress=" + getIpAddress() + ", macAddress=" + getMacAddress() + ", deviceSn=" + getDeviceSn() + ", softVersion=" + getSoftVersion() + ", romVersion=" + getRomVersion() + ")";
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getSoftVersion() {
        return this.softVersion;
    }

    public String getRomVersion() {
        return this.romVersion;
    }
}

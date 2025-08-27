package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "保存设备设置")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSaveDeviceConfigRequest.class */
public class TerminalSaveDeviceConfigRequest implements Serializable {

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSaveDeviceConfigRequest)) {
            return false;
        }
        TerminalSaveDeviceConfigRequest other = (TerminalSaveDeviceConfigRequest) o;
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
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSaveDeviceConfigRequest;
    }

    public int hashCode() {
        Object $deviceName = getDeviceName();
        int result = (1 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceSn = getDeviceSn();
        return (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
    }

    public String toString() {
        return "TerminalSaveDeviceConfigRequest(deviceName=" + getDeviceName() + ", deviceSn=" + getDeviceSn() + ")";
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}

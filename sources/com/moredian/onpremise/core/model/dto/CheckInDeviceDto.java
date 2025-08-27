package com.moredian.onpremise.core.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "签到设备信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/CheckInDeviceDto.class */
public class CheckInDeviceDto implements Serializable {
    private static final long serialVersionUID = -7176817170242310838L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceSn", value = "设备名称")
    private String deviceName;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckInDeviceDto)) {
            return false;
        }
        CheckInDeviceDto other = (CheckInDeviceDto) o;
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
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        return this$deviceName == null ? other$deviceName == null : this$deviceName.equals(other$deviceName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CheckInDeviceDto;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        return (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
    }

    public String toString() {
        return "CheckInDeviceDto(deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}

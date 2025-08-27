package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备类型信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/DeviceTypeResponse.class */
public class DeviceTypeResponse implements Serializable {
    private static final long serialVersionUID = 741561181519211521L;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceTypeResponse)) {
            return false;
        }
        DeviceTypeResponse other = (DeviceTypeResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$deviceTypeName = getDeviceTypeName();
        Object other$deviceTypeName = other.getDeviceTypeName();
        return this$deviceTypeName == null ? other$deviceTypeName == null : this$deviceTypeName.equals(other$deviceTypeName);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceTypeResponse;
    }

    public int hashCode() {
        Object $deviceType = getDeviceType();
        int result = (1 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
        Object $deviceTypeName = getDeviceTypeName();
        return (result * 59) + ($deviceTypeName == null ? 43 : $deviceTypeName.hashCode());
    }

    public String toString() {
        return "DeviceTypeResponse(deviceType=" + getDeviceType() + ", deviceTypeName=" + getDeviceTypeName() + ")";
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public String getDeviceTypeName() {
        return this.deviceTypeName;
    }
}

package com.moredian.onpremise.core.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(description = "测温设备信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/TemperatureDeviceDto.class */
public class TemperatureDeviceDto implements Serializable {
    private static final long serialVersionUID = -8949968556269820234L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "onlineStatus", value = "设备在线状态：1-在线，2-离线")
    private Integer onlineStatus;

    @ApiModelProperty(name = "temperatureEnable", value = "测温开关：1-开启，0-关闭")
    private Integer temperatureEnable;

    @ApiModelProperty(name = "temperatureFactor", value = "测温系数")
    private BigDecimal temperatureFactor;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setTemperatureEnable(Integer temperatureEnable) {
        this.temperatureEnable = temperatureEnable;
    }

    public void setTemperatureFactor(BigDecimal temperatureFactor) {
        this.temperatureFactor = temperatureFactor;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemperatureDeviceDto)) {
            return false;
        }
        TemperatureDeviceDto other = (TemperatureDeviceDto) o;
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
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
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
        Object this$temperatureEnable = getTemperatureEnable();
        Object other$temperatureEnable = other.getTemperatureEnable();
        if (this$temperatureEnable == null) {
            if (other$temperatureEnable != null) {
                return false;
            }
        } else if (!this$temperatureEnable.equals(other$temperatureEnable)) {
            return false;
        }
        Object this$temperatureFactor = getTemperatureFactor();
        Object other$temperatureFactor = other.getTemperatureFactor();
        return this$temperatureFactor == null ? other$temperatureFactor == null : this$temperatureFactor.equals(other$temperatureFactor);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TemperatureDeviceDto;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result2 = (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $onlineStatus = getOnlineStatus();
        int result3 = (result2 * 59) + ($onlineStatus == null ? 43 : $onlineStatus.hashCode());
        Object $temperatureEnable = getTemperatureEnable();
        int result4 = (result3 * 59) + ($temperatureEnable == null ? 43 : $temperatureEnable.hashCode());
        Object $temperatureFactor = getTemperatureFactor();
        return (result4 * 59) + ($temperatureFactor == null ? 43 : $temperatureFactor.hashCode());
    }

    public String toString() {
        return "TemperatureDeviceDto(deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", onlineStatus=" + getOnlineStatus() + ", temperatureEnable=" + getTemperatureEnable() + ", temperatureFactor=" + getTemperatureFactor() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Integer getOnlineStatus() {
        return this.onlineStatus;
    }

    public Integer getTemperatureEnable() {
        return this.temperatureEnable;
    }

    public BigDecimal getTemperatureFactor() {
        return this.temperatureFactor;
    }
}

package com.moredian.onpremise.core.model.dto;

import com.moredian.onpremise.core.utils.MyListUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ApiModel(description = "设备信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/dto/DeviceDto.class */
public class DeviceDto implements Serializable {
    private static final long serialVersionUID = -8936227503766276548L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String toString() {
        return "DeviceDto(deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceDto that = (DeviceDto) o;
        return Objects.equals(this.deviceSn, that.deviceSn);
    }

    public int hashCode() {
        return Objects.hash(this.deviceSn);
    }

    public static List<String> deviceDtoToString(List<DeviceDto> deviceDtos) {
        List<String> result = new ArrayList<>();
        if (MyListUtils.listIsEmpty(deviceDtos)) {
            for (DeviceDto dto : deviceDtos) {
                result.add(dto.getDeviceSn());
            }
        }
        return result;
    }

    public static List<String> distinctDeviceDto(List<DeviceDto> deviceDtos) {
        Set<String> result = new HashSet<>();
        if (MyListUtils.listIsEmpty(deviceDtos)) {
            for (DeviceDto dto : deviceDtos) {
                result.add(dto.getDeviceSn());
            }
        }
        return new ArrayList(result);
    }
}

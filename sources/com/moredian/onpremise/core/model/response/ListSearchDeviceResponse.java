package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备条件筛选列表")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/ListSearchDeviceResponse.class */
public class ListSearchDeviceResponse implements Serializable {

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "onlineStatus", value = "设备在线状态：1-在线，2-离线")
    private Integer onlineStatus;

    @ApiModelProperty(name = "deviceType", value = "设备类型：1-门禁机，2-消费机-C，3-梯控机，4-自证机，5-消费机-A，6-消费机-W，7-销课机，8-温控机-A，9-温控机-B")
    private Integer deviceType;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListSearchDeviceResponse)) {
            return false;
        }
        ListSearchDeviceResponse other = (ListSearchDeviceResponse) o;
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
        Object this$onlineStatus = getOnlineStatus();
        Object other$onlineStatus = other.getOnlineStatus();
        if (this$onlineStatus == null) {
            if (other$onlineStatus != null) {
                return false;
            }
        } else if (!this$onlineStatus.equals(other$onlineStatus)) {
            return false;
        }
        Object this$deviceType = getDeviceType();
        Object other$deviceType = other.getDeviceType();
        return this$deviceType == null ? other$deviceType == null : this$deviceType.equals(other$deviceType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ListSearchDeviceResponse;
    }

    public int hashCode() {
        Object $deviceName = getDeviceName();
        int result = (1 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $onlineStatus = getOnlineStatus();
        int result3 = (result2 * 59) + ($onlineStatus == null ? 43 : $onlineStatus.hashCode());
        Object $deviceType = getDeviceType();
        return (result3 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
    }

    public String toString() {
        return "ListSearchDeviceResponse(deviceName=" + getDeviceName() + ", deviceSn=" + getDeviceSn() + ", onlineStatus=" + getOnlineStatus() + ", deviceType=" + getDeviceType() + ")";
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getOnlineStatus() {
        return this.onlineStatus;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }
}

package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "账户管理设备响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AccountManageDeviceResponse.class */
public class AccountManageDeviceResponse implements Serializable {
    private static final long serialVersionUID = -429552487568956532L;

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Long deviceId;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceType", value = "设备类型：1-门禁机，2-消费机-C，3-梯控机，4-自证机，5-消费机-A，6-消费机-W，7-销课机，8-温控机-A，9-温控机-B")
    private Integer deviceType;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccountManageDeviceResponse)) {
            return false;
        }
        AccountManageDeviceResponse other = (AccountManageDeviceResponse) o;
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
        Object this$deviceType = getDeviceType();
        Object other$deviceType = other.getDeviceType();
        return this$deviceType == null ? other$deviceType == null : this$deviceType.equals(other$deviceType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AccountManageDeviceResponse;
    }

    public int hashCode() {
        Object $deviceId = getDeviceId();
        int result = (1 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $deviceName = getDeviceName();
        int result2 = (result * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $deviceSn = getDeviceSn();
        int result3 = (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceType = getDeviceType();
        return (result3 * 59) + ($deviceType == null ? 43 : $deviceType.hashCode());
    }

    public String toString() {
        return "AccountManageDeviceResponse(deviceId=" + getDeviceId() + ", deviceName=" + getDeviceName() + ", deviceSn=" + getDeviceSn() + ", deviceType=" + getDeviceType() + ")";
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }
}

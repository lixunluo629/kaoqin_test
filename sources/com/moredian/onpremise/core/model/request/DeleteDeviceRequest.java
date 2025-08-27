package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteDeviceRequest.class */
public class DeleteDeviceRequest extends BaseRequest {

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Long deviceId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteDeviceRequest)) {
            return false;
        }
        DeleteDeviceRequest other = (DeleteDeviceRequest) o;
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteDeviceRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deviceId = getDeviceId();
        int result = (1 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $deviceSn = getDeviceSn();
        return (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteDeviceRequest(deviceId=" + getDeviceId() + ", deviceSn=" + getDeviceSn() + ")";
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}

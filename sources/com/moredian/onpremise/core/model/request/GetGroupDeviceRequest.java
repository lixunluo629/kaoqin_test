package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/GetGroupDeviceRequest.class */
public class GetGroupDeviceRequest extends BaseRequest {
    private static final long serialVersionUID = 5805901279158104536L;

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Long deviceId;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GetGroupDeviceRequest)) {
            return false;
        }
        GetGroupDeviceRequest other = (GetGroupDeviceRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceId = getDeviceId();
        Object other$deviceId = other.getDeviceId();
        return this$deviceId == null ? other$deviceId == null : this$deviceId.equals(other$deviceId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof GetGroupDeviceRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deviceId = getDeviceId();
        int result = (1 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "GetGroupDeviceRequest(deviceId=" + getDeviceId() + ")";
    }

    public Long getDeviceId() {
        return this.deviceId;
    }
}

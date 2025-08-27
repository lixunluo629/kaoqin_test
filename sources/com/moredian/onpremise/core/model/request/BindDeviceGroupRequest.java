package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "设备绑定设备组参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BindDeviceGroupRequest.class */
public class BindDeviceGroupRequest extends BaseRequest {
    private static final long serialVersionUID = -2614264496365599069L;

    @ApiModelProperty(name = "deviceSns", value = "设备sn")
    private List<String> deviceSns;

    @ApiModelProperty(name = "deviceGroupId", value = "设备组id")
    private Long deviceGroupId;

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    public void setDeviceGroupId(Long deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BindDeviceGroupRequest)) {
            return false;
        }
        BindDeviceGroupRequest other = (BindDeviceGroupRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        if (this$deviceSns == null) {
            if (other$deviceSns != null) {
                return false;
            }
        } else if (!this$deviceSns.equals(other$deviceSns)) {
            return false;
        }
        Object this$deviceGroupId = getDeviceGroupId();
        Object other$deviceGroupId = other.getDeviceGroupId();
        return this$deviceGroupId == null ? other$deviceGroupId == null : this$deviceGroupId.equals(other$deviceGroupId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof BindDeviceGroupRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deviceSns = getDeviceSns();
        int result = (1 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        Object $deviceGroupId = getDeviceGroupId();
        return (result * 59) + ($deviceGroupId == null ? 43 : $deviceGroupId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "BindDeviceGroupRequest(deviceSns=" + getDeviceSns() + ", deviceGroupId=" + getDeviceGroupId() + ")";
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }

    public Long getDeviceGroupId() {
        return this.deviceGroupId;
    }
}

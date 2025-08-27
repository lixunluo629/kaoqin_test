package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "批量设备预激活参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/PreActiveDeviceListRequest.class */
public class PreActiveDeviceListRequest extends BaseRequest {
    private static final long serialVersionUID = -7981929348339162357L;

    @ApiModelProperty(name = "preActiveDevices", value = "预激活设备")
    private List<PreActiveDeviceRequest> preActiveDevices;

    public void setPreActiveDevices(List<PreActiveDeviceRequest> preActiveDevices) {
        this.preActiveDevices = preActiveDevices;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PreActiveDeviceListRequest)) {
            return false;
        }
        PreActiveDeviceListRequest other = (PreActiveDeviceListRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$preActiveDevices = getPreActiveDevices();
        Object other$preActiveDevices = other.getPreActiveDevices();
        return this$preActiveDevices == null ? other$preActiveDevices == null : this$preActiveDevices.equals(other$preActiveDevices);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof PreActiveDeviceListRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $preActiveDevices = getPreActiveDevices();
        int result = (1 * 59) + ($preActiveDevices == null ? 43 : $preActiveDevices.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "PreActiveDeviceListRequest(preActiveDevices=" + getPreActiveDevices() + ")";
    }

    public List<PreActiveDeviceRequest> getPreActiveDevices() {
        return this.preActiveDevices;
    }
}

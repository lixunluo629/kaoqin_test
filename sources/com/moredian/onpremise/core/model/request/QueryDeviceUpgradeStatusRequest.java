package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "查询设备更新状态")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryDeviceUpgradeStatusRequest.class */
public class QueryDeviceUpgradeStatusRequest extends BaseRequest {

    @ApiModelProperty(name = "deviceSns", value = "当前页面的设备sn集合")
    private List<String> deviceSns;

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryDeviceUpgradeStatusRequest)) {
            return false;
        }
        QueryDeviceUpgradeStatusRequest other = (QueryDeviceUpgradeStatusRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        return this$deviceSns == null ? other$deviceSns == null : this$deviceSns.equals(other$deviceSns);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryDeviceUpgradeStatusRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deviceSns = getDeviceSns();
        int result = (1 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryDeviceUpgradeStatusRequest(deviceSns=" + getDeviceSns() + ")";
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }
}

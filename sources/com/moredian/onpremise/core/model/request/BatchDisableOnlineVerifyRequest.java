package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "批量关闭在线鉴权")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BatchDisableOnlineVerifyRequest.class */
public class BatchDisableOnlineVerifyRequest implements Serializable {
    private static final long serialVersionUID = -2904879246090410848L;

    @ApiModelProperty(name = "deviceSns", value = "需要关闭在线鉴权的设备sn列表")
    private List<String> deviceSns;

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchDisableOnlineVerifyRequest)) {
            return false;
        }
        BatchDisableOnlineVerifyRequest other = (BatchDisableOnlineVerifyRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        return this$deviceSns == null ? other$deviceSns == null : this$deviceSns.equals(other$deviceSns);
    }

    protected boolean canEqual(Object other) {
        return other instanceof BatchDisableOnlineVerifyRequest;
    }

    public int hashCode() {
        Object $deviceSns = getDeviceSns();
        int result = (1 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        return result;
    }

    public String toString() {
        return "BatchDisableOnlineVerifyRequest(deviceSns=" + getDeviceSns() + ")";
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }
}

package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "关闭在线鉴权")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DisableOnlineVerifyRequest.class */
public class DisableOnlineVerifyRequest implements Serializable {
    private static final long serialVersionUID = 8449484705704644394L;

    @ApiModelProperty(name = "deviceSn", value = "需要关闭在线鉴权的设备sn")
    private String deviceSn;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DisableOnlineVerifyRequest)) {
            return false;
        }
        DisableOnlineVerifyRequest other = (DisableOnlineVerifyRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DisableOnlineVerifyRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        return result;
    }

    public String toString() {
        return "DisableOnlineVerifyRequest(deviceSn=" + getDeviceSn() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}

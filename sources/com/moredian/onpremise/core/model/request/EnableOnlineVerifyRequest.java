package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "开启在线鉴权")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/EnableOnlineVerifyRequest.class */
public class EnableOnlineVerifyRequest implements Serializable {
    private static final long serialVersionUID = -5192005143623470037L;

    @ApiModelProperty(name = "deviceSn", value = "需要开启在线鉴权的设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "onlineVerifyUrl", value = "第三方鉴权url")
    private String onlineVerifyUrl;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setOnlineVerifyUrl(String onlineVerifyUrl) {
        this.onlineVerifyUrl = onlineVerifyUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EnableOnlineVerifyRequest)) {
            return false;
        }
        EnableOnlineVerifyRequest other = (EnableOnlineVerifyRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$onlineVerifyUrl = getOnlineVerifyUrl();
        Object other$onlineVerifyUrl = other.getOnlineVerifyUrl();
        return this$onlineVerifyUrl == null ? other$onlineVerifyUrl == null : this$onlineVerifyUrl.equals(other$onlineVerifyUrl);
    }

    protected boolean canEqual(Object other) {
        return other instanceof EnableOnlineVerifyRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $onlineVerifyUrl = getOnlineVerifyUrl();
        return (result * 59) + ($onlineVerifyUrl == null ? 43 : $onlineVerifyUrl.hashCode());
    }

    public String toString() {
        return "EnableOnlineVerifyRequest(deviceSn=" + getDeviceSn() + ", onlineVerifyUrl=" + getOnlineVerifyUrl() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getOnlineVerifyUrl() {
        return this.onlineVerifyUrl;
    }
}

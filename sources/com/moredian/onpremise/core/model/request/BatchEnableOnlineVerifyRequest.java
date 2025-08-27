package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "批量开启在线鉴权")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BatchEnableOnlineVerifyRequest.class */
public class BatchEnableOnlineVerifyRequest implements Serializable {
    private static final long serialVersionUID = -8671884970861198251L;

    @ApiModelProperty(name = "deviceSns", value = "需要开启在线鉴权的设备sn列表")
    private List<String> deviceSns;

    @ApiModelProperty(name = "onlineVerifyUrl", value = "第三方鉴权url")
    private String onlineVerifyUrl;

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    public void setOnlineVerifyUrl(String onlineVerifyUrl) {
        this.onlineVerifyUrl = onlineVerifyUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchEnableOnlineVerifyRequest)) {
            return false;
        }
        BatchEnableOnlineVerifyRequest other = (BatchEnableOnlineVerifyRequest) o;
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
        Object this$onlineVerifyUrl = getOnlineVerifyUrl();
        Object other$onlineVerifyUrl = other.getOnlineVerifyUrl();
        return this$onlineVerifyUrl == null ? other$onlineVerifyUrl == null : this$onlineVerifyUrl.equals(other$onlineVerifyUrl);
    }

    protected boolean canEqual(Object other) {
        return other instanceof BatchEnableOnlineVerifyRequest;
    }

    public int hashCode() {
        Object $deviceSns = getDeviceSns();
        int result = (1 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        Object $onlineVerifyUrl = getOnlineVerifyUrl();
        return (result * 59) + ($onlineVerifyUrl == null ? 43 : $onlineVerifyUrl.hashCode());
    }

    public String toString() {
        return "BatchEnableOnlineVerifyRequest(deviceSns=" + getDeviceSns() + ", onlineVerifyUrl=" + getOnlineVerifyUrl() + ")";
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }

    public String getOnlineVerifyUrl() {
        return this.onlineVerifyUrl;
    }
}

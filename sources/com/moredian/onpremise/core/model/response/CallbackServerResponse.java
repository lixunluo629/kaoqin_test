package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "查询回调服务配置信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/CallbackServerResponse.class */
public class CallbackServerResponse implements Serializable {

    @ApiModelProperty(name = "callbackTag", value = "回调服务tag")
    private String callbackTag;

    @ApiModelProperty(name = "callbackUrl", value = "回调服务通知地址，多个之间逗号隔开")
    private String callbackUrl;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    public void setCallbackTag(String callbackTag) {
        this.callbackTag = callbackTag;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CallbackServerResponse)) {
            return false;
        }
        CallbackServerResponse other = (CallbackServerResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$callbackTag = getCallbackTag();
        Object other$callbackTag = other.getCallbackTag();
        if (this$callbackTag == null) {
            if (other$callbackTag != null) {
                return false;
            }
        } else if (!this$callbackTag.equals(other$callbackTag)) {
            return false;
        }
        Object this$callbackUrl = getCallbackUrl();
        Object other$callbackUrl = other.getCallbackUrl();
        if (this$callbackUrl == null) {
            if (other$callbackUrl != null) {
                return false;
            }
        } else if (!this$callbackUrl.equals(other$callbackUrl)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CallbackServerResponse;
    }

    public int hashCode() {
        Object $callbackTag = getCallbackTag();
        int result = (1 * 59) + ($callbackTag == null ? 43 : $callbackTag.hashCode());
        Object $callbackUrl = getCallbackUrl();
        int result2 = (result * 59) + ($callbackUrl == null ? 43 : $callbackUrl.hashCode());
        Object $deviceSn = getDeviceSn();
        return (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
    }

    public String toString() {
        return "CallbackServerResponse(callbackTag=" + getCallbackTag() + ", callbackUrl=" + getCallbackUrl() + ", deviceSn=" + getDeviceSn() + ")";
    }

    public String getCallbackTag() {
        return this.callbackTag;
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}

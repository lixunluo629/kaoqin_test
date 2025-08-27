package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存回调服务请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveCallbackServerRequest.class */
public class SaveCallbackServerRequest extends BaseRequest {
    private static final long serialVersionUID = 366910105935098387L;

    @ApiModelProperty(name = "callbackTag", value = "回调服务tag,保证机构内唯一,识别成功回调：REC_SUCCESS")
    private String callbackTag;

    @ApiModelProperty(name = "callbackUrl", value = "回调服务通知地址，多个之间英文逗号隔开")
    private String callbackUrl;

    @ApiModelProperty(name = "deviceSn", value = "设备SN，多个之间英文逗号隔开，如:SN-1,SN-2,SN-3")
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

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveCallbackServerRequest)) {
            return false;
        }
        SaveCallbackServerRequest other = (SaveCallbackServerRequest) o;
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

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveCallbackServerRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $callbackTag = getCallbackTag();
        int result = (1 * 59) + ($callbackTag == null ? 43 : $callbackTag.hashCode());
        Object $callbackUrl = getCallbackUrl();
        int result2 = (result * 59) + ($callbackUrl == null ? 43 : $callbackUrl.hashCode());
        Object $deviceSn = getDeviceSn();
        return (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveCallbackServerRequest(callbackTag=" + getCallbackTag() + ", callbackUrl=" + getCallbackUrl() + ", deviceSn=" + getDeviceSn() + ")";
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

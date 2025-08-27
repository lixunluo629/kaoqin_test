package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除回调服务请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteCallbackServerRequest.class */
public class DeleteCallbackServerRequest extends BaseRequest {
    private static final long serialVersionUID = 6872431659656040117L;

    @ApiModelProperty(name = "callbackTag", value = "回调服务tag,保证机构内唯一")
    private String callbackTag;

    @ApiModelProperty(name = "deviceSn", value = "设备sn，多个之间英文逗号隔开，如:SN-1,SN-2,SN-3")
    private String deviceSn;

    public void setCallbackTag(String callbackTag) {
        this.callbackTag = callbackTag;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteCallbackServerRequest)) {
            return false;
        }
        DeleteCallbackServerRequest other = (DeleteCallbackServerRequest) o;
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteCallbackServerRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $callbackTag = getCallbackTag();
        int result = (1 * 59) + ($callbackTag == null ? 43 : $callbackTag.hashCode());
        Object $deviceSn = getDeviceSn();
        return (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteCallbackServerRequest(callbackTag=" + getCallbackTag() + ", deviceSn=" + getDeviceSn() + ")";
    }

    public String getCallbackTag() {
        return this.callbackTag;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}

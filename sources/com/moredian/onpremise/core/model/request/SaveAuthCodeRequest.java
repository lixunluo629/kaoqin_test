package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存授权码")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveAuthCodeRequest.class */
public class SaveAuthCodeRequest extends BaseRequest {

    @ApiModelProperty(name = "authCode", value = "授权码")
    private String authCode;

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAuthCodeRequest)) {
            return false;
        }
        SaveAuthCodeRequest other = (SaveAuthCodeRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$authCode = getAuthCode();
        Object other$authCode = other.getAuthCode();
        return this$authCode == null ? other$authCode == null : this$authCode.equals(other$authCode);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveAuthCodeRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $authCode = getAuthCode();
        int result = (1 * 59) + ($authCode == null ? 43 : $authCode.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveAuthCodeRequest(authCode=" + getAuthCode() + ")";
    }

    public String getAuthCode() {
        return this.authCode;
    }
}

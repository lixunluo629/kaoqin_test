package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查询回调服务请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryCllbackServerRequest.class */
public class QueryCllbackServerRequest extends BaseRequest {
    private static final long serialVersionUID = 1996250226195328182L;

    @ApiModelProperty(name = "callbackTag", value = "回调服务tag")
    private String callbackTag;

    public void setCallbackTag(String callbackTag) {
        this.callbackTag = callbackTag;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryCllbackServerRequest)) {
            return false;
        }
        QueryCllbackServerRequest other = (QueryCllbackServerRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$callbackTag = getCallbackTag();
        Object other$callbackTag = other.getCallbackTag();
        return this$callbackTag == null ? other$callbackTag == null : this$callbackTag.equals(other$callbackTag);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryCllbackServerRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $callbackTag = getCallbackTag();
        int result = (1 * 59) + ($callbackTag == null ? 43 : $callbackTag.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryCllbackServerRequest(callbackTag=" + getCallbackTag() + ")";
    }

    public String getCallbackTag() {
        return this.callbackTag;
    }
}

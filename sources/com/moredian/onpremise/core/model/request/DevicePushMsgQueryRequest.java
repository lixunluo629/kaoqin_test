package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查询推送设备消息结果请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DevicePushMsgQueryRequest.class */
public class DevicePushMsgQueryRequest extends BaseRequest {
    private static final long serialVersionUID = 5386299580509626981L;

    @ApiModelProperty(name = "uuid", value = "uuid唯一，用于查询消息推送结果")
    private String uuid;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DevicePushMsgQueryRequest)) {
            return false;
        }
        DevicePushMsgQueryRequest other = (DevicePushMsgQueryRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        return this$uuid == null ? other$uuid == null : this$uuid.equals(other$uuid);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DevicePushMsgQueryRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $uuid = getUuid();
        int result = (1 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DevicePushMsgQueryRequest(uuid=" + getUuid() + ")";
    }

    public String getUuid() {
        return this.uuid;
    }
}

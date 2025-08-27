package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除应用参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteAppRequest.class */
public class DeleteAppRequest extends BaseRequest {

    @ApiModelProperty(name = "appId", value = "应用id")
    private Long appId;

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteAppRequest)) {
            return false;
        }
        DeleteAppRequest other = (DeleteAppRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$appId = getAppId();
        Object other$appId = other.getAppId();
        return this$appId == null ? other$appId == null : this$appId.equals(other$appId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteAppRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $appId = getAppId();
        int result = (1 * 59) + ($appId == null ? 43 : $appId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteAppRequest(appId=" + getAppId() + ")";
    }

    public Long getAppId() {
        return this.appId;
    }
}

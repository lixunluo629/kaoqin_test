package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "获取机构码")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/GetOrgCodeRequest.class */
public class GetOrgCodeRequest extends BaseRequest {
    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GetOrgCodeRequest)) {
            return false;
        }
        GetOrgCodeRequest other = (GetOrgCodeRequest) o;
        return other.canEqual(this);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof GetOrgCodeRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        return 1;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "GetOrgCodeRequest()";
    }
}

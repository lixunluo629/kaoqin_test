package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "签到补签记录")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/QueryCheckInSupplementDetailRequest.class */
public class QueryCheckInSupplementDetailRequest extends BaseRequest {
    private static final long serialVersionUID = -4006730507606205788L;

    @ApiModelProperty(name = "supplementId", value = "补签ID")
    private Long supplementId;

    public void setSupplementId(Long supplementId) {
        this.supplementId = supplementId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryCheckInSupplementDetailRequest)) {
            return false;
        }
        QueryCheckInSupplementDetailRequest other = (QueryCheckInSupplementDetailRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$supplementId = getSupplementId();
        Object other$supplementId = other.getSupplementId();
        return this$supplementId == null ? other$supplementId == null : this$supplementId.equals(other$supplementId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof QueryCheckInSupplementDetailRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $supplementId = getSupplementId();
        int result = (1 * 59) + ($supplementId == null ? 43 : $supplementId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "QueryCheckInSupplementDetailRequest(supplementId=" + getSupplementId() + ")";
    }

    public Long getSupplementId() {
        return this.supplementId;
    }
}

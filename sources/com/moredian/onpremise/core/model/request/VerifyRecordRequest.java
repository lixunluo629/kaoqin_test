package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "识别记录请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/VerifyRecordRequest.class */
public class VerifyRecordRequest extends BaseRequest {
    private static final long serialVersionUID = -1068883021651687250L;

    @ApiModelProperty(name = "verifyRecordId", value = "识别记录id")
    private Long verifyRecordId;

    public void setVerifyRecordId(Long verifyRecordId) {
        this.verifyRecordId = verifyRecordId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VerifyRecordRequest)) {
            return false;
        }
        VerifyRecordRequest other = (VerifyRecordRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$verifyRecordId = getVerifyRecordId();
        Object other$verifyRecordId = other.getVerifyRecordId();
        return this$verifyRecordId == null ? other$verifyRecordId == null : this$verifyRecordId.equals(other$verifyRecordId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof VerifyRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $verifyRecordId = getVerifyRecordId();
        int result = (1 * 59) + ($verifyRecordId == null ? 43 : $verifyRecordId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "VerifyRecordRequest(verifyRecordId=" + getVerifyRecordId() + ")";
    }

    public Long getVerifyRecordId() {
        return this.verifyRecordId;
    }
}

package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除账户信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteAccountRequest.class */
public class DeleteAccountRequest extends BaseRequest {

    @ApiModelProperty(name = "accountId", value = "账户id")
    private Long accountId;

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteAccountRequest)) {
            return false;
        }
        DeleteAccountRequest other = (DeleteAccountRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$accountId = getAccountId();
        Object other$accountId = other.getAccountId();
        return this$accountId == null ? other$accountId == null : this$accountId.equals(other$accountId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteAccountRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $accountId = getAccountId();
        int result = (1 * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteAccountRequest(accountId=" + getAccountId() + ")";
    }

    public Long getAccountId() {
        return this.accountId;
    }
}

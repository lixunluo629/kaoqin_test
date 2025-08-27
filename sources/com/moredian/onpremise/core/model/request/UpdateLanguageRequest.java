package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "修改语言类型")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UpdateLanguageRequest.class */
public class UpdateLanguageRequest extends BaseRequest {
    private static final long serialVersionUID = 1935065616185128180L;

    @ApiModelProperty(name = "languageType", value = "语言类型")
    private String languageType;

    @ApiModelProperty(name = "accountId", value = "账户id")
    private Long accountId;

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateLanguageRequest)) {
            return false;
        }
        UpdateLanguageRequest other = (UpdateLanguageRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$languageType = getLanguageType();
        Object other$languageType = other.getLanguageType();
        if (this$languageType == null) {
            if (other$languageType != null) {
                return false;
            }
        } else if (!this$languageType.equals(other$languageType)) {
            return false;
        }
        Object this$accountId = getAccountId();
        Object other$accountId = other.getAccountId();
        return this$accountId == null ? other$accountId == null : this$accountId.equals(other$accountId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UpdateLanguageRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $languageType = getLanguageType();
        int result = (1 * 59) + ($languageType == null ? 43 : $languageType.hashCode());
        Object $accountId = getAccountId();
        return (result * 59) + ($accountId == null ? 43 : $accountId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UpdateLanguageRequest(languageType=" + getLanguageType() + ", accountId=" + getAccountId() + ")";
    }

    public String getLanguageType() {
        return this.languageType;
    }

    public Long getAccountId() {
        return this.accountId;
    }
}

package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "重置密码请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ResetPasswordRequest.class */
public class ResetPasswordRequest implements Serializable {

    @ApiModelProperty(name = "accountName", value = "账户名")
    private String accountName;

    @ApiModelProperty(name = "authCode", value = "魔点颁发授权码")
    private String authCode;

    @ApiModelProperty(name = "sessionId", value = "sessionId", hidden = true)
    private String sessionId;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResetPasswordRequest)) {
            return false;
        }
        ResetPasswordRequest other = (ResetPasswordRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$accountName = getAccountName();
        Object other$accountName = other.getAccountName();
        if (this$accountName == null) {
            if (other$accountName != null) {
                return false;
            }
        } else if (!this$accountName.equals(other$accountName)) {
            return false;
        }
        Object this$authCode = getAuthCode();
        Object other$authCode = other.getAuthCode();
        if (this$authCode == null) {
            if (other$authCode != null) {
                return false;
            }
        } else if (!this$authCode.equals(other$authCode)) {
            return false;
        }
        Object this$sessionId = getSessionId();
        Object other$sessionId = other.getSessionId();
        return this$sessionId == null ? other$sessionId == null : this$sessionId.equals(other$sessionId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResetPasswordRequest;
    }

    public int hashCode() {
        Object $accountName = getAccountName();
        int result = (1 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $authCode = getAuthCode();
        int result2 = (result * 59) + ($authCode == null ? 43 : $authCode.hashCode());
        Object $sessionId = getSessionId();
        return (result2 * 59) + ($sessionId == null ? 43 : $sessionId.hashCode());
    }

    public String toString() {
        return "ResetPasswordRequest(accountName=" + getAccountName() + ", authCode=" + getAuthCode() + ", sessionId=" + getSessionId() + ")";
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}

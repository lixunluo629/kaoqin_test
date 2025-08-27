package com.moredian.onpremise.core.model.request;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ModifyPasswordRequest.class */
public class ModifyPasswordRequest extends BaseRequest {

    @ApiModelProperty(name = "accountId", value = "账户id")
    private Long accountId;

    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "密码")
    private String password;

    @ApiModelProperty(name = "confirmPassword", value = "二次确认密码")
    private String confirmPassword;

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ModifyPasswordRequest)) {
            return false;
        }
        ModifyPasswordRequest other = (ModifyPasswordRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$accountId = getAccountId();
        Object other$accountId = other.getAccountId();
        if (this$accountId == null) {
            if (other$accountId != null) {
                return false;
            }
        } else if (!this$accountId.equals(other$accountId)) {
            return false;
        }
        Object this$password = getPassword();
        Object other$password = other.getPassword();
        if (this$password == null) {
            if (other$password != null) {
                return false;
            }
        } else if (!this$password.equals(other$password)) {
            return false;
        }
        Object this$confirmPassword = getConfirmPassword();
        Object other$confirmPassword = other.getConfirmPassword();
        return this$confirmPassword == null ? other$confirmPassword == null : this$confirmPassword.equals(other$confirmPassword);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ModifyPasswordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $accountId = getAccountId();
        int result = (1 * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $password = getPassword();
        int result2 = (result * 59) + ($password == null ? 43 : $password.hashCode());
        Object $confirmPassword = getConfirmPassword();
        return (result2 * 59) + ($confirmPassword == null ? 43 : $confirmPassword.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ModifyPasswordRequest(accountId=" + getAccountId() + ", password=" + getPassword() + ", confirmPassword=" + getConfirmPassword() + ")";
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public String getPassword() {
        return this.password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }
}

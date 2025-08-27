package com.moredian.onpremise.core.model.response;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "同步管理员账号")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncAccountResponse.class */
public class TerminalSyncAccountResponse implements Serializable {
    private static final long serialVersionUID = -554865098171337228L;

    @ApiModelProperty(name = "accountGrade", value = "账号等级：1-超管；2-主管理员；3-子管理员；4-非管理员")
    private Integer accountGrade;

    @ApiModelProperty(name = "accountName", value = "账号名称")
    private String accountName;

    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "账号密码")
    private String password;

    @ApiModelProperty(name = "eigenvalueValue", value = "特征值")
    private String eigenvalueValue;

    @ApiModelProperty(name = "accountId", value = "账户id")
    private Long accountId;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEigenvalueValue(String eigenvalueValue) {
        this.eigenvalueValue = eigenvalueValue;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncAccountResponse)) {
            return false;
        }
        TerminalSyncAccountResponse other = (TerminalSyncAccountResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$accountGrade = getAccountGrade();
        Object other$accountGrade = other.getAccountGrade();
        if (this$accountGrade == null) {
            if (other$accountGrade != null) {
                return false;
            }
        } else if (!this$accountGrade.equals(other$accountGrade)) {
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
        Object this$password = getPassword();
        Object other$password = other.getPassword();
        if (this$password == null) {
            if (other$password != null) {
                return false;
            }
        } else if (!this$password.equals(other$password)) {
            return false;
        }
        Object this$eigenvalueValue = getEigenvalueValue();
        Object other$eigenvalueValue = other.getEigenvalueValue();
        if (this$eigenvalueValue == null) {
            if (other$eigenvalueValue != null) {
                return false;
            }
        } else if (!this$eigenvalueValue.equals(other$eigenvalueValue)) {
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        return this$memberId == null ? other$memberId == null : this$memberId.equals(other$memberId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncAccountResponse;
    }

    public int hashCode() {
        Object $accountGrade = getAccountGrade();
        int result = (1 * 59) + ($accountGrade == null ? 43 : $accountGrade.hashCode());
        Object $accountName = getAccountName();
        int result2 = (result * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $password = getPassword();
        int result3 = (result2 * 59) + ($password == null ? 43 : $password.hashCode());
        Object $eigenvalueValue = getEigenvalueValue();
        int result4 = (result3 * 59) + ($eigenvalueValue == null ? 43 : $eigenvalueValue.hashCode());
        Object $accountId = getAccountId();
        int result5 = (result4 * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $memberId = getMemberId();
        return (result5 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
    }

    public String toString() {
        return "TerminalSyncAccountResponse(accountGrade=" + getAccountGrade() + ", accountName=" + getAccountName() + ", password=" + getPassword() + ", eigenvalueValue=" + getEigenvalueValue() + ", accountId=" + getAccountId() + ", memberId=" + getMemberId() + ")";
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEigenvalueValue() {
        return this.eigenvalueValue;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Long getMemberId() {
        return this.memberId;
    }
}

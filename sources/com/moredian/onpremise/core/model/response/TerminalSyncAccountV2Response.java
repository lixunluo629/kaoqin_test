package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "同步管理员账号,v2")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/TerminalSyncAccountV2Response.class */
public class TerminalSyncAccountV2Response implements Serializable {
    private static final long serialVersionUID = 1280116840612157934L;

    @ApiModelProperty(name = "accountId", value = "账户id")
    private Long accountId;

    @ApiModelProperty(name = "accountGrade", value = "账号等级：1-超管；2-主管理员；3-子管理员；4-非管理员")
    private Integer accountGrade;

    @ApiModelProperty(name = "accountName", value = "账号名称")
    private String accountName;

    @ApiModelProperty(name = "accountPassword", value = "账号密码")
    private String accountPassword;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncAccountV2Response)) {
            return false;
        }
        TerminalSyncAccountV2Response other = (TerminalSyncAccountV2Response) o;
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
        Object this$accountPassword = getAccountPassword();
        Object other$accountPassword = other.getAccountPassword();
        if (this$accountPassword == null) {
            if (other$accountPassword != null) {
                return false;
            }
        } else if (!this$accountPassword.equals(other$accountPassword)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        return this$memberId == null ? other$memberId == null : this$memberId.equals(other$memberId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncAccountV2Response;
    }

    public int hashCode() {
        Object $accountId = getAccountId();
        int result = (1 * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $accountGrade = getAccountGrade();
        int result2 = (result * 59) + ($accountGrade == null ? 43 : $accountGrade.hashCode());
        Object $accountName = getAccountName();
        int result3 = (result2 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $accountPassword = getAccountPassword();
        int result4 = (result3 * 59) + ($accountPassword == null ? 43 : $accountPassword.hashCode());
        Object $memberId = getMemberId();
        return (result4 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
    }

    public String toString() {
        return "TerminalSyncAccountV2Response(accountId=" + getAccountId() + ", accountGrade=" + getAccountGrade() + ", accountName=" + getAccountName() + ", accountPassword=" + getAccountPassword() + ", memberId=" + getMemberId() + ")";
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getAccountPassword() {
        return this.accountPassword;
    }

    public Long getMemberId() {
        return this.memberId;
    }
}

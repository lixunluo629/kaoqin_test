package com.moredian.onpremise.core.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "账户列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AccountListResponse.class */
public class AccountListResponse implements Serializable {
    private static final long serialVersionUID = -4585336864849732579L;

    @ApiModelProperty(name = "accountName", value = "账户名称")
    private String accountName;

    @ApiModelProperty(name = "accountId", value = "账户id")
    private Long accountId;

    @ApiModelProperty(name = "accountGrade", value = "账号等级：1-超管；2-主管理员；3-子管理员")
    private Integer accountGrade;

    @JsonIgnore
    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "密码")
    private String password;

    @ApiModelProperty(name = "memberName", value = "成员姓名")
    private String memberName;

    @ApiModelProperty(name = "memberMobile", value = "成员手机号")
    private String memberMobile;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "moduleManager", value = "是否为模块管理员：1-是，0-否")
    private Integer moduleManager;

    @ApiModelProperty(name = "manageDepts", value = "账户管理部门列表")
    private List<AccountManageDeptResponse> manageDepts;

    @ApiModelProperty(name = "manageDevices", value = "账户管理设备列表")
    private List<AccountManageDeviceResponse> manageDevices;

    @ApiModelProperty(name = "manageAppId", value = "账户管理业务权限id，多个之间逗号隔开")
    private String manageAppId;

    @ApiModelProperty(name = "manageModuleId", value = "管理员管理模块id，多个之间逗号隔开")
    private String manageModuleId;

    @ApiModelProperty(name = "manageGroups", value = "账户管理权限组列表")
    private List<AccountManageGroupResponse> manageGroups;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setModuleManager(Integer moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void setManageDepts(List<AccountManageDeptResponse> manageDepts) {
        this.manageDepts = manageDepts;
    }

    public void setManageDevices(List<AccountManageDeviceResponse> manageDevices) {
        this.manageDevices = manageDevices;
    }

    public void setManageAppId(String manageAppId) {
        this.manageAppId = manageAppId;
    }

    public void setManageModuleId(String manageModuleId) {
        this.manageModuleId = manageModuleId;
    }

    public void setManageGroups(List<AccountManageGroupResponse> manageGroups) {
        this.manageGroups = manageGroups;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccountListResponse)) {
            return false;
        }
        AccountListResponse other = (AccountListResponse) o;
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
        Object this$password = getPassword();
        Object other$password = other.getPassword();
        if (this$password == null) {
            if (other$password != null) {
                return false;
            }
        } else if (!this$password.equals(other$password)) {
            return false;
        }
        Object this$memberName = getMemberName();
        Object other$memberName = other.getMemberName();
        if (this$memberName == null) {
            if (other$memberName != null) {
                return false;
            }
        } else if (!this$memberName.equals(other$memberName)) {
            return false;
        }
        Object this$memberMobile = getMemberMobile();
        Object other$memberMobile = other.getMemberMobile();
        if (this$memberMobile == null) {
            if (other$memberMobile != null) {
                return false;
            }
        } else if (!this$memberMobile.equals(other$memberMobile)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
            return false;
        }
        Object this$moduleManager = getModuleManager();
        Object other$moduleManager = other.getModuleManager();
        if (this$moduleManager == null) {
            if (other$moduleManager != null) {
                return false;
            }
        } else if (!this$moduleManager.equals(other$moduleManager)) {
            return false;
        }
        Object this$manageDepts = getManageDepts();
        Object other$manageDepts = other.getManageDepts();
        if (this$manageDepts == null) {
            if (other$manageDepts != null) {
                return false;
            }
        } else if (!this$manageDepts.equals(other$manageDepts)) {
            return false;
        }
        Object this$manageDevices = getManageDevices();
        Object other$manageDevices = other.getManageDevices();
        if (this$manageDevices == null) {
            if (other$manageDevices != null) {
                return false;
            }
        } else if (!this$manageDevices.equals(other$manageDevices)) {
            return false;
        }
        Object this$manageAppId = getManageAppId();
        Object other$manageAppId = other.getManageAppId();
        if (this$manageAppId == null) {
            if (other$manageAppId != null) {
                return false;
            }
        } else if (!this$manageAppId.equals(other$manageAppId)) {
            return false;
        }
        Object this$manageModuleId = getManageModuleId();
        Object other$manageModuleId = other.getManageModuleId();
        if (this$manageModuleId == null) {
            if (other$manageModuleId != null) {
                return false;
            }
        } else if (!this$manageModuleId.equals(other$manageModuleId)) {
            return false;
        }
        Object this$manageGroups = getManageGroups();
        Object other$manageGroups = other.getManageGroups();
        return this$manageGroups == null ? other$manageGroups == null : this$manageGroups.equals(other$manageGroups);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AccountListResponse;
    }

    public int hashCode() {
        Object $accountName = getAccountName();
        int result = (1 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $accountId = getAccountId();
        int result2 = (result * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $accountGrade = getAccountGrade();
        int result3 = (result2 * 59) + ($accountGrade == null ? 43 : $accountGrade.hashCode());
        Object $password = getPassword();
        int result4 = (result3 * 59) + ($password == null ? 43 : $password.hashCode());
        Object $memberName = getMemberName();
        int result5 = (result4 * 59) + ($memberName == null ? 43 : $memberName.hashCode());
        Object $memberMobile = getMemberMobile();
        int result6 = (result5 * 59) + ($memberMobile == null ? 43 : $memberMobile.hashCode());
        Object $memberId = getMemberId();
        int result7 = (result6 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $moduleManager = getModuleManager();
        int result8 = (result7 * 59) + ($moduleManager == null ? 43 : $moduleManager.hashCode());
        Object $manageDepts = getManageDepts();
        int result9 = (result8 * 59) + ($manageDepts == null ? 43 : $manageDepts.hashCode());
        Object $manageDevices = getManageDevices();
        int result10 = (result9 * 59) + ($manageDevices == null ? 43 : $manageDevices.hashCode());
        Object $manageAppId = getManageAppId();
        int result11 = (result10 * 59) + ($manageAppId == null ? 43 : $manageAppId.hashCode());
        Object $manageModuleId = getManageModuleId();
        int result12 = (result11 * 59) + ($manageModuleId == null ? 43 : $manageModuleId.hashCode());
        Object $manageGroups = getManageGroups();
        return (result12 * 59) + ($manageGroups == null ? 43 : $manageGroups.hashCode());
    }

    public String toString() {
        return "AccountListResponse(accountName=" + getAccountName() + ", accountId=" + getAccountId() + ", accountGrade=" + getAccountGrade() + ", password=" + getPassword() + ", memberName=" + getMemberName() + ", memberMobile=" + getMemberMobile() + ", memberId=" + getMemberId() + ", moduleManager=" + getModuleManager() + ", manageDepts=" + getManageDepts() + ", manageDevices=" + getManageDevices() + ", manageAppId=" + getManageAppId() + ", manageModuleId=" + getManageModuleId() + ", manageGroups=" + getManageGroups() + ")";
    }

    public String getAccountName() {
        return this.accountName;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }

    public String getPassword() {
        return this.password;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getMemberMobile() {
        return this.memberMobile;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Integer getModuleManager() {
        return this.moduleManager;
    }

    public List<AccountManageDeptResponse> getManageDepts() {
        return this.manageDepts;
    }

    public List<AccountManageDeviceResponse> getManageDevices() {
        return this.manageDevices;
    }

    public String getManageAppId() {
        return this.manageAppId;
    }

    public String getManageModuleId() {
        return this.manageModuleId;
    }

    public List<AccountManageGroupResponse> getManageGroups() {
        return this.manageGroups;
    }
}

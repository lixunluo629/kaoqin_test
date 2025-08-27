package com.moredian.onpremise.core.model.request;

import com.mysql.jdbc.NonRegisteringDriver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "保存账户信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveAccountRequest.class */
public class SaveAccountRequest extends BaseRequest {

    @ApiModelProperty(name = "accountId", value = "账户id，新增时不传，修改时必填")
    private Long accountId;

    @ApiModelProperty(name = "accountName", value = "账户名")
    private String accountName;

    @ApiModelProperty(name = NonRegisteringDriver.PASSWORD_PROPERTY_KEY, value = "密码")
    private String password;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "accountGrade", value = "账号等级：1-超管；2-主管理员；3-子管理员")
    private Integer accountGrade;

    @ApiModelProperty(name = "managerDeptId", value = "账户管理部门id，多个之间逗号隔开")
    private String manageDeptId;

    @ApiModelProperty(name = "managerDeviceId", value = "账户管理设备sn，多个之间逗号隔开")
    private String manageDeviceSn;

    @ApiModelProperty(name = "manageAppId", value = "账户管理业务权限id，多个之间逗号隔开")
    private String manageAppId;

    @ApiModelProperty(name = "manageModuleId", value = "账户管理模块，多个之间逗号隔开")
    private String manageModuleId;

    @ApiModelProperty(name = "moduleManager", value = "是否为模块管理员：1-是，0-否")
    private Integer moduleManager;

    @ApiModelProperty(name = "manageGroupId", value = "账户管理权限组，多个之间逗号隔开")
    private String manageGroupId;

    @ApiModelProperty(name = "cloneAccountId", value = "克隆账户id")
    private Long cloneAccountId;

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public void setManageDeviceSn(String manageDeviceSn) {
        this.manageDeviceSn = manageDeviceSn;
    }

    public void setManageAppId(String manageAppId) {
        this.manageAppId = manageAppId;
    }

    public void setManageModuleId(String manageModuleId) {
        this.manageModuleId = manageModuleId;
    }

    public void setModuleManager(Integer moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void setManageGroupId(String manageGroupId) {
        this.manageGroupId = manageGroupId;
    }

    public void setCloneAccountId(Long cloneAccountId) {
        this.cloneAccountId = cloneAccountId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveAccountRequest)) {
            return false;
        }
        SaveAccountRequest other = (SaveAccountRequest) o;
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
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
        Object this$manageDeptId = getManageDeptId();
        Object other$manageDeptId = other.getManageDeptId();
        if (this$manageDeptId == null) {
            if (other$manageDeptId != null) {
                return false;
            }
        } else if (!this$manageDeptId.equals(other$manageDeptId)) {
            return false;
        }
        Object this$manageDeviceSn = getManageDeviceSn();
        Object other$manageDeviceSn = other.getManageDeviceSn();
        if (this$manageDeviceSn == null) {
            if (other$manageDeviceSn != null) {
                return false;
            }
        } else if (!this$manageDeviceSn.equals(other$manageDeviceSn)) {
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
        Object this$moduleManager = getModuleManager();
        Object other$moduleManager = other.getModuleManager();
        if (this$moduleManager == null) {
            if (other$moduleManager != null) {
                return false;
            }
        } else if (!this$moduleManager.equals(other$moduleManager)) {
            return false;
        }
        Object this$manageGroupId = getManageGroupId();
        Object other$manageGroupId = other.getManageGroupId();
        if (this$manageGroupId == null) {
            if (other$manageGroupId != null) {
                return false;
            }
        } else if (!this$manageGroupId.equals(other$manageGroupId)) {
            return false;
        }
        Object this$cloneAccountId = getCloneAccountId();
        Object other$cloneAccountId = other.getCloneAccountId();
        return this$cloneAccountId == null ? other$cloneAccountId == null : this$cloneAccountId.equals(other$cloneAccountId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveAccountRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $accountId = getAccountId();
        int result = (1 * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $accountName = getAccountName();
        int result2 = (result * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $password = getPassword();
        int result3 = (result2 * 59) + ($password == null ? 43 : $password.hashCode());
        Object $memberId = getMemberId();
        int result4 = (result3 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $accountGrade = getAccountGrade();
        int result5 = (result4 * 59) + ($accountGrade == null ? 43 : $accountGrade.hashCode());
        Object $manageDeptId = getManageDeptId();
        int result6 = (result5 * 59) + ($manageDeptId == null ? 43 : $manageDeptId.hashCode());
        Object $manageDeviceSn = getManageDeviceSn();
        int result7 = (result6 * 59) + ($manageDeviceSn == null ? 43 : $manageDeviceSn.hashCode());
        Object $manageAppId = getManageAppId();
        int result8 = (result7 * 59) + ($manageAppId == null ? 43 : $manageAppId.hashCode());
        Object $manageModuleId = getManageModuleId();
        int result9 = (result8 * 59) + ($manageModuleId == null ? 43 : $manageModuleId.hashCode());
        Object $moduleManager = getModuleManager();
        int result10 = (result9 * 59) + ($moduleManager == null ? 43 : $moduleManager.hashCode());
        Object $manageGroupId = getManageGroupId();
        int result11 = (result10 * 59) + ($manageGroupId == null ? 43 : $manageGroupId.hashCode());
        Object $cloneAccountId = getCloneAccountId();
        return (result11 * 59) + ($cloneAccountId == null ? 43 : $cloneAccountId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveAccountRequest(accountId=" + getAccountId() + ", accountName=" + getAccountName() + ", password=" + getPassword() + ", memberId=" + getMemberId() + ", accountGrade=" + getAccountGrade() + ", manageDeptId=" + getManageDeptId() + ", manageDeviceSn=" + getManageDeviceSn() + ", manageAppId=" + getManageAppId() + ", manageModuleId=" + getManageModuleId() + ", moduleManager=" + getModuleManager() + ", manageGroupId=" + getManageGroupId() + ", cloneAccountId=" + getCloneAccountId() + ")";
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getPassword() {
        return this.password;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }

    public String getManageDeptId() {
        return this.manageDeptId;
    }

    public String getManageDeviceSn() {
        return this.manageDeviceSn;
    }

    public String getManageAppId() {
        return this.manageAppId;
    }

    public String getManageModuleId() {
        return this.manageModuleId;
    }

    public Integer getModuleManager() {
        return this.moduleManager;
    }

    public String getManageGroupId() {
        return this.manageGroupId;
    }

    public Long getCloneAccountId() {
        return this.cloneAccountId;
    }
}

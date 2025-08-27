package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.enums.AccountGradeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApiModel(description = "用户登录响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/UserLoginResponse.class */
public class UserLoginResponse implements Serializable {

    @ApiModelProperty(name = "accountName", value = "用户名")
    private String accountName;

    @ApiModelProperty(name = "orgName", value = "机构名称")
    private String orgName;

    @ApiModelProperty(name = "accountId", value = "账户id", hidden = true)
    private Long accountId;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "accountGrade", value = "账号等级：1-超管；2-主管理员；3-子管理员,4-非管理员")
    private Integer accountGrade;

    @ApiModelProperty(name = "firstLoginFlag", value = "初次登录标识：1-是，0-否")
    private Integer firstLoginFlag;

    @ApiModelProperty(name = "hasAuthCodeFlag", value = "是否有授权码标识：1-是，0-否")
    private Integer hasAuthCodeFlag;

    @ApiModelProperty(name = "expireTime", value = "过期时间", hidden = true)
    private Long expireTime;

    @ApiModelProperty(name = "languageType", value = "语言类型")
    private String languageType = "";

    @ApiModelProperty(name = "manageAppId", value = "管理员管理模块id，逗号分隔", hidden = true)
    private String manageAppId = "";

    @ApiModelProperty(name = "manageDeviceSn", value = "管理员管理设备sn，逗号分隔", hidden = true)
    private String manageDeviceSn = "";

    @ApiModelProperty(name = "manageDeptId", value = "管理员管理部门id，逗号分隔", hidden = true)
    private String manageDeptId = "";

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setAccountGrade(Integer accountGrade) {
        this.accountGrade = accountGrade;
    }

    public void setFirstLoginFlag(Integer firstLoginFlag) {
        this.firstLoginFlag = firstLoginFlag;
    }

    public void setHasAuthCodeFlag(Integer hasAuthCodeFlag) {
        this.hasAuthCodeFlag = hasAuthCodeFlag;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public void setManageAppId(String manageAppId) {
        this.manageAppId = manageAppId;
    }

    public void setManageDeviceSn(String manageDeviceSn) {
        this.manageDeviceSn = manageDeviceSn;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserLoginResponse)) {
            return false;
        }
        UserLoginResponse other = (UserLoginResponse) o;
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
        Object this$orgName = getOrgName();
        Object other$orgName = other.getOrgName();
        if (this$orgName == null) {
            if (other$orgName != null) {
                return false;
            }
        } else if (!this$orgName.equals(other$orgName)) {
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
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
        Object this$firstLoginFlag = getFirstLoginFlag();
        Object other$firstLoginFlag = other.getFirstLoginFlag();
        if (this$firstLoginFlag == null) {
            if (other$firstLoginFlag != null) {
                return false;
            }
        } else if (!this$firstLoginFlag.equals(other$firstLoginFlag)) {
            return false;
        }
        Object this$hasAuthCodeFlag = getHasAuthCodeFlag();
        Object other$hasAuthCodeFlag = other.getHasAuthCodeFlag();
        if (this$hasAuthCodeFlag == null) {
            if (other$hasAuthCodeFlag != null) {
                return false;
            }
        } else if (!this$hasAuthCodeFlag.equals(other$hasAuthCodeFlag)) {
            return false;
        }
        Object this$expireTime = getExpireTime();
        Object other$expireTime = other.getExpireTime();
        if (this$expireTime == null) {
            if (other$expireTime != null) {
                return false;
            }
        } else if (!this$expireTime.equals(other$expireTime)) {
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
        Object this$manageAppId = getManageAppId();
        Object other$manageAppId = other.getManageAppId();
        if (this$manageAppId == null) {
            if (other$manageAppId != null) {
                return false;
            }
        } else if (!this$manageAppId.equals(other$manageAppId)) {
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
        Object this$manageDeptId = getManageDeptId();
        Object other$manageDeptId = other.getManageDeptId();
        return this$manageDeptId == null ? other$manageDeptId == null : this$manageDeptId.equals(other$manageDeptId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserLoginResponse;
    }

    public int hashCode() {
        Object $accountName = getAccountName();
        int result = (1 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $orgName = getOrgName();
        int result2 = (result * 59) + ($orgName == null ? 43 : $orgName.hashCode());
        Object $accountId = getAccountId();
        int result3 = (result2 * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $orgId = getOrgId();
        int result4 = (result3 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $accountGrade = getAccountGrade();
        int result5 = (result4 * 59) + ($accountGrade == null ? 43 : $accountGrade.hashCode());
        Object $firstLoginFlag = getFirstLoginFlag();
        int result6 = (result5 * 59) + ($firstLoginFlag == null ? 43 : $firstLoginFlag.hashCode());
        Object $hasAuthCodeFlag = getHasAuthCodeFlag();
        int result7 = (result6 * 59) + ($hasAuthCodeFlag == null ? 43 : $hasAuthCodeFlag.hashCode());
        Object $expireTime = getExpireTime();
        int result8 = (result7 * 59) + ($expireTime == null ? 43 : $expireTime.hashCode());
        Object $languageType = getLanguageType();
        int result9 = (result8 * 59) + ($languageType == null ? 43 : $languageType.hashCode());
        Object $manageAppId = getManageAppId();
        int result10 = (result9 * 59) + ($manageAppId == null ? 43 : $manageAppId.hashCode());
        Object $manageDeviceSn = getManageDeviceSn();
        int result11 = (result10 * 59) + ($manageDeviceSn == null ? 43 : $manageDeviceSn.hashCode());
        Object $manageDeptId = getManageDeptId();
        return (result11 * 59) + ($manageDeptId == null ? 43 : $manageDeptId.hashCode());
    }

    public String toString() {
        return "UserLoginResponse(accountName=" + getAccountName() + ", orgName=" + getOrgName() + ", accountId=" + getAccountId() + ", orgId=" + getOrgId() + ", accountGrade=" + getAccountGrade() + ", firstLoginFlag=" + getFirstLoginFlag() + ", hasAuthCodeFlag=" + getHasAuthCodeFlag() + ", expireTime=" + getExpireTime() + ", languageType=" + getLanguageType() + ", manageAppId=" + getManageAppId() + ", manageDeviceSn=" + getManageDeviceSn() + ", manageDeptId=" + getManageDeptId() + ")";
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Integer getAccountGrade() {
        return this.accountGrade;
    }

    public Integer getFirstLoginFlag() {
        return this.firstLoginFlag;
    }

    public Integer getHasAuthCodeFlag() {
        return this.hasAuthCodeFlag;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public String getLanguageType() {
        return this.languageType;
    }

    public String getManageAppId() {
        return this.manageAppId;
    }

    public String getManageDeviceSn() {
        return this.manageDeviceSn;
    }

    public String getManageDeptId() {
        return this.manageDeptId;
    }

    public static List<String> getAccountManageAppId(String sessionId) {
        String appId;
        String[] appArray;
        List<String> result = null;
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(sessionId);
        if (loginInfo != null && loginInfo.getAccountGrade().intValue() == AccountGradeEnum.CHILDREN_ACCOUNT.getValue() && (appId = loginInfo.getManageAppId()) != null && (appArray = appId.split(",")) != null) {
            if (appArray.length > 0) {
                result = Arrays.asList(appArray);
            } else {
                result = new ArrayList();
                result.add("0");
            }
        }
        return result;
    }

    public static List<String> getAccountManageDeptId(String sessionId) {
        String deptId;
        String[] deptArray;
        List<String> result = null;
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(sessionId);
        if (loginInfo != null && loginInfo.getAccountGrade().intValue() == AccountGradeEnum.CHILDREN_ACCOUNT.getValue() && (deptId = loginInfo.getManageDeptId()) != null && (deptArray = deptId.split(",")) != null) {
            if (deptArray.length > 0) {
                result = Arrays.asList(deptArray);
            } else {
                result = new ArrayList();
                result.add("0");
            }
        }
        return result;
    }

    public static List<String> getAccountManageDeviceSn(String sessionId) {
        String deviceSn;
        String[] deviceArray;
        List<String> result = null;
        UserLoginResponse loginInfo = CacheAdapter.getLoginInfo(sessionId);
        if (loginInfo != null && loginInfo.getAccountGrade().intValue() == AccountGradeEnum.CHILDREN_ACCOUNT.getValue() && (deviceSn = loginInfo.getManageDeviceSn()) != null && (deviceArray = deviceSn.split(",")) != null) {
            if (deviceArray.length > 0) {
                result = Arrays.asList(deviceArray);
            } else {
                result = new ArrayList();
                result.add("0");
            }
        }
        return result;
    }
}

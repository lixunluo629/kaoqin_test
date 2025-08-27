package com.moredian.onpremise.core.model.response;

import com.moredian.onpremise.core.common.constants.AuthConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "机构基本信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/AccountOrgInfoResponse.class */
public class AccountOrgInfoResponse implements Serializable {

    @ApiModelProperty(name = "orgName", value = "机构名称")
    private String orgName;

    @ApiModelProperty(name = "memberNum", value = "成员数")
    private Integer memberNum;

    @ApiModelProperty(name = "deptNum", value = "部门数")
    private Integer deptNum;

    @ApiModelProperty(name = "accountName", value = "账户名称")
    private String accountName;

    @ApiModelProperty(name = AuthConstants.AUTH_PARAM_ORG_CODE_KEY, value = "机构码")
    private String orgCode;

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public void setDeptNum(Integer deptNum) {
        this.deptNum = deptNum;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccountOrgInfoResponse)) {
            return false;
        }
        AccountOrgInfoResponse other = (AccountOrgInfoResponse) o;
        if (!other.canEqual(this)) {
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
        Object this$memberNum = getMemberNum();
        Object other$memberNum = other.getMemberNum();
        if (this$memberNum == null) {
            if (other$memberNum != null) {
                return false;
            }
        } else if (!this$memberNum.equals(other$memberNum)) {
            return false;
        }
        Object this$deptNum = getDeptNum();
        Object other$deptNum = other.getDeptNum();
        if (this$deptNum == null) {
            if (other$deptNum != null) {
                return false;
            }
        } else if (!this$deptNum.equals(other$deptNum)) {
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
        Object this$orgCode = getOrgCode();
        Object other$orgCode = other.getOrgCode();
        return this$orgCode == null ? other$orgCode == null : this$orgCode.equals(other$orgCode);
    }

    protected boolean canEqual(Object other) {
        return other instanceof AccountOrgInfoResponse;
    }

    public int hashCode() {
        Object $orgName = getOrgName();
        int result = (1 * 59) + ($orgName == null ? 43 : $orgName.hashCode());
        Object $memberNum = getMemberNum();
        int result2 = (result * 59) + ($memberNum == null ? 43 : $memberNum.hashCode());
        Object $deptNum = getDeptNum();
        int result3 = (result2 * 59) + ($deptNum == null ? 43 : $deptNum.hashCode());
        Object $accountName = getAccountName();
        int result4 = (result3 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $orgCode = getOrgCode();
        return (result4 * 59) + ($orgCode == null ? 43 : $orgCode.hashCode());
    }

    public String toString() {
        return "AccountOrgInfoResponse(orgName=" + getOrgName() + ", memberNum=" + getMemberNum() + ", deptNum=" + getDeptNum() + ", accountName=" + getAccountName() + ", orgCode=" + getOrgCode() + ")";
    }

    public String getOrgName() {
        return this.orgName;
    }

    public Integer getMemberNum() {
        return this.memberNum;
    }

    public Integer getDeptNum() {
        return this.deptNum;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getOrgCode() {
        return this.orgCode;
    }
}

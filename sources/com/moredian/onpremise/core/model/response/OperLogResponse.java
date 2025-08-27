package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "操作记录响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/OperLogResponse.class */
public class OperLogResponse implements Serializable {
    private static final long serialVersionUID = -4095508359969600362L;
    private Long operLogId;
    private Long orgId;
    private Long accountId;
    private String accountName;
    private String operType;
    private String operDescription;
    private Date gmtCreate;
    private Date gmtModify;
    private Integer deleteOrNot;
    private String operArgs;

    public void setOperLogId(Long operLogId) {
        this.operLogId = operLogId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public void setOperDescription(String operDescription) {
        this.operDescription = operDescription;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public void setDeleteOrNot(Integer deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public void setOperArgs(String operArgs) {
        this.operArgs = operArgs;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OperLogResponse)) {
            return false;
        }
        OperLogResponse other = (OperLogResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$operLogId = getOperLogId();
        Object other$operLogId = other.getOperLogId();
        if (this$operLogId == null) {
            if (other$operLogId != null) {
                return false;
            }
        } else if (!this$operLogId.equals(other$operLogId)) {
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
        Object this$operType = getOperType();
        Object other$operType = other.getOperType();
        if (this$operType == null) {
            if (other$operType != null) {
                return false;
            }
        } else if (!this$operType.equals(other$operType)) {
            return false;
        }
        Object this$operDescription = getOperDescription();
        Object other$operDescription = other.getOperDescription();
        if (this$operDescription == null) {
            if (other$operDescription != null) {
                return false;
            }
        } else if (!this$operDescription.equals(other$operDescription)) {
            return false;
        }
        Object this$gmtCreate = getGmtCreate();
        Object other$gmtCreate = other.getGmtCreate();
        if (this$gmtCreate == null) {
            if (other$gmtCreate != null) {
                return false;
            }
        } else if (!this$gmtCreate.equals(other$gmtCreate)) {
            return false;
        }
        Object this$gmtModify = getGmtModify();
        Object other$gmtModify = other.getGmtModify();
        if (this$gmtModify == null) {
            if (other$gmtModify != null) {
                return false;
            }
        } else if (!this$gmtModify.equals(other$gmtModify)) {
            return false;
        }
        Object this$deleteOrNot = getDeleteOrNot();
        Object other$deleteOrNot = other.getDeleteOrNot();
        if (this$deleteOrNot == null) {
            if (other$deleteOrNot != null) {
                return false;
            }
        } else if (!this$deleteOrNot.equals(other$deleteOrNot)) {
            return false;
        }
        Object this$operArgs = getOperArgs();
        Object other$operArgs = other.getOperArgs();
        return this$operArgs == null ? other$operArgs == null : this$operArgs.equals(other$operArgs);
    }

    protected boolean canEqual(Object other) {
        return other instanceof OperLogResponse;
    }

    public int hashCode() {
        Object $operLogId = getOperLogId();
        int result = (1 * 59) + ($operLogId == null ? 43 : $operLogId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $accountId = getAccountId();
        int result3 = (result2 * 59) + ($accountId == null ? 43 : $accountId.hashCode());
        Object $accountName = getAccountName();
        int result4 = (result3 * 59) + ($accountName == null ? 43 : $accountName.hashCode());
        Object $operType = getOperType();
        int result5 = (result4 * 59) + ($operType == null ? 43 : $operType.hashCode());
        Object $operDescription = getOperDescription();
        int result6 = (result5 * 59) + ($operDescription == null ? 43 : $operDescription.hashCode());
        Object $gmtCreate = getGmtCreate();
        int result7 = (result6 * 59) + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
        Object $gmtModify = getGmtModify();
        int result8 = (result7 * 59) + ($gmtModify == null ? 43 : $gmtModify.hashCode());
        Object $deleteOrNot = getDeleteOrNot();
        int result9 = (result8 * 59) + ($deleteOrNot == null ? 43 : $deleteOrNot.hashCode());
        Object $operArgs = getOperArgs();
        return (result9 * 59) + ($operArgs == null ? 43 : $operArgs.hashCode());
    }

    public String toString() {
        return "OperLogResponse(operLogId=" + getOperLogId() + ", orgId=" + getOrgId() + ", accountId=" + getAccountId() + ", accountName=" + getAccountName() + ", operType=" + getOperType() + ", operDescription=" + getOperDescription() + ", gmtCreate=" + getGmtCreate() + ", gmtModify=" + getGmtModify() + ", deleteOrNot=" + getDeleteOrNot() + ", operArgs=" + getOperArgs() + ")";
    }

    public Long getOperLogId() {
        return this.operLogId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getOperType() {
        return this.operType;
    }

    public String getOperDescription() {
        return this.operDescription;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public Date getGmtModify() {
        return this.gmtModify;
    }

    public Integer getDeleteOrNot() {
        return this.deleteOrNot;
    }

    public String getOperArgs() {
        return this.operArgs;
    }
}

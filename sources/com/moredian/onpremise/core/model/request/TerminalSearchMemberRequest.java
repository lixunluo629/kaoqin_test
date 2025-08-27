package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备端查询员工参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSearchMemberRequest.class */
public class TerminalSearchMemberRequest implements Serializable {
    private static final long serialVersionUID = 1381763162734587667L;

    @ApiModelProperty(name = "identityCard", value = "身份证号")
    private String identityCard;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSearchMemberRequest)) {
            return false;
        }
        TerminalSearchMemberRequest other = (TerminalSearchMemberRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$identityCard = getIdentityCard();
        Object other$identityCard = other.getIdentityCard();
        if (this$identityCard == null) {
            if (other$identityCard != null) {
                return false;
            }
        } else if (!this$identityCard.equals(other$identityCard)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSearchMemberRequest;
    }

    public int hashCode() {
        Object $identityCard = getIdentityCard();
        int result = (1 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
        Object $orgId = getOrgId();
        return (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
    }

    public String toString() {
        return "TerminalSearchMemberRequest(identityCard=" + getIdentityCard() + ", orgId=" + getOrgId() + ")";
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public Long getOrgId() {
        return this.orgId;
    }
}

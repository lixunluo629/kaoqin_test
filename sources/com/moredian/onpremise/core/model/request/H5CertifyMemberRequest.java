package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "h5端认证员工参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/H5CertifyMemberRequest.class */
public class H5CertifyMemberRequest implements Serializable {
    private static final long serialVersionUID = 1381763162734587667L;

    @ApiModelProperty(name = "identityCard", value = "身份证号、工号")
    private String identityCard;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId = 1L;

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof H5CertifyMemberRequest)) {
            return false;
        }
        H5CertifyMemberRequest other = (H5CertifyMemberRequest) o;
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
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name == null) {
            if (other$name != null) {
                return false;
            }
        } else if (!this$name.equals(other$name)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof H5CertifyMemberRequest;
    }

    public int hashCode() {
        Object $identityCard = getIdentityCard();
        int result = (1 * 59) + ($identityCard == null ? 43 : $identityCard.hashCode());
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $orgId = getOrgId();
        return (result2 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
    }

    public String toString() {
        return "H5CertifyMemberRequest(identityCard=" + getIdentityCard() + ", name=" + getName() + ", orgId=" + getOrgId() + ")";
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public String getName() {
        return this.name;
    }

    public Long getOrgId() {
        return this.orgId;
    }
}

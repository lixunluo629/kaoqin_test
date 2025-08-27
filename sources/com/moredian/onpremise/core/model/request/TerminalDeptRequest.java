package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端设备查询部门列表请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalDeptRequest.class */
public class TerminalDeptRequest implements Serializable {
    private static final long serialVersionUID = 7629822436967810097L;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalDeptRequest)) {
            return false;
        }
        TerminalDeptRequest other = (TerminalDeptRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalDeptRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        return result;
    }

    public String toString() {
        return "TerminalDeptRequest(orgId=" + getOrgId() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }
}

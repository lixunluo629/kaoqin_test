package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "终端同步特征值数据请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSyncEigenvalueValueRequest.class */
public class TerminalSyncEigenvalueValueRequest implements Serializable {
    private static final long serialVersionUID = -3634334058189171792L;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "memberIds", value = "成员id列表")
    private List<Long> memberIds;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncEigenvalueValueRequest)) {
            return false;
        }
        TerminalSyncEigenvalueValueRequest other = (TerminalSyncEigenvalueValueRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$memberIds = getMemberIds();
        Object other$memberIds = other.getMemberIds();
        return this$memberIds == null ? other$memberIds == null : this$memberIds.equals(other$memberIds);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncEigenvalueValueRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $memberIds = getMemberIds();
        return (result * 59) + ($memberIds == null ? 43 : $memberIds.hashCode());
    }

    public String toString() {
        return "TerminalSyncEigenvalueValueRequest(orgId=" + getOrgId() + ", memberIds=" + getMemberIds() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public List<Long> getMemberIds() {
        return this.memberIds;
    }
}

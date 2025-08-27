package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "终端同步权限组关联成员数据请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSyncGroupMemberV2Request.class */
public class TerminalSyncGroupMemberV2Request implements Serializable {
    private static final long serialVersionUID = -4187337203040789093L;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "groupIds", value = "权限组id列表")
    private List<Long> groupIds;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncGroupMemberV2Request)) {
            return false;
        }
        TerminalSyncGroupMemberV2Request other = (TerminalSyncGroupMemberV2Request) o;
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
        Object this$groupIds = getGroupIds();
        Object other$groupIds = other.getGroupIds();
        return this$groupIds == null ? other$groupIds == null : this$groupIds.equals(other$groupIds);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncGroupMemberV2Request;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $groupIds = getGroupIds();
        return (result * 59) + ($groupIds == null ? 43 : $groupIds.hashCode());
    }

    public String toString() {
        return "TerminalSyncGroupMemberV2Request(orgId=" + getOrgId() + ", groupIds=" + getGroupIds() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public List<Long> getGroupIds() {
        return this.groupIds;
    }
}

package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "终端同步外部联系人数据请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSyncExternalContactDetailRequest.class */
public class TerminalSyncExternalContactDetailRequest implements Serializable {
    private static final long serialVersionUID = 3040966346506920701L;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "ids", value = "外部联系人id列表")
    private List<Long> ids;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncExternalContactDetailRequest)) {
            return false;
        }
        TerminalSyncExternalContactDetailRequest other = (TerminalSyncExternalContactDetailRequest) o;
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
        Object this$ids = getIds();
        Object other$ids = other.getIds();
        return this$ids == null ? other$ids == null : this$ids.equals(other$ids);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncExternalContactDetailRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $ids = getIds();
        return (result * 59) + ($ids == null ? 43 : $ids.hashCode());
    }

    public String toString() {
        return "TerminalSyncExternalContactDetailRequest(orgId=" + getOrgId() + ", ids=" + getIds() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public List<Long> getIds() {
        return this.ids;
    }
}

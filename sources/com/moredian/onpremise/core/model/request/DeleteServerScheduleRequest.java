package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "删除服务升级任务")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeleteServerScheduleRequest.class */
public class DeleteServerScheduleRequest extends BaseRequest {
    private static final long serialVersionUID = -908003905401704047L;

    @ApiModelProperty(name = "upgradeServerScheduleId", value = "服务升级任务id")
    private Long upgradeServerScheduleId;

    public void setUpgradeServerScheduleId(Long upgradeServerScheduleId) {
        this.upgradeServerScheduleId = upgradeServerScheduleId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteServerScheduleRequest)) {
            return false;
        }
        DeleteServerScheduleRequest other = (DeleteServerScheduleRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$upgradeServerScheduleId = getUpgradeServerScheduleId();
        Object other$upgradeServerScheduleId = other.getUpgradeServerScheduleId();
        return this$upgradeServerScheduleId == null ? other$upgradeServerScheduleId == null : this$upgradeServerScheduleId.equals(other$upgradeServerScheduleId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeleteServerScheduleRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $upgradeServerScheduleId = getUpgradeServerScheduleId();
        int result = (1 * 59) + ($upgradeServerScheduleId == null ? 43 : $upgradeServerScheduleId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeleteServerScheduleRequest(upgradeServerScheduleId=" + getUpgradeServerScheduleId() + ")";
    }

    public Long getUpgradeServerScheduleId() {
        return this.upgradeServerScheduleId;
    }
}

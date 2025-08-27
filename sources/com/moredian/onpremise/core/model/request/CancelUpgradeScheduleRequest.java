package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "取消升级任务请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/CancelUpgradeScheduleRequest.class */
public class CancelUpgradeScheduleRequest extends BaseRequest {
    private static final long serialVersionUID = -1218330935506527269L;

    @ApiModelProperty(name = "upgradeScheduleId", value = "升级任务id")
    private Long upgradeScheduleId;

    public void setUpgradeScheduleId(Long upgradeScheduleId) {
        this.upgradeScheduleId = upgradeScheduleId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CancelUpgradeScheduleRequest)) {
            return false;
        }
        CancelUpgradeScheduleRequest other = (CancelUpgradeScheduleRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$upgradeScheduleId = getUpgradeScheduleId();
        Object other$upgradeScheduleId = other.getUpgradeScheduleId();
        return this$upgradeScheduleId == null ? other$upgradeScheduleId == null : this$upgradeScheduleId.equals(other$upgradeScheduleId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof CancelUpgradeScheduleRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $upgradeScheduleId = getUpgradeScheduleId();
        int result = (1 * 59) + ($upgradeScheduleId == null ? 43 : $upgradeScheduleId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "CancelUpgradeScheduleRequest(upgradeScheduleId=" + getUpgradeScheduleId() + ")";
    }

    public Long getUpgradeScheduleId() {
        return this.upgradeScheduleId;
    }
}

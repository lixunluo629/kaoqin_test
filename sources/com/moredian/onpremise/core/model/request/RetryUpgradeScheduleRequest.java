package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "重试升级任务请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/RetryUpgradeScheduleRequest.class */
public class RetryUpgradeScheduleRequest extends BaseRequest {
    private static final long serialVersionUID = 2532758687480658457L;

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
        if (!(o instanceof RetryUpgradeScheduleRequest)) {
            return false;
        }
        RetryUpgradeScheduleRequest other = (RetryUpgradeScheduleRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$upgradeScheduleId = getUpgradeScheduleId();
        Object other$upgradeScheduleId = other.getUpgradeScheduleId();
        return this$upgradeScheduleId == null ? other$upgradeScheduleId == null : this$upgradeScheduleId.equals(other$upgradeScheduleId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof RetryUpgradeScheduleRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $upgradeScheduleId = getUpgradeScheduleId();
        int result = (1 * 59) + ($upgradeScheduleId == null ? 43 : $upgradeScheduleId.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "RetryUpgradeScheduleRequest(upgradeScheduleId=" + getUpgradeScheduleId() + ")";
    }

    public Long getUpgradeScheduleId() {
        return this.upgradeScheduleId;
    }
}

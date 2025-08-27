package com.moredian.onpremise.core.model.request;

import com.moredian.onpremise.core.utils.Paginator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "设备升级记录请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ListUpgradeRecordRequest.class */
public class ListUpgradeRecordRequest extends BaseRequest {
    private static final long serialVersionUID = -3136301700874968830L;

    @ApiModelProperty(name = "paginator", value = "分页对象")
    private Paginator paginator;

    @ApiModelProperty(name = "upgradeScheduleId", value = "升级任务id")
    private Long upgradeScheduleId;

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setUpgradeScheduleId(Long upgradeScheduleId) {
        this.upgradeScheduleId = upgradeScheduleId;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListUpgradeRecordRequest)) {
            return false;
        }
        ListUpgradeRecordRequest other = (ListUpgradeRecordRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$paginator = getPaginator();
        Object other$paginator = other.getPaginator();
        if (this$paginator == null) {
            if (other$paginator != null) {
                return false;
            }
        } else if (!this$paginator.equals(other$paginator)) {
            return false;
        }
        Object this$upgradeScheduleId = getUpgradeScheduleId();
        Object other$upgradeScheduleId = other.getUpgradeScheduleId();
        return this$upgradeScheduleId == null ? other$upgradeScheduleId == null : this$upgradeScheduleId.equals(other$upgradeScheduleId);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ListUpgradeRecordRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $paginator = getPaginator();
        int result = (1 * 59) + ($paginator == null ? 43 : $paginator.hashCode());
        Object $upgradeScheduleId = getUpgradeScheduleId();
        return (result * 59) + ($upgradeScheduleId == null ? 43 : $upgradeScheduleId.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ListUpgradeRecordRequest(paginator=" + getPaginator() + ", upgradeScheduleId=" + getUpgradeScheduleId() + ")";
    }

    public Paginator getPaginator() {
        return this.paginator;
    }

    public Long getUpgradeScheduleId() {
        return this.upgradeScheduleId;
    }
}

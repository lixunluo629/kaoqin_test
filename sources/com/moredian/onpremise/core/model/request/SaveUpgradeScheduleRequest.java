package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "保存升级任务请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/SaveUpgradeScheduleRequest.class */
public class SaveUpgradeScheduleRequest extends BaseRequest {

    @ApiModelProperty(name = "packagePath", value = "升级包保存路径")
    public String packagePath;

    @ApiModelProperty(name = "upgradeTime", value = "预计升级时间，当前时间累加值")
    private Long upgradeTime;

    @ApiModelProperty(name = "deviceSns", value = "升级设备sn列表")
    private List<String> deviceSns;

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public void setUpgradeTime(Long upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SaveUpgradeScheduleRequest)) {
            return false;
        }
        SaveUpgradeScheduleRequest other = (SaveUpgradeScheduleRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$packagePath = getPackagePath();
        Object other$packagePath = other.getPackagePath();
        if (this$packagePath == null) {
            if (other$packagePath != null) {
                return false;
            }
        } else if (!this$packagePath.equals(other$packagePath)) {
            return false;
        }
        Object this$upgradeTime = getUpgradeTime();
        Object other$upgradeTime = other.getUpgradeTime();
        if (this$upgradeTime == null) {
            if (other$upgradeTime != null) {
                return false;
            }
        } else if (!this$upgradeTime.equals(other$upgradeTime)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        return this$deviceSns == null ? other$deviceSns == null : this$deviceSns.equals(other$deviceSns);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof SaveUpgradeScheduleRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $packagePath = getPackagePath();
        int result = (1 * 59) + ($packagePath == null ? 43 : $packagePath.hashCode());
        Object $upgradeTime = getUpgradeTime();
        int result2 = (result * 59) + ($upgradeTime == null ? 43 : $upgradeTime.hashCode());
        Object $deviceSns = getDeviceSns();
        return (result2 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "SaveUpgradeScheduleRequest(packagePath=" + getPackagePath() + ", upgradeTime=" + getUpgradeTime() + ", deviceSns=" + getDeviceSns() + ")";
    }

    public String getPackagePath() {
        return this.packagePath;
    }

    public Long getUpgradeTime() {
        return this.upgradeTime;
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }
}

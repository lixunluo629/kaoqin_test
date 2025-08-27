package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备更新状态查询结果返回")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/QueryDeviceUpgradeStatusResponse.class */
public class QueryDeviceUpgradeStatusResponse implements Serializable {

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "upgradeStatus", value = "升级状态:1-升级成功；2-升级中；3-升级失败；4-无需升级")
    private Integer upgradeStatus;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueryDeviceUpgradeStatusResponse)) {
            return false;
        }
        QueryDeviceUpgradeStatusResponse other = (QueryDeviceUpgradeStatusResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$upgradeStatus = getUpgradeStatus();
        Object other$upgradeStatus = other.getUpgradeStatus();
        return this$upgradeStatus == null ? other$upgradeStatus == null : this$upgradeStatus.equals(other$upgradeStatus);
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryDeviceUpgradeStatusResponse;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $upgradeStatus = getUpgradeStatus();
        return (result * 59) + ($upgradeStatus == null ? 43 : $upgradeStatus.hashCode());
    }

    public String toString() {
        return "QueryDeviceUpgradeStatusResponse(deviceSn=" + getDeviceSn() + ", upgradeStatus=" + getUpgradeStatus() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getUpgradeStatus() {
        return this.upgradeStatus;
    }
}

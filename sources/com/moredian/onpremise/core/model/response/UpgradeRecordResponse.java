package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备升级记录列表响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/UpgradeRecordResponse.class */
public class UpgradeRecordResponse implements Serializable {
    private static final long serialVersionUID = 5750499248023246991L;

    @ApiModelProperty(name = "deviceSn", value = "需要升级设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "upgradeStatus", value = "升级状态：1-升级成功，2-升级中，3-升级失败，4-等待升级,5-已取消")
    private Integer upgradeStatus;

    @ApiModelProperty(name = "remark", value = "备注信息：失败时可填失败原因")
    private String remark;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpgradeRecordResponse)) {
            return false;
        }
        UpgradeRecordResponse other = (UpgradeRecordResponse) o;
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
        if (this$upgradeStatus == null) {
            if (other$upgradeStatus != null) {
                return false;
            }
        } else if (!this$upgradeStatus.equals(other$upgradeStatus)) {
            return false;
        }
        Object this$remark = getRemark();
        Object other$remark = other.getRemark();
        return this$remark == null ? other$remark == null : this$remark.equals(other$remark);
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpgradeRecordResponse;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $upgradeStatus = getUpgradeStatus();
        int result2 = (result * 59) + ($upgradeStatus == null ? 43 : $upgradeStatus.hashCode());
        Object $remark = getRemark();
        return (result2 * 59) + ($remark == null ? 43 : $remark.hashCode());
    }

    public String toString() {
        return "UpgradeRecordResponse(deviceSn=" + getDeviceSn() + ", upgradeStatus=" + getUpgradeStatus() + ", remark=" + getRemark() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getUpgradeStatus() {
        return this.upgradeStatus;
    }

    public String getRemark() {
        return this.remark;
    }
}

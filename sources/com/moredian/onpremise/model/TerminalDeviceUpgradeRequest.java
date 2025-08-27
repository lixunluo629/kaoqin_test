package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalDeviceUpgradeRequest.class */
public class TerminalDeviceUpgradeRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_UPGRADE_REQUEST;
    private String deviceSn;
    private String upgradePackageUrl;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setUpgradePackageUrl(String upgradePackageUrl) {
        this.upgradePackageUrl = upgradePackageUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalDeviceUpgradeRequest)) {
            return false;
        }
        TerminalDeviceUpgradeRequest other = (TerminalDeviceUpgradeRequest) o;
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
        Object this$upgradePackageUrl = getUpgradePackageUrl();
        Object other$upgradePackageUrl = other.getUpgradePackageUrl();
        return this$upgradePackageUrl == null ? other$upgradePackageUrl == null : this$upgradePackageUrl.equals(other$upgradePackageUrl);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalDeviceUpgradeRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $upgradePackageUrl = getUpgradePackageUrl();
        return (result * 59) + ($upgradePackageUrl == null ? 43 : $upgradePackageUrl.hashCode());
    }

    public String toString() {
        return "TerminalDeviceUpgradeRequest(deviceSn=" + getDeviceSn() + ", upgradePackageUrl=" + getUpgradePackageUrl() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getUpgradePackageUrl() {
        return this.upgradePackageUrl;
    }
}

package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeviceFailureRequest.class */
public class DeviceFailureRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_FAILURE_REQUEST;
    private String deviceSn;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceFailureRequest)) {
            return false;
        }
        DeviceFailureRequest other = (DeviceFailureRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceFailureRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        return result;
    }

    public String toString() {
        return "DeviceFailureRequest(deviceSn=" + getDeviceSn() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}

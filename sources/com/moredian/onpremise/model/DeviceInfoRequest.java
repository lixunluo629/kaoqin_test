package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeviceInfoRequest.class */
public class DeviceInfoRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_INFO_REQUEST;
    private String serialNumber;

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceInfoRequest)) {
            return false;
        }
        DeviceInfoRequest other = (DeviceInfoRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serialNumber = getSerialNumber();
        Object other$serialNumber = other.getSerialNumber();
        return this$serialNumber == null ? other$serialNumber == null : this$serialNumber.equals(other$serialNumber);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceInfoRequest;
    }

    public int hashCode() {
        Object $serialNumber = getSerialNumber();
        int result = (1 * 59) + ($serialNumber == null ? 43 : $serialNumber.hashCode());
        return result;
    }

    public String toString() {
        return "DeviceInfoRequest(serialNumber=" + getSerialNumber() + ")";
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }
}

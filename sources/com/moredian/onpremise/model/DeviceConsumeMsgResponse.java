package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeviceConsumeMsgResponse.class */
public class DeviceConsumeMsgResponse implements Serializable {
    private static final long serialVersionUID = -4198990039633572385L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_CONSUME_MSG_RESPONSE;
    private String deviceSn;
    private String uuid;
    private Integer status;
    private String error;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceConsumeMsgResponse)) {
            return false;
        }
        DeviceConsumeMsgResponse other = (DeviceConsumeMsgResponse) o;
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
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        if (this$uuid == null) {
            if (other$uuid != null) {
                return false;
            }
        } else if (!this$uuid.equals(other$uuid)) {
            return false;
        }
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        if (this$status == null) {
            if (other$status != null) {
                return false;
            }
        } else if (!this$status.equals(other$status)) {
            return false;
        }
        Object this$error = getError();
        Object other$error = other.getError();
        return this$error == null ? other$error == null : this$error.equals(other$error);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceConsumeMsgResponse;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $uuid = getUuid();
        int result2 = (result * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $status = getStatus();
        int result3 = (result2 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $error = getError();
        return (result3 * 59) + ($error == null ? 43 : $error.hashCode());
    }

    public String toString() {
        return "DeviceConsumeMsgResponse(deviceSn=" + getDeviceSn() + ", uuid=" + getUuid() + ", status=" + getStatus() + ", error=" + getError() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }
}

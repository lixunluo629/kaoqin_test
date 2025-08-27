package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/SyncDeviceDefaultShowRequest.class */
public class SyncDeviceDefaultShowRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.SYNC_DEFAULT_SHOW_TYPE_REQUEST;
    private static final long serialVersionUID = -1106902369257565780L;
    private String defaultShowType;

    public void setDefaultShowType(String defaultShowType) {
        this.defaultShowType = defaultShowType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncDeviceDefaultShowRequest)) {
            return false;
        }
        SyncDeviceDefaultShowRequest other = (SyncDeviceDefaultShowRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$defaultShowType = getDefaultShowType();
        Object other$defaultShowType = other.getDefaultShowType();
        return this$defaultShowType == null ? other$defaultShowType == null : this$defaultShowType.equals(other$defaultShowType);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncDeviceDefaultShowRequest;
    }

    public int hashCode() {
        Object $defaultShowType = getDefaultShowType();
        int result = (1 * 59) + ($defaultShowType == null ? 43 : $defaultShowType.hashCode());
        return result;
    }

    public String toString() {
        return "SyncDeviceDefaultShowRequest(defaultShowType=" + getDefaultShowType() + ")";
    }

    public String getDefaultShowType() {
        return this.defaultShowType;
    }

    public SyncDeviceDefaultShowRequest() {
    }

    public SyncDeviceDefaultShowRequest(String defaultShowType) {
        this.defaultShowType = defaultShowType;
    }
}

package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/PullDeviceLogEvent.class */
public class PullDeviceLogEvent implements Serializable {
    private static final long serialVersionUID = 3925532950355850367L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.PULL_DEVICE_LOG_REQUEST;
    private String pullDate;

    public void setPullDate(String pullDate) {
        this.pullDate = pullDate;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PullDeviceLogEvent)) {
            return false;
        }
        PullDeviceLogEvent other = (PullDeviceLogEvent) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$pullDate = getPullDate();
        Object other$pullDate = other.getPullDate();
        return this$pullDate == null ? other$pullDate == null : this$pullDate.equals(other$pullDate);
    }

    protected boolean canEqual(Object other) {
        return other instanceof PullDeviceLogEvent;
    }

    public int hashCode() {
        Object $pullDate = getPullDate();
        int result = (1 * 59) + ($pullDate == null ? 43 : $pullDate.hashCode());
        return result;
    }

    public String toString() {
        return "PullDeviceLogEvent(pullDate=" + getPullDate() + ")";
    }

    public String getPullDate() {
        return this.pullDate;
    }

    public PullDeviceLogEvent() {
    }

    public PullDeviceLogEvent(String pullDate) {
        this.pullDate = pullDate;
    }
}

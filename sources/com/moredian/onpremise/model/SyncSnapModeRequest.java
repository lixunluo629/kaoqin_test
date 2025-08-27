package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/SyncSnapModeRequest.class */
public class SyncSnapModeRequest implements Serializable {
    private static final long serialVersionUID = 7382403118795727906L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.SNAP_MODE_REQUEST;
    private Long lastModifyTime;
    private Integer mode;

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncSnapModeRequest)) {
            return false;
        }
        SyncSnapModeRequest other = (SyncSnapModeRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$lastModifyTime = getLastModifyTime();
        Object other$lastModifyTime = other.getLastModifyTime();
        if (this$lastModifyTime == null) {
            if (other$lastModifyTime != null) {
                return false;
            }
        } else if (!this$lastModifyTime.equals(other$lastModifyTime)) {
            return false;
        }
        Object this$mode = getMode();
        Object other$mode = other.getMode();
        return this$mode == null ? other$mode == null : this$mode.equals(other$mode);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncSnapModeRequest;
    }

    public int hashCode() {
        Object $lastModifyTime = getLastModifyTime();
        int result = (1 * 59) + ($lastModifyTime == null ? 43 : $lastModifyTime.hashCode());
        Object $mode = getMode();
        return (result * 59) + ($mode == null ? 43 : $mode.hashCode());
    }

    public String toString() {
        return "SyncSnapModeRequest(lastModifyTime=" + getLastModifyTime() + ", mode=" + getMode() + ")";
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public Integer getMode() {
        return this.mode;
    }

    public SyncSnapModeRequest() {
    }

    public SyncSnapModeRequest(Integer mode, Long lastModifyTime) {
        this.mode = mode;
        this.lastModifyTime = lastModifyTime;
    }
}

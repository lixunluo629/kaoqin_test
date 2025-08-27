package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/SyncCanteenRequest.class */
public class SyncCanteenRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.SYNC_CANTEEN_REQUEST;
    private static final long serialVersionUID = -2130786741378557472L;
    private Long lastModifyTime;
    private Long orgId;

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncCanteenRequest)) {
            return false;
        }
        SyncCanteenRequest other = (SyncCanteenRequest) o;
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncCanteenRequest;
    }

    public int hashCode() {
        Object $lastModifyTime = getLastModifyTime();
        int result = (1 * 59) + ($lastModifyTime == null ? 43 : $lastModifyTime.hashCode());
        Object $orgId = getOrgId();
        return (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
    }

    public String toString() {
        return "SyncCanteenRequest(lastModifyTime=" + getLastModifyTime() + ", orgId=" + getOrgId() + ")";
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public SyncCanteenRequest() {
    }

    public SyncCanteenRequest(Long lastModifyTime, Long orgId) {
        this.lastModifyTime = lastModifyTime;
        this.orgId = orgId;
    }
}

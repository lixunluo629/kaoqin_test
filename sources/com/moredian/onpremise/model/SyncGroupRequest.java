package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/SyncGroupRequest.class */
public class SyncGroupRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.SYNC_GROUP_REQUEST;
    private Long lastModifyTime;
    private Long orgId;
    private int allSync;

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setAllSync(int allSync) {
        this.allSync = allSync;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncGroupRequest)) {
            return false;
        }
        SyncGroupRequest other = (SyncGroupRequest) o;
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
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        return getAllSync() == other.getAllSync();
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncGroupRequest;
    }

    public int hashCode() {
        Object $lastModifyTime = getLastModifyTime();
        int result = (1 * 59) + ($lastModifyTime == null ? 43 : $lastModifyTime.hashCode());
        Object $orgId = getOrgId();
        return (((result * 59) + ($orgId == null ? 43 : $orgId.hashCode())) * 59) + getAllSync();
    }

    public String toString() {
        return "SyncGroupRequest(lastModifyTime=" + getLastModifyTime() + ", orgId=" + getOrgId() + ", allSync=" + getAllSync() + ")";
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public int getAllSync() {
        return this.allSync;
    }

    public SyncGroupRequest() {
    }

    public SyncGroupRequest(Long lastModifyTime, Long orgId) {
        this.lastModifyTime = lastModifyTime;
        this.orgId = orgId;
        this.allSync = 0;
    }

    public SyncGroupRequest(Long orgId, int allSync) {
        this.lastModifyTime = 0L;
        this.orgId = orgId;
        this.allSync = allSync;
    }
}

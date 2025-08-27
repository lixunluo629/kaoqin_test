package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeleteOnlineCheckCallbackRequest.class */
public class DeleteOnlineCheckCallbackRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DELETE_ONLINE_CHECK_CALLBACK_REQUEST;
    private Long orgId;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteOnlineCheckCallbackRequest)) {
            return false;
        }
        DeleteOnlineCheckCallbackRequest other = (DeleteOnlineCheckCallbackRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeleteOnlineCheckCallbackRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        return result;
    }

    public String toString() {
        return "DeleteOnlineCheckCallbackRequest(orgId=" + getOrgId() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public DeleteOnlineCheckCallbackRequest(Long orgId) {
        this.orgId = orgId;
    }
}

package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeleteQrCheckCallbackRequest.class */
public class DeleteQrCheckCallbackRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DELETE_QR_CHECK_CALLBACK_REQUEST;
    private Long orgId;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeleteQrCheckCallbackRequest)) {
            return false;
        }
        DeleteQrCheckCallbackRequest other = (DeleteQrCheckCallbackRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeleteQrCheckCallbackRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        return result;
    }

    public String toString() {
        return "DeleteQrCheckCallbackRequest(orgId=" + getOrgId() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public DeleteQrCheckCallbackRequest(Long orgId) {
        this.orgId = orgId;
    }
}

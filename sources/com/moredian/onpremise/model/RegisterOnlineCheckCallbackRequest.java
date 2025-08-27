package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/RegisterOnlineCheckCallbackRequest.class */
public class RegisterOnlineCheckCallbackRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.REGISTER_ONLINE_CHECK_CALLBACK_REQUEST;
    private Long orgId;
    private String url;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegisterOnlineCheckCallbackRequest)) {
            return false;
        }
        RegisterOnlineCheckCallbackRequest other = (RegisterOnlineCheckCallbackRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$url = getUrl();
        Object other$url = other.getUrl();
        return this$url == null ? other$url == null : this$url.equals(other$url);
    }

    protected boolean canEqual(Object other) {
        return other instanceof RegisterOnlineCheckCallbackRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $url = getUrl();
        return (result * 59) + ($url == null ? 43 : $url.hashCode());
    }

    public String toString() {
        return "RegisterOnlineCheckCallbackRequest(orgId=" + getOrgId() + ", url=" + getUrl() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getUrl() {
        return this.url;
    }

    public RegisterOnlineCheckCallbackRequest(Long orgId, String url) {
        this.url = url;
        this.orgId = orgId;
    }
}

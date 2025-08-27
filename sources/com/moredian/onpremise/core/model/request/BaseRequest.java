package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BaseRequest.class */
public class BaseRequest implements Serializable {

    @ApiModelProperty(name = "loginAccountId", value = "当前登录的账户", hidden = true)
    private Long loginAccountId;

    @ApiModelProperty(name = "orgId", value = "机构id", hidden = true)
    private Long orgId;

    @ApiModelProperty(name = "sessionId", value = "sessionId", hidden = true)
    private String sessionId;

    public void setLoginAccountId(Long loginAccountId) {
        this.loginAccountId = loginAccountId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BaseRequest)) {
            return false;
        }
        BaseRequest other = (BaseRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$loginAccountId = getLoginAccountId();
        Object other$loginAccountId = other.getLoginAccountId();
        if (this$loginAccountId == null) {
            if (other$loginAccountId != null) {
                return false;
            }
        } else if (!this$loginAccountId.equals(other$loginAccountId)) {
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
        Object this$sessionId = getSessionId();
        Object other$sessionId = other.getSessionId();
        return this$sessionId == null ? other$sessionId == null : this$sessionId.equals(other$sessionId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseRequest;
    }

    public int hashCode() {
        Object $loginAccountId = getLoginAccountId();
        int result = (1 * 59) + ($loginAccountId == null ? 43 : $loginAccountId.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $sessionId = getSessionId();
        return (result2 * 59) + ($sessionId == null ? 43 : $sessionId.hashCode());
    }

    public String toString() {
        return "BaseRequest(loginAccountId=" + getLoginAccountId() + ", orgId=" + getOrgId() + ", sessionId=" + getSessionId() + ")";
    }

    public Long getLoginAccountId() {
        return this.loginAccountId;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}

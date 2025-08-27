package com.moredian.onpremise.model;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/DeviceInfoResponse.class */
public class DeviceInfoResponse extends NettyBaseResponse {
    public static final IOTModelType MODEL_TYPE = IOTModelType.DEVICE_INFO_RESPONSE;
    private Boolean activeFlag;
    private Long orgId;
    private String token;
    private Long systemCurrentTime;

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSystemCurrentTime(Long systemCurrentTime) {
        this.systemCurrentTime = systemCurrentTime;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceInfoResponse)) {
            return false;
        }
        DeviceInfoResponse other = (DeviceInfoResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$activeFlag = getActiveFlag();
        Object other$activeFlag = other.getActiveFlag();
        if (this$activeFlag == null) {
            if (other$activeFlag != null) {
                return false;
            }
        } else if (!this$activeFlag.equals(other$activeFlag)) {
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
        Object this$token = getToken();
        Object other$token = other.getToken();
        if (this$token == null) {
            if (other$token != null) {
                return false;
            }
        } else if (!this$token.equals(other$token)) {
            return false;
        }
        Object this$systemCurrentTime = getSystemCurrentTime();
        Object other$systemCurrentTime = other.getSystemCurrentTime();
        return this$systemCurrentTime == null ? other$systemCurrentTime == null : this$systemCurrentTime.equals(other$systemCurrentTime);
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    protected boolean canEqual(Object other) {
        return other instanceof DeviceInfoResponse;
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public int hashCode() {
        Object $activeFlag = getActiveFlag();
        int result = (1 * 59) + ($activeFlag == null ? 43 : $activeFlag.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $token = getToken();
        int result3 = (result2 * 59) + ($token == null ? 43 : $token.hashCode());
        Object $systemCurrentTime = getSystemCurrentTime();
        return (result3 * 59) + ($systemCurrentTime == null ? 43 : $systemCurrentTime.hashCode());
    }

    @Override // com.moredian.onpremise.model.NettyBaseResponse
    public String toString() {
        return "DeviceInfoResponse(activeFlag=" + getActiveFlag() + ", orgId=" + getOrgId() + ", token=" + getToken() + ", systemCurrentTime=" + getSystemCurrentTime() + ")";
    }

    public Boolean getActiveFlag() {
        return this.activeFlag;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getToken() {
        return this.token;
    }

    public Long getSystemCurrentTime() {
        return this.systemCurrentTime;
    }
}

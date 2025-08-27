package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "查询成员是否有业务请求对象")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/BizCheckRequest.class */
public class BizCheckRequest implements Serializable {

    @ApiModelProperty(name = "orgId", value = "组织id")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "memberId", value = "成员id")
    private Long memberId;

    @ApiModelProperty(name = "verifyTime", value = "识别时间")
    private Long verifyTime;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BizCheckRequest)) {
            return false;
        }
        BizCheckRequest other = (BizCheckRequest) o;
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
        Object this$deviceSn = getDeviceSn();
        Object other$deviceSn = other.getDeviceSn();
        if (this$deviceSn == null) {
            if (other$deviceSn != null) {
                return false;
            }
        } else if (!this$deviceSn.equals(other$deviceSn)) {
            return false;
        }
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        if (this$memberId == null) {
            if (other$memberId != null) {
                return false;
            }
        } else if (!this$memberId.equals(other$memberId)) {
            return false;
        }
        Object this$verifyTime = getVerifyTime();
        Object other$verifyTime = other.getVerifyTime();
        return this$verifyTime == null ? other$verifyTime == null : this$verifyTime.equals(other$verifyTime);
    }

    protected boolean canEqual(Object other) {
        return other instanceof BizCheckRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $memberId = getMemberId();
        int result3 = (result2 * 59) + ($memberId == null ? 43 : $memberId.hashCode());
        Object $verifyTime = getVerifyTime();
        return (result3 * 59) + ($verifyTime == null ? 43 : $verifyTime.hashCode());
    }

    public String toString() {
        return "BizCheckRequest(orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ", memberId=" + getMemberId() + ", verifyTime=" + getVerifyTime() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getVerifyTime() {
        return this.verifyTime;
    }
}

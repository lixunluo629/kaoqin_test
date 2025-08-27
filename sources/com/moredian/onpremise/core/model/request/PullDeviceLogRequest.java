package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "拉取设备日志请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/PullDeviceLogRequest.class */
public class PullDeviceLogRequest implements Serializable {
    private static final long serialVersionUID = 8058224137423739843L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "pullLogDate", value = "需要拉取某一日的日志，格式：yyyy-MM-dd，例：2019-03-20")
    private String pullLogDate;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId = 1L;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setPullLogDate(String pullLogDate) {
        this.pullLogDate = pullLogDate;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PullDeviceLogRequest)) {
            return false;
        }
        PullDeviceLogRequest other = (PullDeviceLogRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$pullLogDate = getPullLogDate();
        Object other$pullLogDate = other.getPullLogDate();
        if (this$pullLogDate == null) {
            if (other$pullLogDate != null) {
                return false;
            }
        } else if (!this$pullLogDate.equals(other$pullLogDate)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof PullDeviceLogRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $pullLogDate = getPullLogDate();
        int result2 = (result * 59) + ($pullLogDate == null ? 43 : $pullLogDate.hashCode());
        Object $orgId = getOrgId();
        return (result2 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
    }

    public String toString() {
        return "PullDeviceLogRequest(deviceSn=" + getDeviceSn() + ", pullLogDate=" + getPullLogDate() + ", orgId=" + getOrgId() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getPullLogDate() {
        return this.pullLogDate;
    }

    public Long getOrgId() {
        return this.orgId;
    }
}

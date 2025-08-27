package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "下载设备日志请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DownloadDeviceLogRequest.class */
public class DownloadDeviceLogRequest implements Serializable {
    private static final long serialVersionUID = 8058224137423739843L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId = 1L;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DownloadDeviceLogRequest)) {
            return false;
        }
        DownloadDeviceLogRequest other = (DownloadDeviceLogRequest) o;
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloadDeviceLogRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $orgId = getOrgId();
        return (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
    }

    public String toString() {
        return "DownloadDeviceLogRequest(deviceSn=" + getDeviceSn() + ", orgId=" + getOrgId() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getOrgId() {
        return this.orgId;
    }
}

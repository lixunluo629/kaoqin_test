package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端同步数据请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalSyncRequest.class */
public class TerminalSyncRequest implements Serializable {

    @ApiModelProperty(name = "lastSyncTime", value = "上次同步时间")
    private Long lastSyncTime;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    public void setLastSyncTime(Long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalSyncRequest)) {
            return false;
        }
        TerminalSyncRequest other = (TerminalSyncRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$lastSyncTime = getLastSyncTime();
        Object other$lastSyncTime = other.getLastSyncTime();
        if (this$lastSyncTime == null) {
            if (other$lastSyncTime != null) {
                return false;
            }
        } else if (!this$lastSyncTime.equals(other$lastSyncTime)) {
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
        return this$deviceSn == null ? other$deviceSn == null : this$deviceSn.equals(other$deviceSn);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalSyncRequest;
    }

    public int hashCode() {
        Object $lastSyncTime = getLastSyncTime();
        int result = (1 * 59) + ($lastSyncTime == null ? 43 : $lastSyncTime.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deviceSn = getDeviceSn();
        return (result2 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
    }

    public String toString() {
        return "TerminalSyncRequest(lastSyncTime=" + getLastSyncTime() + ", orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ")";
    }

    public Long getLastSyncTime() {
        return this.lastSyncTime;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }
}

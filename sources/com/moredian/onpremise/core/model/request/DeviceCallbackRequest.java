package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "设备状态事件回调通知参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeviceCallbackRequest.class */
public class DeviceCallbackRequest implements Serializable {
    private static final long serialVersionUID = 8539335074354030879L;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "event", value = "事件，1-激活；2-解绑；3-上线；4-离线")
    private Integer event;

    @ApiModelProperty(name = "timestamp", value = "时间戳")
    private Long timestamp;

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceCallbackRequest)) {
            return false;
        }
        DeviceCallbackRequest other = (DeviceCallbackRequest) o;
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
        Object this$deviceName = getDeviceName();
        Object other$deviceName = other.getDeviceName();
        if (this$deviceName == null) {
            if (other$deviceName != null) {
                return false;
            }
        } else if (!this$deviceName.equals(other$deviceName)) {
            return false;
        }
        Object this$event = getEvent();
        Object other$event = other.getEvent();
        if (this$event == null) {
            if (other$event != null) {
                return false;
            }
        } else if (!this$event.equals(other$event)) {
            return false;
        }
        Object this$timestamp = getTimestamp();
        Object other$timestamp = other.getTimestamp();
        return this$timestamp == null ? other$timestamp == null : this$timestamp.equals(other$timestamp);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeviceCallbackRequest;
    }

    public int hashCode() {
        Object $orgId = getOrgId();
        int result = (1 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $deviceName = getDeviceName();
        int result3 = (result2 * 59) + ($deviceName == null ? 43 : $deviceName.hashCode());
        Object $event = getEvent();
        int result4 = (result3 * 59) + ($event == null ? 43 : $event.hashCode());
        Object $timestamp = getTimestamp();
        return (result4 * 59) + ($timestamp == null ? 43 : $timestamp.hashCode());
    }

    public String toString() {
        return "DeviceCallbackRequest(orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", event=" + getEvent() + ", timestamp=" + getTimestamp() + ")";
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public Integer getEvent() {
        return this.event;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }
}

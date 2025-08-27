package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "终端设备状态事件回调请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalDeviceCallbackRequest.class */
public class TerminalDeviceCallbackRequest implements Serializable {
    private static final long serialVersionUID = -1918784379068558872L;

    @ApiModelProperty(name = "orgId", value = "orgId")
    private Long orgId;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "deviceName", value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "event", value = "事件，1-激活；2-解绑；3-上线；4-离线")
    private Integer event;

    @ApiModelProperty(name = "timeStamp", value = "时间戳")
    private Long timeStamp;

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

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalDeviceCallbackRequest)) {
            return false;
        }
        TerminalDeviceCallbackRequest other = (TerminalDeviceCallbackRequest) o;
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
        Object this$timeStamp = getTimeStamp();
        Object other$timeStamp = other.getTimeStamp();
        return this$timeStamp == null ? other$timeStamp == null : this$timeStamp.equals(other$timeStamp);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalDeviceCallbackRequest;
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
        Object $timeStamp = getTimeStamp();
        return (result4 * 59) + ($timeStamp == null ? 43 : $timeStamp.hashCode());
    }

    public String toString() {
        return "TerminalDeviceCallbackRequest(orgId=" + getOrgId() + ", deviceSn=" + getDeviceSn() + ", deviceName=" + getDeviceName() + ", event=" + getEvent() + ", timeStamp=" + getTimeStamp() + ")";
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

    public Long getTimeStamp() {
        return this.timeStamp;
    }
}

package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;

@ApiModel(description = "在线鉴权请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/TerminalQrCheckRequest.class */
public class TerminalQrCheckRequest implements Serializable {
    String qrContent;
    String deviceSn;
    Long timeStamp;

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalQrCheckRequest)) {
            return false;
        }
        TerminalQrCheckRequest other = (TerminalQrCheckRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$qrContent = getQrContent();
        Object other$qrContent = other.getQrContent();
        if (this$qrContent == null) {
            if (other$qrContent != null) {
                return false;
            }
        } else if (!this$qrContent.equals(other$qrContent)) {
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
        Object this$timeStamp = getTimeStamp();
        Object other$timeStamp = other.getTimeStamp();
        return this$timeStamp == null ? other$timeStamp == null : this$timeStamp.equals(other$timeStamp);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalQrCheckRequest;
    }

    public int hashCode() {
        Object $qrContent = getQrContent();
        int result = (1 * 59) + ($qrContent == null ? 43 : $qrContent.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $timeStamp = getTimeStamp();
        return (result2 * 59) + ($timeStamp == null ? 43 : $timeStamp.hashCode());
    }

    public String toString() {
        return "TerminalQrCheckRequest(qrContent=" + getQrContent() + ", deviceSn=" + getDeviceSn() + ", timeStamp=" + getTimeStamp() + ")";
    }

    public String getQrContent() {
        return this.qrContent;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Long getTimeStamp() {
        return this.timeStamp;
    }
}

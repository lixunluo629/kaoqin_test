package com.moredian.onpremise.core.model.request;

import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "设备推送文本、语音请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DeviceShowDataRequest.class */
public class DeviceShowDataRequest extends BaseRequest {
    private static final long serialVersionUID = 8690626298263767351L;

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "tipsText", value = "文字内容")
    private String tipsText;

    @ApiModelProperty(name = "tipsSpeech", value = "语音内容")
    private String tipsSpeech;

    @ApiModelProperty(name = RtspHeaders.Values.TIME, value = "展示时间，单位：毫秒")
    private Integer time;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setTipsText(String tipsText) {
        this.tipsText = tipsText;
    }

    public void setTipsSpeech(String tipsSpeech) {
        this.tipsSpeech = tipsSpeech;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeviceShowDataRequest)) {
            return false;
        }
        DeviceShowDataRequest other = (DeviceShowDataRequest) o;
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
        Object this$tipsText = getTipsText();
        Object other$tipsText = other.getTipsText();
        if (this$tipsText == null) {
            if (other$tipsText != null) {
                return false;
            }
        } else if (!this$tipsText.equals(other$tipsText)) {
            return false;
        }
        Object this$tipsSpeech = getTipsSpeech();
        Object other$tipsSpeech = other.getTipsSpeech();
        if (this$tipsSpeech == null) {
            if (other$tipsSpeech != null) {
                return false;
            }
        } else if (!this$tipsSpeech.equals(other$tipsSpeech)) {
            return false;
        }
        Object this$time = getTime();
        Object other$time = other.getTime();
        return this$time == null ? other$time == null : this$time.equals(other$time);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof DeviceShowDataRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $tipsText = getTipsText();
        int result2 = (result * 59) + ($tipsText == null ? 43 : $tipsText.hashCode());
        Object $tipsSpeech = getTipsSpeech();
        int result3 = (result2 * 59) + ($tipsSpeech == null ? 43 : $tipsSpeech.hashCode());
        Object $time = getTime();
        return (result3 * 59) + ($time == null ? 43 : $time.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "DeviceShowDataRequest(deviceSn=" + getDeviceSn() + ", tipsText=" + getTipsText() + ", tipsSpeech=" + getTipsSpeech() + ", time=" + getTime() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getTipsText() {
        return this.tipsText;
    }

    public String getTipsSpeech() {
        return this.tipsSpeech;
    }

    public Integer getTime() {
        return this.time;
    }
}

package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "通知设备侧开始录脸请求")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ExtractNoticeRequest.class */
public class ExtractNoticeRequest extends BaseRequest {
    private static final long serialVersionUID = 8326195370311760721L;

    @ApiModelProperty(name = "jobNum", value = "上传任务的任务序列号，由前端生成")
    private String jobNum;

    @ApiModelProperty(name = "deviceSn", value = "设备sn列表")
    private String deviceSn;

    @ApiModelProperty(name = "expires", value = "指定时间不录脸，设备退出录脸界面，单位：秒")
    private Integer expires;

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExtractNoticeRequest)) {
            return false;
        }
        ExtractNoticeRequest other = (ExtractNoticeRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        if (this$jobNum == null) {
            if (other$jobNum != null) {
                return false;
            }
        } else if (!this$jobNum.equals(other$jobNum)) {
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
        Object this$expires = getExpires();
        Object other$expires = other.getExpires();
        return this$expires == null ? other$expires == null : this$expires.equals(other$expires);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ExtractNoticeRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $jobNum = getJobNum();
        int result = (1 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $deviceSn = getDeviceSn();
        int result2 = (result * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $expires = getExpires();
        return (result2 * 59) + ($expires == null ? 43 : $expires.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ExtractNoticeRequest(jobNum=" + getJobNum() + ", deviceSn=" + getDeviceSn() + ", expires=" + getExpires() + ")";
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public Integer getExpires() {
        return this.expires;
    }
}

package com.moredian.onpremise.core.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "下载设备日志响应信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/response/DownloadDeviceLogResponse.class */
public class DownloadDeviceLogResponse implements Serializable {

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "状态：1-设置日志拉取成功，2-拉取中")
    private Integer status;

    @ApiModelProperty(name = "logUrl", value = "日志路径")
    private String logUrl;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DownloadDeviceLogResponse)) {
            return false;
        }
        DownloadDeviceLogResponse other = (DownloadDeviceLogResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$status = getStatus();
        Object other$status = other.getStatus();
        if (this$status == null) {
            if (other$status != null) {
                return false;
            }
        } else if (!this$status.equals(other$status)) {
            return false;
        }
        Object this$logUrl = getLogUrl();
        Object other$logUrl = other.getLogUrl();
        return this$logUrl == null ? other$logUrl == null : this$logUrl.equals(other$logUrl);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloadDeviceLogResponse;
    }

    public int hashCode() {
        Object $status = getStatus();
        int result = (1 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $logUrl = getLogUrl();
        return (result * 59) + ($logUrl == null ? 43 : $logUrl.hashCode());
    }

    public String toString() {
        return "DownloadDeviceLogResponse(status=" + getStatus() + ", logUrl=" + getLogUrl() + ")";
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getLogUrl() {
        return this.logUrl;
    }
}

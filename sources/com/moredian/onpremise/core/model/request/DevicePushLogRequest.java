package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;

@ApiModel(description = "设备推送日志请求参数")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/DevicePushLogRequest.class */
public class DevicePushLogRequest implements Serializable {

    @ApiModelProperty(name = "deviceSn", value = "设备sn")
    private String deviceSn;

    @ApiModelProperty(name = "fileName", value = "文件名")
    private String fileName;

    @ApiModelProperty(name = "file", value = "上传文件", hidden = true)
    private MultipartFile file;

    @ApiModelProperty(name = "orgId", value = "orgId", hidden = true)
    private Long orgId = 1L;

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DevicePushLogRequest)) {
            return false;
        }
        DevicePushLogRequest other = (DevicePushLogRequest) o;
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
        Object this$fileName = getFileName();
        Object other$fileName = other.getFileName();
        if (this$fileName == null) {
            if (other$fileName != null) {
                return false;
            }
        } else if (!this$fileName.equals(other$fileName)) {
            return false;
        }
        Object this$file = getFile();
        Object other$file = other.getFile();
        if (this$file == null) {
            if (other$file != null) {
                return false;
            }
        } else if (!this$file.equals(other$file)) {
            return false;
        }
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        return this$orgId == null ? other$orgId == null : this$orgId.equals(other$orgId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof DevicePushLogRequest;
    }

    public int hashCode() {
        Object $deviceSn = getDeviceSn();
        int result = (1 * 59) + ($deviceSn == null ? 43 : $deviceSn.hashCode());
        Object $fileName = getFileName();
        int result2 = (result * 59) + ($fileName == null ? 43 : $fileName.hashCode());
        Object $file = getFile();
        int result3 = (result2 * 59) + ($file == null ? 43 : $file.hashCode());
        Object $orgId = getOrgId();
        return (result3 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
    }

    public String toString() {
        return "DevicePushLogRequest(deviceSn=" + getDeviceSn() + ", fileName=" + getFileName() + ", file=" + getFile() + ", orgId=" + getOrgId() + ")";
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    public String getFileName() {
        return this.fileName;
    }

    public MultipartFile getFile() {
        return this.file;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public DevicePushLogRequest() {
    }

    public DevicePushLogRequest(String deviceSn, String fileName, MultipartFile file) {
        this.deviceSn = deviceSn;
        this.fileName = fileName;
        this.file = file;
    }
}

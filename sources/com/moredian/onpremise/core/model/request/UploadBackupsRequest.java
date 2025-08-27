package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

@ApiModel(description = "上传备份数据包")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UploadBackupsRequest.class */
public class UploadBackupsRequest extends BaseRequest {
    private static final long serialVersionUID = -7628753620340454573L;

    @ApiModelProperty(name = "file", value = "上传文件")
    private MultipartFile file;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UploadBackupsRequest)) {
            return false;
        }
        UploadBackupsRequest other = (UploadBackupsRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$file = getFile();
        Object other$file = other.getFile();
        return this$file == null ? other$file == null : this$file.equals(other$file);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UploadBackupsRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $file = getFile();
        int result = (1 * 59) + ($file == null ? 43 : $file.hashCode());
        return result;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UploadBackupsRequest(file=" + getFile() + ")";
    }

    public MultipartFile getFile() {
        return this.file;
    }

    public UploadBackupsRequest() {
    }

    public UploadBackupsRequest(MultipartFile file) {
        this.file = file;
    }
}

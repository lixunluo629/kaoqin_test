package com.moredian.onpremise.core.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

@ApiModel(description = "上传升级包请求信息")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/UploadUpgradePackageRequest.class */
public class UploadUpgradePackageRequest extends BaseRequest {
    private static final long serialVersionUID = 4620651897304222626L;

    @ApiModelProperty(name = "file", value = "上传文件", hidden = true)
    private MultipartFile file;

    @ApiModelProperty(name = "filePath", value = "文件路径")
    private String filePath;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UploadUpgradePackageRequest)) {
            return false;
        }
        UploadUpgradePackageRequest other = (UploadUpgradePackageRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$filePath = getFilePath();
        Object other$filePath = other.getFilePath();
        return this$filePath == null ? other$filePath == null : this$filePath.equals(other$filePath);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof UploadUpgradePackageRequest;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $file = getFile();
        int result = (1 * 59) + ($file == null ? 43 : $file.hashCode());
        Object $filePath = getFilePath();
        return (result * 59) + ($filePath == null ? 43 : $filePath.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "UploadUpgradePackageRequest(file=" + getFile() + ", filePath=" + getFilePath() + ")";
    }

    public MultipartFile getFile() {
        return this.file;
    }

    public UploadUpgradePackageRequest(MultipartFile file) {
        this.file = file;
    }

    public UploadUpgradePackageRequest() {
    }

    public String getFilePath() {
        return this.filePath;
    }
}

package com.moredian.onpremise.core.model.request;

import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "文件上传base64码")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/ImageUploadBase64Request.class */
public class ImageUploadBase64Request extends BaseRequest {

    @ApiModelProperty(name = "image", value = "图片base64码")
    private String image;

    @ApiModelProperty(name = RtspHeaders.Values.URL, value = "图片url，通知设备录脸用", hidden = true)
    private String url;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(name = "endFlag", value = "是否是最后一张标识:1-是，2-否")
    private Integer endFlag;

    @ApiModelProperty(name = "count", value = "批次内上传图片总数量")
    private Integer count;

    @ApiModelProperty(name = "jobNum", value = "上传任务的任务序列号，由前端生成")
    private String jobNum;

    @ApiModelProperty(name = "needExtractFeature", value = "是否需要抽取特征值：1-需要，0-不需要", hidden = true)
    private Integer needExtractFeature;

    @ApiModelProperty(name = "deviceSns", value = "上传时指定抽取特征值的设备sn列表", hidden = true)
    private List<String> deviceSns;

    @ApiModelProperty(name = "currentNum", value = "当前是上传第几张")
    private Integer currentNum;

    @ApiModelProperty(name = "needAutoSave", value = "是否需要自动保存到对应成员:1-是，2-否")
    private Integer needAutoSave = 1;

    public void setImage(String image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setEndFlag(Integer endFlag) {
        this.endFlag = endFlag;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setNeedExtractFeature(Integer needExtractFeature) {
        this.needExtractFeature = needExtractFeature;
    }

    public void setDeviceSns(List<String> deviceSns) {
        this.deviceSns = deviceSns;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public void setNeedAutoSave(Integer needAutoSave) {
        this.needAutoSave = needAutoSave;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ImageUploadBase64Request)) {
            return false;
        }
        ImageUploadBase64Request other = (ImageUploadBase64Request) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$image = getImage();
        Object other$image = other.getImage();
        if (this$image == null) {
            if (other$image != null) {
                return false;
            }
        } else if (!this$image.equals(other$image)) {
            return false;
        }
        Object this$url = getUrl();
        Object other$url = other.getUrl();
        if (this$url == null) {
            if (other$url != null) {
                return false;
            }
        } else if (!this$url.equals(other$url)) {
            return false;
        }
        Object this$memberJobNum = getMemberJobNum();
        Object other$memberJobNum = other.getMemberJobNum();
        if (this$memberJobNum == null) {
            if (other$memberJobNum != null) {
                return false;
            }
        } else if (!this$memberJobNum.equals(other$memberJobNum)) {
            return false;
        }
        Object this$endFlag = getEndFlag();
        Object other$endFlag = other.getEndFlag();
        if (this$endFlag == null) {
            if (other$endFlag != null) {
                return false;
            }
        } else if (!this$endFlag.equals(other$endFlag)) {
            return false;
        }
        Object this$count = getCount();
        Object other$count = other.getCount();
        if (this$count == null) {
            if (other$count != null) {
                return false;
            }
        } else if (!this$count.equals(other$count)) {
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
        Object this$needExtractFeature = getNeedExtractFeature();
        Object other$needExtractFeature = other.getNeedExtractFeature();
        if (this$needExtractFeature == null) {
            if (other$needExtractFeature != null) {
                return false;
            }
        } else if (!this$needExtractFeature.equals(other$needExtractFeature)) {
            return false;
        }
        Object this$deviceSns = getDeviceSns();
        Object other$deviceSns = other.getDeviceSns();
        if (this$deviceSns == null) {
            if (other$deviceSns != null) {
                return false;
            }
        } else if (!this$deviceSns.equals(other$deviceSns)) {
            return false;
        }
        Object this$currentNum = getCurrentNum();
        Object other$currentNum = other.getCurrentNum();
        if (this$currentNum == null) {
            if (other$currentNum != null) {
                return false;
            }
        } else if (!this$currentNum.equals(other$currentNum)) {
            return false;
        }
        Object this$needAutoSave = getNeedAutoSave();
        Object other$needAutoSave = other.getNeedAutoSave();
        return this$needAutoSave == null ? other$needAutoSave == null : this$needAutoSave.equals(other$needAutoSave);
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    protected boolean canEqual(Object other) {
        return other instanceof ImageUploadBase64Request;
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public int hashCode() {
        Object $image = getImage();
        int result = (1 * 59) + ($image == null ? 43 : $image.hashCode());
        Object $url = getUrl();
        int result2 = (result * 59) + ($url == null ? 43 : $url.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result3 = (result2 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $endFlag = getEndFlag();
        int result4 = (result3 * 59) + ($endFlag == null ? 43 : $endFlag.hashCode());
        Object $count = getCount();
        int result5 = (result4 * 59) + ($count == null ? 43 : $count.hashCode());
        Object $jobNum = getJobNum();
        int result6 = (result5 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $needExtractFeature = getNeedExtractFeature();
        int result7 = (result6 * 59) + ($needExtractFeature == null ? 43 : $needExtractFeature.hashCode());
        Object $deviceSns = getDeviceSns();
        int result8 = (result7 * 59) + ($deviceSns == null ? 43 : $deviceSns.hashCode());
        Object $currentNum = getCurrentNum();
        int result9 = (result8 * 59) + ($currentNum == null ? 43 : $currentNum.hashCode());
        Object $needAutoSave = getNeedAutoSave();
        return (result9 * 59) + ($needAutoSave == null ? 43 : $needAutoSave.hashCode());
    }

    @Override // com.moredian.onpremise.core.model.request.BaseRequest
    public String toString() {
        return "ImageUploadBase64Request(image=" + getImage() + ", url=" + getUrl() + ", memberJobNum=" + getMemberJobNum() + ", endFlag=" + getEndFlag() + ", count=" + getCount() + ", jobNum=" + getJobNum() + ", needExtractFeature=" + getNeedExtractFeature() + ", deviceSns=" + getDeviceSns() + ", currentNum=" + getCurrentNum() + ", needAutoSave=" + getNeedAutoSave() + ")";
    }

    public String getImage() {
        return this.image;
    }

    public String getUrl() {
        return this.url;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public Integer getEndFlag() {
        return this.endFlag;
    }

    public Integer getCount() {
        return this.count;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public Integer getNeedExtractFeature() {
        return this.needExtractFeature;
    }

    public List<String> getDeviceSns() {
        return this.deviceSns;
    }

    public Integer getCurrentNum() {
        return this.currentNum;
    }

    public Integer getNeedAutoSave() {
        return this.needAutoSave;
    }
}

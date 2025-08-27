package com.moredian.onpremise.core.model.request;

import io.netty.handler.codec.rtsp.RtspHeaders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(description = "服务端识别")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/request/MatchFeatureRequest.class */
public class MatchFeatureRequest implements Serializable {
    private static final long serialVersionUID = 1355241082524496673L;

    @ApiModelProperty(name = "imageBase64", value = "图片base64码")
    private String imageBase64;

    @ApiModelProperty(name = "orgId", value = "机构id")
    private Long orgId;

    @ApiModelProperty(name = RtspHeaders.Values.URL, value = "图片url")
    private String url;

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MatchFeatureRequest)) {
            return false;
        }
        MatchFeatureRequest other = (MatchFeatureRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$imageBase64 = getImageBase64();
        Object other$imageBase64 = other.getImageBase64();
        if (this$imageBase64 == null) {
            if (other$imageBase64 != null) {
                return false;
            }
        } else if (!this$imageBase64.equals(other$imageBase64)) {
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
        Object this$url = getUrl();
        Object other$url = other.getUrl();
        return this$url == null ? other$url == null : this$url.equals(other$url);
    }

    protected boolean canEqual(Object other) {
        return other instanceof MatchFeatureRequest;
    }

    public int hashCode() {
        Object $imageBase64 = getImageBase64();
        int result = (1 * 59) + ($imageBase64 == null ? 43 : $imageBase64.hashCode());
        Object $orgId = getOrgId();
        int result2 = (result * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $url = getUrl();
        return (result2 * 59) + ($url == null ? 43 : $url.hashCode());
    }

    public String toString() {
        return "MatchFeatureRequest(imageBase64=" + getImageBase64() + ", orgId=" + getOrgId() + ", url=" + getUrl() + ")";
    }

    public String getImageBase64() {
        return this.imageBase64;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getUrl() {
        return this.url;
    }

    public MatchFeatureRequest() {
    }

    public MatchFeatureRequest(String imageBase64, Long orgId) {
        this.imageBase64 = imageBase64;
        this.orgId = orgId;
    }

    public MatchFeatureRequest(String imageBase64, Long orgId, String url) {
        this.imageBase64 = imageBase64;
        this.orgId = orgId;
        this.url = url;
    }
}

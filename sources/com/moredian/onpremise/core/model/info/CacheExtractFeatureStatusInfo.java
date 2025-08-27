package com.moredian.onpremise.core.model.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.web.servlet.tags.BindTag;

@ApiModel(description = "抽取特征值缓存")
/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/model/info/CacheExtractFeatureStatusInfo.class */
public class CacheExtractFeatureStatusInfo implements Serializable {

    @ApiModelProperty(name = "expireTime", value = "过期时间")
    private Long expireTime;

    @ApiModelProperty(name = BindTag.STATUS_VARIABLE_NAME, value = "抽取结果：1-成功，0-失败，2-抽取中，3-设备无法访问下载文件端口")
    private Integer status;

    @ApiModelProperty(value = "底库照url", name = "verifyFaceUrl")
    private String verifyFaceUrl;

    @ApiModelProperty(name = "memberJobNum", value = "工号")
    private String memberJobNum;

    @ApiModelProperty(value = "机构id", name = "orgId")
    private Long orgId;

    @ApiModelProperty(value = "特征值", name = "eigenvalue")
    private String eigenvalue;

    @ApiModelProperty(name = "count", value = "总数")
    private Integer count;

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setEigenvalue(String eigenvalue) {
        this.eigenvalue = eigenvalue;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CacheExtractFeatureStatusInfo)) {
            return false;
        }
        CacheExtractFeatureStatusInfo other = (CacheExtractFeatureStatusInfo) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$expireTime = getExpireTime();
        Object other$expireTime = other.getExpireTime();
        if (this$expireTime == null) {
            if (other$expireTime != null) {
                return false;
            }
        } else if (!this$expireTime.equals(other$expireTime)) {
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
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        if (this$verifyFaceUrl == null) {
            if (other$verifyFaceUrl != null) {
                return false;
            }
        } else if (!this$verifyFaceUrl.equals(other$verifyFaceUrl)) {
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
        Object this$orgId = getOrgId();
        Object other$orgId = other.getOrgId();
        if (this$orgId == null) {
            if (other$orgId != null) {
                return false;
            }
        } else if (!this$orgId.equals(other$orgId)) {
            return false;
        }
        Object this$eigenvalue = getEigenvalue();
        Object other$eigenvalue = other.getEigenvalue();
        if (this$eigenvalue == null) {
            if (other$eigenvalue != null) {
                return false;
            }
        } else if (!this$eigenvalue.equals(other$eigenvalue)) {
            return false;
        }
        Object this$count = getCount();
        Object other$count = other.getCount();
        return this$count == null ? other$count == null : this$count.equals(other$count);
    }

    protected boolean canEqual(Object other) {
        return other instanceof CacheExtractFeatureStatusInfo;
    }

    public int hashCode() {
        Object $expireTime = getExpireTime();
        int result = (1 * 59) + ($expireTime == null ? 43 : $expireTime.hashCode());
        Object $status = getStatus();
        int result2 = (result * 59) + ($status == null ? 43 : $status.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result3 = (result2 * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $memberJobNum = getMemberJobNum();
        int result4 = (result3 * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $orgId = getOrgId();
        int result5 = (result4 * 59) + ($orgId == null ? 43 : $orgId.hashCode());
        Object $eigenvalue = getEigenvalue();
        int result6 = (result5 * 59) + ($eigenvalue == null ? 43 : $eigenvalue.hashCode());
        Object $count = getCount();
        return (result6 * 59) + ($count == null ? 43 : $count.hashCode());
    }

    public String toString() {
        return "CacheExtractFeatureStatusInfo(expireTime=" + getExpireTime() + ", status=" + getStatus() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", memberJobNum=" + getMemberJobNum() + ", orgId=" + getOrgId() + ", eigenvalue=" + getEigenvalue() + ", count=" + getCount() + ")";
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public String getEigenvalue() {
        return this.eigenvalue;
    }

    public Integer getCount() {
        return this.count;
    }
}

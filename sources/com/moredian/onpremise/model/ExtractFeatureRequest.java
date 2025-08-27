package com.moredian.onpremise.model;

import java.io.Serializable;
import java.util.Arrays;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/ExtractFeatureRequest.class */
public class ExtractFeatureRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.EXTRACT_FEATURE_REQUEST;
    private byte[] facePicture;
    private String memberJobNum;
    private Integer endFlag;
    private String pictureUrl;
    private String queryKey;
    private String jobNum;

    public void setFacePicture(byte[] facePicture) {
        this.facePicture = facePicture;
    }

    public void setMemberJobNum(String memberJobNum) {
        this.memberJobNum = memberJobNum;
    }

    public void setEndFlag(Integer endFlag) {
        this.endFlag = endFlag;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExtractFeatureRequest)) {
            return false;
        }
        ExtractFeatureRequest other = (ExtractFeatureRequest) o;
        if (!other.canEqual(this) || !Arrays.equals(getFacePicture(), other.getFacePicture())) {
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
        Object this$pictureUrl = getPictureUrl();
        Object other$pictureUrl = other.getPictureUrl();
        if (this$pictureUrl == null) {
            if (other$pictureUrl != null) {
                return false;
            }
        } else if (!this$pictureUrl.equals(other$pictureUrl)) {
            return false;
        }
        Object this$queryKey = getQueryKey();
        Object other$queryKey = other.getQueryKey();
        if (this$queryKey == null) {
            if (other$queryKey != null) {
                return false;
            }
        } else if (!this$queryKey.equals(other$queryKey)) {
            return false;
        }
        Object this$jobNum = getJobNum();
        Object other$jobNum = other.getJobNum();
        return this$jobNum == null ? other$jobNum == null : this$jobNum.equals(other$jobNum);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExtractFeatureRequest;
    }

    public int hashCode() {
        int result = (1 * 59) + Arrays.hashCode(getFacePicture());
        Object $memberJobNum = getMemberJobNum();
        int result2 = (result * 59) + ($memberJobNum == null ? 43 : $memberJobNum.hashCode());
        Object $endFlag = getEndFlag();
        int result3 = (result2 * 59) + ($endFlag == null ? 43 : $endFlag.hashCode());
        Object $pictureUrl = getPictureUrl();
        int result4 = (result3 * 59) + ($pictureUrl == null ? 43 : $pictureUrl.hashCode());
        Object $queryKey = getQueryKey();
        int result5 = (result4 * 59) + ($queryKey == null ? 43 : $queryKey.hashCode());
        Object $jobNum = getJobNum();
        return (result5 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
    }

    public String toString() {
        return "ExtractFeatureRequest(memberJobNum=" + getMemberJobNum() + ", endFlag=" + getEndFlag() + ", pictureUrl=" + getPictureUrl() + ", queryKey=" + getQueryKey() + ", jobNum=" + getJobNum() + ")";
    }

    public byte[] getFacePicture() {
        return this.facePicture;
    }

    public String getMemberJobNum() {
        return this.memberJobNum;
    }

    public Integer getEndFlag() {
        return this.endFlag;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public String getJobNum() {
        return this.jobNum;
    }
}

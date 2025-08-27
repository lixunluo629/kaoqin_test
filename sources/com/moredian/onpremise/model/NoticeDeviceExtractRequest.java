package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/NoticeDeviceExtractRequest.class */
public class NoticeDeviceExtractRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.NOTICE_DEVICE_EXTRACT_REQUEST;
    private static final long serialVersionUID = -8561434021159235156L;
    private Integer endFlag;
    private String queryKey;
    private String jobNum;
    private Integer expires;

    public void setEndFlag(Integer endFlag) {
        this.endFlag = endFlag;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NoticeDeviceExtractRequest)) {
            return false;
        }
        NoticeDeviceExtractRequest other = (NoticeDeviceExtractRequest) o;
        if (!other.canEqual(this)) {
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
        if (this$jobNum == null) {
            if (other$jobNum != null) {
                return false;
            }
        } else if (!this$jobNum.equals(other$jobNum)) {
            return false;
        }
        Object this$expires = getExpires();
        Object other$expires = other.getExpires();
        return this$expires == null ? other$expires == null : this$expires.equals(other$expires);
    }

    protected boolean canEqual(Object other) {
        return other instanceof NoticeDeviceExtractRequest;
    }

    public int hashCode() {
        Object $endFlag = getEndFlag();
        int result = (1 * 59) + ($endFlag == null ? 43 : $endFlag.hashCode());
        Object $queryKey = getQueryKey();
        int result2 = (result * 59) + ($queryKey == null ? 43 : $queryKey.hashCode());
        Object $jobNum = getJobNum();
        int result3 = (result2 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
        Object $expires = getExpires();
        return (result3 * 59) + ($expires == null ? 43 : $expires.hashCode());
    }

    public String toString() {
        return "NoticeDeviceExtractRequest(endFlag=" + getEndFlag() + ", queryKey=" + getQueryKey() + ", jobNum=" + getJobNum() + ", expires=" + getExpires() + ")";
    }

    public Integer getEndFlag() {
        return this.endFlag;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public String getJobNum() {
        return this.jobNum;
    }

    public Integer getExpires() {
        return this.expires;
    }
}

package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalExtractNoticeRequest.class */
public class TerminalExtractNoticeRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.TERMINAL_EXTRACT_NOTICE_REQUEST;
    private static final long serialVersionUID = -5719088827750880099L;
    private String eigenvalue;
    private String verifyFaceUrl;
    private String queryKey;
    private String jobNum;

    public void setEigenvalue(String eigenvalue) {
        this.eigenvalue = eigenvalue;
    }

    public void setVerifyFaceUrl(String verifyFaceUrl) {
        this.verifyFaceUrl = verifyFaceUrl;
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
        if (!(o instanceof TerminalExtractNoticeRequest)) {
            return false;
        }
        TerminalExtractNoticeRequest other = (TerminalExtractNoticeRequest) o;
        if (!other.canEqual(this)) {
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
        Object this$verifyFaceUrl = getVerifyFaceUrl();
        Object other$verifyFaceUrl = other.getVerifyFaceUrl();
        if (this$verifyFaceUrl == null) {
            if (other$verifyFaceUrl != null) {
                return false;
            }
        } else if (!this$verifyFaceUrl.equals(other$verifyFaceUrl)) {
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
        return other instanceof TerminalExtractNoticeRequest;
    }

    public int hashCode() {
        Object $eigenvalue = getEigenvalue();
        int result = (1 * 59) + ($eigenvalue == null ? 43 : $eigenvalue.hashCode());
        Object $verifyFaceUrl = getVerifyFaceUrl();
        int result2 = (result * 59) + ($verifyFaceUrl == null ? 43 : $verifyFaceUrl.hashCode());
        Object $queryKey = getQueryKey();
        int result3 = (result2 * 59) + ($queryKey == null ? 43 : $queryKey.hashCode());
        Object $jobNum = getJobNum();
        return (result3 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
    }

    public String toString() {
        return "TerminalExtractNoticeRequest(eigenvalue=" + getEigenvalue() + ", verifyFaceUrl=" + getVerifyFaceUrl() + ", queryKey=" + getQueryKey() + ", jobNum=" + getJobNum() + ")";
    }

    public String getEigenvalue() {
        return this.eigenvalue;
    }

    public String getVerifyFaceUrl() {
        return this.verifyFaceUrl;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public String getJobNum() {
        return this.jobNum;
    }
}

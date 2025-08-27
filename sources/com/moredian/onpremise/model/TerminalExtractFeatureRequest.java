package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalExtractFeatureRequest.class */
public class TerminalExtractFeatureRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.TERMINAL_EXTRACT_FEATURE_REQUEST;
    private String eigenvalue;
    private String queryKey;
    private String jobNum;

    public void setEigenvalue(String eigenvalue) {
        this.eigenvalue = eigenvalue;
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
        if (!(o instanceof TerminalExtractFeatureRequest)) {
            return false;
        }
        TerminalExtractFeatureRequest other = (TerminalExtractFeatureRequest) o;
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
        return other instanceof TerminalExtractFeatureRequest;
    }

    public int hashCode() {
        Object $eigenvalue = getEigenvalue();
        int result = (1 * 59) + ($eigenvalue == null ? 43 : $eigenvalue.hashCode());
        Object $queryKey = getQueryKey();
        int result2 = (result * 59) + ($queryKey == null ? 43 : $queryKey.hashCode());
        Object $jobNum = getJobNum();
        return (result2 * 59) + ($jobNum == null ? 43 : $jobNum.hashCode());
    }

    public String toString() {
        return "TerminalExtractFeatureRequest(eigenvalue=" + getEigenvalue() + ", queryKey=" + getQueryKey() + ", jobNum=" + getJobNum() + ")";
    }

    public String getEigenvalue() {
        return this.eigenvalue;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public String getJobNum() {
        return this.jobNum;
    }
}

package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/ExtractFeatureResponse.class */
public class ExtractFeatureResponse implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.EXTRACT_FEATURE_RESPONSE;
    private String eigenvalue;
    private Long memberId;

    public void setEigenvalue(String eigenvalue) {
        this.eigenvalue = eigenvalue;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExtractFeatureResponse)) {
            return false;
        }
        ExtractFeatureResponse other = (ExtractFeatureResponse) o;
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
        Object this$memberId = getMemberId();
        Object other$memberId = other.getMemberId();
        return this$memberId == null ? other$memberId == null : this$memberId.equals(other$memberId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExtractFeatureResponse;
    }

    public int hashCode() {
        Object $eigenvalue = getEigenvalue();
        int result = (1 * 59) + ($eigenvalue == null ? 43 : $eigenvalue.hashCode());
        Object $memberId = getMemberId();
        return (result * 59) + ($memberId == null ? 43 : $memberId.hashCode());
    }

    public String toString() {
        return "ExtractFeatureResponse(eigenvalue=" + getEigenvalue() + ", memberId=" + getMemberId() + ")";
    }

    public String getEigenvalue() {
        return this.eigenvalue;
    }

    public Long getMemberId() {
        return this.memberId;
    }
}

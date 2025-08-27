package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalExtractFeatureResponse.class */
public class TerminalExtractFeatureResponse implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.TERMINAL_EXTRACT_FEATURE_RESPONSE;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalExtractFeatureResponse)) {
            return false;
        }
        TerminalExtractFeatureResponse other = (TerminalExtractFeatureResponse) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalExtractFeatureResponse;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "TerminalExtractFeatureResponse()";
    }
}

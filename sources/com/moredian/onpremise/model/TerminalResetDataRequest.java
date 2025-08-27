package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalResetDataRequest.class */
public class TerminalResetDataRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.RESET_DATA_REQUEST;
    private static final long serialVersionUID = 7402356877500590274L;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalResetDataRequest)) {
            return false;
        }
        TerminalResetDataRequest other = (TerminalResetDataRequest) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalResetDataRequest;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "TerminalResetDataRequest()";
    }
}

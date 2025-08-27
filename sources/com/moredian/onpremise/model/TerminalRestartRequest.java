package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalRestartRequest.class */
public class TerminalRestartRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.RESTART_REQUEST;
    private static final long serialVersionUID = 1230558408686743729L;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalRestartRequest)) {
            return false;
        }
        TerminalRestartRequest other = (TerminalRestartRequest) o;
        return other.canEqual(this);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalRestartRequest;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "TerminalRestartRequest()";
    }
}

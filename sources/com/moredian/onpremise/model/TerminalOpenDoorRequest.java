package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalOpenDoorRequest.class */
public class TerminalOpenDoorRequest implements Serializable {
    public static final IOTModelType MODEL_TYPE = IOTModelType.TERMINAL_OPEN_DOOR_REQUEST;
    private static final long serialVersionUID = 6015455615427654382L;
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalOpenDoorRequest)) {
            return false;
        }
        TerminalOpenDoorRequest other = (TerminalOpenDoorRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$key = getKey();
        Object other$key = other.getKey();
        return this$key == null ? other$key == null : this$key.equals(other$key);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalOpenDoorRequest;
    }

    public int hashCode() {
        Object $key = getKey();
        int result = (1 * 59) + ($key == null ? 43 : $key.hashCode());
        return result;
    }

    public String toString() {
        return "TerminalOpenDoorRequest(key=" + getKey() + ")";
    }

    public TerminalOpenDoorRequest() {
    }

    public TerminalOpenDoorRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}

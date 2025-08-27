package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/TerminalPushMsgRequest.class */
public class TerminalPushMsgRequest implements Serializable {
    private static final long serialVersionUID = 1333836395161369467L;
    public static final IOTModelType MODEL_TYPE = IOTModelType.PUSH_MSG_REQUEST;
    private Integer type;
    private String msg;
    private String uuid;

    public void setType(Integer type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalPushMsgRequest)) {
            return false;
        }
        TerminalPushMsgRequest other = (TerminalPushMsgRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type == null) {
            if (other$type != null) {
                return false;
            }
        } else if (!this$type.equals(other$type)) {
            return false;
        }
        Object this$msg = getMsg();
        Object other$msg = other.getMsg();
        if (this$msg == null) {
            if (other$msg != null) {
                return false;
            }
        } else if (!this$msg.equals(other$msg)) {
            return false;
        }
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        return this$uuid == null ? other$uuid == null : this$uuid.equals(other$uuid);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TerminalPushMsgRequest;
    }

    public int hashCode() {
        Object $type = getType();
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $msg = getMsg();
        int result2 = (result * 59) + ($msg == null ? 43 : $msg.hashCode());
        Object $uuid = getUuid();
        return (result2 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
    }

    public String toString() {
        return "TerminalPushMsgRequest(type=" + getType() + ", msg=" + getMsg() + ", uuid=" + getUuid() + ")";
    }

    public Integer getType() {
        return this.type;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getUuid() {
        return this.uuid;
    }

    public TerminalPushMsgRequest(Integer type, String msg, String uuid) {
        this.type = type;
        this.msg = msg;
        this.uuid = uuid;
    }
}

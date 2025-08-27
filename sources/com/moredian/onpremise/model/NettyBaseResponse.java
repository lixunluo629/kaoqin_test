package com.moredian.onpremise.model;

import java.io.Serializable;

/* loaded from: onpremise-event-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/model/NettyBaseResponse.class */
public class NettyBaseResponse implements Serializable {
    private Integer code;
    private String errorMessage;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NettyBaseResponse)) {
            return false;
        }
        NettyBaseResponse other = (NettyBaseResponse) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$code = getCode();
        Object other$code = other.getCode();
        if (this$code == null) {
            if (other$code != null) {
                return false;
            }
        } else if (!this$code.equals(other$code)) {
            return false;
        }
        Object this$errorMessage = getErrorMessage();
        Object other$errorMessage = other.getErrorMessage();
        return this$errorMessage == null ? other$errorMessage == null : this$errorMessage.equals(other$errorMessage);
    }

    protected boolean canEqual(Object other) {
        return other instanceof NettyBaseResponse;
    }

    public int hashCode() {
        Object $code = getCode();
        int result = (1 * 59) + ($code == null ? 43 : $code.hashCode());
        Object $errorMessage = getErrorMessage();
        return (result * 59) + ($errorMessage == null ? 43 : $errorMessage.hashCode());
    }

    public String toString() {
        return "NettyBaseResponse(code=" + getCode() + ", errorMessage=" + getErrorMessage() + ")";
    }

    public Integer getCode() {
        return this.code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}

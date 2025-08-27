package com.moredian.onpremise.core.exception;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/exception/BizException.class */
public class BizException extends RuntimeException {
    private String errorCode;

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BizException)) {
            return false;
        }
        BizException other = (BizException) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$errorCode = getErrorCode();
        Object other$errorCode = other.getErrorCode();
        return this$errorCode == null ? other$errorCode == null : this$errorCode.equals(other$errorCode);
    }

    protected boolean canEqual(Object other) {
        return other instanceof BizException;
    }

    public int hashCode() {
        Object $errorCode = getErrorCode();
        int result = (1 * 59) + ($errorCode == null ? 43 : $errorCode.hashCode());
        return result;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "BizException(errorCode=" + getErrorCode() + ")";
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public BizException() {
    }

    public BizException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

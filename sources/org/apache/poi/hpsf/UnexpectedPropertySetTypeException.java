package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/UnexpectedPropertySetTypeException.class */
public class UnexpectedPropertySetTypeException extends HPSFException {
    public UnexpectedPropertySetTypeException() {
    }

    public UnexpectedPropertySetTypeException(String msg) {
        super(msg);
    }

    public UnexpectedPropertySetTypeException(Throwable reason) {
        super(reason);
    }

    public UnexpectedPropertySetTypeException(String msg, Throwable reason) {
        super(msg, reason);
    }
}

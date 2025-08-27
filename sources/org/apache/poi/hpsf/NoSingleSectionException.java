package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/NoSingleSectionException.class */
public class NoSingleSectionException extends HPSFRuntimeException {
    public NoSingleSectionException() {
    }

    public NoSingleSectionException(String msg) {
        super(msg);
    }

    public NoSingleSectionException(Throwable reason) {
        super(reason);
    }

    public NoSingleSectionException(String msg, Throwable reason) {
        super(msg, reason);
    }
}

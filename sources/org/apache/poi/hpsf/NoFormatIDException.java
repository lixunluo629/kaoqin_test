package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/NoFormatIDException.class */
public class NoFormatIDException extends HPSFRuntimeException {
    public NoFormatIDException() {
    }

    public NoFormatIDException(String msg) {
        super(msg);
    }

    public NoFormatIDException(Throwable reason) {
        super(reason);
    }

    public NoFormatIDException(String msg, Throwable reason) {
        super(msg, reason);
    }
}

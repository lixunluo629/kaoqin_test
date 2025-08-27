package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/MissingSectionException.class */
public class MissingSectionException extends HPSFRuntimeException {
    public MissingSectionException() {
    }

    public MissingSectionException(String msg) {
        super(msg);
    }

    public MissingSectionException(Throwable reason) {
        super(reason);
    }

    public MissingSectionException(String msg, Throwable reason) {
        super(msg, reason);
    }
}

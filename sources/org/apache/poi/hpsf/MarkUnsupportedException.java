package org.apache.poi.hpsf;

/* loaded from: poi-3.17.jar:org/apache/poi/hpsf/MarkUnsupportedException.class */
public class MarkUnsupportedException extends HPSFException {
    public MarkUnsupportedException() {
    }

    public MarkUnsupportedException(String msg) {
        super(msg);
    }

    public MarkUnsupportedException(Throwable reason) {
        super(reason);
    }

    public MarkUnsupportedException(String msg, Throwable reason) {
        super(msg, reason);
    }
}

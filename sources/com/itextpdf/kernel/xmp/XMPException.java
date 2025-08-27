package com.itextpdf.kernel.xmp;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/XMPException.class */
public class XMPException extends Exception {
    private int errorCode;

    public XMPException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public XMPException(String message, int errorCode, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}

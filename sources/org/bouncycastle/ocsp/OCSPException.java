package org.bouncycastle.ocsp;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/ocsp/OCSPException.class */
public class OCSPException extends Exception {
    Exception e;

    public OCSPException(String str) {
        super(str);
    }

    public OCSPException(String str, Exception exc) {
        super(str);
        this.e = exc;
    }

    public Exception getUnderlyingException() {
        return this.e;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.e;
    }
}

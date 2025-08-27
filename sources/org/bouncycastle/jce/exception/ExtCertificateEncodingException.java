package org.bouncycastle.jce.exception;

import java.security.cert.CertificateEncodingException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/exception/ExtCertificateEncodingException.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/exception/ExtCertificateEncodingException.class */
public class ExtCertificateEncodingException extends CertificateEncodingException implements ExtException {
    private Throwable cause;

    public ExtCertificateEncodingException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable, org.bouncycastle.jce.exception.ExtException
    public Throwable getCause() {
        return this.cause;
    }
}

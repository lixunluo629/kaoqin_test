package org.bouncycastle.x509;

import java.security.cert.CertificateEncodingException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/x509/ExtCertificateEncodingException.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/x509/ExtCertificateEncodingException.class */
class ExtCertificateEncodingException extends CertificateEncodingException {
    Throwable cause;

    ExtCertificateEncodingException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}

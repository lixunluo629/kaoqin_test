package org.bouncycastle.pkix.jcajce;

import java.security.cert.CertPathValidatorException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/pkix/jcajce/CRLNotFoundException.class */
class CRLNotFoundException extends CertPathValidatorException {
    CRLNotFoundException(String str) {
        super(str);
    }

    public CRLNotFoundException(String str, Throwable th) {
        super(str, th);
    }
}

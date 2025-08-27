package org.bouncycastle.cert.jcajce;

import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/jcajce/DefaultCertHelper.class */
class DefaultCertHelper extends CertHelper {
    DefaultCertHelper() {
    }

    @Override // org.bouncycastle.cert.jcajce.CertHelper
    protected CertificateFactory createCertificateFactory(String str) throws CertificateException {
        return CertificateFactory.getInstance(str);
    }
}

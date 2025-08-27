package org.bouncycastle.crypto.tls;

import org.bouncycastle.asn1.x509.X509CertificateStructure;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/CertificateVerifyer.class */
public interface CertificateVerifyer {
    boolean isValid(X509CertificateStructure[] x509CertificateStructureArr);
}

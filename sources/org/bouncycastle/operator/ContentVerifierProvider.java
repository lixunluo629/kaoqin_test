package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/operator/ContentVerifierProvider.class */
public interface ContentVerifierProvider {
    boolean hasAssociatedCertificate();

    X509CertificateHolder getAssociatedCertificate();

    ContentVerifier get(AlgorithmIdentifier algorithmIdentifier) throws OperatorCreationException;
}

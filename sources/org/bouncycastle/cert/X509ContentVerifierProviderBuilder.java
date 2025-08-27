package org.bouncycastle.cert;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/X509ContentVerifierProviderBuilder.class */
public interface X509ContentVerifierProviderBuilder {
    ContentVerifierProvider build(SubjectPublicKeyInfo subjectPublicKeyInfo) throws OperatorCreationException;

    ContentVerifierProvider build(X509CertificateHolder x509CertificateHolder) throws OperatorCreationException;
}

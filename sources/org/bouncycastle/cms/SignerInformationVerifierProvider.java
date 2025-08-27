package org.bouncycastle.cms;

import org.bouncycastle.operator.OperatorCreationException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/SignerInformationVerifierProvider.class */
public interface SignerInformationVerifierProvider {
    SignerInformationVerifier get(SignerId signerId) throws OperatorCreationException;
}

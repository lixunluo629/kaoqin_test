package org.bouncycastle.operator.bc;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.signers.DSADigestSigner;
import org.bouncycastle.crypto.signers.DSASigner;
import org.bouncycastle.operator.OperatorCreationException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/operator/bc/BcDSAContentSignerBuilder.class */
public class BcDSAContentSignerBuilder extends BcContentSignerBuilder {
    public BcDSAContentSignerBuilder(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2) {
        super(algorithmIdentifier, algorithmIdentifier2);
    }

    @Override // org.bouncycastle.operator.bc.BcContentSignerBuilder
    protected Signer createSigner(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2) throws OperatorCreationException {
        return new DSADigestSigner(new DSASigner(), this.digestProvider.get(algorithmIdentifier2));
    }
}

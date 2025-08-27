package org.bouncycastle.cms.bc;

import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.KeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.bc.BcAsymmetricKeyWrapper;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/bc/BcKeyTransRecipientInfoGenerator.class */
public abstract class BcKeyTransRecipientInfoGenerator extends KeyTransRecipientInfoGenerator {
    public BcKeyTransRecipientInfoGenerator(X509CertificateHolder x509CertificateHolder, BcAsymmetricKeyWrapper bcAsymmetricKeyWrapper) {
        super(new IssuerAndSerialNumber(x509CertificateHolder.toASN1Structure()), bcAsymmetricKeyWrapper);
    }

    public BcKeyTransRecipientInfoGenerator(byte[] bArr, BcAsymmetricKeyWrapper bcAsymmetricKeyWrapper) {
        super(bArr, bcAsymmetricKeyWrapper);
    }
}

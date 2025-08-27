package org.bouncycastle.pkix;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientId;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/pkix/PKIXIdentity.class */
public class PKIXIdentity {
    private final PrivateKeyInfo privateKeyInfo;
    private final X509CertificateHolder[] certificateHolders;

    public PKIXIdentity(PrivateKeyInfo privateKeyInfo, X509CertificateHolder[] x509CertificateHolderArr) {
        this.privateKeyInfo = privateKeyInfo;
        this.certificateHolders = new X509CertificateHolder[x509CertificateHolderArr.length];
        System.arraycopy(x509CertificateHolderArr, 0, this.certificateHolders, 0, x509CertificateHolderArr.length);
    }

    public PrivateKeyInfo getPrivateKeyInfo() {
        return this.privateKeyInfo;
    }

    public X509CertificateHolder getCertificate() {
        return this.certificateHolders[0];
    }

    public RecipientId getRecipientId() {
        return new KeyTransRecipientId(this.certificateHolders[0].getIssuer(), this.certificateHolders[0].getSerialNumber(), getSubjectKeyIdentifier());
    }

    private byte[] getSubjectKeyIdentifier() {
        SubjectKeyIdentifier subjectKeyIdentifierFromExtensions = SubjectKeyIdentifier.fromExtensions(this.certificateHolders[0].getExtensions());
        if (subjectKeyIdentifierFromExtensions == null) {
            return null;
        }
        return subjectKeyIdentifierFromExtensions.getKeyIdentifier();
    }
}

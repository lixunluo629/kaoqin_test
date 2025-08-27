package org.bouncycastle.cert.crmf;

import java.io.IOException;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.crmf.CertRequest;
import org.bouncycastle.asn1.crmf.PKMACValue;
import org.bouncycastle.asn1.crmf.POPOSigningKey;
import org.bouncycastle.asn1.crmf.POPOSigningKeyInput;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.operator.ContentSigner;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/crmf/ProofOfPossessionSigningKeyBuilder.class */
public class ProofOfPossessionSigningKeyBuilder {
    private CertRequest certRequest;
    private SubjectPublicKeyInfo pubKeyInfo;
    private GeneralName name;
    private PKMACValue publicKeyMAC;

    public ProofOfPossessionSigningKeyBuilder(CertRequest certRequest) {
        this.certRequest = certRequest;
    }

    public ProofOfPossessionSigningKeyBuilder(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.pubKeyInfo = subjectPublicKeyInfo;
    }

    public ProofOfPossessionSigningKeyBuilder setSender(GeneralName generalName) {
        this.name = generalName;
        return this;
    }

    public ProofOfPossessionSigningKeyBuilder setPublicKeyMac(PKMACValueGenerator pKMACValueGenerator, char[] cArr) throws CRMFException {
        this.publicKeyMAC = pKMACValueGenerator.generate(cArr, this.pubKeyInfo);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.crmf.CertRequest] */
    /* JADX WARN: Type inference failed for: r10v0, types: [org.bouncycastle.asn1.ASN1Object] */
    /* JADX WARN: Type inference failed for: r10v1, types: [org.bouncycastle.asn1.ASN1Object] */
    public POPOSigningKey build(ContentSigner contentSigner) throws IOException {
        POPOSigningKeyInput pOPOSigningKeyInput;
        if (this.name != null && this.publicKeyMAC != null) {
            throw new IllegalStateException("name and publicKeyMAC cannot both be set.");
        }
        if (this.certRequest != null) {
            pOPOSigningKeyInput = null;
            CRMFUtil.derEncodeToStream(this.certRequest, contentSigner.getOutputStream());
        } else if (this.name != null) {
            ?? pOPOSigningKeyInput2 = new POPOSigningKeyInput(this.name, this.pubKeyInfo);
            CRMFUtil.derEncodeToStream(pOPOSigningKeyInput2, contentSigner.getOutputStream());
            pOPOSigningKeyInput = pOPOSigningKeyInput2;
        } else {
            ?? pOPOSigningKeyInput3 = new POPOSigningKeyInput(this.publicKeyMAC, this.pubKeyInfo);
            CRMFUtil.derEncodeToStream(pOPOSigningKeyInput3, contentSigner.getOutputStream());
            pOPOSigningKeyInput = pOPOSigningKeyInput3;
        }
        return new POPOSigningKey(pOPOSigningKeyInput, contentSigner.getAlgorithmIdentifier(), new DERBitString(contentSigner.getSignature()));
    }
}

package org.bouncycastle.cert.crmf;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.crmf.AttributeTypeAndValue;
import org.bouncycastle.asn1.crmf.CRMFObjectIdentifiers;
import org.bouncycastle.asn1.crmf.CertReqMsg;
import org.bouncycastle.asn1.crmf.CertTemplate;
import org.bouncycastle.asn1.crmf.Controls;
import org.bouncycastle.asn1.crmf.PKIArchiveOptions;
import org.bouncycastle.asn1.crmf.POPOSigningKey;
import org.bouncycastle.asn1.crmf.ProofOfPossession;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.operator.ContentVerifier;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Encodable;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/crmf/CertificateRequestMessage.class */
public class CertificateRequestMessage implements Encodable {
    public static final int popRaVerified = 0;
    public static final int popSigningKey = 1;
    public static final int popKeyEncipherment = 2;
    public static final int popKeyAgreement = 3;
    private final CertReqMsg certReqMsg;
    private final Controls controls;

    private static CertReqMsg parseBytes(byte[] bArr) throws IOException {
        try {
            return CertReqMsg.getInstance(ASN1Primitive.fromByteArray(bArr));
        } catch (ClassCastException e) {
            throw new CertIOException("malformed data: " + e.getMessage(), e);
        } catch (IllegalArgumentException e2) {
            throw new CertIOException("malformed data: " + e2.getMessage(), e2);
        }
    }

    public CertificateRequestMessage(byte[] bArr) throws IOException {
        this(parseBytes(bArr));
    }

    public CertificateRequestMessage(CertReqMsg certReqMsg) {
        this.certReqMsg = certReqMsg;
        this.controls = certReqMsg.getCertReq().getControls();
    }

    public CertReqMsg toASN1Structure() {
        return this.certReqMsg;
    }

    public CertTemplate getCertTemplate() {
        return this.certReqMsg.getCertReq().getCertTemplate();
    }

    public boolean hasControls() {
        return this.controls != null;
    }

    public boolean hasControl(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return findControl(aSN1ObjectIdentifier) != null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    /* JADX WARN: Type inference failed for: r1v3, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    public Control getControl(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        AttributeTypeAndValue attributeTypeAndValueFindControl = findControl(aSN1ObjectIdentifier);
        if (attributeTypeAndValueFindControl == null) {
            return null;
        }
        if (attributeTypeAndValueFindControl.getType().equals((ASN1Primitive) CRMFObjectIdentifiers.id_regCtrl_pkiArchiveOptions)) {
            return new PKIArchiveControl(PKIArchiveOptions.getInstance(attributeTypeAndValueFindControl.getValue()));
        }
        if (attributeTypeAndValueFindControl.getType().equals((ASN1Primitive) CRMFObjectIdentifiers.id_regCtrl_regToken)) {
            return new RegTokenControl(DERUTF8String.getInstance(attributeTypeAndValueFindControl.getValue()));
        }
        if (attributeTypeAndValueFindControl.getType().equals((ASN1Primitive) CRMFObjectIdentifiers.id_regCtrl_authenticator)) {
            return new AuthenticatorControl(DERUTF8String.getInstance(attributeTypeAndValueFindControl.getValue()));
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private AttributeTypeAndValue findControl(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        if (this.controls == null) {
            return null;
        }
        AttributeTypeAndValue[] attributeTypeAndValueArray = this.controls.toAttributeTypeAndValueArray();
        AttributeTypeAndValue attributeTypeAndValue = null;
        int i = 0;
        while (true) {
            if (i == attributeTypeAndValueArray.length) {
                break;
            }
            if (attributeTypeAndValueArray[i].getType().equals((ASN1Primitive) aSN1ObjectIdentifier)) {
                attributeTypeAndValue = attributeTypeAndValueArray[i];
                break;
            }
            i++;
        }
        return attributeTypeAndValue;
    }

    public boolean hasProofOfPossession() {
        return this.certReqMsg.getPopo() != null;
    }

    public int getProofOfPossessionType() {
        return this.certReqMsg.getPopo().getType();
    }

    public boolean hasSigningKeyProofOfPossessionWithPKMAC() {
        ProofOfPossession popo = this.certReqMsg.getPopo();
        return popo.getType() == 1 && POPOSigningKey.getInstance(popo.getObject()).getPoposkInput().getPublicKeyMAC() != null;
    }

    public boolean isValidSigningKeyPOP(ContentVerifierProvider contentVerifierProvider) throws CRMFException, IllegalStateException {
        ProofOfPossession popo = this.certReqMsg.getPopo();
        if (popo.getType() != 1) {
            throw new IllegalStateException("not Signing Key type of proof of possession");
        }
        POPOSigningKey pOPOSigningKey = POPOSigningKey.getInstance(popo.getObject());
        if (pOPOSigningKey.getPoposkInput() == null || pOPOSigningKey.getPoposkInput().getPublicKeyMAC() == null) {
            return verifySignature(contentVerifierProvider, pOPOSigningKey);
        }
        throw new IllegalStateException("verification requires password check");
    }

    public boolean isValidSigningKeyPOP(ContentVerifierProvider contentVerifierProvider, PKMACBuilder pKMACBuilder, char[] cArr) throws CRMFException, IllegalStateException {
        ProofOfPossession popo = this.certReqMsg.getPopo();
        if (popo.getType() != 1) {
            throw new IllegalStateException("not Signing Key type of proof of possession");
        }
        POPOSigningKey pOPOSigningKey = POPOSigningKey.getInstance(popo.getObject());
        if (pOPOSigningKey.getPoposkInput() == null || pOPOSigningKey.getPoposkInput().getSender() != null) {
            throw new IllegalStateException("no PKMAC present in proof of possession");
        }
        if (new PKMACValueVerifier(pKMACBuilder).isValid(pOPOSigningKey.getPoposkInput().getPublicKeyMAC(), cArr, getCertTemplate().getPublicKey())) {
            return verifySignature(contentVerifierProvider, pOPOSigningKey);
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v11, types: [org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.crmf.POPOSigningKeyInput] */
    /* JADX WARN: Type inference failed for: r0v7, types: [org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.crmf.CertRequest] */
    private boolean verifySignature(ContentVerifierProvider contentVerifierProvider, POPOSigningKey pOPOSigningKey) throws CRMFException, IOException {
        try {
            ContentVerifier contentVerifier = contentVerifierProvider.get(pOPOSigningKey.getAlgorithmIdentifier());
            if (pOPOSigningKey.getPoposkInput() != null) {
                CRMFUtil.derEncodeToStream(pOPOSigningKey.getPoposkInput(), contentVerifier.getOutputStream());
            } else {
                CRMFUtil.derEncodeToStream(this.certReqMsg.getCertReq(), contentVerifier.getOutputStream());
            }
            return contentVerifier.verify(pOPOSigningKey.getSignature().getOctets());
        } catch (OperatorCreationException e) {
            throw new CRMFException("unable to create verifier: " + e.getMessage(), e);
        }
    }

    @Override // org.bouncycastle.util.Encodable
    public byte[] getEncoded() throws IOException {
        return this.certReqMsg.getEncoded();
    }
}

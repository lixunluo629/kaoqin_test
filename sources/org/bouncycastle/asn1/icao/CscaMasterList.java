package org.bouncycastle.asn1.icao;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.X509CertificateStructure;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/icao/CscaMasterList.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/icao/CscaMasterList.class */
public class CscaMasterList extends ASN1Encodable {
    private DERInteger version;
    private X509CertificateStructure[] certList;

    public static CscaMasterList getInstance(Object obj) {
        if (obj instanceof CscaMasterList) {
            return (CscaMasterList) obj;
        }
        if (obj != null) {
            return new CscaMasterList(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    private CscaMasterList(ASN1Sequence aSN1Sequence) {
        this.version = new DERInteger(0);
        if (aSN1Sequence == null || aSN1Sequence.size() == 0) {
            throw new IllegalArgumentException("null or empty sequence passed.");
        }
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("Incorrect sequence size: " + aSN1Sequence.size());
        }
        this.version = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
        ASN1Set aSN1Set = ASN1Set.getInstance(aSN1Sequence.getObjectAt(1));
        this.certList = new X509CertificateStructure[aSN1Set.size()];
        for (int i = 0; i < this.certList.length; i++) {
            this.certList[i] = X509CertificateStructure.getInstance(aSN1Set.getObjectAt(i));
        }
    }

    public CscaMasterList(X509CertificateStructure[] x509CertificateStructureArr) {
        this.version = new DERInteger(0);
        this.certList = copyCertList(x509CertificateStructureArr);
    }

    public int getVersion() {
        return this.version.getValue().intValue();
    }

    public X509CertificateStructure[] getCertStructs() {
        return copyCertList(this.certList);
    }

    private X509CertificateStructure[] copyCertList(X509CertificateStructure[] x509CertificateStructureArr) {
        X509CertificateStructure[] x509CertificateStructureArr2 = new X509CertificateStructure[x509CertificateStructureArr.length];
        for (int i = 0; i != x509CertificateStructureArr2.length; i++) {
            x509CertificateStructureArr2[i] = x509CertificateStructureArr[i];
        }
        return x509CertificateStructureArr2;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i = 0; i < this.certList.length; i++) {
            aSN1EncodableVector2.add(this.certList[i]);
        }
        aSN1EncodableVector.add(new DERSet(aSN1EncodableVector2));
        return new DERSequence(aSN1EncodableVector);
    }
}

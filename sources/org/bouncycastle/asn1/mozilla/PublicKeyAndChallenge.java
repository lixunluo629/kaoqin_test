package org.bouncycastle.asn1.mozilla;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/mozilla/PublicKeyAndChallenge.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/mozilla/PublicKeyAndChallenge.class */
public class PublicKeyAndChallenge extends ASN1Encodable {
    private ASN1Sequence pkacSeq;
    private SubjectPublicKeyInfo spki;
    private DERIA5String challenge;

    public static PublicKeyAndChallenge getInstance(Object obj) {
        if (obj instanceof PublicKeyAndChallenge) {
            return (PublicKeyAndChallenge) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new PublicKeyAndChallenge((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unkown object in factory: " + obj.getClass().getName());
    }

    public PublicKeyAndChallenge(ASN1Sequence aSN1Sequence) {
        this.pkacSeq = aSN1Sequence;
        this.spki = SubjectPublicKeyInfo.getInstance(aSN1Sequence.getObjectAt(0));
        this.challenge = DERIA5String.getInstance(aSN1Sequence.getObjectAt(1));
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.pkacSeq;
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.spki;
    }

    public DERIA5String getChallenge() {
        return this.challenge;
    }
}

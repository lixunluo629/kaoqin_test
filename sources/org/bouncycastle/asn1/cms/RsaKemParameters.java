package org.bouncycastle.asn1.cms;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cms/RsaKemParameters.class */
public class RsaKemParameters extends ASN1Object {
    private final AlgorithmIdentifier keyDerivationFunction;
    private final BigInteger keyLength;

    private RsaKemParameters(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("ASN.1 SEQUENCE should be of length 2");
        }
        this.keyDerivationFunction = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        this.keyLength = ASN1Integer.getInstance((Object) aSN1Sequence.getObjectAt(1)).getValue();
    }

    public static RsaKemParameters getInstance(Object obj) {
        if (obj instanceof RsaKemParameters) {
            return (RsaKemParameters) obj;
        }
        if (obj != null) {
            return new RsaKemParameters(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public RsaKemParameters(AlgorithmIdentifier algorithmIdentifier, int i) {
        this.keyDerivationFunction = algorithmIdentifier;
        this.keyLength = BigInteger.valueOf(i);
    }

    public AlgorithmIdentifier getKeyDerivationFunction() {
        return this.keyDerivationFunction;
    }

    public BigInteger getKeyLength() {
        return this.keyLength;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
        aSN1EncodableVector.add((ASN1Encodable) this.keyDerivationFunction);
        aSN1EncodableVector.add((ASN1Encodable) new ASN1Integer(this.keyLength));
        return new DERSequence(aSN1EncodableVector);
    }
}

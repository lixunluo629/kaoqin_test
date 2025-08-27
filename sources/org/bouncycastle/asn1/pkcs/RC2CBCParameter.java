package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/pkcs/RC2CBCParameter.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/pkcs/RC2CBCParameter.class */
public class RC2CBCParameter extends ASN1Encodable {
    DERInteger version;
    ASN1OctetString iv;

    public static RC2CBCParameter getInstance(Object obj) {
        if (obj instanceof ASN1Sequence) {
            return new RC2CBCParameter((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in RC2CBCParameter factory");
    }

    public RC2CBCParameter(byte[] bArr) {
        this.version = null;
        this.iv = new DEROctetString(bArr);
    }

    public RC2CBCParameter(int i, byte[] bArr) {
        this.version = new DERInteger(i);
        this.iv = new DEROctetString(bArr);
    }

    public RC2CBCParameter(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() == 1) {
            this.version = null;
            this.iv = (ASN1OctetString) aSN1Sequence.getObjectAt(0);
        } else {
            this.version = (DERInteger) aSN1Sequence.getObjectAt(0);
            this.iv = (ASN1OctetString) aSN1Sequence.getObjectAt(1);
        }
    }

    public BigInteger getRC2ParameterVersion() {
        if (this.version == null) {
            return null;
        }
        return this.version.getValue();
    }

    public byte[] getIV() {
        return this.iv.getOctets();
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != null) {
            aSN1EncodableVector.add(this.version);
        }
        aSN1EncodableVector.add(this.iv);
        return new DERSequence(aSN1EncodableVector);
    }
}

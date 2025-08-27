package org.bouncycastle.asn1.misc;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/misc/IDEACBCPar.class */
public class IDEACBCPar extends ASN1Object {
    ASN1OctetString iv;

    public static IDEACBCPar getInstance(Object obj) {
        if (obj instanceof IDEACBCPar) {
            return (IDEACBCPar) obj;
        }
        if (obj != null) {
            return new IDEACBCPar(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public IDEACBCPar(byte[] bArr) {
        this.iv = new DEROctetString(bArr);
    }

    private IDEACBCPar(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() == 1) {
            this.iv = (ASN1OctetString) aSN1Sequence.getObjectAt(0);
        } else {
            this.iv = null;
        }
    }

    public byte[] getIV() {
        if (this.iv != null) {
            return Arrays.clone(this.iv.getOctets());
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(1);
        if (this.iv != null) {
            aSN1EncodableVector.add((ASN1Encodable) this.iv);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

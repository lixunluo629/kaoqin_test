package org.bouncycastle.asn1.eac;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/eac/UnsignedInteger.class */
public class UnsignedInteger extends ASN1Object {
    private int tagNo;
    private BigInteger value;

    public UnsignedInteger(int i, BigInteger bigInteger) {
        this.tagNo = i;
        this.value = bigInteger;
    }

    private UnsignedInteger(ASN1TaggedObject aSN1TaggedObject) {
        this.tagNo = aSN1TaggedObject.getTagNo();
        this.value = new BigInteger(1, ASN1OctetString.getInstance(aSN1TaggedObject, false).getOctets());
    }

    public static UnsignedInteger getInstance(Object obj) {
        if (obj instanceof UnsignedInteger) {
            return (UnsignedInteger) obj;
        }
        if (obj != null) {
            return new UnsignedInteger(ASN1TaggedObject.getInstance(obj));
        }
        return null;
    }

    private byte[] convertValue() {
        byte[] byteArray = this.value.toByteArray();
        if (byteArray[0] != 0) {
            return byteArray;
        }
        byte[] bArr = new byte[byteArray.length - 1];
        System.arraycopy(byteArray, 1, bArr, 0, bArr.length);
        return bArr;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public BigInteger getValue() {
        return this.value;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERTaggedObject] */
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, (ASN1Encodable) new DEROctetString(convertValue()));
    }
}

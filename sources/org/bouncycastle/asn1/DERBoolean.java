package org.bouncycastle.asn1;

import java.io.IOException;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/DERBoolean.class */
public class DERBoolean extends ASN1Object {
    byte value;
    public static final DERBoolean FALSE = new DERBoolean(false);
    public static final DERBoolean TRUE = new DERBoolean(true);

    public static DERBoolean getInstance(Object obj) {
        if (obj == null || (obj instanceof DERBoolean)) {
            return (DERBoolean) obj;
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERBoolean getInstance(boolean z) {
        return z ? TRUE : FALSE;
    }

    public static DERBoolean getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        DERObject object = aSN1TaggedObject.getObject();
        return (z || (object instanceof DERBoolean)) ? getInstance(object) : new DERBoolean(((ASN1OctetString) object).getOctets());
    }

    public DERBoolean(byte[] bArr) {
        if (bArr.length != 1) {
            throw new IllegalArgumentException("byte value should have 1 byte in it");
        }
        this.value = bArr[0];
    }

    public DERBoolean(boolean z) {
        this.value = z ? (byte) -1 : (byte) 0;
    }

    public boolean isTrue() {
        return this.value != 0;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        dEROutputStream.writeEncoded(1, new byte[]{this.value});
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    protected boolean asn1Equals(DERObject dERObject) {
        return dERObject != null && (dERObject instanceof DERBoolean) && this.value == ((DERBoolean) dERObject).value;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        return this.value;
    }

    public String toString() {
        return this.value != 0 ? "TRUE" : "FALSE";
    }
}

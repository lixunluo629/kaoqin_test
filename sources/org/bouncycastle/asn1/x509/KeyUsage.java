package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERBitString;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/KeyUsage.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/KeyUsage.class */
public class KeyUsage extends DERBitString {
    public static final int digitalSignature = 128;
    public static final int nonRepudiation = 64;
    public static final int keyEncipherment = 32;
    public static final int dataEncipherment = 16;
    public static final int keyAgreement = 8;
    public static final int keyCertSign = 4;
    public static final int cRLSign = 2;
    public static final int encipherOnly = 1;
    public static final int decipherOnly = 32768;

    public static DERBitString getInstance(Object obj) {
        return obj instanceof KeyUsage ? (KeyUsage) obj : obj instanceof X509Extension ? new KeyUsage(DERBitString.getInstance(X509Extension.convertValueToObject((X509Extension) obj))) : new KeyUsage(DERBitString.getInstance(obj));
    }

    public KeyUsage(int i) {
        super(getBytes(i), getPadBits(i));
    }

    public KeyUsage(DERBitString dERBitString) {
        super(dERBitString.getBytes(), dERBitString.getPadBits());
    }

    @Override // org.bouncycastle.asn1.DERBitString
    public String toString() {
        return this.data.length == 1 ? "KeyUsage: 0x" + Integer.toHexString(this.data[0] & 255) : "KeyUsage: 0x" + Integer.toHexString(((this.data[1] & 255) << 8) | (this.data[0] & 255));
    }
}

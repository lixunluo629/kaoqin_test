package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Encodable.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1Encodable.class */
public abstract class ASN1Encodable implements DEREncodable {
    public static final String DER = "DER";
    public static final String BER = "BER";

    public byte[] getEncoded() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ASN1OutputStream(byteArrayOutputStream).writeObject(this);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] getEncoded(String str) throws IOException {
        if (!str.equals("DER")) {
            return getEncoded();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new DEROutputStream(byteArrayOutputStream).writeObject(this);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] getDEREncoded() {
        try {
            return getEncoded("DER");
        } catch (IOException e) {
            return null;
        }
    }

    public int hashCode() {
        return toASN1Object().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DEREncodable) {
            return toASN1Object().equals(((DEREncodable) obj).getDERObject());
        }
        return false;
    }

    @Override // org.bouncycastle.asn1.DEREncodable
    public DERObject getDERObject() {
        return toASN1Object();
    }

    public abstract DERObject toASN1Object();
}

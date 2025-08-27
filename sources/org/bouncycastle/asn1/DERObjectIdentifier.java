package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DERObjectIdentifier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/DERObjectIdentifier.class */
public class DERObjectIdentifier extends ASN1Object {
    String identifier;

    public static DERObjectIdentifier getInstance(Object obj) {
        if (obj == null || (obj instanceof DERObjectIdentifier)) {
            return (DERObjectIdentifier) obj;
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERObjectIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        DERObject object = aSN1TaggedObject.getObject();
        return (z || (object instanceof DERObjectIdentifier)) ? getInstance(object) : new ASN1ObjectIdentifier(ASN1OctetString.getInstance(aSN1TaggedObject.getObject()).getOctets());
    }

    DERObjectIdentifier(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        long j = 0;
        BigInteger bigIntegerOr = null;
        boolean z = true;
        for (int i = 0; i != bArr.length; i++) {
            int i2 = bArr[i] & 255;
            if (j < 36028797018963968L) {
                j = (j * 128) + (i2 & 127);
                if ((i2 & 128) == 0) {
                    if (z) {
                        switch (((int) j) / 40) {
                            case 0:
                                stringBuffer.append('0');
                                break;
                            case 1:
                                stringBuffer.append('1');
                                j -= 40;
                                break;
                            default:
                                stringBuffer.append('2');
                                j -= 80;
                                break;
                        }
                        z = false;
                    }
                    stringBuffer.append('.');
                    stringBuffer.append(j);
                    j = 0;
                }
            } else {
                bigIntegerOr = (bigIntegerOr == null ? BigInteger.valueOf(j) : bigIntegerOr).shiftLeft(7).or(BigInteger.valueOf(i2 & 127));
                if ((i2 & 128) == 0) {
                    stringBuffer.append('.');
                    stringBuffer.append(bigIntegerOr);
                    bigIntegerOr = null;
                    j = 0;
                }
            }
        }
        this.identifier = stringBuffer.toString();
    }

    public DERObjectIdentifier(String str) {
        if (!isValidIdentifier(str)) {
            throw new IllegalArgumentException("string " + str + " not an OID");
        }
        this.identifier = str;
    }

    public String getId() {
        return this.identifier;
    }

    private void writeField(OutputStream outputStream, long j) throws IOException {
        byte[] bArr = new byte[9];
        int i = 8;
        bArr[8] = (byte) (((int) j) & 127);
        while (j >= 128) {
            j >>= 7;
            i--;
            bArr[i] = (byte) ((((int) j) & 127) | 128);
        }
        outputStream.write(bArr, i, 9 - i);
    }

    private void writeField(OutputStream outputStream, BigInteger bigInteger) throws IOException {
        int iBitLength = (bigInteger.bitLength() + 6) / 7;
        if (iBitLength == 0) {
            outputStream.write(0);
            return;
        }
        BigInteger bigIntegerShiftRight = bigInteger;
        byte[] bArr = new byte[iBitLength];
        for (int i = iBitLength - 1; i >= 0; i--) {
            bArr[i] = (byte) ((bigIntegerShiftRight.intValue() & 127) | 128);
            bigIntegerShiftRight = bigIntegerShiftRight.shiftRight(7);
        }
        int i2 = iBitLength - 1;
        bArr[i2] = (byte) (bArr[i2] & Byte.MAX_VALUE);
        outputStream.write(bArr);
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        OIDTokenizer oIDTokenizer = new OIDTokenizer(this.identifier);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream dEROutputStream2 = new DEROutputStream(byteArrayOutputStream);
        writeField(byteArrayOutputStream, (Integer.parseInt(oIDTokenizer.nextToken()) * 40) + Integer.parseInt(oIDTokenizer.nextToken()));
        while (oIDTokenizer.hasMoreTokens()) {
            String strNextToken = oIDTokenizer.nextToken();
            if (strNextToken.length() < 18) {
                writeField(byteArrayOutputStream, Long.parseLong(strNextToken));
            } else {
                writeField(byteArrayOutputStream, new BigInteger(strNextToken));
            }
        }
        dEROutputStream2.close();
        dEROutputStream.writeEncoded(6, byteArrayOutputStream.toByteArray());
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        if (dERObject instanceof DERObjectIdentifier) {
            return this.identifier.equals(((DERObjectIdentifier) dERObject).identifier);
        }
        return false;
    }

    public String toString() {
        return getId();
    }

    private static boolean isValidIdentifier(String str) {
        char cCharAt;
        boolean z;
        if (str.length() < 3 || str.charAt(1) != '.' || (cCharAt = str.charAt(0)) < '0' || cCharAt > '2') {
            return false;
        }
        boolean z2 = false;
        for (int length = str.length() - 1; length >= 2; length--) {
            char cCharAt2 = str.charAt(length);
            if ('0' <= cCharAt2 && cCharAt2 <= '9') {
                z = true;
            } else {
                if (cCharAt2 != '.' || !z2) {
                    return false;
                }
                z = false;
            }
            z2 = z;
        }
        return z2;
    }
}

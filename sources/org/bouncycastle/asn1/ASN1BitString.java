package org.bouncycastle.asn1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1BitString.class */
public abstract class ASN1BitString extends ASN1Primitive implements ASN1String {
    private static final char[] table = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected final byte[] data;
    protected final int padBits;

    protected static int getPadBits(int i) {
        int i2 = 0;
        int i3 = 3;
        while (true) {
            if (i3 < 0) {
                break;
            }
            if (i3 != 0) {
                if ((i >> (i3 * 8)) != 0) {
                    i2 = (i >> (i3 * 8)) & 255;
                    break;
                }
                i3--;
            } else {
                if (i != 0) {
                    i2 = i & 255;
                    break;
                }
                i3--;
            }
        }
        if (i2 == 0) {
            return 0;
        }
        int i4 = 1;
        while (true) {
            int i5 = i2 << 1;
            i2 = i5;
            if ((i5 & 255) == 0) {
                return 8 - i4;
            }
            i4++;
        }
    }

    protected static byte[] getBytes(int i) {
        if (i == 0) {
            return new byte[0];
        }
        int i2 = 4;
        for (int i3 = 3; i3 >= 1 && (i & (255 << (i3 * 8))) == 0; i3--) {
            i2--;
        }
        byte[] bArr = new byte[i2];
        for (int i4 = 0; i4 < i2; i4++) {
            bArr[i4] = (byte) ((i >> (i4 * 8)) & 255);
        }
        return bArr;
    }

    protected ASN1BitString(byte b, int i) {
        if (i > 7 || i < 0) {
            throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
        }
        this.data = new byte[]{b};
        this.padBits = i;
    }

    public ASN1BitString(byte[] bArr, int i) {
        if (bArr == null) {
            throw new NullPointerException("'data' cannot be null");
        }
        if (bArr.length == 0 && i != 0) {
            throw new IllegalArgumentException("zero length data with non-zero pad bits");
        }
        if (i > 7 || i < 0) {
            throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
        }
        this.data = Arrays.clone(bArr);
        this.padBits = i;
    }

    @Override // org.bouncycastle.asn1.ASN1String
    public String getString() {
        StringBuffer stringBuffer = new StringBuffer("#");
        try {
            byte[] encoded = getEncoded();
            for (int i = 0; i != encoded.length; i++) {
                stringBuffer.append(table[(encoded[i] >>> 4) & 15]);
                stringBuffer.append(table[encoded[i] & 15]);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            throw new ASN1ParsingException("Internal error encoding BitString: " + e.getMessage(), e);
        }
    }

    public int intValue() {
        int i = 0;
        int iMin = Math.min(4, this.data.length - 1);
        for (int i2 = 0; i2 < iMin; i2++) {
            i |= (this.data[i2] & 255) << (8 * i2);
        }
        if (0 <= iMin && iMin < 4) {
            i |= (((byte) (this.data[iMin] & (255 << this.padBits))) & 255) << (8 * iMin);
        }
        return i;
    }

    public byte[] getOctets() {
        if (this.padBits != 0) {
            throw new IllegalStateException("attempt to get non-octet aligned data from BIT STRING");
        }
        return Arrays.clone(this.data);
    }

    public byte[] getBytes() {
        return derForm(this.data, this.padBits);
    }

    public int getPadBits() {
        return this.padBits;
    }

    public String toString() {
        return getString();
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        int length = this.data.length - 1;
        if (length < 0) {
            return 1;
        }
        return ((Arrays.hashCode(this.data, 0, length) * 257) ^ ((byte) (this.data[length] & (255 << this.padBits)))) ^ this.padBits;
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1BitString)) {
            return false;
        }
        ASN1BitString aSN1BitString = (ASN1BitString) aSN1Primitive;
        if (this.padBits != aSN1BitString.padBits) {
            return false;
        }
        byte[] bArr = this.data;
        byte[] bArr2 = aSN1BitString.data;
        int length = bArr.length;
        if (length != bArr2.length) {
            return false;
        }
        int i = length - 1;
        if (i < 0) {
            return true;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (bArr[i2] != bArr2[i2]) {
                return false;
            }
        }
        return ((byte) (bArr[i] & (255 << this.padBits))) == ((byte) (bArr2[i] & (255 << this.padBits)));
    }

    protected static byte[] derForm(byte[] bArr, int i) {
        if (0 == bArr.length) {
            return bArr;
        }
        byte[] bArrClone = Arrays.clone(bArr);
        int length = bArr.length - 1;
        bArrClone[length] = (byte) (bArrClone[length] & (255 << i));
        return bArrClone;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    static ASN1BitString fromInputStream(int i, InputStream inputStream) throws IOException {
        if (i < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        int i2 = inputStream.read();
        byte[] bArr = new byte[i - 1];
        if (bArr.length != 0) {
            if (Streams.readFully(inputStream, bArr) != bArr.length) {
                throw new EOFException("EOF encountered in middle of BIT STRING");
            }
            if (i2 > 0 && i2 < 8 && bArr[bArr.length - 1] != ((byte) (bArr[bArr.length - 1] & (255 << i2)))) {
                return new DLBitString(bArr, i2);
            }
        }
        return new DERBitString(bArr, i2);
    }

    public ASN1Primitive getLoadedObject() {
        return toASN1Primitive();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERBitString] */
    @Override // org.bouncycastle.asn1.ASN1Primitive
    ASN1Primitive toDERObject() {
        return new DERBitString(this.data, this.padBits);
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive
    ASN1Primitive toDLObject() {
        return new DLBitString(this.data, this.padBits);
    }

    @Override // org.bouncycastle.asn1.ASN1Primitive
    abstract void encode(ASN1OutputStream aSN1OutputStream, boolean z) throws IOException;
}

package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/encoders/Base64Encoder.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/encoders/Base64Encoder.class */
public class Base64Encoder implements Encoder {
    protected final byte[] encodingTable = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    protected byte padding = 61;
    protected final byte[] decodingTable = new byte[128];

    protected void initialiseDecodingTable() {
        for (int i = 0; i < this.encodingTable.length; i++) {
            this.decodingTable[this.encodingTable[i]] = (byte) i;
        }
    }

    public Base64Encoder() {
        initialiseDecodingTable();
    }

    @Override // org.bouncycastle.util.encoders.Encoder
    public int encode(byte[] bArr, int i, int i2, OutputStream outputStream) throws IOException {
        int i3 = i2 % 3;
        int i4 = i2 - i3;
        for (int i5 = i; i5 < i + i4; i5 += 3) {
            int i6 = bArr[i5] & 255;
            int i7 = bArr[i5 + 1] & 255;
            int i8 = bArr[i5 + 2] & 255;
            outputStream.write(this.encodingTable[(i6 >>> 2) & 63]);
            outputStream.write(this.encodingTable[((i6 << 4) | (i7 >>> 4)) & 63]);
            outputStream.write(this.encodingTable[((i7 << 2) | (i8 >>> 6)) & 63]);
            outputStream.write(this.encodingTable[i8 & 63]);
        }
        switch (i3) {
            case 1:
                int i9 = bArr[i + i4] & 255;
                outputStream.write(this.encodingTable[(i9 >>> 2) & 63]);
                outputStream.write(this.encodingTable[(i9 << 4) & 63]);
                outputStream.write(this.padding);
                outputStream.write(this.padding);
                break;
            case 2:
                int i10 = bArr[i + i4] & 255;
                int i11 = bArr[i + i4 + 1] & 255;
                outputStream.write(this.encodingTable[(i10 >>> 2) & 63]);
                outputStream.write(this.encodingTable[((i10 << 4) | (i11 >>> 4)) & 63]);
                outputStream.write(this.encodingTable[(i11 << 2) & 63]);
                outputStream.write(this.padding);
                break;
        }
        return ((i4 / 3) * 4) + (i3 == 0 ? 0 : 4);
    }

    private boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    @Override // org.bouncycastle.util.encoders.Encoder
    public int decode(byte[] bArr, int i, int i2, OutputStream outputStream) throws IOException {
        int i3 = 0;
        int i4 = i + i2;
        while (i4 > i && ignore((char) bArr[i4 - 1])) {
            i4--;
        }
        int i5 = i4 - 4;
        int iNextI = nextI(bArr, i, i5);
        while (true) {
            int i6 = iNextI;
            if (i6 >= i5) {
                return i3 + decodeLastBlock(outputStream, (char) bArr[i4 - 4], (char) bArr[i4 - 3], (char) bArr[i4 - 2], (char) bArr[i4 - 1]);
            }
            byte b = this.decodingTable[bArr[i6]];
            int iNextI2 = nextI(bArr, i6 + 1, i5);
            int i7 = iNextI2 + 1;
            byte b2 = this.decodingTable[bArr[iNextI2]];
            int iNextI3 = nextI(bArr, i7, i5);
            int i8 = iNextI3 + 1;
            byte b3 = this.decodingTable[bArr[iNextI3]];
            int iNextI4 = nextI(bArr, i8, i5);
            int i9 = iNextI4 + 1;
            byte b4 = this.decodingTable[bArr[iNextI4]];
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            outputStream.write((b3 << 6) | b4);
            i3 += 3;
            iNextI = nextI(bArr, i9, i5);
        }
    }

    private int nextI(byte[] bArr, int i, int i2) {
        while (i < i2 && ignore((char) bArr[i])) {
            i++;
        }
        return i;
    }

    @Override // org.bouncycastle.util.encoders.Encoder
    public int decode(String str, OutputStream outputStream) throws IOException {
        int i = 0;
        int length = str.length();
        while (length > 0 && ignore(str.charAt(length - 1))) {
            length--;
        }
        int i2 = length - 4;
        int iNextI = nextI(str, 0, i2);
        while (true) {
            int i3 = iNextI;
            if (i3 >= i2) {
                return i + decodeLastBlock(outputStream, str.charAt(length - 4), str.charAt(length - 3), str.charAt(length - 2), str.charAt(length - 1));
            }
            byte b = this.decodingTable[str.charAt(i3)];
            int iNextI2 = nextI(str, i3 + 1, i2);
            int i4 = iNextI2 + 1;
            byte b2 = this.decodingTable[str.charAt(iNextI2)];
            int iNextI3 = nextI(str, i4, i2);
            int i5 = iNextI3 + 1;
            byte b3 = this.decodingTable[str.charAt(iNextI3)];
            int iNextI4 = nextI(str, i5, i2);
            int i6 = iNextI4 + 1;
            byte b4 = this.decodingTable[str.charAt(iNextI4)];
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            outputStream.write((b3 << 6) | b4);
            i += 3;
            iNextI = nextI(str, i6, i2);
        }
    }

    private int decodeLastBlock(OutputStream outputStream, char c, char c2, char c3, char c4) throws IOException {
        if (c3 == this.padding) {
            outputStream.write((this.decodingTable[c] << 2) | (this.decodingTable[c2] >> 4));
            return 1;
        }
        if (c4 == this.padding) {
            byte b = this.decodingTable[c];
            byte b2 = this.decodingTable[c2];
            byte b3 = this.decodingTable[c3];
            outputStream.write((b << 2) | (b2 >> 4));
            outputStream.write((b2 << 4) | (b3 >> 2));
            return 2;
        }
        byte b4 = this.decodingTable[c];
        byte b5 = this.decodingTable[c2];
        byte b6 = this.decodingTable[c3];
        byte b7 = this.decodingTable[c4];
        outputStream.write((b4 << 2) | (b5 >> 4));
        outputStream.write((b5 << 4) | (b6 >> 2));
        outputStream.write((b6 << 6) | b7);
        return 3;
    }

    private int nextI(String str, int i, int i2) {
        while (i < i2 && ignore(str.charAt(i))) {
            i++;
        }
        return i;
    }
}

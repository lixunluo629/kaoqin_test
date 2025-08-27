package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/encoders/HexEncoder.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/encoders/HexEncoder.class */
public class HexEncoder implements Encoder {
    protected final byte[] encodingTable = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    protected final byte[] decodingTable = new byte[128];

    protected void initialiseDecodingTable() {
        for (int i = 0; i < this.encodingTable.length; i++) {
            this.decodingTable[this.encodingTable[i]] = (byte) i;
        }
        this.decodingTable[65] = this.decodingTable[97];
        this.decodingTable[66] = this.decodingTable[98];
        this.decodingTable[67] = this.decodingTable[99];
        this.decodingTable[68] = this.decodingTable[100];
        this.decodingTable[69] = this.decodingTable[101];
        this.decodingTable[70] = this.decodingTable[102];
    }

    public HexEncoder() {
        initialiseDecodingTable();
    }

    @Override // org.bouncycastle.util.encoders.Encoder
    public int encode(byte[] bArr, int i, int i2, OutputStream outputStream) throws IOException {
        for (int i3 = i; i3 < i + i2; i3++) {
            int i4 = bArr[i3] & 255;
            outputStream.write(this.encodingTable[i4 >>> 4]);
            outputStream.write(this.encodingTable[i4 & 15]);
        }
        return i2 * 2;
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
        int i5 = i;
        while (i5 < i4) {
            while (i5 < i4 && ignore((char) bArr[i5])) {
                i5++;
            }
            int i6 = i5;
            int i7 = i5 + 1;
            byte b = this.decodingTable[bArr[i6]];
            while (i7 < i4 && ignore((char) bArr[i7])) {
                i7++;
            }
            int i8 = i7;
            i5 = i7 + 1;
            outputStream.write((b << 4) | this.decodingTable[bArr[i8]]);
            i3++;
        }
        return i3;
    }

    @Override // org.bouncycastle.util.encoders.Encoder
    public int decode(String str, OutputStream outputStream) throws IOException {
        int i = 0;
        int length = str.length();
        while (length > 0 && ignore(str.charAt(length - 1))) {
            length--;
        }
        int i2 = 0;
        while (i2 < length) {
            while (i2 < length && ignore(str.charAt(i2))) {
                i2++;
            }
            int i3 = i2;
            int i4 = i2 + 1;
            byte b = this.decodingTable[str.charAt(i3)];
            while (i4 < length && ignore(str.charAt(i4))) {
                i4++;
            }
            int i5 = i4;
            i2 = i4 + 1;
            outputStream.write((b << 4) | this.decodingTable[str.charAt(i5)]);
            i++;
        }
        return i;
    }
}

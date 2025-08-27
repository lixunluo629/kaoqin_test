package org.bouncycastle.mime.encoding;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/encoding/Base64InputStream.class */
public class Base64InputStream extends InputStream {
    private static final byte[] decodingTable = new byte[128];
    InputStream in;
    int[] outBuf = new int[3];
    int bufPtr = 3;
    boolean isEndOfStream;

    private int decode(int i, int i2, int i3, int i4, int[] iArr) throws EOFException {
        if (i4 < 0) {
            throw new EOFException("unexpected end of file in armored stream.");
        }
        if (i3 == 61) {
            iArr[2] = (((decodingTable[i] & 255) << 2) | ((decodingTable[i2] & 255) >> 4)) & 255;
            return 2;
        }
        if (i4 == 61) {
            byte b = decodingTable[i];
            byte b2 = decodingTable[i2];
            byte b3 = decodingTable[i3];
            iArr[1] = ((b << 2) | (b2 >> 4)) & 255;
            iArr[2] = ((b2 << 4) | (b3 >> 2)) & 255;
            return 1;
        }
        byte b4 = decodingTable[i];
        byte b5 = decodingTable[i2];
        byte b6 = decodingTable[i3];
        byte b7 = decodingTable[i4];
        iArr[0] = ((b4 << 2) | (b5 >> 4)) & 255;
        iArr[1] = ((b5 << 4) | (b6 >> 2)) & 255;
        iArr[2] = ((b6 << 6) | b7) & 255;
        return 0;
    }

    public Base64InputStream(InputStream inputStream) {
        this.in = inputStream;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.in.available();
    }

    private int readIgnoreSpace() throws IOException {
        int i = this.in.read();
        while (true) {
            int i2 = i;
            if (i2 != 32 && i2 != 9) {
                return i2;
            }
            i = this.in.read();
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int i;
        if (this.bufPtr > 2) {
            int ignoreSpace = readIgnoreSpace();
            if (ignoreSpace == 13 || ignoreSpace == 10) {
                int ignoreSpace2 = readIgnoreSpace();
                while (true) {
                    i = ignoreSpace2;
                    if (i != 10 && i != 13) {
                        break;
                    }
                    ignoreSpace2 = readIgnoreSpace();
                }
                if (i < 0) {
                    this.isEndOfStream = true;
                    return -1;
                }
                this.bufPtr = decode(i, readIgnoreSpace(), readIgnoreSpace(), readIgnoreSpace(), this.outBuf);
            } else {
                if (ignoreSpace < 0) {
                    this.isEndOfStream = true;
                    return -1;
                }
                this.bufPtr = decode(ignoreSpace, readIgnoreSpace(), readIgnoreSpace(), readIgnoreSpace(), this.outBuf);
            }
        }
        int[] iArr = this.outBuf;
        int i2 = this.bufPtr;
        this.bufPtr = i2 + 1;
        return iArr[i2];
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    static {
        for (int i = 65; i <= 90; i++) {
            decodingTable[i] = (byte) (i - 65);
        }
        for (int i2 = 97; i2 <= 122; i2++) {
            decodingTable[i2] = (byte) ((i2 - 97) + 26);
        }
        for (int i3 = 48; i3 <= 57; i3++) {
            decodingTable[i3] = (byte) ((i3 - 48) + 52);
        }
        decodingTable[43] = 62;
        decodingTable[47] = 63;
    }
}

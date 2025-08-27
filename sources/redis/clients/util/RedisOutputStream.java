package redis.clients.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/RedisOutputStream.class */
public final class RedisOutputStream extends FilterOutputStream {
    protected final byte[] buf;
    protected int count;
    private static final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
    private static final byte[] DigitTens = {48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57};
    private static final byte[] DigitOnes = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
    private static final byte[] digits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};

    public RedisOutputStream(OutputStream out) {
        this(out, 8192);
    }

    public RedisOutputStream(OutputStream out, int size) {
        super(out);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[size];
    }

    private void flushBuffer() throws IOException {
        if (this.count > 0) {
            this.out.write(this.buf, 0, this.count);
            this.count = 0;
        }
    }

    public void write(byte b) throws IOException {
        if (this.count == this.buf.length) {
            flushBuffer();
        }
        byte[] bArr = this.buf;
        int i = this.count;
        this.count = i + 1;
        bArr[i] = b;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        if (len >= this.buf.length) {
            flushBuffer();
            this.out.write(b, off, len);
        } else {
            if (len >= this.buf.length - this.count) {
                flushBuffer();
            }
            System.arraycopy(b, off, this.buf, this.count, len);
            this.count += len;
        }
    }

    public void writeAsciiCrLf(String in) throws IOException {
        int size = in.length();
        for (int i = 0; i != size; i++) {
            if (this.count == this.buf.length) {
                flushBuffer();
            }
            byte[] bArr = this.buf;
            int i2 = this.count;
            this.count = i2 + 1;
            bArr[i2] = (byte) in.charAt(i);
        }
        writeCrLf();
    }

    public static boolean isSurrogate(char ch2) {
        return ch2 >= 55296 && ch2 <= 57343;
    }

    public static int utf8Length(String str) {
        int strLen = str.length();
        int utfLen = 0;
        int i = 0;
        while (i != strLen) {
            char c = str.charAt(i);
            if (c < 128) {
                utfLen++;
            } else if (c < 2048) {
                utfLen += 2;
            } else if (isSurrogate(c)) {
                i++;
                utfLen += 4;
            } else {
                utfLen += 3;
            }
            i++;
        }
        return utfLen;
    }

    public void writeCrLf() throws IOException {
        if (2 >= this.buf.length - this.count) {
            flushBuffer();
        }
        byte[] bArr = this.buf;
        int i = this.count;
        this.count = i + 1;
        bArr[i] = 13;
        byte[] bArr2 = this.buf;
        int i2 = this.count;
        this.count = i2 + 1;
        bArr2[i2] = 10;
    }

    public void writeUtf8CrLf(String str) throws IOException {
        char c;
        int strLen = str.length();
        int i = 0;
        while (i < strLen && (c = str.charAt(i)) < 128) {
            if (this.count == this.buf.length) {
                flushBuffer();
            }
            byte[] bArr = this.buf;
            int i2 = this.count;
            this.count = i2 + 1;
            bArr[i2] = (byte) c;
            i++;
        }
        while (i < strLen) {
            char c2 = str.charAt(i);
            if (c2 < 128) {
                if (this.count == this.buf.length) {
                    flushBuffer();
                }
                byte[] bArr2 = this.buf;
                int i3 = this.count;
                this.count = i3 + 1;
                bArr2[i3] = (byte) c2;
            } else if (c2 < 2048) {
                if (2 >= this.buf.length - this.count) {
                    flushBuffer();
                }
                byte[] bArr3 = this.buf;
                int i4 = this.count;
                this.count = i4 + 1;
                bArr3[i4] = (byte) (192 | (c2 >> 6));
                byte[] bArr4 = this.buf;
                int i5 = this.count;
                this.count = i5 + 1;
                bArr4[i5] = (byte) (128 | (c2 & '?'));
            } else if (isSurrogate(c2)) {
                if (4 >= this.buf.length - this.count) {
                    flushBuffer();
                }
                int i6 = i;
                i++;
                int uc = Character.toCodePoint(c2, str.charAt(i6));
                byte[] bArr5 = this.buf;
                int i7 = this.count;
                this.count = i7 + 1;
                bArr5[i7] = (byte) (240 | (uc >> 18));
                byte[] bArr6 = this.buf;
                int i8 = this.count;
                this.count = i8 + 1;
                bArr6[i8] = (byte) (128 | ((uc >> 12) & 63));
                byte[] bArr7 = this.buf;
                int i9 = this.count;
                this.count = i9 + 1;
                bArr7[i9] = (byte) (128 | ((uc >> 6) & 63));
                byte[] bArr8 = this.buf;
                int i10 = this.count;
                this.count = i10 + 1;
                bArr8[i10] = (byte) (128 | (uc & 63));
            } else {
                if (3 >= this.buf.length - this.count) {
                    flushBuffer();
                }
                byte[] bArr9 = this.buf;
                int i11 = this.count;
                this.count = i11 + 1;
                bArr9[i11] = (byte) (224 | (c2 >> '\f'));
                byte[] bArr10 = this.buf;
                int i12 = this.count;
                this.count = i12 + 1;
                bArr10[i12] = (byte) (128 | ((c2 >> 6) & 63));
                byte[] bArr11 = this.buf;
                int i13 = this.count;
                this.count = i13 + 1;
                bArr11[i13] = (byte) (128 | (c2 & '?'));
            }
            i++;
        }
        writeCrLf();
    }

    public void writeIntCrLf(int value) throws IOException {
        if (value < 0) {
            write((byte) 45);
            value = -value;
        }
        int size = 0;
        while (value > sizeTable[size]) {
            size++;
        }
        int size2 = size + 1;
        if (size2 >= this.buf.length - this.count) {
            flushBuffer();
        }
        int charPos = this.count + size2;
        while (value >= 65536) {
            int q = value / 100;
            int r = value - (((q << 6) + (q << 5)) + (q << 2));
            value = q;
            int charPos2 = charPos - 1;
            this.buf[charPos2] = DigitOnes[r];
            charPos = charPos2 - 1;
            this.buf[charPos] = DigitTens[r];
        }
        do {
            int q2 = (value * 52429) >>> 19;
            charPos--;
            this.buf[charPos] = digits[value - ((q2 << 3) + (q2 << 1))];
            value = q2;
        } while (value != 0);
        this.count += size2;
        writeCrLf();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        flushBuffer();
        this.out.flush();
    }
}

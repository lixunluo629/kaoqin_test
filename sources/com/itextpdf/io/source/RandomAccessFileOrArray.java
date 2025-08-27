package com.itextpdf.io.source;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/RandomAccessFileOrArray.class */
public class RandomAccessFileOrArray implements DataInput, Serializable {
    private static final long serialVersionUID = -169314546265954851L;
    public static boolean plainRandomAccess = false;
    private IRandomAccessSource byteSource;
    private long byteSourcePosition;
    private byte back;
    private boolean isBack = false;

    public RandomAccessFileOrArray(IRandomAccessSource byteSource) {
        this.byteSource = byteSource;
    }

    public RandomAccessFileOrArray createView() {
        ensureByteSourceIsThreadSafe();
        return new RandomAccessFileOrArray(new IndependentRandomAccessSource(this.byteSource));
    }

    public IRandomAccessSource createSourceView() {
        ensureByteSourceIsThreadSafe();
        return new IndependentRandomAccessSource(this.byteSource);
    }

    public void pushBack(byte b) {
        this.back = b;
        this.isBack = true;
    }

    public int read() throws IOException {
        if (this.isBack) {
            this.isBack = false;
            return this.back & 255;
        }
        IRandomAccessSource iRandomAccessSource = this.byteSource;
        long j = this.byteSourcePosition;
        this.byteSourcePosition = j + 1;
        return iRandomAccessSource.get(j);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int byteSourceCount;
        if (len == 0) {
            return 0;
        }
        int count = 0;
        if (this.isBack && len > 0) {
            this.isBack = false;
            off++;
            b[off] = this.back;
            len--;
            count = 0 + 1;
        }
        if (len > 0 && (byteSourceCount = this.byteSource.get(this.byteSourcePosition, b, off, len)) > 0) {
            count += byteSourceCount;
            this.byteSourcePosition += byteSourceCount;
        }
        if (count == 0) {
            return -1;
        }
        return count;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] b, int off, int len) throws IOException {
        int n = 0;
        do {
            int count = read(b, off + n, len - n);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        } while (n < len);
    }

    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0L;
        }
        int adj = 0;
        if (this.isBack) {
            this.isBack = false;
            if (n == 1) {
                return 1L;
            }
            n--;
            adj = 1;
        }
        long pos = getPosition();
        long len = length();
        long newpos = pos + n;
        if (newpos > len) {
            newpos = len;
        }
        seek(newpos);
        return (newpos - pos) + adj;
    }

    @Override // java.io.DataInput
    public int skipBytes(int n) throws IOException {
        return (int) skip(n);
    }

    public void close() throws IOException {
        this.isBack = false;
        this.byteSource.close();
    }

    public long length() throws IOException {
        return this.byteSource.length();
    }

    public void seek(long pos) throws IOException {
        this.byteSourcePosition = pos;
        this.isBack = false;
    }

    public long getPosition() throws IOException {
        return this.byteSourcePosition - (this.isBack ? 1 : 0);
    }

    @Override // java.io.DataInput
    public boolean readBoolean() throws IOException {
        int ch2 = read();
        if (ch2 < 0) {
            throw new EOFException();
        }
        return ch2 != 0;
    }

    @Override // java.io.DataInput
    public byte readByte() throws IOException {
        int ch2 = read();
        if (ch2 < 0) {
            throw new EOFException();
        }
        return (byte) ch2;
    }

    @Override // java.io.DataInput
    public int readUnsignedByte() throws IOException {
        int ch2 = read();
        if (ch2 < 0) {
            throw new EOFException();
        }
        return ch2;
    }

    @Override // java.io.DataInput
    public short readShort() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch1 << 8) + ch2);
    }

    public final short readShortLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch2 << 8) + ch1);
    }

    @Override // java.io.DataInput
    public int readUnsignedShort() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 << 8) + ch2;
    }

    public final int readUnsignedShortLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch2 << 8) + ch1;
    }

    @Override // java.io.DataInput
    public char readChar() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (char) ((ch1 << 8) + ch2);
    }

    public final char readCharLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (char) ((ch2 << 8) + ch2);
    }

    @Override // java.io.DataInput
    public int readInt() throws IOException {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        int ch4 = read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4;
    }

    public final int readIntLE() throws IOException {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        int ch4 = read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1;
    }

    public final long readUnsignedInt() throws IOException {
        long ch1 = read();
        long ch2 = read();
        long ch3 = read();
        long ch4 = read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4;
    }

    public final long readUnsignedIntLE() throws IOException {
        long ch1 = read();
        long ch2 = read();
        long ch3 = read();
        long ch4 = read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1;
    }

    @Override // java.io.DataInput
    public long readLong() throws IOException {
        return (readInt() << 32) + (readInt() & 4294967295L);
    }

    public final long readLongLE() throws IOException {
        int i1 = readIntLE();
        int i2 = readIntLE();
        return (i2 << 32) + (i1 & 4294967295L);
    }

    @Override // java.io.DataInput
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final float readFloatLE() throws IOException {
        return Float.intBitsToFloat(readIntLE());
    }

    @Override // java.io.DataInput
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final double readDoubleLE() throws IOException {
        return Double.longBitsToDouble(readLongLE());
    }

    @Override // java.io.DataInput
    public String readLine() throws IOException {
        StringBuilder input = new StringBuilder();
        int c = -1;
        boolean eol = false;
        while (!eol) {
            int i = read();
            c = i;
            switch (i) {
                case -1:
                case 10:
                    eol = true;
                    break;
                case 13:
                    eol = true;
                    long cur = getPosition();
                    if (read() == 10) {
                        break;
                    } else {
                        seek(cur);
                        break;
                    }
                default:
                    input.append((char) c);
                    break;
            }
        }
        if (c == -1 && input.length() == 0) {
            return null;
        }
        return input.toString();
    }

    @Override // java.io.DataInput
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    public String readString(int length, String encoding) throws IOException {
        byte[] buf = new byte[length];
        readFully(buf);
        return new String(buf, encoding);
    }

    private void ensureByteSourceIsThreadSafe() {
        if (!(this.byteSource instanceof ThreadSafeRandomAccessSource)) {
            this.byteSource = new ThreadSafeRandomAccessSource(this.byteSource);
        }
    }
}

package org.apache.poi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/* loaded from: poi-3.17.jar:org/apache/poi/util/RLEDecompressingInputStream.class */
public class RLEDecompressingInputStream extends InputStream {
    private static final int[] POWER2 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768};
    private final InputStream in;
    private final byte[] buf = new byte[4096];
    private int pos = 0;
    private int len;

    public RLEDecompressingInputStream(InputStream in) throws IOException {
        this.in = in;
        int header = in.read();
        if (header != 1) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Header byte 0x01 expected, received 0x%02X", Integer.valueOf(header & 255)));
        }
        this.len = readChunk();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.len == -1) {
            return -1;
        }
        if (this.pos >= this.len) {
            int chunk = readChunk();
            this.len = chunk;
            if (chunk == -1) {
                return -1;
            }
        }
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int l) throws IOException {
        if (this.len == -1) {
            return -1;
        }
        int offset = off;
        int length = l;
        while (length > 0) {
            if (this.pos >= this.len) {
                int chunk = readChunk();
                this.len = chunk;
                if (chunk == -1) {
                    if (offset > off) {
                        return offset - off;
                    }
                    return -1;
                }
            }
            int c = Math.min(length, this.len - this.pos);
            System.arraycopy(this.buf, this.pos, b, offset, c);
            this.pos += c;
            length -= c;
            offset += c;
        }
        return l;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        long j = n;
        while (true) {
            long length = j;
            if (length > 0) {
                if (this.pos >= this.len) {
                    int chunk = readChunk();
                    this.len = chunk;
                    if (chunk == -1) {
                        return -1L;
                    }
                }
                int c = (int) Math.min(n, this.len - this.pos);
                this.pos += c;
                j = length - c;
            } else {
                return n;
            }
        }
    }

    @Override // java.io.InputStream
    public int available() {
        if (this.len > 0) {
            return this.len - this.pos;
        }
        return 0;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    private int readChunk() throws IOException {
        this.pos = 0;
        int w = readShort(this.in);
        if (w == -1) {
            return -1;
        }
        int chunkSize = (w & 4095) + 1;
        if ((w & 28672) != 12288) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Chunksize header A should be 0x3000, received 0x%04X", Integer.valueOf(w & 57344)));
        }
        boolean rawChunk = (w & 32768) == 0;
        if (rawChunk) {
            if (this.in.read(this.buf, 0, chunkSize) < chunkSize) {
                throw new IllegalStateException(String.format(Locale.ROOT, "Not enough bytes read, expected %d", Integer.valueOf(chunkSize)));
            }
            return chunkSize;
        }
        int inOffset = 0;
        int outOffset = 0;
        while (inOffset < chunkSize) {
            int tokenFlags = this.in.read();
            inOffset++;
            if (tokenFlags == -1) {
                break;
            }
            for (int n = 0; n < 8 && inOffset < chunkSize; n++) {
                if ((tokenFlags & POWER2[n]) == 0) {
                    int b = this.in.read();
                    if (b == -1) {
                        return -1;
                    }
                    int i = outOffset;
                    outOffset++;
                    this.buf[i] = (byte) b;
                    inOffset++;
                } else {
                    int token = readShort(this.in);
                    if (token == -1) {
                        return -1;
                    }
                    inOffset += 2;
                    int copyLenBits = getCopyLenBits(outOffset - 1);
                    int copyOffset = (token >> copyLenBits) + 1;
                    int copyLen = (token & (POWER2[copyLenBits] - 1)) + 3;
                    int startPos = outOffset - copyOffset;
                    int endPos = startPos + copyLen;
                    for (int i2 = startPos; i2 < endPos; i2++) {
                        int i3 = outOffset;
                        outOffset++;
                        this.buf[i3] = this.buf[i2];
                    }
                }
            }
        }
        return outOffset;
    }

    static int getCopyLenBits(int offset) {
        for (int n = 11; n >= 4; n--) {
            if ((offset & POWER2[n]) != 0) {
                return 15 - n;
            }
        }
        return 12;
    }

    public int readShort() throws IOException {
        return readShort(this);
    }

    public int readInt() throws IOException {
        return readInt(this);
    }

    private int readShort(InputStream stream) throws IOException {
        int b1;
        int b0 = stream.read();
        if (b0 == -1 || (b1 = stream.read()) == -1) {
            return -1;
        }
        return (b0 & 255) | ((b1 & 255) << 8);
    }

    private int readInt(InputStream stream) throws IOException {
        int b1;
        int b2;
        int b3;
        int b0 = stream.read();
        if (b0 == -1 || (b1 = stream.read()) == -1 || (b2 = stream.read()) == -1 || (b3 = stream.read()) == -1) {
            return -1;
        }
        return (b0 & 255) | ((b1 & 255) << 8) | ((b2 & 255) << 16) | ((b3 & 255) << 24);
    }

    public static byte[] decompress(byte[] compressed) throws IOException {
        return decompress(compressed, 0, compressed.length);
    }

    public static byte[] decompress(byte[] compressed, int offset, int length) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream instream = new ByteArrayInputStream(compressed, offset, length);
        InputStream stream = new RLEDecompressingInputStream(instream);
        IOUtils.copy(stream, out);
        stream.close();
        out.close();
        return out.toByteArray();
    }
}

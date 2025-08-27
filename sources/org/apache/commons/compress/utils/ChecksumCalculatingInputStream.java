package org.apache.commons.compress.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/ChecksumCalculatingInputStream.class */
public class ChecksumCalculatingInputStream extends InputStream {
    private final InputStream in;
    private final Checksum checksum;

    public ChecksumCalculatingInputStream(Checksum checksum, InputStream in) {
        if (checksum == null) {
            throw new NullPointerException("Parameter checksum must not be null");
        }
        if (in == null) {
            throw new NullPointerException("Parameter in must not be null");
        }
        this.checksum = checksum;
        this.in = in;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int ret = this.in.read();
        if (ret >= 0) {
            this.checksum.update(ret);
        }
        return ret;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int ret = this.in.read(b, off, len);
        if (ret >= 0) {
            this.checksum.update(b, off, ret);
        }
        return ret;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        if (read() >= 0) {
            return 1L;
        }
        return 0L;
    }

    public long getValue() {
        return this.checksum.getValue();
    }
}

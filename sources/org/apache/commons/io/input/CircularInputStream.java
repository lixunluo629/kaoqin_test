package org.apache.commons.io.input;

import java.io.IOException;
import java.util.Objects;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/CircularInputStream.class */
public class CircularInputStream extends AbstractInputStream {
    private long byteCount;
    private int position = -1;
    private final byte[] repeatedContent;
    private final long targetByteCount;

    private static byte[] validate(byte[] repeatContent) {
        Objects.requireNonNull(repeatContent, "repeatContent");
        for (byte b : repeatContent) {
            if (b == -1) {
                throw new IllegalArgumentException("repeatContent contains the end-of-stream marker -1");
            }
        }
        return repeatContent;
    }

    public CircularInputStream(byte[] repeatContent, long targetByteCount) {
        this.repeatedContent = validate(repeatContent);
        if (repeatContent.length == 0) {
            throw new IllegalArgumentException("repeatContent is empty.");
        }
        this.targetByteCount = targetByteCount;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (isClosed()) {
            return 0;
        }
        if (this.targetByteCount <= 2147483647L) {
            return Math.max(Integer.MAX_VALUE, (int) this.targetByteCount);
        }
        return Integer.MAX_VALUE;
    }

    @Override // org.apache.commons.io.input.AbstractInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.byteCount = this.targetByteCount;
    }

    @Override // java.io.InputStream
    public int read() {
        if (this.targetByteCount >= 0 || isClosed()) {
            if (this.byteCount == this.targetByteCount) {
                return -1;
            }
            this.byteCount++;
        }
        this.position = (this.position + 1) % this.repeatedContent.length;
        return this.repeatedContent[this.position] & 255;
    }
}

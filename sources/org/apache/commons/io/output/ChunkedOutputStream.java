package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.build.AbstractStreamBuilder;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/ChunkedOutputStream.class */
public class ChunkedOutputStream extends FilterOutputStream {
    private final int chunkSize;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/ChunkedOutputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<ChunkedOutputStream, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public ChunkedOutputStream get() throws IOException {
            return new ChunkedOutputStream(getOutputStream(), getBufferSize());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public ChunkedOutputStream(OutputStream stream) {
        this(stream, 8192);
    }

    @Deprecated
    public ChunkedOutputStream(OutputStream stream, int chunkSize) {
        super(stream);
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize <= 0");
        }
        this.chunkSize = chunkSize;
    }

    int getChunkSize() {
        return this.chunkSize;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] data, int srcOffset, int length) throws IOException {
        int bytes = length;
        int i = srcOffset;
        while (true) {
            int dstOffset = i;
            if (bytes > 0) {
                int chunk = Math.min(bytes, this.chunkSize);
                this.out.write(data, dstOffset, chunk);
                bytes -= chunk;
                i = dstOffset + chunk;
            } else {
                return;
            }
        }
    }
}

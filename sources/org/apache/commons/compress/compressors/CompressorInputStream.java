package org.apache.commons.compress.compressors;

import java.io.InputStream;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/compressors/CompressorInputStream.class */
public abstract class CompressorInputStream extends InputStream {
    private long bytesRead = 0;

    protected void count(int read) {
        count(read);
    }

    protected void count(long read) {
        if (read != -1) {
            this.bytesRead += read;
        }
    }

    protected void pushedBackBytes(long pushedBack) {
        this.bytesRead -= pushedBack;
    }

    @Deprecated
    public int getCount() {
        return (int) this.bytesRead;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }

    public long getUncompressedCount() {
        return getBytesRead();
    }
}

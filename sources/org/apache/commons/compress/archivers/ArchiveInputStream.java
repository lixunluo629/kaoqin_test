package org.apache.commons.compress.archivers;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/ArchiveInputStream.class */
public abstract class ArchiveInputStream extends InputStream {
    private static final int BYTE_MASK = 255;
    private final byte[] single = new byte[1];
    private long bytesRead = 0;

    public abstract ArchiveEntry getNextEntry() throws IOException;

    @Override // java.io.InputStream
    public int read() throws IOException {
        int num = read(this.single, 0, 1);
        if (num == -1) {
            return -1;
        }
        return this.single[0] & 255;
    }

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

    public boolean canReadEntryData(ArchiveEntry archiveEntry) {
        return true;
    }
}

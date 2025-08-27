package org.apache.commons.compress.archivers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/archivers/ArchiveOutputStream.class */
public abstract class ArchiveOutputStream extends OutputStream {
    static final int BYTE_MASK = 255;
    private final byte[] oneByte = new byte[1];
    private long bytesWritten = 0;

    public abstract void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException;

    public abstract void closeArchiveEntry() throws IOException;

    public abstract void finish() throws IOException;

    public abstract ArchiveEntry createArchiveEntry(File file, String str) throws IOException;

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.oneByte[0] = (byte) (b & 255);
        write(this.oneByte, 0, 1);
    }

    protected void count(int written) {
        count(written);
    }

    protected void count(long written) {
        if (written != -1) {
            this.bytesWritten += written;
        }
    }

    @Deprecated
    public int getCount() {
        return (int) this.bytesWritten;
    }

    public long getBytesWritten() {
        return this.bytesWritten;
    }

    public boolean canWriteEntryData(ArchiveEntry archiveEntry) {
        return true;
    }
}

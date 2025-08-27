package com.itextpdf.io.source;

import java.io.IOException;
import java.nio.channels.FileChannel;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/MappedChannelRandomAccessSource.class */
class MappedChannelRandomAccessSource implements IRandomAccessSource {
    private final FileChannel channel;
    private final long offset;
    private final long length;
    private ByteBufferRandomAccessSource source;

    public MappedChannelRandomAccessSource(FileChannel channel, long offset, long length) {
        if (offset < 0) {
            throw new IllegalArgumentException(offset + " is negative");
        }
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is zero or negative");
        }
        this.channel = channel;
        this.offset = offset;
        this.length = length;
        this.source = null;
    }

    void open() throws IOException {
        if (this.source != null) {
            return;
        }
        if (!this.channel.isOpen()) {
            throw new IllegalStateException("Channel is closed");
        }
        this.source = new ByteBufferRandomAccessSource(this.channel.map(FileChannel.MapMode.READ_ONLY, this.offset, this.length));
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position) throws IOException {
        if (this.source == null) {
            throw new IOException("RandomAccessSource not opened");
        }
        return this.source.get(position);
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long position, byte[] bytes, int off, int len) throws IOException {
        if (this.source == null) {
            throw new IOException("RandomAccessSource not opened");
        }
        return this.source.get(position, bytes, off, len);
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        return this.length;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        if (this.source == null) {
            return;
        }
        this.source.close();
        this.source = null;
    }

    public String toString() {
        return getClass().getName() + " (" + this.offset + ", " + this.length + ")";
    }
}

package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.PdfException;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/MemoryLimitsAwareOutputStream.class */
class MemoryLimitsAwareOutputStream extends ByteArrayOutputStream {
    private static final int DEFAULT_MAX_STREAM_SIZE = 2147483639;
    private int maxStreamSize;

    public MemoryLimitsAwareOutputStream() {
        this.maxStreamSize = 2147483639;
    }

    public MemoryLimitsAwareOutputStream(int size) {
        super(size);
        this.maxStreamSize = 2147483639;
    }

    public long getMaxStreamSize() {
        return this.maxStreamSize;
    }

    public MemoryLimitsAwareOutputStream setMaxStreamSize(int maxStreamSize) {
        this.maxStreamSize = maxStreamSize;
        return this;
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public synchronized void write(byte[] b, int off, int len) {
        if (off < 0 || off > b.length || len < 0 || (off + len) - b.length > 0) {
            throw new IndexOutOfBoundsException();
        }
        int minCapacity = this.count + len;
        if (minCapacity < 0) {
            throw new MemoryLimitsAwareException(PdfException.DuringDecompressionSingleStreamOccupiedMoreThanMaxIntegerValue);
        }
        if (minCapacity > this.maxStreamSize) {
            throw new MemoryLimitsAwareException(PdfException.DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed);
        }
        int oldCapacity = this.buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity < 0 || newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - this.maxStreamSize > 0) {
            this.buf = Arrays.copyOf(this.buf, this.maxStreamSize);
        }
        super.write(b, off, len);
    }
}

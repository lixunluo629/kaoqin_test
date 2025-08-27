package com.itextpdf.io.source;

import java.io.IOException;
import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/ArrayRandomAccessSource.class */
class ArrayRandomAccessSource implements IRandomAccessSource, Serializable {
    private static final long serialVersionUID = 8497059230517630513L;
    private byte[] array;

    public ArrayRandomAccessSource(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Passed byte array can not be null.");
        }
        this.array = array;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long offset) {
        if (offset >= this.array.length) {
            return -1;
        }
        return 255 & this.array[(int) offset];
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public int get(long offset, byte[] bytes, int off, int len) {
        if (this.array == null) {
            throw new IllegalStateException("Already closed");
        }
        if (offset >= this.array.length) {
            return -1;
        }
        if (offset + len > this.array.length) {
            len = (int) (this.array.length - offset);
        }
        System.arraycopy(this.array, (int) offset, bytes, off, len);
        return len;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public long length() {
        return this.array.length;
    }

    @Override // com.itextpdf.io.source.IRandomAccessSource
    public void close() throws IOException {
        this.array = null;
    }
}

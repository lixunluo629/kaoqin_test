package com.itextpdf.io.source;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/ByteArrayOutputStream.class */
public class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {
    public ByteArrayOutputStream() {
    }

    public ByteArrayOutputStream(int size) {
        super(size);
    }

    public ByteArrayOutputStream assignBytes(byte[] bytes, int count) {
        this.buf = bytes;
        this.count = count;
        return this;
    }

    public ByteArrayOutputStream assignBytes(byte[] bytes) {
        this.buf = bytes;
        this.count = bytes.length;
        return this;
    }
}

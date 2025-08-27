package com.itextpdf.io.source;

import java.io.IOException;
import java.util.zip.Deflater;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/source/DeflaterOutputStream.class */
public class DeflaterOutputStream extends java.util.zip.DeflaterOutputStream {
    public DeflaterOutputStream(java.io.OutputStream out, int level, int size) {
        super(out, new Deflater(level), size);
    }

    public DeflaterOutputStream(java.io.OutputStream out, int level) {
        this(out, level, 512);
    }

    public DeflaterOutputStream(java.io.OutputStream out) {
        this(out, -1);
    }

    @Override // java.util.zip.DeflaterOutputStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        finish();
        super.close();
    }

    @Override // java.util.zip.DeflaterOutputStream
    public void finish() throws IOException {
        super.finish();
        this.def.end();
    }
}

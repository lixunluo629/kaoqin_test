package com.itextpdf.kernel.crypto;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.hssf.record.chart.FontBasisRecord;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/crypto/OutputStreamStandardEncryption.class */
public class OutputStreamStandardEncryption extends OutputStreamEncryption {
    protected ARCFOUREncryption arcfour;

    public OutputStreamStandardEncryption(OutputStream out, byte[] key, int off, int len) {
        super(out);
        this.arcfour = new ARCFOUREncryption();
        this.arcfour.prepareARCFOURKey(key, off, len);
    }

    public OutputStreamStandardEncryption(OutputStream out, byte[] key) {
        this(out, key, 0, key.length);
    }

    @Override // com.itextpdf.kernel.crypto.OutputStreamEncryption, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        byte[] b2 = new byte[Math.min(len, FontBasisRecord.sid)];
        while (len > 0) {
            int sz = Math.min(len, b2.length);
            this.arcfour.encryptARCFOUR(b, off, sz, b2, 0);
            this.out.write(b2, 0, sz);
            len -= sz;
            off += sz;
        }
    }

    @Override // com.itextpdf.kernel.crypto.OutputStreamEncryption
    public void finish() {
    }
}

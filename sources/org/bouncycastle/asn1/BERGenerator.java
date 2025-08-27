package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/BERGenerator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/BERGenerator.class */
public class BERGenerator extends ASN1Generator {
    private boolean _tagged;
    private boolean _isExplicit;
    private int _tagNo;

    protected BERGenerator(OutputStream outputStream) {
        super(outputStream);
        this._tagged = false;
    }

    public BERGenerator(OutputStream outputStream, int i, boolean z) {
        super(outputStream);
        this._tagged = false;
        this._tagged = true;
        this._isExplicit = z;
        this._tagNo = i;
    }

    @Override // org.bouncycastle.asn1.ASN1Generator
    public OutputStream getRawOutputStream() {
        return this._out;
    }

    private void writeHdr(int i) throws IOException {
        this._out.write(i);
        this._out.write(128);
    }

    protected void writeBERHeader(int i) throws IOException {
        if (!this._tagged) {
            writeHdr(i);
            return;
        }
        int i2 = this._tagNo | 128;
        if (this._isExplicit) {
            writeHdr(i2 | 32);
            writeHdr(i);
        } else if ((i & 32) != 0) {
            writeHdr(i2 | 32);
        } else {
            writeHdr(i2);
        }
    }

    protected void writeBERBody(InputStream inputStream) throws IOException {
        while (true) {
            int i = inputStream.read();
            if (i < 0) {
                return;
            } else {
                this._out.write(i);
            }
        }
    }

    protected void writeBEREnd() throws IOException {
        this._out.write(0);
        this._out.write(0);
        if (this._tagged && this._isExplicit) {
            this._out.write(0);
            this._out.write(0);
        }
    }
}

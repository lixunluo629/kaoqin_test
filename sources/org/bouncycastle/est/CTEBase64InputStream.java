package org.bouncycastle.est;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.util.encoders.Base64;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/est/CTEBase64InputStream.class */
class CTEBase64InputStream extends InputStream {
    protected final InputStream src;
    protected final byte[] rawBuf = new byte[1024];
    protected final byte[] data = new byte[768];
    protected final OutputStream dataOutputStream = new OutputStream() { // from class: org.bouncycastle.est.CTEBase64InputStream.1
        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
            byte[] bArr = CTEBase64InputStream.this.data;
            CTEBase64InputStream cTEBase64InputStream = CTEBase64InputStream.this;
            int i2 = cTEBase64InputStream.wp;
            cTEBase64InputStream.wp = i2 + 1;
            bArr[i2] = (byte) i;
        }
    };
    protected final Long max;
    protected int rp;
    protected int wp;
    protected boolean end;
    protected long read;

    public CTEBase64InputStream(InputStream inputStream, Long l) {
        this.src = inputStream;
        this.max = l;
    }

    protected int pullFromSrc() throws IOException {
        int i;
        if (this.read >= this.max.longValue()) {
            return -1;
        }
        int i2 = 0;
        do {
            i = this.src.read();
            if (i >= 33 || i == 13 || i == 10) {
                if (i2 >= this.rawBuf.length) {
                    throw new IOException("Content Transfer Encoding, base64 line length > 1024");
                }
                int i3 = i2;
                i2++;
                this.rawBuf[i3] = (byte) i;
                this.read++;
            } else if (i >= 0) {
                this.read++;
            }
            if (i <= -1 || i2 >= this.rawBuf.length || i == 10) {
                break;
            }
        } while (this.read < this.max.longValue());
        if (i2 > 0) {
            try {
                Base64.decode(this.rawBuf, 0, i2, this.dataOutputStream);
            } catch (Exception e) {
                throw new IOException("Decode Base64 Content-Transfer-Encoding: " + e);
            }
        } else if (i == -1) {
            return -1;
        }
        return this.wp;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.rp == this.wp) {
            this.rp = 0;
            this.wp = 0;
            int iPullFromSrc = pullFromSrc();
            if (iPullFromSrc == -1) {
                return iPullFromSrc;
            }
        }
        byte[] bArr = this.data;
        int i = this.rp;
        this.rp = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.src.close();
    }
}

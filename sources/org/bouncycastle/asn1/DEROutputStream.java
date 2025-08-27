package org.bouncycastle.asn1;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DEROutputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/DEROutputStream.class */
public class DEROutputStream extends FilterOutputStream implements DERTags {
    public DEROutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    void writeLength(int i) throws IOException {
        if (i <= 127) {
            write((byte) i);
            return;
        }
        int i2 = 1;
        int i3 = i;
        while (true) {
            int i4 = i3 >>> 8;
            i3 = i4;
            if (i4 == 0) {
                break;
            } else {
                i2++;
            }
        }
        write((byte) (i2 | 128));
        for (int i5 = (i2 - 1) * 8; i5 >= 0; i5 -= 8) {
            write((byte) (i >> i5));
        }
    }

    void writeEncoded(int i, byte[] bArr) throws IOException {
        write(i);
        writeLength(bArr.length);
        write(bArr);
    }

    void writeTag(int i, int i2) throws IOException {
        if (i2 < 31) {
            write(i | i2);
            return;
        }
        write(i | 31);
        if (i2 < 128) {
            write(i2);
            return;
        }
        byte[] bArr = new byte[5];
        int length = bArr.length - 1;
        bArr[length] = (byte) (i2 & 127);
        do {
            i2 >>= 7;
            length--;
            bArr[length] = (byte) ((i2 & 127) | 128);
        } while (i2 > 127);
        write(bArr, length, bArr.length - length);
    }

    void writeEncoded(int i, int i2, byte[] bArr) throws IOException {
        writeTag(i, i2);
        writeLength(bArr.length);
        write(bArr);
    }

    protected void writeNull() throws IOException {
        write(5);
        write(0);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        this.out.write(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
    }

    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            writeNull();
        } else if (obj instanceof DERObject) {
            ((DERObject) obj).encode(this);
        } else {
            if (!(obj instanceof DEREncodable)) {
                throw new IOException("object not DEREncodable");
            }
            ((DEREncodable) obj).getDERObject().encode(this);
        }
    }
}

package org.bouncycastle.util.io.pem;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.tomcat.util.net.jsse.PEMFile;
import org.bouncycastle.util.encoders.Base64;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/io/pem/PemWriter.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/io/pem/PemWriter.class */
public class PemWriter extends BufferedWriter {
    private static final int LINE_LENGTH = 64;
    private final int nlLength;
    private char[] buf;

    public PemWriter(Writer writer) {
        super(writer);
        this.buf = new char[64];
        String property = System.getProperty("line.separator");
        if (property != null) {
            this.nlLength = property.length();
        } else {
            this.nlLength = 2;
        }
    }

    public int getOutputSize(PemObject pemObject) {
        int length = (2 * (pemObject.getType().length() + 10 + this.nlLength)) + 6 + 4;
        if (!pemObject.getHeaders().isEmpty()) {
            for (PemHeader pemHeader : pemObject.getHeaders()) {
                length += pemHeader.getName().length() + ": ".length() + pemHeader.getValue().length() + this.nlLength;
            }
            length += this.nlLength;
        }
        int length2 = ((pemObject.getContent().length + 2) / 3) * 4;
        return length + length2 + ((((length2 + 64) - 1) / 64) * this.nlLength);
    }

    public void writeObject(PemObjectGenerator pemObjectGenerator) throws IOException {
        PemObject pemObjectGenerate = pemObjectGenerator.generate();
        writePreEncapsulationBoundary(pemObjectGenerate.getType());
        if (!pemObjectGenerate.getHeaders().isEmpty()) {
            for (PemHeader pemHeader : pemObjectGenerate.getHeaders()) {
                write(pemHeader.getName());
                write(": ");
                write(pemHeader.getValue());
                newLine();
            }
            newLine();
        }
        writeEncoded(pemObjectGenerate.getContent());
        writePostEncapsulationBoundary(pemObjectGenerate.getType());
    }

    private void writeEncoded(byte[] bArr) throws IOException {
        byte[] bArrEncode = Base64.encode(bArr);
        int length = 0;
        while (true) {
            int i = length;
            if (i >= bArrEncode.length) {
                return;
            }
            int i2 = 0;
            while (i2 != this.buf.length && i + i2 < bArrEncode.length) {
                this.buf[i2] = (char) bArrEncode[i + i2];
                i2++;
            }
            write(this.buf, 0, i2);
            newLine();
            length = i + this.buf.length;
        }
    }

    private void writePreEncapsulationBoundary(String str) throws IOException {
        write(PEMFile.Part.BEGIN_BOUNDARY + str + "-----");
        newLine();
    }

    private void writePostEncapsulationBoundary(String str) throws IOException {
        write(PEMFile.Part.END_BOUNDARY + str + "-----");
        newLine();
    }
}

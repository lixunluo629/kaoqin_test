package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.X509CertificateStructure;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/Certificate.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/Certificate.class */
public class Certificate {
    public static final Certificate EMPTY_CHAIN = new Certificate(new X509CertificateStructure[0]);
    protected X509CertificateStructure[] certs;

    protected static Certificate parse(InputStream inputStream) throws IOException {
        int uint24 = TlsUtils.readUint24(inputStream);
        if (uint24 == 0) {
            return EMPTY_CHAIN;
        }
        Vector vector = new Vector();
        while (uint24 > 0) {
            int uint242 = TlsUtils.readUint24(inputStream);
            uint24 -= 3 + uint242;
            byte[] bArr = new byte[uint242];
            TlsUtils.readFully(bArr, inputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            vector.addElement(X509CertificateStructure.getInstance(new ASN1InputStream(byteArrayInputStream).readObject()));
            if (byteArrayInputStream.available() > 0) {
                throw new IllegalArgumentException("Sorry, there is garbage data left after the certificate");
            }
        }
        X509CertificateStructure[] x509CertificateStructureArr = new X509CertificateStructure[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            x509CertificateStructureArr[i] = (X509CertificateStructure) vector.elementAt(i);
        }
        return new Certificate(x509CertificateStructureArr);
    }

    protected void encode(OutputStream outputStream) throws IOException {
        Vector vector = new Vector();
        int length = 0;
        for (int i = 0; i < this.certs.length; i++) {
            byte[] encoded = this.certs[i].getEncoded("DER");
            vector.addElement(encoded);
            length += encoded.length + 3;
        }
        TlsUtils.writeUint24(length + 3, outputStream);
        TlsUtils.writeUint24(length, outputStream);
        for (int i2 = 0; i2 < vector.size(); i2++) {
            TlsUtils.writeOpaque24((byte[]) vector.elementAt(i2), outputStream);
        }
    }

    public Certificate(X509CertificateStructure[] x509CertificateStructureArr) {
        if (x509CertificateStructureArr == null) {
            throw new IllegalArgumentException("'certs' cannot be null");
        }
        this.certs = x509CertificateStructureArr;
    }

    public X509CertificateStructure[] getCerts() {
        X509CertificateStructure[] x509CertificateStructureArr = new X509CertificateStructure[this.certs.length];
        System.arraycopy(this.certs, 0, x509CertificateStructureArr, 0, this.certs.length);
        return x509CertificateStructureArr;
    }

    public boolean isEmpty() {
        return this.certs.length == 0;
    }
}

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/BEROutputStream.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/BEROutputStream.class */
public class BEROutputStream extends DEROutputStream {
    public BEROutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override // org.bouncycastle.asn1.DEROutputStream
    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            writeNull();
        } else if (obj instanceof DERObject) {
            ((DERObject) obj).encode(this);
        } else {
            if (!(obj instanceof DEREncodable)) {
                throw new IOException("object not BEREncodable");
            }
            ((DEREncodable) obj).getDERObject().encode(this);
        }
    }
}

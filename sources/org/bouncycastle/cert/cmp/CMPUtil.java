package org.bouncycastle.cert.cmp;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Object;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/cmp/CMPUtil.class */
class CMPUtil {
    CMPUtil() {
    }

    static void derEncodeToStream(ASN1Object aSN1Object, OutputStream outputStream) throws IOException {
        try {
            aSN1Object.encodeTo(outputStream, "DER");
            outputStream.close();
        } catch (IOException e) {
            throw new CMPRuntimeException("unable to DER encode object: " + e.getMessage(), e);
        }
    }
}

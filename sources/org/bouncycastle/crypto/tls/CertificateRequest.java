package org.bouncycastle.crypto.tls;

import java.util.Vector;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/CertificateRequest.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/CertificateRequest.class */
public class CertificateRequest {
    private short[] certificateTypes;
    private Vector certificateAuthorities;

    public CertificateRequest(short[] sArr, Vector vector) {
        this.certificateTypes = sArr;
        this.certificateAuthorities = vector;
    }

    public short[] getCertificateTypes() {
        return this.certificateTypes;
    }

    public Vector getCertificateAuthorities() {
        return this.certificateAuthorities;
    }
}

package org.bouncycastle.crypto.tls;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/SecurityParameters.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/SecurityParameters.class */
public class SecurityParameters {
    byte[] clientRandom = null;
    byte[] serverRandom = null;
    byte[] masterSecret = null;

    public byte[] getClientRandom() {
        return this.clientRandom;
    }

    public byte[] getServerRandom() {
        return this.serverRandom;
    }

    public byte[] getMasterSecret() {
        return this.masterSecret;
    }
}

package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsCipherFactory.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsCipherFactory.class */
public interface TlsCipherFactory {
    TlsCipher createCipher(TlsClientContext tlsClientContext, int i, int i2) throws IOException;
}

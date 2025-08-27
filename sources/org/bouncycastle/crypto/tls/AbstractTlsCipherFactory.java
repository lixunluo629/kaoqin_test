package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/AbstractTlsCipherFactory.class */
public class AbstractTlsCipherFactory implements TlsCipherFactory {
    public TlsCipher createCipher(TlsContext tlsContext, int i, int i2) throws IOException {
        throw new TlsFatalAlert((short) 80);
    }
}

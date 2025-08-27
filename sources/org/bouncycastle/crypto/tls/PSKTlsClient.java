package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/PSKTlsClient.class */
public class PSKTlsClient extends AbstractTlsClient {
    protected TlsDHVerifier dhVerifier;
    protected TlsPSKIdentity pskIdentity;

    public PSKTlsClient(TlsPSKIdentity tlsPSKIdentity) {
        this(new DefaultTlsCipherFactory(), tlsPSKIdentity);
    }

    public PSKTlsClient(TlsCipherFactory tlsCipherFactory, TlsPSKIdentity tlsPSKIdentity) {
        this(tlsCipherFactory, new DefaultTlsDHVerifier(), tlsPSKIdentity);
    }

    public PSKTlsClient(TlsCipherFactory tlsCipherFactory, TlsDHVerifier tlsDHVerifier, TlsPSKIdentity tlsPSKIdentity) {
        super(tlsCipherFactory);
        this.dhVerifier = tlsDHVerifier;
        this.pskIdentity = tlsPSKIdentity;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public int[] getCipherSuites() {
        return new int[]{49207, 49205};
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public TlsKeyExchange getKeyExchange() throws IOException {
        int keyExchangeAlgorithm = TlsUtils.getKeyExchangeAlgorithm(this.selectedCipherSuite);
        switch (keyExchangeAlgorithm) {
            case 13:
            case 14:
            case 15:
            case 24:
                return createPSKKeyExchange(keyExchangeAlgorithm);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public TlsAuthentication getAuthentication() throws IOException {
        throw new TlsFatalAlert((short) 80);
    }

    protected TlsKeyExchange createPSKKeyExchange(int i) {
        return new TlsPSKKeyExchange(i, this.supportedSignatureAlgorithms, this.pskIdentity, null, this.dhVerifier, null, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }
}

package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/AbstractTlsKeyExchange.class */
public abstract class AbstractTlsKeyExchange implements TlsKeyExchange {
    protected int keyExchange;
    protected Vector supportedSignatureAlgorithms;
    protected TlsContext context;

    protected AbstractTlsKeyExchange(int i, Vector vector) {
        this.keyExchange = i;
        this.supportedSignatureAlgorithms = vector;
    }

    protected DigitallySigned parseSignature(InputStream inputStream) throws IOException {
        DigitallySigned digitallySigned = DigitallySigned.parse(this.context, inputStream);
        SignatureAndHashAlgorithm algorithm = digitallySigned.getAlgorithm();
        if (algorithm != null) {
            TlsUtils.verifySupportedSignatureAlgorithm(this.supportedSignatureAlgorithms, algorithm);
        }
        return digitallySigned;
    }

    public void init(TlsContext tlsContext) {
        this.context = tlsContext;
        ProtocolVersion clientVersion = tlsContext.getClientVersion();
        if (!TlsUtils.isSignatureAlgorithmsExtensionAllowed(clientVersion)) {
            if (this.supportedSignatureAlgorithms != null) {
                throw new IllegalStateException("supported_signature_algorithms not allowed for " + clientVersion);
            }
        } else if (this.supportedSignatureAlgorithms == null) {
            switch (this.keyExchange) {
                case 1:
                case 5:
                case 9:
                case 15:
                case 18:
                case 19:
                case 23:
                    this.supportedSignatureAlgorithms = TlsUtils.getDefaultRSASignatureAlgorithms();
                    return;
                case 2:
                case 4:
                case 6:
                case 8:
                case 10:
                case 11:
                case 12:
                case 20:
                default:
                    throw new IllegalStateException("unsupported key exchange algorithm");
                case 3:
                case 7:
                case 22:
                    this.supportedSignatureAlgorithms = TlsUtils.getDefaultDSSSignatureAlgorithms();
                    return;
                case 13:
                case 14:
                case 21:
                case 24:
                    return;
                case 16:
                case 17:
                    this.supportedSignatureAlgorithms = TlsUtils.getDefaultECDSASignatureAlgorithms();
                    return;
            }
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void processServerCertificate(Certificate certificate) throws IOException {
        if (this.supportedSignatureAlgorithms == null) {
        }
    }

    public void processServerCredentials(TlsCredentials tlsCredentials) throws IOException {
        processServerCertificate(tlsCredentials.getCertificate());
    }

    public boolean requiresServerKeyExchange() {
        return false;
    }

    public byte[] generateServerKeyExchange() throws IOException {
        if (requiresServerKeyExchange()) {
            throw new TlsFatalAlert((short) 80);
        }
        return null;
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void skipServerKeyExchange() throws IOException {
        if (requiresServerKeyExchange()) {
            throw new TlsFatalAlert((short) 10);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void processServerKeyExchange(InputStream inputStream) throws IOException {
        if (!requiresServerKeyExchange()) {
            throw new TlsFatalAlert((short) 10);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void skipClientCredentials() throws IOException {
    }

    public void processClientCertificate(Certificate certificate) throws IOException {
    }

    public void processClientKeyExchange(InputStream inputStream) throws IOException {
        throw new TlsFatalAlert((short) 80);
    }
}

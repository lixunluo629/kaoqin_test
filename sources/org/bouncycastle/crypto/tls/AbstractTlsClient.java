package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/AbstractTlsClient.class */
public abstract class AbstractTlsClient extends AbstractTlsPeer implements TlsClient {
    protected TlsCipherFactory cipherFactory;
    protected TlsClientContext context;
    protected Vector supportedSignatureAlgorithms;
    protected int[] namedCurves;
    protected short[] clientECPointFormats;
    protected short[] serverECPointFormats;
    protected int selectedCipherSuite;
    protected short selectedCompressionMethod;

    public AbstractTlsClient() {
        this(new DefaultTlsCipherFactory());
    }

    public AbstractTlsClient(TlsCipherFactory tlsCipherFactory) {
        this.cipherFactory = tlsCipherFactory;
    }

    protected boolean allowUnexpectedServerExtension(Integer num, byte[] bArr) throws IOException {
        switch (num.intValue()) {
            case 10:
                TlsECCUtils.readSupportedEllipticCurvesExtension(bArr);
                break;
            case 11:
                TlsECCUtils.readSupportedPointFormatsExtension(bArr);
                break;
        }
        return true;
    }

    protected void checkForUnexpectedServerExtension(Hashtable hashtable, Integer num) throws IOException {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, num);
        if (extensionData != null && !allowUnexpectedServerExtension(num, extensionData)) {
            throw new TlsFatalAlert((short) 47);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public void init(TlsClientContext tlsClientContext) {
        this.context = tlsClientContext;
    }

    public TlsSession getSessionToResume() {
        return null;
    }

    public ProtocolVersion getClientHelloRecordLayerVersion() {
        return getClientVersion();
    }

    public ProtocolVersion getClientVersion() {
        return ProtocolVersion.TLSv12;
    }

    public boolean isFallback() {
        return false;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public Hashtable getClientExtensions() throws IOException {
        Hashtable hashtableEnsureExtensionsInitialised = null;
        if (TlsUtils.isSignatureAlgorithmsExtensionAllowed(this.context.getClientVersion())) {
            this.supportedSignatureAlgorithms = TlsUtils.getDefaultSupportedSignatureAlgorithms();
            hashtableEnsureExtensionsInitialised = TlsExtensionsUtils.ensureExtensionsInitialised(null);
            TlsUtils.addSignatureAlgorithmsExtension(hashtableEnsureExtensionsInitialised, this.supportedSignatureAlgorithms);
        }
        if (TlsECCUtils.containsECCCipherSuites(getCipherSuites())) {
            this.namedCurves = new int[]{23, 24};
            this.clientECPointFormats = new short[]{0, 1, 2};
            hashtableEnsureExtensionsInitialised = TlsExtensionsUtils.ensureExtensionsInitialised(hashtableEnsureExtensionsInitialised);
            TlsECCUtils.addSupportedEllipticCurvesExtension(hashtableEnsureExtensionsInitialised, this.namedCurves);
            TlsECCUtils.addSupportedPointFormatsExtension(hashtableEnsureExtensionsInitialised, this.clientECPointFormats);
        }
        return hashtableEnsureExtensionsInitialised;
    }

    public ProtocolVersion getMinimumVersion() {
        return ProtocolVersion.TLSv10;
    }

    public void notifyServerVersion(ProtocolVersion protocolVersion) throws IOException {
        if (!getMinimumVersion().isEqualOrEarlierVersionOf(protocolVersion)) {
            throw new TlsFatalAlert((short) 70);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public short[] getCompressionMethods() {
        return new short[]{0};
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public void notifySessionID(byte[] bArr) {
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public void notifySelectedCipherSuite(int i) {
        this.selectedCipherSuite = i;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public void notifySelectedCompressionMethod(short s) {
        this.selectedCompressionMethod = s;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public void processServerExtensions(Hashtable hashtable) throws IOException {
        if (hashtable != null) {
            checkForUnexpectedServerExtension(hashtable, TlsUtils.EXT_signature_algorithms);
            checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_elliptic_curves);
            if (TlsECCUtils.isECCCipherSuite(this.selectedCipherSuite)) {
                this.serverECPointFormats = TlsECCUtils.getSupportedPointFormatsExtension(hashtable);
            } else {
                checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_ec_point_formats);
            }
            checkForUnexpectedServerExtension(hashtable, TlsExtensionsUtils.EXT_padding);
        }
    }

    public void processServerSupplementalData(Vector vector) throws IOException {
        if (vector != null) {
            throw new TlsFatalAlert((short) 10);
        }
    }

    public Vector getClientSupplementalData() throws IOException {
        return null;
    }

    @Override // org.bouncycastle.crypto.tls.TlsPeer, org.bouncycastle.crypto.tls.TlsClient
    public TlsCompression getCompression() throws IOException {
        switch (this.selectedCompressionMethod) {
            case 0:
                return new TlsNullCompression();
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [org.bouncycastle.crypto.tls.TlsClientContext, org.bouncycastle.crypto.tls.TlsContext] */
    @Override // org.bouncycastle.crypto.tls.TlsPeer, org.bouncycastle.crypto.tls.TlsClient
    public TlsCipher getCipher() throws IOException {
        return this.cipherFactory.createCipher((TlsContext) this.context, TlsUtils.getEncryptionAlgorithm(this.selectedCipherSuite), TlsUtils.getMACAlgorithm(this.selectedCipherSuite));
    }

    public void notifyNewSessionTicket(NewSessionTicket newSessionTicket) throws IOException {
    }
}

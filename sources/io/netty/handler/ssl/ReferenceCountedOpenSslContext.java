package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.internal.tcnative.CertificateVerifier;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import io.netty.internal.tcnative.SSLPrivateKeyMethod;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SuppressJava6Requirement;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslContext.class */
public abstract class ReferenceCountedOpenSslContext extends SslContext implements ReferenceCounted {
    private static final Integer DH_KEY_LENGTH;
    protected static final int VERIFY_DEPTH = 10;
    protected long ctx;
    private final List<String> unmodifiableCiphers;
    private final long sessionCacheSize;
    private final long sessionTimeout;
    private final OpenSslApplicationProtocolNegotiator apn;
    private final int mode;
    private final ResourceLeakTracker<ReferenceCountedOpenSslContext> leak;
    private final AbstractReferenceCounted refCnt;
    final Certificate[] keyCertChain;
    final ClientAuth clientAuth;
    final String[] protocols;
    final boolean enableOcsp;
    final OpenSslEngineMap engineMap;
    final ReadWriteLock ctxLock;
    private volatile int bioNonApplicationBufferSize;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ReferenceCountedOpenSslContext.class);
    private static final int DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE = Math.max(1, SystemPropertyUtil.getInt("io.netty.handler.ssl.openssl.bioNonApplicationBufferSize", 2048));
    static final boolean USE_TASKS = SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useTasks", false);
    private static final ResourceLeakDetector<ReferenceCountedOpenSslContext> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ReferenceCountedOpenSslContext.class);
    static final OpenSslApplicationProtocolNegotiator NONE_PROTOCOL_NEGOTIATOR = new OpenSslApplicationProtocolNegotiator() { // from class: io.netty.handler.ssl.ReferenceCountedOpenSslContext.2
        @Override // io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator
        public ApplicationProtocolConfig.Protocol protocol() {
            return ApplicationProtocolConfig.Protocol.NONE;
        }

        @Override // io.netty.handler.ssl.ApplicationProtocolNegotiator
        public List<String> protocols() {
            return Collections.emptyList();
        }

        @Override // io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator
        public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior() {
            return ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
        }

        @Override // io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator
        public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
            return ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT;
        }
    };

    @Override // io.netty.handler.ssl.SslContext
    public abstract OpenSslSessionContext sessionContext();

    static {
        Integer dhLen = null;
        try {
            String dhKeySize = SystemPropertyUtil.get("jdk.tls.ephemeralDHKeySize");
            if (dhKeySize != null) {
                try {
                    dhLen = Integer.valueOf(dhKeySize);
                } catch (NumberFormatException e) {
                    logger.debug("ReferenceCountedOpenSslContext supports -Djdk.tls.ephemeralDHKeySize={int}, but got: " + dhKeySize);
                }
            }
        } catch (Throwable th) {
        }
        DH_KEY_LENGTH = dhLen;
    }

    ReferenceCountedOpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apnCfg, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, boolean leakDetection) throws SSLException {
        this(ciphers, cipherFilter, toNegotiator(apnCfg), sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, leakDetection);
    }

    ReferenceCountedOpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, boolean leakDetection) throws SSLException {
        super(startTls);
        this.refCnt = new AbstractReferenceCounted() { // from class: io.netty.handler.ssl.ReferenceCountedOpenSslContext.1
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !ReferenceCountedOpenSslContext.class.desiredAssertionStatus();
            }

            @Override // io.netty.util.ReferenceCounted
            public ReferenceCounted touch(Object hint) {
                if (ReferenceCountedOpenSslContext.this.leak != null) {
                    ReferenceCountedOpenSslContext.this.leak.record(hint);
                }
                return ReferenceCountedOpenSslContext.this;
            }

            @Override // io.netty.util.AbstractReferenceCounted
            protected void deallocate() {
                ReferenceCountedOpenSslContext.this.destroy();
                if (ReferenceCountedOpenSslContext.this.leak != null) {
                    boolean closed = ReferenceCountedOpenSslContext.this.leak.close(ReferenceCountedOpenSslContext.this);
                    if (!$assertionsDisabled && !closed) {
                        throw new AssertionError();
                    }
                }
            }
        };
        this.engineMap = new DefaultOpenSslEngineMap();
        this.ctxLock = new ReentrantReadWriteLock();
        this.bioNonApplicationBufferSize = DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE;
        OpenSsl.ensureAvailability();
        if (enableOcsp && !OpenSsl.isOcspSupported()) {
            throw new IllegalStateException("OCSP is not supported.");
        }
        if (mode != 1 && mode != 0) {
            throw new IllegalArgumentException("mode most be either SSL.SSL_MODE_SERVER or SSL.SSL_MODE_CLIENT");
        }
        this.leak = leakDetection ? leakDetector.track(this) : null;
        this.mode = mode;
        this.clientAuth = isServer() ? (ClientAuth) ObjectUtil.checkNotNull(clientAuth, "clientAuth") : ClientAuth.NONE;
        this.protocols = protocols;
        this.enableOcsp = enableOcsp;
        this.keyCertChain = keyCertChain == null ? null : (Certificate[]) keyCertChain.clone();
        this.unmodifiableCiphers = Arrays.asList(((CipherSuiteFilter) ObjectUtil.checkNotNull(cipherFilter, "cipherFilter")).filterCipherSuites(ciphers, OpenSsl.DEFAULT_CIPHERS, OpenSsl.availableJavaCipherSuites()));
        this.apn = (OpenSslApplicationProtocolNegotiator) ObjectUtil.checkNotNull(apn, "apn");
        try {
            try {
                int protocolOpts = 30;
                this.ctx = SSLContext.make(OpenSsl.isTlsv13Supported() ? 30 | 32 : protocolOpts, mode);
                boolean tlsv13Supported = OpenSsl.isTlsv13Supported();
                StringBuilder cipherBuilder = new StringBuilder();
                StringBuilder cipherTLSv13Builder = new StringBuilder();
                try {
                    try {
                        if (this.unmodifiableCiphers.isEmpty()) {
                            SSLContext.setCipherSuite(this.ctx, "", false);
                            if (tlsv13Supported) {
                                SSLContext.setCipherSuite(this.ctx, "", true);
                            }
                        } else {
                            CipherSuiteConverter.convertToCipherStrings(this.unmodifiableCiphers, cipherBuilder, cipherTLSv13Builder, OpenSsl.isBoringSSL());
                            SSLContext.setCipherSuite(this.ctx, cipherBuilder.toString(), false);
                            if (tlsv13Supported) {
                                SSLContext.setCipherSuite(this.ctx, cipherTLSv13Builder.toString(), true);
                            }
                        }
                        int options = SSLContext.getOptions(this.ctx) | SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_NO_TLSv1_3 | SSL.SSL_OP_CIPHER_SERVER_PREFERENCE | SSL.SSL_OP_NO_COMPRESSION | SSL.SSL_OP_NO_TICKET;
                        SSLContext.setOptions(this.ctx, cipherBuilder.length() == 0 ? options | SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_NO_TLSv1 | SSL.SSL_OP_NO_TLSv1_1 | SSL.SSL_OP_NO_TLSv1_2 : options);
                        SSLContext.setMode(this.ctx, SSLContext.getMode(this.ctx) | SSL.SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER);
                        if (DH_KEY_LENGTH != null) {
                            SSLContext.setTmpDHLength(this.ctx, DH_KEY_LENGTH.intValue());
                        }
                        List<String> nextProtoList = apn.protocols();
                        if (!nextProtoList.isEmpty()) {
                            String[] appProtocols = (String[]) nextProtoList.toArray(new String[0]);
                            int selectorBehavior = opensslSelectorFailureBehavior(apn.selectorFailureBehavior());
                            switch (apn.protocol()) {
                                case NPN:
                                    SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
                                    break;
                                case ALPN:
                                    SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
                                    break;
                                case NPN_AND_ALPN:
                                    SSLContext.setNpnProtos(this.ctx, appProtocols, selectorBehavior);
                                    SSLContext.setAlpnProtos(this.ctx, appProtocols, selectorBehavior);
                                    break;
                                default:
                                    throw new Error();
                            }
                        }
                        sessionCacheSize = sessionCacheSize <= 0 ? SSLContext.setSessionCacheSize(this.ctx, 20480L) : sessionCacheSize;
                        this.sessionCacheSize = sessionCacheSize;
                        SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
                        sessionTimeout = sessionTimeout <= 0 ? SSLContext.setSessionCacheTimeout(this.ctx, 300L) : sessionTimeout;
                        this.sessionTimeout = sessionTimeout;
                        SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
                        if (enableOcsp) {
                            SSLContext.enableOcsp(this.ctx, isClient());
                        }
                        SSLContext.setUseTasks(this.ctx, USE_TASKS);
                        if (1 == 0) {
                            release();
                        }
                    } catch (SSLException e) {
                        throw e;
                    }
                } catch (Exception e2) {
                    throw new SSLException("failed to set cipher suite: " + this.unmodifiableCiphers, e2);
                }
            } catch (Exception e3) {
                throw new SSLException("failed to create an SSL_CTX", e3);
            }
        } catch (Throwable th) {
            if (0 == 0) {
                release();
            }
            throw th;
        }
    }

    private static int opensslSelectorFailureBehavior(ApplicationProtocolConfig.SelectorFailureBehavior behavior) {
        switch (behavior) {
            case NO_ADVERTISE:
                return 0;
            case CHOOSE_MY_LAST_PROTOCOL:
                return 1;
            default:
                throw new Error();
        }
    }

    @Override // io.netty.handler.ssl.SslContext
    public final List<String> cipherSuites() {
        return this.unmodifiableCiphers;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final long sessionCacheSize() {
        return this.sessionCacheSize;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final long sessionTimeout() {
        return this.sessionTimeout;
    }

    @Override // io.netty.handler.ssl.SslContext
    public ApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return this.apn;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final boolean isClient() {
        return this.mode == 0;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
        return newEngine0(alloc, peerHost, peerPort, true);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected final SslHandler newHandler(ByteBufAllocator alloc, boolean startTls) {
        return new SslHandler(newEngine0(alloc, null, -1, false), startTls);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls) {
        return new SslHandler(newEngine0(alloc, peerHost, peerPort, false), startTls);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected SslHandler newHandler(ByteBufAllocator alloc, boolean startTls, Executor executor) {
        return new SslHandler(newEngine0(alloc, null, -1, false), startTls, executor);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls, Executor executor) {
        return new SslHandler(newEngine0(alloc, peerHost, peerPort, false), executor);
    }

    SSLEngine newEngine0(ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode) {
        return new ReferenceCountedOpenSslEngine(this, alloc, peerHost, peerPort, jdkCompatibilityMode, true);
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc) {
        return newEngine(alloc, null, -1);
    }

    @Deprecated
    public final long context() {
        return sslCtxPointer();
    }

    @Deprecated
    public final OpenSslSessionStats stats() {
        return sessionContext().stats();
    }

    @Deprecated
    public void setRejectRemoteInitiatedRenegotiation(boolean rejectRemoteInitiatedRenegotiation) {
        if (!rejectRemoteInitiatedRenegotiation) {
            throw new UnsupportedOperationException("Renegotiation is not supported");
        }
    }

    @Deprecated
    public boolean getRejectRemoteInitiatedRenegotiation() {
        return true;
    }

    public void setBioNonApplicationBufferSize(int bioNonApplicationBufferSize) {
        this.bioNonApplicationBufferSize = ObjectUtil.checkPositiveOrZero(bioNonApplicationBufferSize, "bioNonApplicationBufferSize");
    }

    public int getBioNonApplicationBufferSize() {
        return this.bioNonApplicationBufferSize;
    }

    @Deprecated
    public final void setTicketKeys(byte[] keys) {
        sessionContext().setTicketKeys(keys);
    }

    @Deprecated
    public final long sslCtxPointer() {
        Lock readerLock = this.ctxLock.readLock();
        readerLock.lock();
        try {
            long sslCtx = SSLContext.getSslCtx(this.ctx);
            readerLock.unlock();
            return sslCtx;
        } catch (Throwable th) {
            readerLock.unlock();
            throw th;
        }
    }

    public final void setPrivateKeyMethod(OpenSslPrivateKeyMethod method) {
        ObjectUtil.checkNotNull(method, JamXmlElements.METHOD);
        Lock writerLock = this.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setPrivateKeyMethod(this.ctx, new PrivateKeyMethod(this.engineMap, method));
        } finally {
            writerLock.unlock();
        }
    }

    final void setUseTasks(boolean useTasks) {
        Lock writerLock = this.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setUseTasks(this.ctx, useTasks);
        } finally {
            writerLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroy() {
        Lock writerLock = this.ctxLock.writeLock();
        writerLock.lock();
        try {
            if (this.ctx != 0) {
                if (this.enableOcsp) {
                    SSLContext.disableOcsp(this.ctx);
                }
                SSLContext.free(this.ctx);
                this.ctx = 0L;
                OpenSslSessionContext context = sessionContext();
                if (context != null) {
                    context.destroy();
                }
            }
        } finally {
            writerLock.unlock();
        }
    }

    protected static X509Certificate[] certificates(byte[][] chain) {
        X509Certificate[] peerCerts = new X509Certificate[chain.length];
        for (int i = 0; i < peerCerts.length; i++) {
            peerCerts[i] = new OpenSslX509Certificate(chain[i]);
        }
        return peerCerts;
    }

    protected static X509TrustManager chooseTrustManager(TrustManager[] managers) {
        for (TrustManager m : managers) {
            if (m instanceof X509TrustManager) {
                if (PlatformDependent.javaVersion() >= 7) {
                    return OpenSslX509TrustManagerWrapper.wrapIfNeeded((X509TrustManager) m);
                }
                return (X509TrustManager) m;
            }
        }
        throw new IllegalStateException("no X509TrustManager found");
    }

    protected static X509KeyManager chooseX509KeyManager(KeyManager[] kms) {
        for (KeyManager km : kms) {
            if (km instanceof X509KeyManager) {
                return (X509KeyManager) km;
            }
        }
        throw new IllegalStateException("no X509KeyManager found");
    }

    static OpenSslApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config) {
        if (config == null) {
            return NONE_PROTOCOL_NEGOTIATOR;
        }
        switch (config.protocol()) {
            case NPN:
            case ALPN:
            case NPN_AND_ALPN:
                switch (config.selectedListenerFailureBehavior()) {
                    case CHOOSE_MY_LAST_PROTOCOL:
                    case ACCEPT:
                        switch (config.selectorFailureBehavior()) {
                            case NO_ADVERTISE:
                            case CHOOSE_MY_LAST_PROTOCOL:
                                return new OpenSslDefaultApplicationProtocolNegotiator(config);
                            default:
                                throw new UnsupportedOperationException("OpenSSL provider does not support " + config.selectorFailureBehavior() + " behavior");
                        }
                    default:
                        throw new UnsupportedOperationException("OpenSSL provider does not support " + config.selectedListenerFailureBehavior() + " behavior");
                }
            case NONE:
                return NONE_PROTOCOL_NEGOTIATOR;
            default:
                throw new Error();
        }
    }

    @SuppressJava6Requirement(reason = "Guarded by java version check")
    static boolean useExtendedTrustManager(X509TrustManager trustManager) {
        return PlatformDependent.javaVersion() >= 7 && (trustManager instanceof X509ExtendedTrustManager);
    }

    @Override // io.netty.util.ReferenceCounted
    public final int refCnt() {
        return this.refCnt.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted retain() {
        this.refCnt.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted retain(int increment) {
        this.refCnt.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted touch() {
        this.refCnt.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted touch(Object hint) {
        this.refCnt.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final boolean release() {
        return this.refCnt.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public final boolean release(int decrement) {
        return this.refCnt.release(decrement);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslContext$AbstractCertificateVerifier.class */
    static abstract class AbstractCertificateVerifier extends CertificateVerifier {
        private final OpenSslEngineMap engineMap;

        abstract void verify(ReferenceCountedOpenSslEngine referenceCountedOpenSslEngine, X509Certificate[] x509CertificateArr, String str) throws Exception;

        AbstractCertificateVerifier(OpenSslEngineMap engineMap) {
            this.engineMap = engineMap;
        }

        public final int verify(long ssl, byte[][] chain, String auth) {
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
            }
            X509Certificate[] peerCerts = ReferenceCountedOpenSslContext.certificates(chain);
            try {
                verify(engine, peerCerts, auth);
                return CertificateVerifier.X509_V_OK;
            } catch (Throwable cause) {
                ReferenceCountedOpenSslContext.logger.debug("verification of certificate failed", cause);
                engine.initHandshakeException(cause);
                if (cause instanceof OpenSslCertificateException) {
                    return ((OpenSslCertificateException) cause).errorCode();
                }
                if (cause instanceof CertificateExpiredException) {
                    return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                }
                if (cause instanceof CertificateNotYetValidException) {
                    return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                }
                if (PlatformDependent.javaVersion() >= 7) {
                    return translateToError(cause);
                }
                return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
            }
        }

        @SuppressJava6Requirement(reason = "Usage guarded by java version check")
        private static int translateToError(Throwable cause) {
            if (cause instanceof CertificateRevokedException) {
                return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
            }
            Throwable cause2 = cause.getCause();
            while (true) {
                Throwable wrapped = cause2;
                if (wrapped != null) {
                    if (wrapped instanceof CertPathValidatorException) {
                        CertPathValidatorException ex = (CertPathValidatorException) wrapped;
                        CertPathValidatorException.Reason reason = ex.getReason();
                        if (reason == CertPathValidatorException.BasicReason.EXPIRED) {
                            return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                        }
                        if (reason == CertPathValidatorException.BasicReason.NOT_YET_VALID) {
                            return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                        }
                        if (reason == CertPathValidatorException.BasicReason.REVOKED) {
                            return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
                        }
                    }
                    cause2 = wrapped.getCause();
                } else {
                    return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
                }
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslContext$DefaultOpenSslEngineMap.class */
    private static final class DefaultOpenSslEngineMap implements OpenSslEngineMap {
        private final Map<Long, ReferenceCountedOpenSslEngine> engines;

        private DefaultOpenSslEngineMap() {
            this.engines = PlatformDependent.newConcurrentHashMap();
        }

        @Override // io.netty.handler.ssl.OpenSslEngineMap
        public ReferenceCountedOpenSslEngine remove(long ssl) {
            return this.engines.remove(Long.valueOf(ssl));
        }

        @Override // io.netty.handler.ssl.OpenSslEngineMap
        public void add(ReferenceCountedOpenSslEngine engine) {
            this.engines.put(Long.valueOf(engine.sslPointer()), engine);
        }

        @Override // io.netty.handler.ssl.OpenSslEngineMap
        public ReferenceCountedOpenSslEngine get(long ssl) {
            return this.engines.get(Long.valueOf(ssl));
        }
    }

    static void setKeyMaterial(long ctx, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword) throws SSLException {
        long keyBio = 0;
        long keyCertChainBio = 0;
        long keyCertChainBio2 = 0;
        PemEncoded encoded = null;
        try {
            try {
                try {
                    encoded = PemX509Certificate.toPEM(ByteBufAllocator.DEFAULT, true, keyCertChain);
                    keyCertChainBio = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
                    keyCertChainBio2 = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
                    if (key != null) {
                        keyBio = toBIO(ByteBufAllocator.DEFAULT, key);
                    }
                    SSLContext.setCertificateBio(ctx, keyCertChainBio, keyBio, keyPassword == null ? "" : keyPassword);
                    SSLContext.setCertificateChainBio(ctx, keyCertChainBio2, true);
                    freeBio(keyBio);
                    freeBio(keyCertChainBio);
                    freeBio(keyCertChainBio2);
                    if (encoded != null) {
                        encoded.release();
                    }
                } catch (SSLException e) {
                    throw e;
                }
            } catch (Exception e2) {
                throw new SSLException("failed to set certificate and key", e2);
            }
        } catch (Throwable th) {
            freeBio(keyBio);
            freeBio(keyCertChainBio);
            freeBio(keyCertChainBio2);
            if (encoded != null) {
                encoded.release();
            }
            throw th;
        }
    }

    static void freeBio(long bio) {
        if (bio != 0) {
            SSL.freeBIO(bio);
        }
    }

    static long toBIO(ByteBufAllocator allocator, PrivateKey key) throws Exception {
        if (key == null) {
            return 0L;
        }
        PemEncoded pem = PemPrivateKey.toPEM(allocator, true, key);
        try {
            long bio = toBIO(allocator, pem.retain());
            pem.release();
            return bio;
        } catch (Throwable th) {
            pem.release();
            throw th;
        }
    }

    static long toBIO(ByteBufAllocator allocator, X509Certificate... certChain) throws Exception {
        if (certChain == null) {
            return 0L;
        }
        if (certChain.length == 0) {
            throw new IllegalArgumentException("certChain can't be empty");
        }
        PemEncoded pem = PemX509Certificate.toPEM(allocator, true, certChain);
        try {
            long bio = toBIO(allocator, pem.retain());
            pem.release();
            return bio;
        } catch (Throwable th) {
            pem.release();
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    static long toBIO(ByteBufAllocator allocator, PemEncoded pem) throws Exception {
        try {
            ByteBuf content = pem.content();
            if (content.isDirect()) {
                long jNewBIO = newBIO(content.retainedSlice());
                pem.release();
                return jNewBIO;
            }
            ByteBuf buffer = allocator.directBuffer(content.readableBytes());
            try {
                buffer.writeBytes(content, content.readerIndex(), content.readableBytes());
                long jNewBIO2 = newBIO(buffer.retainedSlice());
                try {
                    if (pem.isSensitive()) {
                        SslUtils.zeroout(buffer);
                    }
                    buffer.release();
                    return jNewBIO2;
                } catch (Throwable th) {
                    throw th;
                }
            } catch (Throwable th2) {
                try {
                    if (pem.isSensitive()) {
                        SslUtils.zeroout(buffer);
                    }
                    buffer.release();
                    throw th2;
                } finally {
                    buffer.release();
                }
            }
        } finally {
            pem.release();
        }
    }

    private static long newBIO(ByteBuf buffer) throws Exception {
        try {
            long bio = SSL.newMemBIO();
            int readable = buffer.readableBytes();
            if (SSL.bioWrite(bio, OpenSsl.memoryAddress(buffer) + buffer.readerIndex(), readable) != readable) {
                SSL.freeBIO(bio);
                throw new IllegalStateException("Could not write data to memory BIO");
            }
            return bio;
        } finally {
            buffer.release();
        }
    }

    static OpenSslKeyMaterialProvider providerFor(KeyManagerFactory factory, String password) {
        if (factory instanceof OpenSslX509KeyManagerFactory) {
            return ((OpenSslX509KeyManagerFactory) factory).newProvider();
        }
        if (factory instanceof OpenSslCachingX509KeyManagerFactory) {
            return ((OpenSslCachingX509KeyManagerFactory) factory).newProvider(password);
        }
        return new OpenSslKeyMaterialProvider(chooseX509KeyManager(factory.getKeyManagers()), password);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslContext$PrivateKeyMethod.class */
    private static final class PrivateKeyMethod implements SSLPrivateKeyMethod {
        private final OpenSslEngineMap engineMap;
        private final OpenSslPrivateKeyMethod keyMethod;

        PrivateKeyMethod(OpenSslEngineMap engineMap, OpenSslPrivateKeyMethod keyMethod) {
            this.engineMap = engineMap;
            this.keyMethod = keyMethod;
        }

        private ReferenceCountedOpenSslEngine retrieveEngine(long ssl) throws SSLException {
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                throw new SSLException("Could not find a " + StringUtil.simpleClassName((Class<?>) ReferenceCountedOpenSslEngine.class) + " for sslPointer " + ssl);
            }
            return engine;
        }

        public byte[] sign(long ssl, int signatureAlgorithm, byte[] digest) throws Exception {
            ReferenceCountedOpenSslEngine engine = retrieveEngine(ssl);
            try {
                return verifyResult(this.keyMethod.sign(engine, signatureAlgorithm, digest));
            } catch (Exception e) {
                engine.initHandshakeException(e);
                throw e;
            }
        }

        public byte[] decrypt(long ssl, byte[] input) throws Exception {
            ReferenceCountedOpenSslEngine engine = retrieveEngine(ssl);
            try {
                return verifyResult(this.keyMethod.decrypt(engine, input));
            } catch (Exception e) {
                engine.initHandshakeException(e);
                throw e;
            }
        }

        private static byte[] verifyResult(byte[] result) throws SignatureException {
            if (result == null) {
                throw new SignatureException();
            }
            return result;
        }
    }
}

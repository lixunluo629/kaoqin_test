package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.ReferenceCountedOpenSslContext;
import io.netty.internal.tcnative.CertificateCallback;
import io.netty.internal.tcnative.SSLContext;
import io.netty.internal.tcnative.SniHostNameMatcher;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SuppressJava6Requirement;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslServerContext.class */
public final class ReferenceCountedOpenSslServerContext extends ReferenceCountedOpenSslContext {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ReferenceCountedOpenSslServerContext.class);
    private static final byte[] ID = {110, 101, 116, 116, 121};
    private final OpenSslServerSessionContext sessionContext;

    ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, String keyStore) throws SSLException {
        this(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, toNegotiator(apn), sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls, enableOcsp, keyStore);
    }

    ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, String keyStore) throws SSLException {
        super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 1, (Certificate[]) keyCertChain, clientAuth, protocols, startTls, enableOcsp, true);
        boolean success = false;
        try {
            this.sessionContext = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, keyStore);
            success = true;
            if (1 == 0) {
                release();
            }
        } catch (Throwable th) {
            if (!success) {
                release();
            }
            throw th;
        }
    }

    @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext, io.netty.handler.ssl.SslContext
    public OpenSslServerSessionContext sessionContext() {
        return this.sessionContext;
    }

    static OpenSslServerSessionContext newSessionContext(ReferenceCountedOpenSslContext thiz, long ctx, OpenSslEngineMap engineMap, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, String keyStore) throws SSLException {
        OpenSslKeyMaterialProvider keyMaterialProvider = null;
        try {
            try {
                SSLContext.setVerify(ctx, 0, 10);
                if (!OpenSsl.useKeyManagerFactory()) {
                    if (keyManagerFactory != null) {
                        throw new IllegalArgumentException("KeyManagerFactory not supported");
                    }
                    ObjectUtil.checkNotNull(keyCertChain, "keyCertChain");
                    setKeyMaterial(ctx, keyCertChain, key, keyPassword);
                } else {
                    if (keyManagerFactory == null) {
                        char[] keyPasswordChars = keyStorePassword(keyPassword);
                        KeyStore ks = buildKeyStore(keyCertChain, key, keyPasswordChars, keyStore);
                        if (ks.aliases().hasMoreElements()) {
                            keyManagerFactory = new OpenSslX509KeyManagerFactory();
                        } else {
                            keyManagerFactory = new OpenSslCachingX509KeyManagerFactory(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()));
                        }
                        keyManagerFactory.init(ks, keyPasswordChars);
                    }
                    keyMaterialProvider = providerFor(keyManagerFactory, keyPassword);
                    SSLContext.setCertificateCallback(ctx, new OpenSslServerCertificateCallback(engineMap, new OpenSslKeyMaterialManager(keyMaterialProvider)));
                }
                try {
                    try {
                        if (trustCertCollection != null) {
                            trustManagerFactory = buildTrustManagerFactory(trustCertCollection, trustManagerFactory, keyStore);
                        } else if (trustManagerFactory == null) {
                            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                            trustManagerFactory.init((KeyStore) null);
                        }
                        X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
                        setVerifyCallback(ctx, engineMap, manager);
                        X509Certificate[] issuers = manager.getAcceptedIssuers();
                        if (issuers != null && issuers.length > 0) {
                            long bio = 0;
                            try {
                                bio = toBIO(ByteBufAllocator.DEFAULT, issuers);
                                if (!SSLContext.setCACertificateBio(ctx, bio)) {
                                    throw new SSLException("unable to setup accepted issuers for trustmanager " + manager);
                                }
                                freeBio(bio);
                            } catch (Throwable th) {
                                freeBio(bio);
                                throw th;
                            }
                        }
                        if (PlatformDependent.javaVersion() >= 8) {
                            SSLContext.setSniHostnameMatcher(ctx, new OpenSslSniHostnameMatcher(engineMap));
                        }
                        OpenSslServerSessionContext sessionContext = new OpenSslServerSessionContext(thiz, keyMaterialProvider);
                        sessionContext.setSessionIdContext(ID);
                        OpenSslKeyMaterialProvider keyMaterialProvider2 = null;
                        if (0 != 0) {
                            keyMaterialProvider2.destroy();
                        }
                        return sessionContext;
                    } catch (Exception e) {
                        throw new SSLException("unable to setup trustmanager", e);
                    }
                } catch (SSLException e2) {
                    throw e2;
                }
            } catch (Exception e3) {
                throw new SSLException("failed to set certificate and key", e3);
            }
        } catch (Throwable th2) {
            if (keyMaterialProvider != null) {
                keyMaterialProvider.destroy();
            }
            throw th2;
        }
    }

    @SuppressJava6Requirement(reason = "Guarded by java version check")
    private static void setVerifyCallback(long ctx, OpenSslEngineMap engineMap, X509TrustManager manager) {
        if (useExtendedTrustManager(manager)) {
            SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager) manager));
        } else {
            SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslServerContext$OpenSslServerCertificateCallback.class */
    private static final class OpenSslServerCertificateCallback implements CertificateCallback {
        private final OpenSslEngineMap engineMap;
        private final OpenSslKeyMaterialManager keyManagerHolder;

        OpenSslServerCertificateCallback(OpenSslEngineMap engineMap, OpenSslKeyMaterialManager keyManagerHolder) {
            this.engineMap = engineMap;
            this.keyManagerHolder = keyManagerHolder;
        }

        public void handle(long ssl, byte[] keyTypeBytes, byte[][] asn1DerEncodedPrincipals) throws Exception {
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                return;
            }
            try {
                this.keyManagerHolder.setKeyMaterialServerSide(engine);
            } catch (Throwable cause) {
                ReferenceCountedOpenSslServerContext.logger.debug("Failed to set the server-side key material", cause);
                engine.initHandshakeException(cause);
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslServerContext$TrustManagerVerifyCallback.class */
    private static final class TrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
        private final X509TrustManager manager;

        TrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509TrustManager manager) {
            super(engineMap);
            this.manager = manager;
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext.AbstractCertificateVerifier
        void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
            this.manager.checkClientTrusted(peerCerts, auth);
        }
    }

    @SuppressJava6Requirement(reason = "Usage guarded by java version check")
    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslServerContext$ExtendedTrustManagerVerifyCallback.class */
    private static final class ExtendedTrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
        private final X509ExtendedTrustManager manager;

        ExtendedTrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509ExtendedTrustManager manager) {
            super(engineMap);
            this.manager = OpenSslTlsv13X509ExtendedTrustManager.wrap(manager);
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext.AbstractCertificateVerifier
        void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
            this.manager.checkClientTrusted(peerCerts, auth, engine);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslServerContext$OpenSslSniHostnameMatcher.class */
    private static final class OpenSslSniHostnameMatcher implements SniHostNameMatcher {
        private final OpenSslEngineMap engineMap;

        OpenSslSniHostnameMatcher(OpenSslEngineMap engineMap) {
            this.engineMap = engineMap;
        }

        public boolean match(long ssl, String hostname) {
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                ReferenceCountedOpenSslServerContext.logger.warn("No ReferenceCountedOpenSslEngine found for SSL pointer: {}", Long.valueOf(ssl));
                return false;
            }
            return engine.checkSniHostnameMatch(hostname.getBytes(CharsetUtil.UTF_8));
        }
    }
}

package io.netty.handler.ssl;

import com.moredian.onpremise.core.utils.RSAUtils;
import io.netty.handler.ssl.ReferenceCountedOpenSslContext;
import io.netty.internal.tcnative.CertificateCallback;
import io.netty.internal.tcnative.SSLContext;
import io.netty.util.internal.SuppressJava6Requirement;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslClientContext.class */
public final class ReferenceCountedOpenSslClientContext extends ReferenceCountedOpenSslContext {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ReferenceCountedOpenSslClientContext.class);
    private static final Set<String> SUPPORTED_KEY_TYPES = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList(RSAUtils.RSA_KEY_ALGORITHM, "DH_RSA", "EC", "EC_RSA", "EC_EC")));
    private final OpenSslSessionContext sessionContext;

    ReferenceCountedOpenSslClientContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout, boolean enableOcsp, String keyStore) throws SSLException {
        super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, 0, (Certificate[]) keyCertChain, ClientAuth.NONE, protocols, false, enableOcsp, true);
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
    public OpenSslSessionContext sessionContext() {
        return this.sessionContext;
    }

    static OpenSslSessionContext newSessionContext(ReferenceCountedOpenSslContext thiz, long ctx, OpenSslEngineMap engineMap, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, String keyStore) throws SSLException {
        KeyManagerFactory keyManagerFactory2;
        if ((key == null && keyCertChain != null) || (key != null && keyCertChain == null)) {
            throw new IllegalArgumentException("Either both keyCertChain and key needs to be null or none of them");
        }
        OpenSslKeyMaterialProvider keyMaterialProvider = null;
        try {
            try {
                if (!OpenSsl.useKeyManagerFactory()) {
                    if (keyManagerFactory != null) {
                        throw new IllegalArgumentException("KeyManagerFactory not supported");
                    }
                    if (keyCertChain != null) {
                        setKeyMaterial(ctx, keyCertChain, key, keyPassword);
                    }
                } else {
                    if (keyManagerFactory == null && keyCertChain != null) {
                        char[] keyPasswordChars = keyStorePassword(keyPassword);
                        KeyStore ks = buildKeyStore(keyCertChain, key, keyPasswordChars, keyStore);
                        if (ks.aliases().hasMoreElements()) {
                            keyManagerFactory2 = new OpenSslX509KeyManagerFactory();
                        } else {
                            keyManagerFactory2 = new OpenSslCachingX509KeyManagerFactory(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()));
                        }
                        keyManagerFactory2.init(ks, keyPasswordChars);
                        keyMaterialProvider = providerFor(keyManagerFactory2, keyPassword);
                    } else if (keyManagerFactory != null) {
                        keyMaterialProvider = providerFor(keyManagerFactory, keyPassword);
                    }
                    if (keyMaterialProvider != null) {
                        OpenSslKeyMaterialManager materialManager = new OpenSslKeyMaterialManager(keyMaterialProvider);
                        SSLContext.setCertificateCallback(ctx, new OpenSslClientCertificateCallback(engineMap, materialManager));
                    }
                }
                SSLContext.setVerify(ctx, 1, 10);
                try {
                    if (trustCertCollection != null) {
                        trustManagerFactory = buildTrustManagerFactory(trustCertCollection, trustManagerFactory, keyStore);
                    } else if (trustManagerFactory == null) {
                        trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        trustManagerFactory.init((KeyStore) null);
                    }
                    X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
                    setVerifyCallback(ctx, engineMap, manager);
                    OpenSslClientSessionContext context = new OpenSslClientSessionContext(thiz, keyMaterialProvider);
                    OpenSslKeyMaterialProvider keyMaterialProvider2 = null;
                    if (0 != 0) {
                        keyMaterialProvider2.destroy();
                    }
                    return context;
                } catch (Exception e) {
                    if (keyMaterialProvider != null) {
                        keyMaterialProvider.destroy();
                    }
                    throw new SSLException("unable to setup trustmanager", e);
                }
            } catch (Exception e2) {
                throw new SSLException("failed to set certificate and key", e2);
            }
        } catch (Throwable th) {
            if (keyMaterialProvider != null) {
                keyMaterialProvider.destroy();
            }
            throw th;
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

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslClientContext$OpenSslClientSessionContext.class */
    static final class OpenSslClientSessionContext extends OpenSslSessionContext {
        OpenSslClientSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider) {
            super(context, provider);
        }

        @Override // javax.net.ssl.SSLSessionContext
        public void setSessionTimeout(int seconds) {
            if (seconds < 0) {
                throw new IllegalArgumentException();
            }
        }

        @Override // javax.net.ssl.SSLSessionContext
        public int getSessionTimeout() {
            return 0;
        }

        @Override // javax.net.ssl.SSLSessionContext
        public void setSessionCacheSize(int size) {
            if (size < 0) {
                throw new IllegalArgumentException();
            }
        }

        @Override // javax.net.ssl.SSLSessionContext
        public int getSessionCacheSize() {
            return 0;
        }

        @Override // io.netty.handler.ssl.OpenSslSessionContext
        public void setSessionCacheEnabled(boolean enabled) {
        }

        @Override // io.netty.handler.ssl.OpenSslSessionContext
        public boolean isSessionCacheEnabled() {
            return false;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslClientContext$TrustManagerVerifyCallback.class */
    private static final class TrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
        private final X509TrustManager manager;

        TrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509TrustManager manager) {
            super(engineMap);
            this.manager = manager;
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext.AbstractCertificateVerifier
        void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
            this.manager.checkServerTrusted(peerCerts, auth);
        }
    }

    @SuppressJava6Requirement(reason = "Usage guarded by java version check")
    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslClientContext$ExtendedTrustManagerVerifyCallback.class */
    private static final class ExtendedTrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
        private final X509ExtendedTrustManager manager;

        ExtendedTrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509ExtendedTrustManager manager) {
            super(engineMap);
            this.manager = OpenSslTlsv13X509ExtendedTrustManager.wrap(manager);
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext.AbstractCertificateVerifier
        void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
            this.manager.checkServerTrusted(peerCerts, auth, engine);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ReferenceCountedOpenSslClientContext$OpenSslClientCertificateCallback.class */
    private static final class OpenSslClientCertificateCallback implements CertificateCallback {
        private final OpenSslEngineMap engineMap;
        private final OpenSslKeyMaterialManager keyManagerHolder;

        OpenSslClientCertificateCallback(OpenSslEngineMap engineMap, OpenSslKeyMaterialManager keyManagerHolder) {
            this.engineMap = engineMap;
            this.keyManagerHolder = keyManagerHolder;
        }

        public void handle(long ssl, byte[] keyTypeBytes, byte[][] asn1DerEncodedPrincipals) throws Exception {
            X500Principal[] issuers;
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                return;
            }
            try {
                Set<String> keyTypesSet = supportedClientKeyTypes(keyTypeBytes);
                String[] keyTypes = (String[]) keyTypesSet.toArray(new String[0]);
                if (asn1DerEncodedPrincipals == null) {
                    issuers = null;
                } else {
                    issuers = new X500Principal[asn1DerEncodedPrincipals.length];
                    for (int i = 0; i < asn1DerEncodedPrincipals.length; i++) {
                        issuers[i] = new X500Principal(asn1DerEncodedPrincipals[i]);
                    }
                }
                this.keyManagerHolder.setKeyMaterialClientSide(engine, keyTypes, issuers);
            } catch (Throwable cause) {
                ReferenceCountedOpenSslClientContext.logger.debug("request of key failed", cause);
                engine.initHandshakeException(cause);
            }
        }

        private static Set<String> supportedClientKeyTypes(byte[] clientCertificateTypes) {
            if (clientCertificateTypes == null) {
                return ReferenceCountedOpenSslClientContext.SUPPORTED_KEY_TYPES;
            }
            Set<String> result = new HashSet<>(clientCertificateTypes.length);
            for (byte keyTypeCode : clientCertificateTypes) {
                String keyType = clientKeyType(keyTypeCode);
                if (keyType != null) {
                    result.add(keyType);
                }
            }
            return result;
        }

        private static String clientKeyType(byte clientCertificateType) {
            switch (clientCertificateType) {
                case 1:
                    return RSAUtils.RSA_KEY_ALGORITHM;
                case 3:
                    return "DH_RSA";
                case 64:
                    return "EC";
                case 65:
                    return "EC_RSA";
                case 66:
                    return "EC_EC";
                default:
                    return null;
            }
        }
    }
}

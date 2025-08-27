package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.internal.tcnative.Buffer;
import io.netty.internal.tcnative.CertificateCallback;
import io.netty.internal.tcnative.Library;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.language.bm.Rule;
import org.apache.tomcat.util.net.Constants;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSsl.class */
public final class OpenSsl {
    private static final InternalLogger logger;
    private static final Throwable UNAVAILABILITY_CAUSE;
    static final List<String> DEFAULT_CIPHERS;
    static final Set<String> AVAILABLE_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_OPENSSL_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_JAVA_CIPHER_SUITES;
    private static final boolean SUPPORTS_KEYMANAGER_FACTORY;
    private static final boolean USE_KEYMANAGER_FACTORY;
    private static final boolean SUPPORTS_OCSP;
    private static final boolean TLSV13_SUPPORTED;
    private static final boolean IS_BORINGSSL;
    static final Set<String> SUPPORTED_PROTOCOLS_SET;
    private static final String CERT = "-----BEGIN CERTIFICATE-----\nMIICrjCCAZagAwIBAgIIdSvQPv1QAZQwDQYJKoZIhvcNAQELBQAwFjEUMBIGA1UEAxMLZXhhbXBs\nZS5jb20wIBcNMTgwNDA2MjIwNjU5WhgPOTk5OTEyMzEyMzU5NTlaMBYxFDASBgNVBAMTC2V4YW1w\nbGUuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAggbWsmDQ6zNzRZ5AW8E3eoGl\nqWvOBDb5Fs1oBRrVQHuYmVAoaqwDzXYJ0LOwa293AgWEQ1jpcbZ2hpoYQzqEZBTLnFhMrhRFlH6K\nbJND8Y33kZ/iSVBBDuGbdSbJShlM+4WwQ9IAso4MZ4vW3S1iv5fGGpLgbtXRmBf/RU8omN0Gijlv\nWlLWHWijLN8xQtySFuBQ7ssW8RcKAary3pUm6UUQB+Co6lnfti0Tzag8PgjhAJq2Z3wbsGRnP2YS\nvYoaK6qzmHXRYlp/PxrjBAZAmkLJs4YTm/XFF+fkeYx4i9zqHbyone5yerRibsHaXZWLnUL+rFoe\nMdKvr0VS3sGmhQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQADQi441pKmXf9FvUV5EHU4v8nJT9Iq\nyqwsKwXnr7AsUlDGHBD7jGrjAXnG5rGxuNKBQ35wRxJATKrUtyaquFUL6H8O6aGQehiFTk6zmPbe\n12Gu44vqqTgIUxnv3JQJiox8S2hMxsSddpeCmSdvmalvD6WG4NthH6B9ZaBEiep1+0s0RUaBYn73\nI7CCUaAtbjfR6pcJjrFk5ei7uwdQZFSJtkP2z8r7zfeANJddAKFlkaMWn7u+OIVuB4XPooWicObk\nNAHFtP65bocUYnDpTVdiyvn8DdqyZ/EO8n1bBKBzuSLplk2msW4pdgaFgY7Vw/0wzcFXfUXmL1uy\nG8sQD/wx\n-----END CERTIFICATE-----";
    private static final String KEY = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCBtayYNDrM3NFnkBbwTd6gaWp\na84ENvkWzWgFGtVAe5iZUChqrAPNdgnQs7Brb3cCBYRDWOlxtnaGmhhDOoRkFMucWEyuFEWUfops\nk0PxjfeRn+JJUEEO4Zt1JslKGUz7hbBD0gCyjgxni9bdLWK/l8YakuBu1dGYF/9FTyiY3QaKOW9a\nUtYdaKMs3zFC3JIW4FDuyxbxFwoBqvLelSbpRRAH4KjqWd+2LRPNqDw+COEAmrZnfBuwZGc/ZhK9\nihorqrOYddFiWn8/GuMEBkCaQsmzhhOb9cUX5+R5jHiL3OodvKid7nJ6tGJuwdpdlYudQv6sWh4x\n0q+vRVLewaaFAgMBAAECggEAP8tPJvFtTxhNJAkCloHz0D0vpDHqQBMgntlkgayqmBqLwhyb18pR\ni0qwgh7HHc7wWqOOQuSqlEnrWRrdcI6TSe8R/sErzfTQNoznKWIPYcI/hskk4sdnQ//Yn9/Jvnsv\nU/BBjOTJxtD+sQbhAl80JcA3R+5sArURQkfzzHOL/YMqzAsn5hTzp7HZCxUqBk3KaHRxV7NefeOE\nxlZuWSmxYWfbFIs4kx19/1t7h8CHQWezw+G60G2VBtSBBxDnhBWvqG6R/wpzJ3nEhPLLY9T+XIHe\nipzdMOOOUZorfIg7M+pyYPji+ZIZxIpY5OjrOzXHciAjRtr5Y7l99K1CG1LguQKBgQDrQfIMxxtZ\nvxU/1cRmUV9l7pt5bjV5R6byXq178LxPKVYNjdZ840Q0/OpZEVqaT1xKVi35ohP1QfNjxPLlHD+K\niDAR9z6zkwjIrbwPCnb5kuXy4lpwPcmmmkva25fI7qlpHtbcuQdoBdCfr/KkKaUCMPyY89LCXgEw\n5KTDj64UywKBgQCNfbO+eZLGzhiHhtNJurresCsIGWlInv322gL8CSfBMYl6eNfUTZvUDdFhPISL\nUljKWzXDrjw0ujFSPR0XhUGtiq89H+HUTuPPYv25gVXO+HTgBFZEPl4PpA+BUsSVZy0NddneyqLk\n42Wey9omY9Q8WsdNQS5cbUvy0uG6WFoX7wKBgQDZ1jpW8pa0x2bZsQsm4vo+3G5CRnZlUp+XlWt2\ndDcp5dC0xD1zbs1dc0NcLeGDOTDv9FSl7hok42iHXXq8AygjEm/QcuwwQ1nC2HxmQP5holAiUs4D\nWHM8PWs3wFYPzE459EBoKTxeaeP/uWAn+he8q7d5uWvSZlEcANs/6e77eQKBgD21Ar0hfFfj7mK8\n9E0FeRZBsqK3omkfnhcYgZC11Xa2SgT1yvs2Va2n0RcdM5kncr3eBZav2GYOhhAdwyBM55XuE/sO\neokDVutNeuZ6d5fqV96TRaRBpvgfTvvRwxZ9hvKF4Vz+9wfn/JvCwANaKmegF6ejs7pvmF3whq2k\ndrZVAoGAX5YxQ5XMTD0QbMAl7/6qp6S58xNoVdfCkmkj1ZLKaHKIjS/benkKGlySVQVPexPfnkZx\np/Vv9yyphBoudiTBS9Uog66ueLYZqpgxlM/6OhYg86Gm3U2ycvMxYjBM1NFiyze21AqAhI+HX+Ot\nmraV2/guSgDgZAhukRZzeQ2RucI=\n-----END PRIVATE KEY-----";
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v147, types: [io.netty.handler.ssl.PemEncoded, java.lang.String[]] */
    static {
        long sslCtx;
        long certBio;
        long keyBio;
        long cert;
        long key;
        $assertionsDisabled = !OpenSsl.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) OpenSsl.class);
        Throwable cause = null;
        if (SystemPropertyUtil.getBoolean("io.netty.handler.ssl.noOpenSsl", false)) {
            cause = new UnsupportedOperationException("OpenSSL was explicit disabled with -Dio.netty.handler.ssl.noOpenSsl=true");
            logger.debug("netty-tcnative explicit disabled; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.", cause);
        } else {
            try {
                Class.forName("io.netty.internal.tcnative.SSL", false, OpenSsl.class.getClassLoader());
            } catch (ClassNotFoundException t) {
                cause = t;
                logger.debug("netty-tcnative not in the classpath; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.");
            }
            if (cause == null) {
                try {
                    loadTcNative();
                } catch (Throwable t2) {
                    cause = t2;
                    logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable, unless the application has already loaded the symbols by some other means. See https://netty.io/wiki/forked-tomcat-native.html for more information.", t2);
                }
                try {
                    String engine = SystemPropertyUtil.get("io.netty.handler.ssl.openssl.engine", null);
                    if (engine == null) {
                        logger.debug("Initialize netty-tcnative using engine: 'default'");
                    } else {
                        logger.debug("Initialize netty-tcnative using engine: '{}'", engine);
                    }
                    initializeTcNative(engine);
                    cause = null;
                } catch (Throwable t3) {
                    if (cause == null) {
                        cause = t3;
                    }
                    logger.debug("Failed to initialize netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable. See https://netty.io/wiki/forked-tomcat-native.html for more information.", t3);
                }
            }
        }
        UNAVAILABILITY_CAUSE = cause;
        if (cause == null) {
            logger.debug("netty-tcnative using native library: {}", SSL.versionString());
            List<String> defaultCiphers = new ArrayList<>();
            LinkedHashSet linkedHashSet = new LinkedHashSet(128);
            boolean supportsKeyManagerFactory = false;
            boolean useKeyManagerFactory = false;
            boolean tlsv13Supported = false;
            IS_BORINGSSL = "BoringSSL".equals(versionString());
            try {
                sslCtx = SSLContext.make(63, 1);
                certBio = 0;
                keyBio = 0;
                cert = 0;
                key = 0;
            } catch (Exception e) {
                logger.warn("Failed to get the list of available OpenSSL cipher suites.", (Throwable) e);
            }
            try {
                try {
                    StringBuilder tlsv13Ciphers = new StringBuilder();
                    Iterator<String> it = SslUtils.TLSV13_CIPHERS.iterator();
                    while (it.hasNext()) {
                        String converted = CipherSuiteConverter.toOpenSsl(it.next(), IS_BORINGSSL);
                        if (converted != null) {
                            tlsv13Ciphers.append(converted).append(':');
                        }
                    }
                    if (tlsv13Ciphers.length() == 0) {
                        tlsv13Supported = false;
                    } else {
                        tlsv13Ciphers.setLength(tlsv13Ciphers.length() - 1);
                        SSLContext.setCipherSuite(sslCtx, tlsv13Ciphers.toString(), true);
                        tlsv13Supported = true;
                    }
                } catch (Exception e2) {
                    tlsv13Supported = false;
                }
                SSLContext.setCipherSuite(sslCtx, Rule.ALL, false);
                long ssl = SSL.newSSL(sslCtx, true);
                try {
                    ?? ciphers = SSL.getCiphers(ssl);
                    for (String str : ciphers) {
                        if (str != 0 && !str.isEmpty() && !linkedHashSet.contains(str) && (tlsv13Supported || !SslUtils.isTLSv13Cipher(str))) {
                            linkedHashSet.add(str);
                        }
                    }
                    if (IS_BORINGSSL) {
                        Collections.addAll(linkedHashSet, "TLS_AES_128_GCM_SHA256", "TLS_AES_256_GCM_SHA384", "TLS_CHACHA20_POLY1305_SHA256", "AEAD-AES128-GCM-SHA256", "AEAD-AES256-GCM-SHA384", "AEAD-CHACHA20-POLY1305-SHA256");
                    }
                    try {
                        PemEncoded privateKey = PemPrivateKey.valueOf(KEY.getBytes(CharsetUtil.US_ASCII));
                        try {
                            SSLContext.setCertificateCallback(sslCtx, (CertificateCallback) null);
                            X509Certificate certificate = selfSignedCertificate();
                            certBio = ReferenceCountedOpenSslContext.toBIO(ByteBufAllocator.DEFAULT, certificate);
                            cert = SSL.parseX509Chain(certBio);
                            keyBio = ReferenceCountedOpenSslContext.toBIO(UnpooledByteBufAllocator.DEFAULT, privateKey.retain());
                            key = SSL.parsePrivateKey(keyBio, (String) null);
                            SSL.setKeyMaterial(ssl, cert, key);
                            supportsKeyManagerFactory = true;
                            try {
                                boolean propertySet = SystemPropertyUtil.contains("io.netty.handler.ssl.openssl.useKeyManagerFactory");
                                if (!IS_BORINGSSL) {
                                    useKeyManagerFactory = SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useKeyManagerFactory", true);
                                    if (propertySet) {
                                        logger.info("System property 'io.netty.handler.ssl.openssl.useKeyManagerFactory' is deprecated and so will be ignored in the future");
                                    }
                                } else {
                                    useKeyManagerFactory = true;
                                    if (propertySet) {
                                        logger.info("System property 'io.netty.handler.ssl.openssl.useKeyManagerFactory' is deprecated and will be ignored when using BoringSSL");
                                    }
                                }
                            } catch (Throwable th) {
                                logger.debug("Failed to get useKeyManagerFactory system property.");
                            }
                            privateKey.release();
                        } catch (Error e3) {
                            logger.debug("KeyManagerFactory not supported.");
                            privateKey.release();
                        }
                        SSL.freeSSL(ssl);
                        if (certBio != 0) {
                            SSL.freeBIO(certBio);
                        }
                        if (keyBio != 0) {
                            SSL.freeBIO(keyBio);
                        }
                        if (cert != 0) {
                            SSL.freeX509Chain(cert);
                        }
                        if (key != 0) {
                            SSL.freePrivateKey(key);
                        }
                        SSLContext.free(sslCtx);
                        AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.unmodifiableSet(linkedHashSet);
                        Set<String> availableJavaCipherSuites = new LinkedHashSet<>(AVAILABLE_OPENSSL_CIPHER_SUITES.size() * 2);
                        for (String cipher : AVAILABLE_OPENSSL_CIPHER_SUITES) {
                            if (!SslUtils.isTLSv13Cipher(cipher)) {
                                availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, Constants.SSL_PROTO_TLS));
                                availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, ch.qos.logback.core.net.ssl.SSL.DEFAULT_PROTOCOL));
                            } else {
                                availableJavaCipherSuites.add(cipher);
                            }
                        }
                        SslUtils.addIfSupported(availableJavaCipherSuites, defaultCiphers, SslUtils.DEFAULT_CIPHER_SUITES);
                        SslUtils.addIfSupported(availableJavaCipherSuites, defaultCiphers, SslUtils.TLSV13_CIPHER_SUITES);
                        SslUtils.useFallbackCiphersIfDefaultIsEmpty(defaultCiphers, availableJavaCipherSuites);
                        DEFAULT_CIPHERS = Collections.unmodifiableList(defaultCiphers);
                        AVAILABLE_JAVA_CIPHER_SUITES = Collections.unmodifiableSet(availableJavaCipherSuites);
                        Set<String> availableCipherSuites = new LinkedHashSet<>(AVAILABLE_OPENSSL_CIPHER_SUITES.size() + AVAILABLE_JAVA_CIPHER_SUITES.size());
                        availableCipherSuites.addAll(AVAILABLE_OPENSSL_CIPHER_SUITES);
                        availableCipherSuites.addAll(AVAILABLE_JAVA_CIPHER_SUITES);
                        AVAILABLE_CIPHER_SUITES = availableCipherSuites;
                        SUPPORTS_KEYMANAGER_FACTORY = supportsKeyManagerFactory;
                        USE_KEYMANAGER_FACTORY = useKeyManagerFactory;
                        Set<String> protocols = new LinkedHashSet<>(6);
                        protocols.add(Constants.SSL_PROTO_SSLv2Hello);
                        if (doesSupportProtocol(1, SSL.SSL_OP_NO_SSLv2)) {
                            protocols.add(Constants.SSL_PROTO_SSLv2);
                        }
                        if (doesSupportProtocol(2, SSL.SSL_OP_NO_SSLv3)) {
                            protocols.add(Constants.SSL_PROTO_SSLv3);
                        }
                        if (doesSupportProtocol(4, SSL.SSL_OP_NO_TLSv1)) {
                            protocols.add(Constants.SSL_PROTO_TLSv1);
                        }
                        if (doesSupportProtocol(8, SSL.SSL_OP_NO_TLSv1_1)) {
                            protocols.add(Constants.SSL_PROTO_TLSv1_1);
                        }
                        if (doesSupportProtocol(16, SSL.SSL_OP_NO_TLSv1_2)) {
                            protocols.add(Constants.SSL_PROTO_TLSv1_2);
                        }
                        if (tlsv13Supported && doesSupportProtocol(32, SSL.SSL_OP_NO_TLSv1_3)) {
                            protocols.add(Constants.SSL_PROTO_TLSv1_3);
                            TLSV13_SUPPORTED = true;
                        } else {
                            TLSV13_SUPPORTED = false;
                        }
                        SUPPORTED_PROTOCOLS_SET = Collections.unmodifiableSet(protocols);
                        SUPPORTS_OCSP = doesSupportOcsp();
                        if (logger.isDebugEnabled()) {
                            logger.debug("Supported protocols (OpenSSL): {} ", SUPPORTED_PROTOCOLS_SET);
                            logger.debug("Default cipher suites (OpenSSL): {}", DEFAULT_CIPHERS);
                            return;
                        }
                        return;
                    } catch (Throwable th2) {
                        ciphers.release();
                        throw th2;
                    }
                } catch (Throwable th3) {
                    SSL.freeSSL(ssl);
                    if (0 != 0) {
                        SSL.freeBIO(0L);
                    }
                    if (0 != 0) {
                        SSL.freeBIO(0L);
                    }
                    if (0 != 0) {
                        SSL.freeX509Chain(0L);
                    }
                    if (0 != 0) {
                        SSL.freePrivateKey(0L);
                    }
                    throw th3;
                }
            } catch (Throwable th4) {
                SSLContext.free(sslCtx);
                throw th4;
            }
        }
        DEFAULT_CIPHERS = Collections.emptyList();
        AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.emptySet();
        AVAILABLE_JAVA_CIPHER_SUITES = Collections.emptySet();
        AVAILABLE_CIPHER_SUITES = Collections.emptySet();
        SUPPORTS_KEYMANAGER_FACTORY = false;
        USE_KEYMANAGER_FACTORY = false;
        SUPPORTED_PROTOCOLS_SET = Collections.emptySet();
        SUPPORTS_OCSP = false;
        TLSV13_SUPPORTED = false;
        IS_BORINGSSL = false;
    }

    static X509Certificate selfSignedCertificate() throws CertificateException {
        return (X509Certificate) SslContext.X509_CERT_FACTORY.generateCertificate(new ByteArrayInputStream(CERT.getBytes(CharsetUtil.US_ASCII)));
    }

    private static boolean doesSupportOcsp() {
        boolean supportsOcsp = false;
        if (version() >= 268443648) {
            long sslCtx = -1;
            try {
                sslCtx = SSLContext.make(16, 1);
                SSLContext.enableOcsp(sslCtx, false);
                supportsOcsp = true;
                if (sslCtx != -1) {
                    SSLContext.free(sslCtx);
                }
            } catch (Exception e) {
                if (sslCtx != -1) {
                    SSLContext.free(sslCtx);
                }
            } catch (Throwable th) {
                if (sslCtx != -1) {
                    SSLContext.free(sslCtx);
                }
                throw th;
            }
        }
        return supportsOcsp;
    }

    private static boolean doesSupportProtocol(int protocol, int opt) {
        if (opt == 0) {
            return false;
        }
        long sslCtx = -1;
        try {
            sslCtx = SSLContext.make(protocol, 2);
            if (sslCtx != -1) {
                SSLContext.free(sslCtx);
            }
            return true;
        } catch (Exception e) {
            if (sslCtx != -1) {
                SSLContext.free(sslCtx);
            }
            return false;
        } catch (Throwable th) {
            if (sslCtx != -1) {
                SSLContext.free(sslCtx);
            }
            throw th;
        }
    }

    public static boolean isAvailable() {
        return UNAVAILABILITY_CAUSE == null;
    }

    @Deprecated
    public static boolean isAlpnSupported() {
        return ((long) version()) >= 268443648;
    }

    public static boolean isOcspSupported() {
        return SUPPORTS_OCSP;
    }

    public static int version() {
        if (isAvailable()) {
            return SSL.version();
        }
        return -1;
    }

    public static String versionString() {
        if (isAvailable()) {
            return SSL.versionString();
        }
        return null;
    }

    public static void ensureAvailability() {
        if (UNAVAILABILITY_CAUSE != null) {
            throw ((Error) new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
        }
    }

    public static Throwable unavailabilityCause() {
        return UNAVAILABILITY_CAUSE;
    }

    @Deprecated
    public static Set<String> availableCipherSuites() {
        return availableOpenSslCipherSuites();
    }

    public static Set<String> availableOpenSslCipherSuites() {
        return AVAILABLE_OPENSSL_CIPHER_SUITES;
    }

    public static Set<String> availableJavaCipherSuites() {
        return AVAILABLE_JAVA_CIPHER_SUITES;
    }

    public static boolean isCipherSuiteAvailable(String cipherSuite) {
        String converted = CipherSuiteConverter.toOpenSsl(cipherSuite, IS_BORINGSSL);
        if (converted != null) {
            cipherSuite = converted;
        }
        return AVAILABLE_OPENSSL_CIPHER_SUITES.contains(cipherSuite);
    }

    public static boolean supportsKeyManagerFactory() {
        return SUPPORTS_KEYMANAGER_FACTORY;
    }

    @Deprecated
    public static boolean supportsHostnameValidation() {
        return isAvailable();
    }

    static boolean useKeyManagerFactory() {
        return USE_KEYMANAGER_FACTORY;
    }

    static long memoryAddress(ByteBuf buf) {
        if ($assertionsDisabled || buf.isDirect()) {
            return buf.hasMemoryAddress() ? buf.memoryAddress() : Buffer.address(buf.nioBuffer());
        }
        throw new AssertionError();
    }

    private OpenSsl() {
    }

    private static void loadTcNative() throws Exception {
        String os = PlatformDependent.normalizedOs();
        String arch = PlatformDependent.normalizedArch();
        Set<String> libNames = new LinkedHashSet<>(5);
        if ("linux".equalsIgnoreCase(os)) {
            Set<String> classifiers = PlatformDependent.normalizedLinuxClassifiers();
            for (String classifier : classifiers) {
                libNames.add("netty_tcnative_" + os + '_' + arch + "_" + classifier);
            }
            libNames.add("netty_tcnative_" + os + '_' + arch);
            libNames.add("netty_tcnative_" + os + '_' + arch + "_fedora");
        } else {
            libNames.add("netty_tcnative_" + os + '_' + arch);
        }
        libNames.add("netty_tcnative_" + arch);
        libNames.add("netty_tcnative");
        NativeLibraryLoader.loadFirstAvailable(SSL.class.getClassLoader(), (String[]) libNames.toArray(new String[0]));
    }

    private static boolean initializeTcNative(String engine) throws Exception {
        return Library.initialize("provided", engine);
    }

    static void releaseIfNeeded(ReferenceCounted counted) {
        if (counted.refCnt() > 0) {
            ReferenceCountUtil.safeRelease(counted);
        }
    }

    static boolean isTlsv13Supported() {
        return TLSV13_SUPPORTED;
    }

    static boolean isBoringSSL() {
        return IS_BORINGSSL;
    }
}

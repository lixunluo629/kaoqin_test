package io.netty.handler.ssl.util;

import com.moredian.onpremise.core.utils.RSAUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/util/SelfSignedCertificate.class */
public final class SelfSignedCertificate {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SelfSignedCertificate.class);
    private static final Date DEFAULT_NOT_BEFORE = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotBefore", System.currentTimeMillis() - 31536000000L));
    private static final Date DEFAULT_NOT_AFTER = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotAfter", 253402300799000L));
    private static final int DEFAULT_KEY_LENGTH_BITS = SystemPropertyUtil.getInt("io.netty.handler.ssl.util.selfSignedKeyStrength", 2048);
    private final File certificate;
    private final File privateKey;
    private final X509Certificate cert;
    private final PrivateKey key;

    public SelfSignedCertificate() throws CertificateException {
        this(DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, RSAUtils.RSA_KEY_ALGORITHM, DEFAULT_KEY_LENGTH_BITS);
    }

    public SelfSignedCertificate(Date notBefore, Date notAfter) throws CertificateException {
        this("localhost", notBefore, notAfter, RSAUtils.RSA_KEY_ALGORITHM, DEFAULT_KEY_LENGTH_BITS);
    }

    public SelfSignedCertificate(Date notBefore, Date notAfter, String algorithm, int bits) throws CertificateException {
        this("localhost", notBefore, notAfter, algorithm, bits);
    }

    public SelfSignedCertificate(String fqdn) throws CertificateException {
        this(fqdn, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, RSAUtils.RSA_KEY_ALGORITHM, DEFAULT_KEY_LENGTH_BITS);
    }

    public SelfSignedCertificate(String fqdn, String algorithm, int bits) throws CertificateException {
        this(fqdn, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, algorithm, bits);
    }

    public SelfSignedCertificate(String fqdn, Date notBefore, Date notAfter) throws CertificateException {
        this(fqdn, ThreadLocalInsecureRandom.current(), DEFAULT_KEY_LENGTH_BITS, notBefore, notAfter, RSAUtils.RSA_KEY_ALGORITHM);
    }

    public SelfSignedCertificate(String fqdn, Date notBefore, Date notAfter, String algorithm, int bits) throws CertificateException {
        this(fqdn, ThreadLocalInsecureRandom.current(), bits, notBefore, notAfter, algorithm);
    }

    public SelfSignedCertificate(String fqdn, SecureRandom random, int bits) throws CertificateException {
        this(fqdn, random, bits, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, RSAUtils.RSA_KEY_ALGORITHM);
    }

    public SelfSignedCertificate(String fqdn, SecureRandom random, String algorithm, int bits) throws CertificateException {
        this(fqdn, random, bits, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, algorithm);
    }

    public SelfSignedCertificate(String fqdn, SecureRandom random, int bits, Date notBefore, Date notAfter) throws CertificateException {
        this(fqdn, random, bits, notBefore, notAfter, RSAUtils.RSA_KEY_ALGORITHM);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:41:0x017f A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00fc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public SelfSignedCertificate(java.lang.String r8, java.security.SecureRandom r9, int r10, java.util.Date r11, java.util.Date r12, java.lang.String r13) throws java.security.NoSuchAlgorithmException, java.io.IOException, java.security.cert.CertificateException {
        /*
            Method dump skipped, instructions count: 384
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.util.SelfSignedCertificate.<init>(java.lang.String, java.security.SecureRandom, int, java.util.Date, java.util.Date, java.lang.String):void");
    }

    public File certificate() {
        return this.certificate;
    }

    public File privateKey() {
        return this.privateKey;
    }

    public X509Certificate cert() {
        return this.cert;
    }

    public PrivateKey key() {
        return this.key;
    }

    public void delete() {
        safeDelete(this.certificate);
        safeDelete(this.privateKey);
    }

    /* JADX WARN: Finally extract failed */
    static String[] newSelfSignedCertificate(String fqdn, PrivateKey key, X509Certificate cert) throws IOException, CertificateEncodingException {
        ByteBuf wrappedBuf = Unpooled.wrappedBuffer(key.getEncoded());
        try {
            ByteBuf encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                String keyText = "-----BEGIN PRIVATE KEY-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
                encodedBuf.release();
                File keyFile = File.createTempFile("keyutil_" + fqdn + '_', ".key");
                keyFile.deleteOnExit();
                OutputStream keyOut = new FileOutputStream(keyFile);
                try {
                    keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
                    keyOut.close();
                    keyOut = null;
                    if (0 != 0) {
                        safeClose(keyFile, null);
                        safeDelete(keyFile);
                    }
                    wrappedBuf = Unpooled.wrappedBuffer(cert.getEncoded());
                    try {
                        encodedBuf = Base64.encode(wrappedBuf, true);
                        try {
                            String certText = "-----BEGIN CERTIFICATE-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
                            encodedBuf.release();
                            wrappedBuf.release();
                            File certFile = File.createTempFile("keyutil_" + fqdn + '_', ".crt");
                            certFile.deleteOnExit();
                            OutputStream certOut = new FileOutputStream(certFile);
                            try {
                                certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
                                certOut.close();
                                certOut = null;
                                if (0 != 0) {
                                    safeClose(certFile, null);
                                    safeDelete(certFile);
                                    safeDelete(keyFile);
                                }
                                return new String[]{certFile.getPath(), keyFile.getPath()};
                            } catch (Throwable th) {
                                if (certOut != null) {
                                    safeClose(certFile, certOut);
                                    safeDelete(certFile);
                                    safeDelete(keyFile);
                                }
                                throw th;
                            }
                        } finally {
                        }
                    } finally {
                        wrappedBuf.release();
                    }
                } catch (Throwable th2) {
                    if (keyOut != null) {
                        safeClose(keyFile, keyOut);
                        safeDelete(keyFile);
                    }
                    throw th2;
                }
            } finally {
            }
        } finally {
        }
    }

    private static void safeDelete(File certFile) {
        if (!certFile.delete() && logger.isWarnEnabled()) {
            logger.warn("Failed to delete a file: " + certFile);
        }
    }

    private static void safeClose(File keyFile, OutputStream keyOut) throws IOException {
        try {
            keyOut.close();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to close a file: " + keyFile, (Throwable) e);
            }
        }
    }
}

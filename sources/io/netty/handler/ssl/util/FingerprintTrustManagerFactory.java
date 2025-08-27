package io.netty.handler.ssl.util;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/util/FingerprintTrustManagerFactory.class */
public final class FingerprintTrustManagerFactory extends SimpleTrustManagerFactory {
    private static final int SHA1_BYTE_LEN = 20;
    private static final int SHA1_HEX_LEN = 40;
    private final TrustManager tm;
    private final byte[][] fingerprints;
    private static final Pattern FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
    private static final Pattern FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
    private static final FastThreadLocal<MessageDigest> tlmd = new FastThreadLocal<MessageDigest>() { // from class: io.netty.handler.ssl.util.FingerprintTrustManagerFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new Error(e);
            }
        }
    };

    public FingerprintTrustManagerFactory(Iterable<String> fingerprints) {
        this(toFingerprintArray(fingerprints));
    }

    public FingerprintTrustManagerFactory(String... fingerprints) {
        this(toFingerprintArray(Arrays.asList(fingerprints)));
    }

    public FingerprintTrustManagerFactory(byte[]... fingerprints) {
        byte[] f;
        this.tm = new X509TrustManager() { // from class: io.netty.handler.ssl.util.FingerprintTrustManagerFactory.2
            @Override // javax.net.ssl.X509TrustManager
            public void checkClientTrusted(X509Certificate[] chain, String s) throws CertificateException {
                checkTrusted("client", chain);
            }

            @Override // javax.net.ssl.X509TrustManager
            public void checkServerTrusted(X509Certificate[] chain, String s) throws CertificateException {
                checkTrusted("server", chain);
            }

            private void checkTrusted(String type, X509Certificate[] chain) throws CertificateException {
                X509Certificate cert = chain[0];
                byte[] fingerprint = fingerprint(cert);
                boolean found = false;
                byte[][] bArr = FingerprintTrustManagerFactory.this.fingerprints;
                int length = bArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    byte[] allowedFingerprint = bArr[i];
                    if (!Arrays.equals(fingerprint, allowedFingerprint)) {
                        i++;
                    } else {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new CertificateException(type + " certificate with unknown fingerprint: " + cert.getSubjectDN());
                }
            }

            private byte[] fingerprint(X509Certificate cert) throws CertificateEncodingException {
                MessageDigest md = (MessageDigest) FingerprintTrustManagerFactory.tlmd.get();
                md.reset();
                return md.digest(cert.getEncoded());
            }

            @Override // javax.net.ssl.X509TrustManager
            public X509Certificate[] getAcceptedIssuers() {
                return EmptyArrays.EMPTY_X509_CERTIFICATES;
            }
        };
        ObjectUtil.checkNotNull(fingerprints, "fingerprints");
        ArrayList arrayList = new ArrayList(fingerprints.length);
        int length = fingerprints.length;
        for (int i = 0; i < length && (f = fingerprints[i]) != null; i++) {
            if (f.length != 20) {
                throw new IllegalArgumentException("malformed fingerprint: " + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(f)) + " (expected: SHA1)");
            }
            arrayList.add(f.clone());
        }
        this.fingerprints = (byte[][]) arrayList.toArray((Object[]) new byte[0]);
    }

    private static byte[][] toFingerprintArray(Iterable<String> fingerprints) {
        String f;
        ObjectUtil.checkNotNull(fingerprints, "fingerprints");
        List<byte[]> list = new ArrayList<>();
        Iterator<String> it = fingerprints.iterator();
        while (it.hasNext() && (f = it.next()) != null) {
            if (!FINGERPRINT_PATTERN.matcher(f).matches()) {
                throw new IllegalArgumentException("malformed fingerprint: " + f);
            }
            String f2 = FINGERPRINT_STRIP_PATTERN.matcher(f).replaceAll("");
            if (f2.length() != 40) {
                throw new IllegalArgumentException("malformed fingerprint: " + f2 + " (expected: SHA1)");
            }
            list.add(StringUtil.decodeHexDump(f2));
        }
        return (byte[][]) list.toArray((Object[]) new byte[0]);
    }

    @Override // io.netty.handler.ssl.util.SimpleTrustManagerFactory
    protected void engineInit(KeyStore keyStore) throws Exception {
    }

    @Override // io.netty.handler.ssl.util.SimpleTrustManagerFactory
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }

    @Override // io.netty.handler.ssl.util.SimpleTrustManagerFactory
    protected TrustManager[] engineGetTrustManagers() {
        return new TrustManager[]{this.tm};
    }
}

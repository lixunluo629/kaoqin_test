package com.mysql.jdbc;

import com.moredian.onpremise.core.utils.RSAUtils;
import com.mysql.jdbc.SocketMetadata;
import com.mysql.jdbc.util.Base64Decoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ExportControlled.class */
public class ExportControlled {
    private static final String SQL_STATE_BAD_SSL_PARAMS = "08000";
    private static final String TLSv1 = "TLSv1";
    private static final String TLSv1_1 = "TLSv1.1";
    private static final String TLSv1_2 = "TLSv1.2";
    private static final String[] TLS_PROTOCOLS = {"TLSv1.2", "TLSv1.1", "TLSv1"};

    protected static boolean enabled() {
        return true;
    }

    protected static void transformSocketToSSLSocket(MysqlIO mysqlIO) throws SQLException {
        String[] tryProtocols;
        SocketFactory sslFact = new StandardSSLSocketFactory(getSSLSocketFactoryDefaultOrConfigured(mysqlIO), mysqlIO.socketFactory, mysqlIO.mysqlConnection);
        try {
            mysqlIO.mysqlConnection = sslFact.connect(mysqlIO.host, mysqlIO.port, null);
            String enabledTLSProtocols = mysqlIO.connection.getEnabledTLSProtocols();
            if (enabledTLSProtocols != null && enabledTLSProtocols.length() > 0) {
                tryProtocols = enabledTLSProtocols.split("\\s*,\\s*");
            } else if (mysqlIO.versionMeetsMinimum(8, 0, 4) || (mysqlIO.versionMeetsMinimum(5, 6, 0) && Util.isEnterpriseEdition(mysqlIO.getServerVersion()))) {
                tryProtocols = TLS_PROTOCOLS;
            } else {
                tryProtocols = new String[]{"TLSv1.1", "TLSv1"};
            }
            List<String> configuredProtocols = new ArrayList<>(Arrays.asList(tryProtocols));
            List<String> jvmSupportedProtocols = Arrays.asList(((SSLSocket) mysqlIO.mysqlConnection).getSupportedProtocols());
            List<String> allowedProtocols = new ArrayList<>();
            String[] arr$ = TLS_PROTOCOLS;
            for (String protocol : arr$) {
                if (jvmSupportedProtocols.contains(protocol) && configuredProtocols.contains(protocol)) {
                    allowedProtocols.add(protocol);
                }
            }
            ((SSLSocket) mysqlIO.mysqlConnection).setEnabledProtocols((String[]) allowedProtocols.toArray(new String[0]));
            String enabledSSLCipherSuites = mysqlIO.connection.getEnabledSSLCipherSuites();
            boolean overrideCiphers = enabledSSLCipherSuites != null && enabledSSLCipherSuites.length() > 0;
            List<String> allowedCiphers = null;
            if (overrideCiphers) {
                allowedCiphers = new ArrayList<>();
                List<String> availableCiphers = Arrays.asList(((SSLSocket) mysqlIO.mysqlConnection).getEnabledCipherSuites());
                String[] arr$2 = enabledSSLCipherSuites.split("\\s*,\\s*");
                for (String cipher : arr$2) {
                    if (availableCiphers.contains(cipher)) {
                        allowedCiphers.add(cipher);
                    }
                }
            } else {
                boolean disableDHAlgorithm = false;
                if ((mysqlIO.versionMeetsMinimum(5, 5, 45) && !mysqlIO.versionMeetsMinimum(5, 6, 0)) || ((mysqlIO.versionMeetsMinimum(5, 6, 26) && !mysqlIO.versionMeetsMinimum(5, 7, 0)) || mysqlIO.versionMeetsMinimum(5, 7, 6))) {
                    if (Util.getJVMVersion() < 8) {
                        disableDHAlgorithm = true;
                    }
                } else if (Util.getJVMVersion() >= 8) {
                    disableDHAlgorithm = true;
                }
                if (disableDHAlgorithm) {
                    allowedCiphers = new ArrayList<>();
                    String[] arr$3 = ((SSLSocket) mysqlIO.mysqlConnection).getEnabledCipherSuites();
                    for (String cipher2 : arr$3) {
                        if (!disableDHAlgorithm || (cipher2.indexOf("_DHE_") <= -1 && cipher2.indexOf("_DH_") <= -1)) {
                            allowedCiphers.add(cipher2);
                        }
                    }
                }
            }
            if (allowedCiphers != null) {
                ((SSLSocket) mysqlIO.mysqlConnection).setEnabledCipherSuites((String[]) allowedCiphers.toArray(new String[0]));
            }
            ((SSLSocket) mysqlIO.mysqlConnection).startHandshake();
            if (mysqlIO.connection.getUseUnbufferedInput()) {
                mysqlIO.mysqlInput = mysqlIO.mysqlConnection.getInputStream();
            } else {
                mysqlIO.mysqlInput = new BufferedInputStream(mysqlIO.mysqlConnection.getInputStream(), 16384);
            }
            mysqlIO.mysqlOutput = new BufferedOutputStream(mysqlIO.mysqlConnection.getOutputStream(), 16384);
            mysqlIO.mysqlOutput.flush();
            mysqlIO.socketFactory = sslFact;
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(mysqlIO.connection, mysqlIO.getLastPacketSentTimeMs(), mysqlIO.getLastPacketReceivedTimeMs(), ioEx, mysqlIO.getExceptionInterceptor());
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ExportControlled$StandardSSLSocketFactory.class */
    public static class StandardSSLSocketFactory implements SocketFactory, SocketMetadata {
        private SSLSocket rawSocket = null;
        private final SSLSocketFactory sslFact;
        private final SocketFactory existingSocketFactory;
        private final Socket existingSocket;

        public StandardSSLSocketFactory(SSLSocketFactory sslFact, SocketFactory existingSocketFactory, Socket existingSocket) {
            this.sslFact = sslFact;
            this.existingSocketFactory = existingSocketFactory;
            this.existingSocket = existingSocket;
        }

        @Override // com.mysql.jdbc.SocketFactory
        public Socket afterHandshake() throws IOException {
            this.existingSocketFactory.afterHandshake();
            return this.rawSocket;
        }

        @Override // com.mysql.jdbc.SocketFactory
        public Socket beforeHandshake() throws IOException {
            return this.rawSocket;
        }

        @Override // com.mysql.jdbc.SocketFactory
        public Socket connect(String host, int portNumber, Properties props) throws IOException {
            this.rawSocket = (SSLSocket) this.sslFact.createSocket(this.existingSocket, host, portNumber, true);
            return this.rawSocket;
        }

        @Override // com.mysql.jdbc.SocketMetadata
        public boolean isLocallyConnected(ConnectionImpl conn) throws SQLException {
            return SocketMetadata.Helper.isLocallyConnected(conn);
        }
    }

    private ExportControlled() {
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ExportControlled$X509TrustManagerWrapper.class */
    public static class X509TrustManagerWrapper implements X509TrustManager {
        private X509TrustManager origTm;
        private boolean verifyServerCert;
        private CertificateFactory certFactory;
        private PKIXParameters validatorParams;
        private CertPathValidator validator;

        public X509TrustManagerWrapper(X509TrustManager tm, boolean verifyServerCertificate) throws CertificateException {
            this.origTm = null;
            this.verifyServerCert = false;
            this.certFactory = null;
            this.validatorParams = null;
            this.validator = null;
            this.origTm = tm;
            this.verifyServerCert = verifyServerCertificate;
            if (verifyServerCertificate) {
                try {
                    Set<TrustAnchor> anch = new HashSet<>();
                    X509Certificate[] arr$ = tm.getAcceptedIssuers();
                    for (X509Certificate cert : arr$) {
                        anch.add(new TrustAnchor(cert, null));
                    }
                    this.validatorParams = new PKIXParameters(anch);
                    this.validatorParams.setRevocationEnabled(false);
                    this.validator = CertPathValidator.getInstance("PKIX");
                    this.certFactory = CertificateFactory.getInstance("X.509");
                } catch (Exception e) {
                    throw new CertificateException(e);
                }
            }
        }

        public X509TrustManagerWrapper() {
            this.origTm = null;
            this.verifyServerCert = false;
            this.certFactory = null;
            this.validatorParams = null;
            this.validator = null;
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return this.origTm != null ? this.origTm.getAcceptedIssuers() : new X509Certificate[0];
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException, CertPathValidatorException, InvalidAlgorithmParameterException {
            for (X509Certificate x509Certificate : chain) {
                x509Certificate.checkValidity();
            }
            if (this.validatorParams != null) {
                X509CertSelector certSelect = new X509CertSelector();
                certSelect.setSerialNumber(chain[0].getSerialNumber());
                try {
                    CertPath certPath = this.certFactory.generateCertPath(Arrays.asList(chain));
                    CertPathValidatorResult result = this.validator.validate(certPath, this.validatorParams);
                    ((PKIXCertPathValidatorResult) result).getTrustAnchor().getTrustedCert().checkValidity();
                } catch (InvalidAlgorithmParameterException e) {
                    throw new CertificateException(e);
                } catch (CertPathValidatorException e2) {
                    throw new CertificateException(e2);
                }
            }
            if (this.verifyServerCert) {
                this.origTm.checkServerTrusted(chain, authType);
            }
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.origTm.checkClientTrusted(chain, authType);
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private static javax.net.ssl.SSLSocketFactory getSSLSocketFactoryDefaultOrConfigured(com.mysql.jdbc.MysqlIO r6) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 1166
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ExportControlled.getSSLSocketFactoryDefaultOrConfigured(com.mysql.jdbc.MysqlIO):javax.net.ssl.SSLSocketFactory");
    }

    public static boolean isSSLEstablished(Socket socket) {
        if (socket == null) {
            return false;
        }
        return SSLSocket.class.isAssignableFrom(socket.getClass());
    }

    public static RSAPublicKey decodeRSAPublicKey(String key, ExceptionInterceptor interceptor) throws SQLException, NoSuchAlgorithmException {
        try {
            if (key == null) {
                throw new SQLException("key parameter is null");
            }
            int offset = key.indexOf(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR) + 1;
            int len = key.indexOf("-----END PUBLIC KEY-----") - offset;
            byte[] certificateData = Base64Decoder.decode(key.getBytes(), offset, len);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(certificateData);
            KeyFactory kf = KeyFactory.getInstance(RSAUtils.RSA_KEY_ALGORITHM);
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception ex) {
            throw SQLError.createSQLException("Unable to decode public key", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, ex, interceptor);
        }
    }

    public static byte[] encryptWithRSAPublicKey(byte[] source, RSAPublicKey key, String transformation, ExceptionInterceptor interceptor) throws SQLException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(1, key);
            return cipher.doFinal(source);
        } catch (Exception ex) {
            throw SQLError.createSQLException(ex.getMessage(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, ex, interceptor);
        }
    }
}

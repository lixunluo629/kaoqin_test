package io.netty.handler.ssl;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SuppressJava6Requirement;
import java.net.Socket;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.X509ExtendedTrustManager;
import org.apache.tomcat.util.net.Constants;

@SuppressJava6Requirement(reason = "Usage guarded by java version check")
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslTlsv13X509ExtendedTrustManager.class */
final class OpenSslTlsv13X509ExtendedTrustManager extends X509ExtendedTrustManager {
    private final X509ExtendedTrustManager tm;

    private OpenSslTlsv13X509ExtendedTrustManager(X509ExtendedTrustManager tm) {
        this.tm = tm;
    }

    static X509ExtendedTrustManager wrap(X509ExtendedTrustManager tm) {
        if (PlatformDependent.javaVersion() < 11 && OpenSsl.isTlsv13Supported()) {
            return new OpenSslTlsv13X509ExtendedTrustManager(tm);
        }
        return tm;
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
        this.tm.checkClientTrusted(x509Certificates, s, socket);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
        this.tm.checkServerTrusted(x509Certificates, s, socket);
    }

    private static SSLEngine wrapEngine(final SSLEngine engine) {
        final SSLSession session = engine.getHandshakeSession();
        if (session != null && Constants.SSL_PROTO_TLSv1_3.equals(session.getProtocol())) {
            return new JdkSslEngine(engine) { // from class: io.netty.handler.ssl.OpenSslTlsv13X509ExtendedTrustManager.1
                @Override // io.netty.handler.ssl.JdkSslEngine, io.netty.handler.ssl.ApplicationProtocolAccessor
                public String getNegotiatedApplicationProtocol() {
                    if (engine instanceof ApplicationProtocolAccessor) {
                        return ((ApplicationProtocolAccessor) engine).getNegotiatedApplicationProtocol();
                    }
                    return super.getNegotiatedApplicationProtocol();
                }

                @Override // io.netty.handler.ssl.JdkSslEngine, javax.net.ssl.SSLEngine
                public SSLSession getHandshakeSession() {
                    if (PlatformDependent.javaVersion() >= 7 && (session instanceof ExtendedOpenSslSession)) {
                        final ExtendedOpenSslSession extendedOpenSslSession = (ExtendedOpenSslSession) session;
                        return new ExtendedOpenSslSession(extendedOpenSslSession) { // from class: io.netty.handler.ssl.OpenSslTlsv13X509ExtendedTrustManager.1.1
                            @Override // io.netty.handler.ssl.ExtendedOpenSslSession, javax.net.ssl.ExtendedSSLSession
                            public List getRequestedServerNames() {
                                return extendedOpenSslSession.getRequestedServerNames();
                            }

                            @Override // javax.net.ssl.ExtendedSSLSession
                            public String[] getPeerSupportedSignatureAlgorithms() {
                                return extendedOpenSslSession.getPeerSupportedSignatureAlgorithms();
                            }

                            @Override // io.netty.handler.ssl.ExtendedOpenSslSession, javax.net.ssl.SSLSession
                            public String getProtocol() {
                                return Constants.SSL_PROTO_TLSv1_2;
                            }
                        };
                    }
                    return new SSLSession() { // from class: io.netty.handler.ssl.OpenSslTlsv13X509ExtendedTrustManager.1.2
                        @Override // javax.net.ssl.SSLSession
                        public byte[] getId() {
                            return session.getId();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public SSLSessionContext getSessionContext() {
                            return session.getSessionContext();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public long getCreationTime() {
                            return session.getCreationTime();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public long getLastAccessedTime() {
                            return session.getLastAccessedTime();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public void invalidate() {
                            session.invalidate();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public boolean isValid() {
                            return session.isValid();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public void putValue(String s, Object o) {
                            session.putValue(s, o);
                        }

                        @Override // javax.net.ssl.SSLSession
                        public Object getValue(String s) {
                            return session.getValue(s);
                        }

                        @Override // javax.net.ssl.SSLSession
                        public void removeValue(String s) {
                            session.removeValue(s);
                        }

                        @Override // javax.net.ssl.SSLSession
                        public String[] getValueNames() {
                            return session.getValueNames();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
                            return session.getPeerCertificates();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public Certificate[] getLocalCertificates() {
                            return session.getLocalCertificates();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public javax.security.cert.X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
                            return session.getPeerCertificateChain();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
                            return session.getPeerPrincipal();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public Principal getLocalPrincipal() {
                            return session.getLocalPrincipal();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public String getCipherSuite() {
                            return session.getCipherSuite();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public String getProtocol() {
                            return Constants.SSL_PROTO_TLSv1_2;
                        }

                        @Override // javax.net.ssl.SSLSession
                        public String getPeerHost() {
                            return session.getPeerHost();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public int getPeerPort() {
                            return session.getPeerPort();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public int getPacketBufferSize() {
                            return session.getPacketBufferSize();
                        }

                        @Override // javax.net.ssl.SSLSession
                        public int getApplicationBufferSize() {
                            return session.getApplicationBufferSize();
                        }
                    };
                }
            };
        }
        return engine;
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
        this.tm.checkClientTrusted(x509Certificates, s, wrapEngine(sslEngine));
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
        this.tm.checkServerTrusted(x509Certificates, s, wrapEngine(sslEngine));
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        this.tm.checkClientTrusted(x509Certificates, s);
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        this.tm.checkServerTrusted(x509Certificates, s);
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return this.tm.getAcceptedIssuers();
    }
}

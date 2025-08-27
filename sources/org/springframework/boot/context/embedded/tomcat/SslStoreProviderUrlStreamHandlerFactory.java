package org.springframework.boot.context.embedded.tomcat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.springframework.boot.context.embedded.SslStoreProvider;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/SslStoreProviderUrlStreamHandlerFactory.class */
class SslStoreProviderUrlStreamHandlerFactory implements URLStreamHandlerFactory {
    private static final String PROTOCOL = "springbootssl";
    private static final String KEY_STORE_PATH = "keyStore";
    static final String KEY_STORE_URL = "springbootssl:keyStore";
    private static final String TRUST_STORE_PATH = "trustStore";
    static final String TRUST_STORE_URL = "springbootssl:trustStore";
    private final SslStoreProvider sslStoreProvider;

    SslStoreProviderUrlStreamHandlerFactory(SslStoreProvider sslStoreProvider) {
        this.sslStoreProvider = sslStoreProvider;
    }

    @Override // java.net.URLStreamHandlerFactory
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (PROTOCOL.equals(protocol)) {
            return new URLStreamHandler() { // from class: org.springframework.boot.context.embedded.tomcat.SslStoreProviderUrlStreamHandlerFactory.1
                @Override // java.net.URLStreamHandler
                protected URLConnection openConnection(URL url) throws IOException {
                    try {
                        if (SslStoreProviderUrlStreamHandlerFactory.KEY_STORE_PATH.equals(url.getPath())) {
                            return new KeyStoreUrlConnection(url, SslStoreProviderUrlStreamHandlerFactory.this.sslStoreProvider.getKeyStore());
                        }
                        if (SslStoreProviderUrlStreamHandlerFactory.TRUST_STORE_PATH.equals(url.getPath())) {
                            return new KeyStoreUrlConnection(url, SslStoreProviderUrlStreamHandlerFactory.this.sslStoreProvider.getTrustStore());
                        }
                        throw new IOException("Invalid path: " + url.getPath());
                    } catch (Exception ex) {
                        throw new IOException(ex);
                    }
                }
            };
        }
        return null;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/SslStoreProviderUrlStreamHandlerFactory$KeyStoreUrlConnection.class */
    private static final class KeyStoreUrlConnection extends URLConnection {
        private final KeyStore keyStore;

        private KeyStoreUrlConnection(URL url, KeyStore keyStore) {
            super(url);
            this.keyStore = keyStore;
        }

        @Override // java.net.URLConnection
        public void connect() throws IOException {
        }

        @Override // java.net.URLConnection
        public InputStream getInputStream() throws NoSuchAlgorithmException, IOException, CertificateException, KeyStoreException {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                this.keyStore.store(stream, new char[0]);
                return new ByteArrayInputStream(stream.toByteArray());
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }
    }
}

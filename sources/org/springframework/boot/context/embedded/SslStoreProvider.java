package org.springframework.boot.context.embedded;

import java.security.KeyStore;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/SslStoreProvider.class */
public interface SslStoreProvider {
    KeyStore getKeyStore() throws Exception;

    KeyStore getTrustStore() throws Exception;
}

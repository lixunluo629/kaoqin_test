package io.netty.handler.ssl;

import io.netty.handler.ssl.util.SimpleKeyManagerFactory;
import io.netty.util.internal.ObjectUtil;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.ManagerFactoryParameters;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/KeyManagerFactoryWrapper.class */
final class KeyManagerFactoryWrapper extends SimpleKeyManagerFactory {
    private final KeyManager km;

    KeyManagerFactoryWrapper(KeyManager km) {
        this.km = (KeyManager) ObjectUtil.checkNotNull(km, "km");
    }

    @Override // io.netty.handler.ssl.util.SimpleKeyManagerFactory
    protected void engineInit(KeyStore keyStore, char[] var2) throws Exception {
    }

    @Override // io.netty.handler.ssl.util.SimpleKeyManagerFactory
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }

    @Override // io.netty.handler.ssl.util.SimpleKeyManagerFactory
    protected KeyManager[] engineGetKeyManagers() {
        return new KeyManager[]{this.km};
    }
}

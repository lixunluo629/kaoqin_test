package io.netty.handler.ssl;

import io.netty.handler.ssl.util.SimpleTrustManagerFactory;
import io.netty.util.internal.ObjectUtil;
import java.security.KeyStore;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/TrustManagerFactoryWrapper.class */
final class TrustManagerFactoryWrapper extends SimpleTrustManagerFactory {
    private final TrustManager tm;

    TrustManagerFactoryWrapper(TrustManager tm) {
        this.tm = (TrustManager) ObjectUtil.checkNotNull(tm, "tm");
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

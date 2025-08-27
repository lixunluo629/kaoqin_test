package io.netty.handler.ssl;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslEngineMap.class */
interface OpenSslEngineMap {
    ReferenceCountedOpenSslEngine remove(long j);

    void add(ReferenceCountedOpenSslEngine referenceCountedOpenSslEngine);

    ReferenceCountedOpenSslEngine get(long j);
}

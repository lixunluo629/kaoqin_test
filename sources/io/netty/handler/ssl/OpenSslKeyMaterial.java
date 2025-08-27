package io.netty.handler.ssl;

import io.netty.util.ReferenceCounted;
import java.security.cert.X509Certificate;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslKeyMaterial.class */
interface OpenSslKeyMaterial extends ReferenceCounted {
    X509Certificate[] certificateChain();

    long certificateChainAddress();

    long privateKeyAddress();

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial retain();

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial retain(int i);

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial touch();

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial touch(Object obj);

    @Override // io.netty.util.ReferenceCounted
    boolean release();

    @Override // io.netty.util.ReferenceCounted
    boolean release(int i);
}

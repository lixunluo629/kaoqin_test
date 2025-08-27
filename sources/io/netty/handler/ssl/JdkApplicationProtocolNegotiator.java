package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLEngine;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/JdkApplicationProtocolNegotiator.class */
public interface JdkApplicationProtocolNegotiator extends ApplicationProtocolNegotiator {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/JdkApplicationProtocolNegotiator$ProtocolSelectionListener.class */
    public interface ProtocolSelectionListener {
        void unsupported();

        void selected(String str) throws Exception;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/JdkApplicationProtocolNegotiator$ProtocolSelectionListenerFactory.class */
    public interface ProtocolSelectionListenerFactory {
        ProtocolSelectionListener newListener(SSLEngine sSLEngine, List<String> list);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/JdkApplicationProtocolNegotiator$ProtocolSelector.class */
    public interface ProtocolSelector {
        void unsupported();

        String select(List<String> list) throws Exception;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/JdkApplicationProtocolNegotiator$ProtocolSelectorFactory.class */
    public interface ProtocolSelectorFactory {
        ProtocolSelector newSelector(SSLEngine sSLEngine, Set<String> set);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/JdkApplicationProtocolNegotiator$SslEngineWrapperFactory.class */
    public interface SslEngineWrapperFactory {
        SSLEngine wrapSslEngine(SSLEngine sSLEngine, JdkApplicationProtocolNegotiator jdkApplicationProtocolNegotiator, boolean z);
    }

    SslEngineWrapperFactory wrapperFactory();

    ProtocolSelectorFactory protocolSelectorFactory();

    ProtocolSelectionListenerFactory protocolListenerFactory();

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/JdkApplicationProtocolNegotiator$AllocatorAwareSslEngineWrapperFactory.class */
    public static abstract class AllocatorAwareSslEngineWrapperFactory implements SslEngineWrapperFactory {
        abstract SSLEngine wrapSslEngine(SSLEngine sSLEngine, ByteBufAllocator byteBufAllocator, JdkApplicationProtocolNegotiator jdkApplicationProtocolNegotiator, boolean z);

        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.SslEngineWrapperFactory
        public final SSLEngine wrapSslEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
            return wrapSslEngine(engine, ByteBufAllocator.DEFAULT, applicationNegotiator, isServer);
        }
    }
}

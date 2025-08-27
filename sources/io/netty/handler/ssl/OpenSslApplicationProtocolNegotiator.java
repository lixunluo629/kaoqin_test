package io.netty.handler.ssl;

import io.netty.handler.ssl.ApplicationProtocolConfig;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslApplicationProtocolNegotiator.class */
public interface OpenSslApplicationProtocolNegotiator extends ApplicationProtocolNegotiator {
    ApplicationProtocolConfig.Protocol protocol();

    ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior();

    ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior();
}

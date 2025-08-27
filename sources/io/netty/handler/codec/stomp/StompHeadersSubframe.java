package io.netty.handler.codec.stomp;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/stomp/StompHeadersSubframe.class */
public interface StompHeadersSubframe extends StompSubframe {
    StompCommand command();

    StompHeaders headers();
}

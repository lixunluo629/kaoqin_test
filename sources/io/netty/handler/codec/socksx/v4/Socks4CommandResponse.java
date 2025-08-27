package io.netty.handler.codec.socksx.v4;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v4/Socks4CommandResponse.class */
public interface Socks4CommandResponse extends Socks4Message {
    Socks4CommandStatus status();

    String dstAddr();

    int dstPort();
}

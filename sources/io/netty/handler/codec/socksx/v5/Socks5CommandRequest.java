package io.netty.handler.codec.socksx.v5;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v5/Socks5CommandRequest.class */
public interface Socks5CommandRequest extends Socks5Message {
    Socks5CommandType type();

    Socks5AddressType dstAddrType();

    String dstAddr();

    int dstPort();
}

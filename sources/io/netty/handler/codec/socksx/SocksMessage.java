package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResultProvider;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/SocksMessage.class */
public interface SocksMessage extends DecoderResultProvider {
    SocksVersion version();
}

package io.netty.handler.codec;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/DecoderResultProvider.class */
public interface DecoderResultProvider {
    DecoderResult decoderResult();

    void setDecoderResult(DecoderResult decoderResult);
}

package io.netty.handler.codec.http2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/StreamByteDistributor.class */
public interface StreamByteDistributor {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/StreamByteDistributor$StreamState.class */
    public interface StreamState {
        Http2Stream stream();

        long pendingBytes();

        boolean hasFrame();

        int windowSize();
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/StreamByteDistributor$Writer.class */
    public interface Writer {
        void write(Http2Stream http2Stream, int i);
    }

    void updateStreamableBytes(StreamState streamState);

    void updateDependencyTree(int i, int i2, short s, boolean z);

    boolean distribute(int i, Writer writer) throws Http2Exception;
}

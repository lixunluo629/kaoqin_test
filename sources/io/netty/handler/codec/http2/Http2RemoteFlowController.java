package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2RemoteFlowController.class */
public interface Http2RemoteFlowController extends Http2FlowController {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2RemoteFlowController$FlowControlled.class */
    public interface FlowControlled {
        int size();

        void error(ChannelHandlerContext channelHandlerContext, Throwable th);

        void writeComplete();

        void write(ChannelHandlerContext channelHandlerContext, int i);

        boolean merge(ChannelHandlerContext channelHandlerContext, FlowControlled flowControlled);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2RemoteFlowController$Listener.class */
    public interface Listener {
        void writabilityChanged(Http2Stream http2Stream);
    }

    ChannelHandlerContext channelHandlerContext();

    void addFlowControlled(Http2Stream http2Stream, FlowControlled flowControlled);

    boolean hasFlowControlled(Http2Stream http2Stream);

    void writePendingBytes() throws Http2Exception;

    void listener(Listener listener);

    boolean isWritable(Http2Stream http2Stream);

    void channelWritabilityChanged() throws Http2Exception;

    void updateDependencyTree(int i, int i2, short s, boolean z);
}

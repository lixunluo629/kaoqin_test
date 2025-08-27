package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Connection.class */
public interface Http2Connection {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Connection$Endpoint.class */
    public interface Endpoint<F extends Http2FlowController> {
        int incrementAndGetNextStreamId();

        boolean isValidStreamId(int i);

        boolean mayHaveCreatedStream(int i);

        boolean created(Http2Stream http2Stream);

        boolean canOpenStream();

        Http2Stream createStream(int i, boolean z) throws Http2Exception;

        Http2Stream reservePushStream(int i, Http2Stream http2Stream) throws Http2Exception;

        boolean isServer();

        void allowPushTo(boolean z);

        boolean allowPushTo();

        int numActiveStreams();

        int maxActiveStreams();

        void maxActiveStreams(int i);

        int lastStreamCreated();

        int lastStreamKnownByPeer();

        F flowController();

        void flowController(F f);

        Endpoint<? extends Http2FlowController> opposite();
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Connection$Listener.class */
    public interface Listener {
        void onStreamAdded(Http2Stream http2Stream);

        void onStreamActive(Http2Stream http2Stream);

        void onStreamHalfClosed(Http2Stream http2Stream);

        void onStreamClosed(Http2Stream http2Stream);

        void onStreamRemoved(Http2Stream http2Stream);

        void onGoAwaySent(int i, long j, ByteBuf byteBuf);

        void onGoAwayReceived(int i, long j, ByteBuf byteBuf);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2Connection$PropertyKey.class */
    public interface PropertyKey {
    }

    Future<Void> close(Promise<Void> promise);

    PropertyKey newKey();

    void addListener(Listener listener);

    void removeListener(Listener listener);

    Http2Stream stream(int i);

    boolean streamMayHaveExisted(int i);

    Http2Stream connectionStream();

    int numActiveStreams();

    Http2Stream forEachActiveStream(Http2StreamVisitor http2StreamVisitor) throws Http2Exception;

    boolean isServer();

    Endpoint<Http2LocalFlowController> local();

    Endpoint<Http2RemoteFlowController> remote();

    boolean goAwayReceived();

    void goAwayReceived(int i, long j, ByteBuf byteBuf) throws Http2Exception;

    boolean goAwaySent();

    boolean goAwaySent(int i, long j, ByteBuf byteBuf) throws Http2Exception;
}

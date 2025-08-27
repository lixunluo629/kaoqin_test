package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.codec.http2.HttpConversionUtil;
import io.netty.util.ReferenceCountUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HttpToHttp2ConnectionHandler.class */
public class HttpToHttp2ConnectionHandler extends Http2ConnectionHandler {
    private final boolean validateHeaders;
    private int currentStreamId;

    protected HttpToHttp2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean validateHeaders) {
        super(decoder, encoder, initialSettings);
        this.validateHeaders = validateHeaders;
    }

    protected HttpToHttp2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean validateHeaders, boolean decoupleCloseAndGoAway) {
        super(decoder, encoder, initialSettings, decoupleCloseAndGoAway);
        this.validateHeaders = validateHeaders;
    }

    private int getStreamId(HttpHeaders httpHeaders) throws Exception {
        return httpHeaders.getInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), connection().local().incrementAndGetNextStreamId());
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (!(msg instanceof HttpMessage) && !(msg instanceof HttpContent)) {
            ctx.write(msg, promise);
            return;
        }
        boolean release = true;
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            try {
                Http2ConnectionEncoder encoder = encoder();
                boolean endStream = false;
                if (msg instanceof HttpMessage) {
                    HttpMessage httpMsg = (HttpMessage) msg;
                    this.currentStreamId = getStreamId(httpMsg.headers());
                    Http2Headers http2Headers = HttpConversionUtil.toHttp2Headers(httpMsg, this.validateHeaders);
                    endStream = (msg instanceof FullHttpMessage) && !((FullHttpMessage) msg).content().isReadable();
                    writeHeaders(ctx, encoder, this.currentStreamId, httpMsg.headers(), http2Headers, endStream, promiseAggregator);
                }
                if (!endStream && (msg instanceof HttpContent)) {
                    boolean isLastContent = false;
                    HttpHeaders trailers = EmptyHttpHeaders.INSTANCE;
                    Http2Headers http2Trailers = EmptyHttp2Headers.INSTANCE;
                    if (msg instanceof LastHttpContent) {
                        isLastContent = true;
                        LastHttpContent lastContent = (LastHttpContent) msg;
                        trailers = lastContent.trailingHeaders();
                        http2Trailers = HttpConversionUtil.toHttp2Headers(trailers, this.validateHeaders);
                    }
                    ByteBuf content = ((HttpContent) msg).content();
                    boolean endStream2 = isLastContent && trailers.isEmpty();
                    encoder.writeData(ctx, this.currentStreamId, content, 0, endStream2, promiseAggregator.newPromise());
                    release = false;
                    if (!trailers.isEmpty()) {
                        writeHeaders(ctx, encoder, this.currentStreamId, trailers, http2Trailers, true, promiseAggregator);
                    }
                }
                if (release) {
                    ReferenceCountUtil.release(msg);
                }
                promiseAggregator.doneAllocatingPromises();
            } catch (Throwable t) {
                onError(ctx, true, t);
                promiseAggregator.setFailure(t);
                if (release) {
                    ReferenceCountUtil.release(msg);
                }
                promiseAggregator.doneAllocatingPromises();
            }
        } catch (Throwable th) {
            if (release) {
                ReferenceCountUtil.release(msg);
            }
            promiseAggregator.doneAllocatingPromises();
            throw th;
        }
    }

    private static void writeHeaders(ChannelHandlerContext ctx, Http2ConnectionEncoder encoder, int streamId, HttpHeaders headers, Http2Headers http2Headers, boolean endStream, Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
        int dependencyId = headers.getInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text(), 0);
        short weight = headers.getShort(HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), (short) 16);
        encoder.writeHeaders(ctx, streamId, http2Headers, dependencyId, weight, false, 0, endStream, promiseAggregator.newPromise());
    }
}

package io.netty.handler.codec.http;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpServerKeepAliveHandler.class */
public class HttpServerKeepAliveHandler extends ChannelDuplexHandler {
    private static final String MULTIPART_PREFIX = "multipart";
    private boolean persistentConnection = true;
    private int pendingResponses;

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if (this.persistentConnection) {
                this.pendingResponses++;
                this.persistentConnection = HttpUtil.isKeepAlive(request);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            trackResponse(response);
            if (!HttpUtil.isKeepAlive(response) || !isSelfDefinedMessageLength(response)) {
                this.pendingResponses = 0;
                this.persistentConnection = false;
            }
            if (!shouldKeepAlive()) {
                HttpUtil.setKeepAlive(response, false);
            }
        }
        if ((msg instanceof LastHttpContent) && !shouldKeepAlive()) {
            promise = promise.unvoid().addListener2((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
        }
        super.write(ctx, msg, promise);
    }

    private void trackResponse(HttpResponse response) {
        if (!isInformational(response)) {
            this.pendingResponses--;
        }
    }

    private boolean shouldKeepAlive() {
        return this.pendingResponses != 0 || this.persistentConnection;
    }

    private static boolean isSelfDefinedMessageLength(HttpResponse response) {
        return HttpUtil.isContentLengthSet(response) || HttpUtil.isTransferEncodingChunked(response) || isMultipart(response) || isInformational(response) || response.status().code() == HttpResponseStatus.NO_CONTENT.code();
    }

    private static boolean isInformational(HttpResponse response) {
        return response.status().codeClass() == HttpStatusClass.INFORMATIONAL;
    }

    private static boolean isMultipart(HttpResponse response) {
        String contentType = response.headers().get(HttpHeaderNames.CONTENT_TYPE);
        return contentType != null && contentType.regionMatches(true, 0, MULTIPART_PREFIX, 0, MULTIPART_PREFIX.length());
    }
}

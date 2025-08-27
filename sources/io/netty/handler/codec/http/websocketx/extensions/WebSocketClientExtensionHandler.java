package io.netty.handler.codec.http.websocketx.extensions;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/extensions/WebSocketClientExtensionHandler.class */
public class WebSocketClientExtensionHandler extends ChannelDuplexHandler {
    private final List<WebSocketClientExtensionHandshaker> extensionHandshakers;

    public WebSocketClientExtensionHandler(WebSocketClientExtensionHandshaker... extensionHandshakers) {
        ObjectUtil.checkNotNull(extensionHandshakers, "extensionHandshakers");
        if (extensionHandshakers.length == 0) {
            throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
        }
        this.extensionHandshakers = Arrays.asList(extensionHandshakers);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ((msg instanceof HttpRequest) && WebSocketExtensionUtil.isWebsocketUpgrade(((HttpRequest) msg).headers())) {
            HttpRequest request = (HttpRequest) msg;
            String headerValue = request.headers().getAsString(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
            for (WebSocketClientExtensionHandshaker extensionHandshaker : this.extensionHandshakers) {
                WebSocketExtensionData extensionData = extensionHandshaker.newRequestData();
                headerValue = WebSocketExtensionUtil.appendExtension(headerValue, extensionData.name(), extensionData.parameters());
            }
            request.headers().set(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
        }
        super.write(ctx, msg, promise);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WebSocketClientExtension validExtension;
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            if (WebSocketExtensionUtil.isWebsocketUpgrade(response.headers())) {
                String extensionsHeader = response.headers().getAsString(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
                if (extensionsHeader != null) {
                    List<WebSocketExtensionData> extensions = WebSocketExtensionUtil.extractExtensions(extensionsHeader);
                    List<WebSocketClientExtension> validExtensions = new ArrayList<>(extensions.size());
                    int rsv = 0;
                    for (WebSocketExtensionData extensionData : extensions) {
                        Iterator<WebSocketClientExtensionHandshaker> extensionHandshakersIterator = this.extensionHandshakers.iterator();
                        WebSocketClientExtension webSocketClientExtensionHandshakeExtension = null;
                        while (true) {
                            validExtension = webSocketClientExtensionHandshakeExtension;
                            if (validExtension != null || !extensionHandshakersIterator.hasNext()) {
                                break;
                            }
                            WebSocketClientExtensionHandshaker extensionHandshaker = extensionHandshakersIterator.next();
                            webSocketClientExtensionHandshakeExtension = extensionHandshaker.handshakeExtension(extensionData);
                        }
                        if (validExtension != null && (validExtension.rsv() & rsv) == 0) {
                            rsv |= validExtension.rsv();
                            validExtensions.add(validExtension);
                        } else {
                            throw new CodecException("invalid WebSocket Extension handshake for \"" + extensionsHeader + '\"');
                        }
                    }
                    for (WebSocketClientExtension validExtension2 : validExtensions) {
                        WebSocketExtensionDecoder decoder = validExtension2.newExtensionDecoder();
                        WebSocketExtensionEncoder encoder = validExtension2.newExtensionEncoder();
                        ctx.pipeline().addAfter(ctx.name(), decoder.getClass().getName(), decoder);
                        ctx.pipeline().addAfter(ctx.name(), encoder.getClass().getName(), encoder);
                    }
                }
                ctx.pipeline().remove(ctx.name());
            }
        }
        super.channelRead(ctx, msg);
    }
}

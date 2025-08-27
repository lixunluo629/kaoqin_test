package io.netty.handler.codec.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpServerUpgradeHandler.class */
public class HttpServerUpgradeHandler extends HttpObjectAggregator {
    private final SourceCodec sourceCodec;
    private final UpgradeCodecFactory upgradeCodecFactory;
    private boolean handlingUpgrade;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpServerUpgradeHandler$SourceCodec.class */
    public interface SourceCodec {
        void upgradeFrom(ChannelHandlerContext channelHandlerContext);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpServerUpgradeHandler$UpgradeCodec.class */
    public interface UpgradeCodec {
        Collection<CharSequence> requiredUpgradeHeaders();

        boolean prepareUpgradeResponse(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, HttpHeaders httpHeaders);

        void upgradeTo(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpServerUpgradeHandler$UpgradeCodecFactory.class */
    public interface UpgradeCodecFactory {
        UpgradeCodec newUpgradeCodec(CharSequence charSequence);
    }

    @Override // io.netty.handler.codec.MessageAggregator, io.netty.handler.codec.MessageToMessageDecoder
    protected /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, Object obj, List list) throws Exception {
        decode(channelHandlerContext, (HttpObject) obj, (List<Object>) list);
    }

    static {
        $assertionsDisabled = !HttpServerUpgradeHandler.class.desiredAssertionStatus();
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpServerUpgradeHandler$UpgradeEvent.class */
    public static final class UpgradeEvent implements ReferenceCounted {
        private final CharSequence protocol;
        private final FullHttpRequest upgradeRequest;

        UpgradeEvent(CharSequence protocol, FullHttpRequest upgradeRequest) {
            this.protocol = protocol;
            this.upgradeRequest = upgradeRequest;
        }

        public CharSequence protocol() {
            return this.protocol;
        }

        public FullHttpRequest upgradeRequest() {
            return this.upgradeRequest;
        }

        @Override // io.netty.util.ReferenceCounted
        public int refCnt() {
            return this.upgradeRequest.refCnt();
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent retain() {
            this.upgradeRequest.retain();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent retain(int increment) {
            this.upgradeRequest.retain(increment);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent touch() {
            this.upgradeRequest.touch();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public UpgradeEvent touch(Object hint) {
            this.upgradeRequest.touch(hint);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release() {
            return this.upgradeRequest.release();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return this.upgradeRequest.release(decrement);
        }

        public String toString() {
            return "UpgradeEvent [protocol=" + ((Object) this.protocol) + ", upgradeRequest=" + this.upgradeRequest + ']';
        }
    }

    public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory) {
        this(sourceCodec, upgradeCodecFactory, 0);
    }

    public HttpServerUpgradeHandler(SourceCodec sourceCodec, UpgradeCodecFactory upgradeCodecFactory, int maxContentLength) {
        super(maxContentLength);
        this.sourceCodec = (SourceCodec) ObjectUtil.checkNotNull(sourceCodec, "sourceCodec");
        this.upgradeCodecFactory = (UpgradeCodecFactory) ObjectUtil.checkNotNull(upgradeCodecFactory, "upgradeCodecFactory");
    }

    protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
        FullHttpRequest fullRequest;
        this.handlingUpgrade |= isUpgradeRequest(msg);
        if (!this.handlingUpgrade) {
            ReferenceCountUtil.retain(msg);
            out.add(msg);
            return;
        }
        if (msg instanceof FullHttpRequest) {
            fullRequest = (FullHttpRequest) msg;
            ReferenceCountUtil.retain(msg);
            out.add(msg);
        } else {
            super.decode(ctx, (ChannelHandlerContext) msg, out);
            if (out.isEmpty()) {
                return;
            }
            if (!$assertionsDisabled && out.size() != 1) {
                throw new AssertionError();
            }
            this.handlingUpgrade = false;
            fullRequest = (FullHttpRequest) out.get(0);
        }
        if (upgrade(ctx, fullRequest)) {
            out.clear();
        }
    }

    private static boolean isUpgradeRequest(HttpObject msg) {
        return (msg instanceof HttpRequest) && ((HttpRequest) msg).headers().get(HttpHeaderNames.UPGRADE) != null;
    }

    private boolean upgrade(ChannelHandlerContext ctx, FullHttpRequest request) {
        List<String> connectionHeaderValues;
        List<CharSequence> requestedProtocols = splitHeader(request.headers().get(HttpHeaderNames.UPGRADE));
        int numRequestedProtocols = requestedProtocols.size();
        UpgradeCodec upgradeCodec = null;
        CharSequence upgradeProtocol = null;
        int i = 0;
        while (true) {
            if (i >= numRequestedProtocols) {
                break;
            }
            CharSequence p = requestedProtocols.get(i);
            UpgradeCodec c = this.upgradeCodecFactory.newUpgradeCodec(p);
            if (c == null) {
                i++;
            } else {
                upgradeProtocol = p;
                upgradeCodec = c;
                break;
            }
        }
        if (upgradeCodec == null || (connectionHeaderValues = request.headers().getAll(HttpHeaderNames.CONNECTION)) == null) {
            return false;
        }
        StringBuilder concatenatedConnectionValue = new StringBuilder(connectionHeaderValues.size() * 10);
        for (CharSequence connectionHeaderValue : connectionHeaderValues) {
            concatenatedConnectionValue.append(connectionHeaderValue).append(',');
        }
        concatenatedConnectionValue.setLength(concatenatedConnectionValue.length() - 1);
        Collection<CharSequence> requiredHeaders = upgradeCodec.requiredUpgradeHeaders();
        List<CharSequence> values = splitHeader(concatenatedConnectionValue);
        if (!AsciiString.containsContentEqualsIgnoreCase(values, HttpHeaderNames.UPGRADE) || !AsciiString.containsAllContentEqualsIgnoreCase(values, requiredHeaders)) {
            return false;
        }
        for (CharSequence requiredHeader : requiredHeaders) {
            if (!request.headers().contains(requiredHeader)) {
                return false;
            }
        }
        FullHttpResponse upgradeResponse = createUpgradeResponse(upgradeProtocol);
        if (!upgradeCodec.prepareUpgradeResponse(ctx, request, upgradeResponse.headers())) {
            return false;
        }
        UpgradeEvent event = new UpgradeEvent(upgradeProtocol, request);
        try {
            ChannelFuture writeComplete = ctx.writeAndFlush(upgradeResponse);
            this.sourceCodec.upgradeFrom(ctx);
            upgradeCodec.upgradeTo(ctx, request);
            ctx.pipeline().remove(this);
            ctx.fireUserEventTriggered(event.retain());
            writeComplete.addListener2((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE_ON_FAILURE);
            event.release();
            return true;
        } catch (Throwable th) {
            event.release();
            throw th;
        }
    }

    private static FullHttpResponse createUpgradeResponse(CharSequence upgradeProtocol) {
        DefaultFullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS, Unpooled.EMPTY_BUFFER, false);
        res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
        res.headers().add(HttpHeaderNames.UPGRADE, upgradeProtocol);
        return res;
    }

    private static List<CharSequence> splitHeader(CharSequence header) {
        StringBuilder builder = new StringBuilder(header.length());
        List<CharSequence> protocols = new ArrayList<>(4);
        for (int i = 0; i < header.length(); i++) {
            char c = header.charAt(i);
            if (!Character.isWhitespace(c)) {
                if (c == ',') {
                    protocols.add(builder.toString());
                    builder.setLength(0);
                } else {
                    builder.append(c);
                }
            }
        }
        if (builder.length() > 0) {
            protocols.add(builder.toString());
        }
        return protocols;
    }
}

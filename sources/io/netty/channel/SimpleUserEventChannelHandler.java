package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/SimpleUserEventChannelHandler.class */
public abstract class SimpleUserEventChannelHandler<I> extends ChannelInboundHandlerAdapter {
    private final TypeParameterMatcher matcher;
    private final boolean autoRelease;

    protected abstract void eventReceived(ChannelHandlerContext channelHandlerContext, I i) throws Exception;

    protected SimpleUserEventChannelHandler() {
        this(true);
    }

    protected SimpleUserEventChannelHandler(boolean autoRelease) {
        this.matcher = TypeParameterMatcher.find(this, SimpleUserEventChannelHandler.class, "I");
        this.autoRelease = autoRelease;
    }

    protected SimpleUserEventChannelHandler(Class<? extends I> eventType) {
        this(eventType, true);
    }

    protected SimpleUserEventChannelHandler(Class<? extends I> eventType, boolean autoRelease) {
        this.matcher = TypeParameterMatcher.get(eventType);
        this.autoRelease = autoRelease;
    }

    protected boolean acceptEvent(Object evt) throws Exception {
        return this.matcher.match(evt);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        boolean release = true;
        try {
            if (acceptEvent(obj)) {
                eventReceived(ctx, obj);
            } else {
                release = false;
                ctx.fireUserEventTriggered(obj);
            }
            if (this.autoRelease && release) {
                ReferenceCountUtil.release(obj);
            }
        } catch (Throwable th) {
            if (this.autoRelease && 1 != 0) {
                ReferenceCountUtil.release(obj);
            }
            throw th;
        }
    }
}

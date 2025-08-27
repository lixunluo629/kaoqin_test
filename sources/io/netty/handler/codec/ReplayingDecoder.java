package io.netty.handler.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Signal;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/ReplayingDecoder.class */
public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder {
    static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class, "REPLAY");
    private final ReplayingDecoderByteBuf replayable;
    private S state;
    private int checkpoint;

    protected ReplayingDecoder() {
        this(null);
    }

    protected ReplayingDecoder(S initialState) {
        this.replayable = new ReplayingDecoderByteBuf();
        this.checkpoint = -1;
        this.state = initialState;
    }

    protected void checkpoint() {
        this.checkpoint = internalBuffer().readerIndex();
    }

    protected void checkpoint(S state) {
        checkpoint();
        state(state);
    }

    protected S state() {
        return this.state;
    }

    protected S state(S newState) {
        S oldState = this.state;
        this.state = newState;
        return oldState;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    final void channelInputClosed(ChannelHandlerContext ctx, List<Object> out) throws Exception {
        try {
            this.replayable.terminate();
            if (this.cumulation != null) {
                callDecode(ctx, internalBuffer(), out);
            } else {
                this.replayable.setCumulation(Unpooled.EMPTY_BUFFER);
            }
            decodeLast(ctx, this.replayable, out);
        } catch (Signal replay) {
            replay.expect(REPLAY);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0065 A[Catch: Signal -> 0x00a9, DecoderException -> 0x0117, Exception -> 0x011c, TRY_ENTER, TryCatch #2 {Signal -> 0x00a9, blocks: (B:12:0x004f, B:15:0x0065, B:17:0x0070, B:19:0x0079, B:21:0x0082, B:22:0x00a2), top: B:51:0x004f }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0062 A[SYNTHETIC] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void callDecode(io.netty.channel.ChannelHandlerContext r6, io.netty.buffer.ByteBuf r7, java.util.List<java.lang.Object> r8) {
        /*
            Method dump skipped, instructions count: 297
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.ReplayingDecoder.callDecode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }
}

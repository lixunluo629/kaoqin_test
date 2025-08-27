package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v5/Socks5InitialRequestDecoder.class */
public class Socks5InitialRequestDecoder extends ReplayingDecoder<State> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v5/Socks5InitialRequestDecoder$State.class */
    enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5InitialRequestDecoder() {
        super(State.INIT);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00b3 A[Catch: Exception -> 0x00cf, TryCatch #0 {Exception -> 0x00cf, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0028, B:6:0x0039, B:7:0x0067, B:8:0x0068, B:11:0x007f, B:12:0x0091, B:13:0x00a8, B:15:0x00b3, B:16:0x00c3), top: B:21:0x0000 }] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r6, io.netty.buffer.ByteBuf r7, java.util.List<java.lang.Object> r8) throws java.lang.Exception {
        /*
            r5 = this;
            int[] r0 = io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder.AnonymousClass1.$SwitchMap$io$netty$handler$codec$socksx$v5$Socks5InitialRequestDecoder$State     // Catch: java.lang.Exception -> Lcf
            r1 = r5
            java.lang.Object r1 = r1.state()     // Catch: java.lang.Exception -> Lcf
            io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder$State r1 = (io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder.State) r1     // Catch: java.lang.Exception -> Lcf
            int r1 = r1.ordinal()     // Catch: java.lang.Exception -> Lcf
            r0 = r0[r1]     // Catch: java.lang.Exception -> Lcf
            switch(r0) {
                case 1: goto L28;
                case 2: goto La8;
                case 3: goto Lc3;
                default: goto Lcc;
            }     // Catch: java.lang.Exception -> Lcf
        L28:
            r0 = r7
            byte r0 = r0.readByte()     // Catch: java.lang.Exception -> Lcf
            r9 = r0
            r0 = r9
            io.netty.handler.codec.socksx.SocksVersion r1 = io.netty.handler.codec.socksx.SocksVersion.SOCKS5     // Catch: java.lang.Exception -> Lcf
            byte r1 = r1.byteValue()     // Catch: java.lang.Exception -> Lcf
            if (r0 == r1) goto L68
            io.netty.handler.codec.DecoderException r0 = new io.netty.handler.codec.DecoderException     // Catch: java.lang.Exception -> Lcf
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lcf
            r3 = r2
            r3.<init>()     // Catch: java.lang.Exception -> Lcf
            java.lang.String r3 = "unsupported version: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Lcf
            r3 = r9
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Lcf
            java.lang.String r3 = " (expected: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Lcf
            io.netty.handler.codec.socksx.SocksVersion r3 = io.netty.handler.codec.socksx.SocksVersion.SOCKS5     // Catch: java.lang.Exception -> Lcf
            byte r3 = r3.byteValue()     // Catch: java.lang.Exception -> Lcf
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Lcf
            r3 = 41
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Lcf
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> Lcf
            r1.<init>(r2)     // Catch: java.lang.Exception -> Lcf
            throw r0     // Catch: java.lang.Exception -> Lcf
        L68:
            r0 = r7
            short r0 = r0.readUnsignedByte()     // Catch: java.lang.Exception -> Lcf
            r10 = r0
            r0 = r10
            io.netty.handler.codec.socksx.v5.Socks5AuthMethod[] r0 = new io.netty.handler.codec.socksx.v5.Socks5AuthMethod[r0]     // Catch: java.lang.Exception -> Lcf
            r11 = r0
            r0 = 0
            r12 = r0
        L78:
            r0 = r12
            r1 = r10
            if (r0 >= r1) goto L91
            r0 = r11
            r1 = r12
            r2 = r7
            byte r2 = r2.readByte()     // Catch: java.lang.Exception -> Lcf
            io.netty.handler.codec.socksx.v5.Socks5AuthMethod r2 = io.netty.handler.codec.socksx.v5.Socks5AuthMethod.valueOf(r2)     // Catch: java.lang.Exception -> Lcf
            r0[r1] = r2     // Catch: java.lang.Exception -> Lcf
            int r12 = r12 + 1
            goto L78
        L91:
            r0 = r8
            io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest r1 = new io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest     // Catch: java.lang.Exception -> Lcf
            r2 = r1
            r3 = r11
            r2.<init>(r3)     // Catch: java.lang.Exception -> Lcf
            boolean r0 = r0.add(r1)     // Catch: java.lang.Exception -> Lcf
            r0 = r5
            io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder$State r1 = io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder.State.SUCCESS     // Catch: java.lang.Exception -> Lcf
            r0.checkpoint(r1)     // Catch: java.lang.Exception -> Lcf
        La8:
            r0 = r5
            int r0 = r0.actualReadableBytes()     // Catch: java.lang.Exception -> Lcf
            r9 = r0
            r0 = r9
            if (r0 <= 0) goto Lcc
            r0 = r8
            r1 = r7
            r2 = r9
            io.netty.buffer.ByteBuf r1 = r1.readRetainedSlice(r2)     // Catch: java.lang.Exception -> Lcf
            boolean r0 = r0.add(r1)     // Catch: java.lang.Exception -> Lcf
            goto Lcc
        Lc3:
            r0 = r7
            r1 = r5
            int r1 = r1.actualReadableBytes()     // Catch: java.lang.Exception -> Lcf
            io.netty.buffer.ByteBuf r0 = r0.skipBytes(r1)     // Catch: java.lang.Exception -> Lcf
        Lcc:
            goto Ld8
        Lcf:
            r9 = move-exception
            r0 = r5
            r1 = r8
            r2 = r9
            r0.fail(r1, r2)
        Ld8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    private void fail(List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        checkpoint(State.FAILURE);
        Socks5Message m = new DefaultSocks5InitialRequest(Socks5AuthMethod.NO_AUTH);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}

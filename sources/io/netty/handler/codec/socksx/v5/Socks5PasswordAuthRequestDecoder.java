package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PasswordAuthRequestDecoder.class */
public class Socks5PasswordAuthRequestDecoder extends ReplayingDecoder<State> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v5/Socks5PasswordAuthRequestDecoder$State.class */
    enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5PasswordAuthRequestDecoder() {
        super(State.INIT);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00c1 A[Catch: Exception -> 0x00dd, TryCatch #0 {Exception -> 0x00dd, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0028, B:6:0x003c, B:7:0x005c, B:8:0x005d, B:9:0x00b6, B:11:0x00c1, B:12:0x00d1), top: B:17:0x0000 }] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r10, io.netty.buffer.ByteBuf r11, java.util.List<java.lang.Object> r12) throws java.lang.Exception {
        /*
            r9 = this;
            int[] r0 = io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder.AnonymousClass1.$SwitchMap$io$netty$handler$codec$socksx$v5$Socks5PasswordAuthRequestDecoder$State     // Catch: java.lang.Exception -> Ldd
            r1 = r9
            java.lang.Object r1 = r1.state()     // Catch: java.lang.Exception -> Ldd
            io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder$State r1 = (io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder.State) r1     // Catch: java.lang.Exception -> Ldd
            int r1 = r1.ordinal()     // Catch: java.lang.Exception -> Ldd
            r0 = r0[r1]     // Catch: java.lang.Exception -> Ldd
            switch(r0) {
                case 1: goto L28;
                case 2: goto Lb6;
                case 3: goto Ld1;
                default: goto Lda;
            }     // Catch: java.lang.Exception -> Ldd
        L28:
            r0 = r11
            int r0 = r0.readerIndex()     // Catch: java.lang.Exception -> Ldd
            r13 = r0
            r0 = r11
            r1 = r13
            byte r0 = r0.getByte(r1)     // Catch: java.lang.Exception -> Ldd
            r14 = r0
            r0 = r14
            r1 = 1
            if (r0 == r1) goto L5d
            io.netty.handler.codec.DecoderException r0 = new io.netty.handler.codec.DecoderException     // Catch: java.lang.Exception -> Ldd
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Ldd
            r3 = r2
            r3.<init>()     // Catch: java.lang.Exception -> Ldd
            java.lang.String r3 = "unsupported subnegotiation version: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ldd
            r3 = r14
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ldd
            java.lang.String r3 = " (expected: 1)"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ldd
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> Ldd
            r1.<init>(r2)     // Catch: java.lang.Exception -> Ldd
            throw r0     // Catch: java.lang.Exception -> Ldd
        L5d:
            r0 = r11
            r1 = r13
            r2 = 1
            int r1 = r1 + r2
            short r0 = r0.getUnsignedByte(r1)     // Catch: java.lang.Exception -> Ldd
            r15 = r0
            r0 = r11
            r1 = r13
            r2 = 2
            int r1 = r1 + r2
            r2 = r15
            int r1 = r1 + r2
            short r0 = r0.getUnsignedByte(r1)     // Catch: java.lang.Exception -> Ldd
            r16 = r0
            r0 = r15
            r1 = r16
            int r0 = r0 + r1
            r1 = 3
            int r0 = r0 + r1
            r17 = r0
            r0 = r11
            r1 = r17
            io.netty.buffer.ByteBuf r0 = r0.skipBytes(r1)     // Catch: java.lang.Exception -> Ldd
            r0 = r12
            io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest r1 = new io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest     // Catch: java.lang.Exception -> Ldd
            r2 = r1
            r3 = r11
            r4 = r13
            r5 = 2
            int r4 = r4 + r5
            r5 = r15
            java.nio.charset.Charset r6 = io.netty.util.CharsetUtil.US_ASCII     // Catch: java.lang.Exception -> Ldd
            java.lang.String r3 = r3.toString(r4, r5, r6)     // Catch: java.lang.Exception -> Ldd
            r4 = r11
            r5 = r13
            r6 = 3
            int r5 = r5 + r6
            r6 = r15
            int r5 = r5 + r6
            r6 = r16
            java.nio.charset.Charset r7 = io.netty.util.CharsetUtil.US_ASCII     // Catch: java.lang.Exception -> Ldd
            java.lang.String r4 = r4.toString(r5, r6, r7)     // Catch: java.lang.Exception -> Ldd
            r2.<init>(r3, r4)     // Catch: java.lang.Exception -> Ldd
            boolean r0 = r0.add(r1)     // Catch: java.lang.Exception -> Ldd
            r0 = r9
            io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder$State r1 = io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder.State.SUCCESS     // Catch: java.lang.Exception -> Ldd
            r0.checkpoint(r1)     // Catch: java.lang.Exception -> Ldd
        Lb6:
            r0 = r9
            int r0 = r0.actualReadableBytes()     // Catch: java.lang.Exception -> Ldd
            r13 = r0
            r0 = r13
            if (r0 <= 0) goto Lda
            r0 = r12
            r1 = r11
            r2 = r13
            io.netty.buffer.ByteBuf r1 = r1.readRetainedSlice(r2)     // Catch: java.lang.Exception -> Ldd
            boolean r0 = r0.add(r1)     // Catch: java.lang.Exception -> Ldd
            goto Lda
        Ld1:
            r0 = r11
            r1 = r9
            int r1 = r1.actualReadableBytes()     // Catch: java.lang.Exception -> Ldd
            io.netty.buffer.ByteBuf r0 = r0.skipBytes(r1)     // Catch: java.lang.Exception -> Ldd
        Lda:
            goto Le6
        Ldd:
            r13 = move-exception
            r0 = r9
            r1 = r12
            r2 = r13
            r0.fail(r1, r2)
        Le6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    private void fail(List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        checkpoint(State.FAILURE);
        Socks5Message m = new DefaultSocks5PasswordAuthRequest("", "");
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}

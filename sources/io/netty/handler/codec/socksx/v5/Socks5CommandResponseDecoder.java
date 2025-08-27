package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v5/Socks5CommandResponseDecoder.class */
public class Socks5CommandResponseDecoder extends ReplayingDecoder<State> {
    private final Socks5AddressDecoder addressDecoder;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socksx/v5/Socks5CommandResponseDecoder$State.class */
    enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public Socks5CommandResponseDecoder() {
        this(Socks5AddressDecoder.DEFAULT);
    }

    public Socks5CommandResponseDecoder(Socks5AddressDecoder addressDecoder) {
        super(State.INIT);
        this.addressDecoder = (Socks5AddressDecoder) ObjectUtil.checkNotNull(addressDecoder, "addressDecoder");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00bc A[Catch: Exception -> 0x00d8, TryCatch #0 {Exception -> 0x00d8, blocks: (B:2:0x0000, B:3:0x000e, B:4:0x0028, B:6:0x0039, B:7:0x0067, B:8:0x0068, B:9:0x00b1, B:11:0x00bc, B:12:0x00cc), top: B:17:0x0000 }] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r9, io.netty.buffer.ByteBuf r10, java.util.List<java.lang.Object> r11) throws java.lang.Exception {
        /*
            r8 = this;
            int[] r0 = io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder.AnonymousClass1.$SwitchMap$io$netty$handler$codec$socksx$v5$Socks5CommandResponseDecoder$State     // Catch: java.lang.Exception -> Ld8
            r1 = r8
            java.lang.Object r1 = r1.state()     // Catch: java.lang.Exception -> Ld8
            io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder$State r1 = (io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder.State) r1     // Catch: java.lang.Exception -> Ld8
            int r1 = r1.ordinal()     // Catch: java.lang.Exception -> Ld8
            r0 = r0[r1]     // Catch: java.lang.Exception -> Ld8
            switch(r0) {
                case 1: goto L28;
                case 2: goto Lb1;
                case 3: goto Lcc;
                default: goto Ld5;
            }     // Catch: java.lang.Exception -> Ld8
        L28:
            r0 = r10
            byte r0 = r0.readByte()     // Catch: java.lang.Exception -> Ld8
            r12 = r0
            r0 = r12
            io.netty.handler.codec.socksx.SocksVersion r1 = io.netty.handler.codec.socksx.SocksVersion.SOCKS5     // Catch: java.lang.Exception -> Ld8
            byte r1 = r1.byteValue()     // Catch: java.lang.Exception -> Ld8
            if (r0 == r1) goto L68
            io.netty.handler.codec.DecoderException r0 = new io.netty.handler.codec.DecoderException     // Catch: java.lang.Exception -> Ld8
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Ld8
            r3 = r2
            r3.<init>()     // Catch: java.lang.Exception -> Ld8
            java.lang.String r3 = "unsupported version: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ld8
            r3 = r12
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ld8
            java.lang.String r3 = " (expected: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ld8
            io.netty.handler.codec.socksx.SocksVersion r3 = io.netty.handler.codec.socksx.SocksVersion.SOCKS5     // Catch: java.lang.Exception -> Ld8
            byte r3 = r3.byteValue()     // Catch: java.lang.Exception -> Ld8
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ld8
            r3 = 41
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> Ld8
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> Ld8
            r1.<init>(r2)     // Catch: java.lang.Exception -> Ld8
            throw r0     // Catch: java.lang.Exception -> Ld8
        L68:
            r0 = r10
            byte r0 = r0.readByte()     // Catch: java.lang.Exception -> Ld8
            io.netty.handler.codec.socksx.v5.Socks5CommandStatus r0 = io.netty.handler.codec.socksx.v5.Socks5CommandStatus.valueOf(r0)     // Catch: java.lang.Exception -> Ld8
            r13 = r0
            r0 = r10
            r1 = 1
            io.netty.buffer.ByteBuf r0 = r0.skipBytes(r1)     // Catch: java.lang.Exception -> Ld8
            r0 = r10
            byte r0 = r0.readByte()     // Catch: java.lang.Exception -> Ld8
            io.netty.handler.codec.socksx.v5.Socks5AddressType r0 = io.netty.handler.codec.socksx.v5.Socks5AddressType.valueOf(r0)     // Catch: java.lang.Exception -> Ld8
            r14 = r0
            r0 = r8
            io.netty.handler.codec.socksx.v5.Socks5AddressDecoder r0 = r0.addressDecoder     // Catch: java.lang.Exception -> Ld8
            r1 = r14
            r2 = r10
            java.lang.String r0 = r0.decodeAddress(r1, r2)     // Catch: java.lang.Exception -> Ld8
            r15 = r0
            r0 = r10
            int r0 = r0.readUnsignedShort()     // Catch: java.lang.Exception -> Ld8
            r16 = r0
            r0 = r11
            io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse r1 = new io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse     // Catch: java.lang.Exception -> Ld8
            r2 = r1
            r3 = r13
            r4 = r14
            r5 = r15
            r6 = r16
            r2.<init>(r3, r4, r5, r6)     // Catch: java.lang.Exception -> Ld8
            boolean r0 = r0.add(r1)     // Catch: java.lang.Exception -> Ld8
            r0 = r8
            io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder$State r1 = io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder.State.SUCCESS     // Catch: java.lang.Exception -> Ld8
            r0.checkpoint(r1)     // Catch: java.lang.Exception -> Ld8
        Lb1:
            r0 = r8
            int r0 = r0.actualReadableBytes()     // Catch: java.lang.Exception -> Ld8
            r12 = r0
            r0 = r12
            if (r0 <= 0) goto Ld5
            r0 = r11
            r1 = r10
            r2 = r12
            io.netty.buffer.ByteBuf r1 = r1.readRetainedSlice(r2)     // Catch: java.lang.Exception -> Ld8
            boolean r0 = r0.add(r1)     // Catch: java.lang.Exception -> Ld8
            goto Ld5
        Lcc:
            r0 = r10
            r1 = r8
            int r1 = r1.actualReadableBytes()     // Catch: java.lang.Exception -> Ld8
            io.netty.buffer.ByteBuf r0 = r0.skipBytes(r1)     // Catch: java.lang.Exception -> Ld8
        Ld5:
            goto Le1
        Ld8:
            r12 = move-exception
            r0 = r8
            r1 = r11
            r2 = r12
            r0.fail(r1, r2)
        Le1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    private void fail(List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        checkpoint(State.FAILURE);
        Socks5Message m = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4, null, 0);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
}

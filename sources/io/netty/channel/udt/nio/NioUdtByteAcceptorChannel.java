package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.udt.UdtChannel;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/udt/nio/NioUdtByteAcceptorChannel.class */
public class NioUdtByteAcceptorChannel extends NioUdtAcceptorChannel {
    public NioUdtByteAcceptorChannel() {
        super(TypeUDT.STREAM);
    }

    @Override // io.netty.channel.udt.nio.NioUdtAcceptorChannel
    protected UdtChannel newConnectorChannel(SocketChannelUDT channelUDT) {
        return new NioUdtByteConnectorChannel(this, channelUDT);
    }
}

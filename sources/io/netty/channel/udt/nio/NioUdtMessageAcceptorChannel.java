package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.udt.UdtChannel;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/udt/nio/NioUdtMessageAcceptorChannel.class */
public class NioUdtMessageAcceptorChannel extends NioUdtAcceptorChannel {
    public NioUdtMessageAcceptorChannel() {
        super(TypeUDT.DATAGRAM);
    }

    @Override // io.netty.channel.udt.nio.NioUdtAcceptorChannel
    protected UdtChannel newConnectorChannel(SocketChannelUDT channelUDT) {
        return new NioUdtMessageConnectorChannel(this, channelUDT);
    }
}

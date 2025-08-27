package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/udt/nio/NioUdtByteRendezvousChannel.class */
public class NioUdtByteRendezvousChannel extends NioUdtByteConnectorChannel {
    public NioUdtByteRendezvousChannel() {
        super((SocketChannelUDT) NioUdtProvider.newRendezvousChannelUDT(TypeUDT.STREAM));
    }
}

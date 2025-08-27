package io.netty.channel.rxtx;

import java.net.SocketAddress;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/rxtx/RxtxDeviceAddress.class */
public class RxtxDeviceAddress extends SocketAddress {
    private static final long serialVersionUID = -2907820090993709523L;
    private final String value;

    public RxtxDeviceAddress(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

package io.netty.channel;

import java.net.ConnectException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ConnectTimeoutException.class */
public class ConnectTimeoutException extends ConnectException {
    private static final long serialVersionUID = 2317065249988317463L;

    public ConnectTimeoutException(String msg) {
        super(msg);
    }

    public ConnectTimeoutException() {
    }
}

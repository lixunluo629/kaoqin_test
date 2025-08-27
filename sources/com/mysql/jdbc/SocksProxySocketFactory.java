package com.mysql.jdbc;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/SocksProxySocketFactory.class */
public class SocksProxySocketFactory extends StandardSocketFactory {
    public static int SOCKS_DEFAULT_PORT = 1080;

    @Override // com.mysql.jdbc.StandardSocketFactory
    protected Socket createSocket(Properties props) {
        String socksProxyHost = props.getProperty("socksProxyHost");
        String socksProxyPortString = props.getProperty("socksProxyPort", String.valueOf(SOCKS_DEFAULT_PORT));
        int socksProxyPort = SOCKS_DEFAULT_PORT;
        try {
            socksProxyPort = Integer.valueOf(socksProxyPortString).intValue();
        } catch (NumberFormatException e) {
        }
        return new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(socksProxyHost, socksProxyPort)));
    }
}

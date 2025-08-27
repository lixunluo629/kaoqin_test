package com.mysql.jdbc;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/SocketFactory.class */
public interface SocketFactory {
    Socket afterHandshake() throws IOException;

    Socket beforeHandshake() throws IOException;

    Socket connect(String str, int i, Properties properties) throws IOException;
}

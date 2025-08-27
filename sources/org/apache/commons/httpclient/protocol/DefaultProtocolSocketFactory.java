package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.commons.httpclient.params.HttpConnectionParams;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/DefaultProtocolSocketFactory.class */
public class DefaultProtocolSocketFactory implements ProtocolSocketFactory {
    private static final DefaultProtocolSocketFactory factory = new DefaultProtocolSocketFactory();

    static DefaultProtocolSocketFactory getSocketFactory() {
        return factory;
    }

    @Override // org.apache.commons.httpclient.protocol.ProtocolSocketFactory
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException {
        return new Socket(host, port, localAddress, localPort);
    }

    @Override // org.apache.commons.httpclient.protocol.ProtocolSocketFactory
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IllegalAccessException, InterruptedException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, IOException, IllegalArgumentException, InvocationTargetException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            return createSocket(host, port, localAddress, localPort);
        }
        Socket socket = ReflectionSocketFactory.createSocket("javax.net.SocketFactory", host, port, localAddress, localPort, timeout);
        if (socket == null) {
            socket = ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
        }
        return socket;
    }

    @Override // org.apache.commons.httpclient.protocol.ProtocolSocketFactory
    public Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    public int hashCode() {
        return getClass().hashCode();
    }
}

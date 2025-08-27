package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.httpclient.params.HttpConnectionParams;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/SSLProtocolSocketFactory.class */
public class SSLProtocolSocketFactory implements SecureProtocolSocketFactory {
    private static final SSLProtocolSocketFactory factory = new SSLProtocolSocketFactory();

    static SSLProtocolSocketFactory getSocketFactory() {
        return factory;
    }

    @Override // org.apache.commons.httpclient.protocol.ProtocolSocketFactory
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException {
        return SSLSocketFactory.getDefault().createSocket(host, port, clientHost, clientPort);
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
        Socket socket = ReflectionSocketFactory.createSocket("javax.net.ssl.SSLSocketFactory", host, port, localAddress, localPort, timeout);
        if (socket == null) {
            socket = ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
        }
        return socket;
    }

    @Override // org.apache.commons.httpclient.protocol.ProtocolSocketFactory
    public Socket createSocket(String host, int port) throws IOException {
        return SSLSocketFactory.getDefault().createSocket(host, port);
    }

    @Override // org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(socket, host, port, autoClose);
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    public int hashCode() {
        return getClass().hashCode();
    }
}

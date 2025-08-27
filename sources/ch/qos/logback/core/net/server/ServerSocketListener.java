package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;
import ch.qos.logback.core.util.CloseUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/net/server/ServerSocketListener.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/net/server/ServerSocketListener.class */
public abstract class ServerSocketListener<T extends Client> implements ServerListener<T> {
    private final ServerSocket serverSocket;

    protected abstract T createClient(String str, Socket socket) throws IOException;

    public ServerSocketListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override // ch.qos.logback.core.net.server.ServerListener
    public T acceptClient() throws IOException {
        Socket socketAccept = this.serverSocket.accept();
        return (T) createClient(socketAddressToString(socketAccept.getRemoteSocketAddress()), socketAccept);
    }

    @Override // ch.qos.logback.core.net.server.ServerListener, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        CloseUtil.closeQuietly(this.serverSocket);
    }

    public String toString() {
        return socketAddressToString(this.serverSocket.getLocalSocketAddress());
    }

    private String socketAddressToString(SocketAddress address) {
        String addr = address.toString();
        int i = addr.indexOf("/");
        if (i >= 0) {
            addr = addr.substring(i + 1);
        }
        return addr;
    }
}

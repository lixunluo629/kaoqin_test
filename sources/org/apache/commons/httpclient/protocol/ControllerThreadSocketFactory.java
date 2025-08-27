package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.util.TimeoutController;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/ControllerThreadSocketFactory.class */
public final class ControllerThreadSocketFactory {
    private ControllerThreadSocketFactory() {
    }

    public static Socket createSocket(ProtocolSocketFactory socketfactory, String host, int port, InetAddress localAddress, int localPort, int timeout) throws InterruptedException, IOException {
        SocketTask task = new SocketTask(socketfactory, host, port, localAddress, localPort) { // from class: org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory.1
            private final ProtocolSocketFactory val$socketfactory;
            private final String val$host;
            private final int val$port;
            private final InetAddress val$localAddress;
            private final int val$localPort;

            {
                this.val$socketfactory = socketfactory;
                this.val$host = host;
                this.val$port = port;
                this.val$localAddress = localAddress;
                this.val$localPort = localPort;
            }

            @Override // org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory.SocketTask
            public void doit() throws IOException {
                setSocket(this.val$socketfactory.createSocket(this.val$host, this.val$port, this.val$localAddress, this.val$localPort));
            }
        };
        try {
            TimeoutController.execute(task, timeout);
            Socket socket = task.getSocket();
            if (task.exception == null) {
                return socket;
            }
            throw task.exception;
        } catch (TimeoutController.TimeoutException e) {
            throw new ConnectTimeoutException(new StringBuffer().append("The host did not accept the connection within timeout of ").append(timeout).append(" ms").toString());
        }
    }

    public static Socket createSocket(SocketTask task, int timeout) throws InterruptedException, IOException {
        try {
            TimeoutController.execute(task, timeout);
            Socket socket = task.getSocket();
            if (task.exception == null) {
                return socket;
            }
            throw task.exception;
        } catch (TimeoutController.TimeoutException e) {
            throw new ConnectTimeoutException(new StringBuffer().append("The host did not accept the connection within timeout of ").append(timeout).append(" ms").toString());
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/protocol/ControllerThreadSocketFactory$SocketTask.class */
    public static abstract class SocketTask implements Runnable {
        private Socket socket;
        private IOException exception;

        public abstract void doit() throws IOException;

        protected void setSocket(Socket newSocket) {
            this.socket = newSocket;
        }

        protected Socket getSocket() {
            return this.socket;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                doit();
            } catch (IOException e) {
                this.exception = e;
            }
        }
    }
}

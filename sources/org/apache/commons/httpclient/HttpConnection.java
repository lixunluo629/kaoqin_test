package org.apache.commons.httpclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.httpclient.util.ExceptionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpConnection.class */
public class HttpConnection {
    private static final byte[] CRLF = {13, 10};
    private static final Log LOG;
    private String hostName;
    private int portNumber;
    private String proxyHostName;
    private int proxyPortNumber;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private InputStream lastResponseInputStream;
    protected boolean isOpen;
    private Protocol protocolInUse;
    private HttpConnectionParams params;
    private boolean locked;
    private boolean usingSecureSocket;
    private boolean tunnelEstablished;
    private HttpConnectionManager httpConnectionManager;
    private InetAddress localAddress;
    static Class class$org$apache$commons$httpclient$HttpConnection;

    public HttpConnection(String host, int port) {
        this(null, -1, host, null, port, Protocol.getProtocol("http"));
    }

    public HttpConnection(String host, int port, Protocol protocol) {
        this(null, -1, host, null, port, protocol);
    }

    public HttpConnection(String host, String virtualHost, int port, Protocol protocol) {
        this(null, -1, host, virtualHost, port, protocol);
    }

    public HttpConnection(String proxyHost, int proxyPort, String host, int port) {
        this(proxyHost, proxyPort, host, null, port, Protocol.getProtocol("http"));
    }

    public HttpConnection(HostConfiguration hostConfiguration) {
        this(hostConfiguration.getProxyHost(), hostConfiguration.getProxyPort(), hostConfiguration.getHost(), hostConfiguration.getPort(), hostConfiguration.getProtocol());
        this.localAddress = hostConfiguration.getLocalAddress();
    }

    public HttpConnection(String proxyHost, int proxyPort, String host, String virtualHost, int port, Protocol protocol) {
        this(proxyHost, proxyPort, host, port, protocol);
    }

    public HttpConnection(String proxyHost, int proxyPort, String host, int port, Protocol protocol) {
        this.hostName = null;
        this.portNumber = -1;
        this.proxyHostName = null;
        this.proxyPortNumber = -1;
        this.socket = null;
        this.inputStream = null;
        this.outputStream = null;
        this.lastResponseInputStream = null;
        this.isOpen = false;
        this.params = new HttpConnectionParams();
        this.locked = false;
        this.usingSecureSocket = false;
        this.tunnelEstablished = false;
        if (host == null) {
            throw new IllegalArgumentException("host parameter is null");
        }
        if (protocol == null) {
            throw new IllegalArgumentException("protocol is null");
        }
        this.proxyHostName = proxyHost;
        this.proxyPortNumber = proxyPort;
        this.hostName = host;
        this.portNumber = protocol.resolvePort(port);
        this.protocolInUse = protocol;
    }

    protected Socket getSocket() {
        return this.socket;
    }

    public String getHost() {
        return this.hostName;
    }

    public void setHost(String host) throws IllegalStateException {
        if (host == null) {
            throw new IllegalArgumentException("host parameter is null");
        }
        assertNotOpen();
        this.hostName = host;
    }

    public String getVirtualHost() {
        return this.hostName;
    }

    public void setVirtualHost(String host) throws IllegalStateException {
        assertNotOpen();
    }

    public int getPort() {
        if (this.portNumber < 0) {
            return isSecure() ? 443 : 80;
        }
        return this.portNumber;
    }

    public void setPort(int port) throws IllegalStateException {
        assertNotOpen();
        this.portNumber = port;
    }

    public String getProxyHost() {
        return this.proxyHostName;
    }

    public void setProxyHost(String host) throws IllegalStateException {
        assertNotOpen();
        this.proxyHostName = host;
    }

    public int getProxyPort() {
        return this.proxyPortNumber;
    }

    public void setProxyPort(int port) throws IllegalStateException {
        assertNotOpen();
        this.proxyPortNumber = port;
    }

    public boolean isSecure() {
        return this.protocolInUse.isSecure();
    }

    public Protocol getProtocol() {
        return this.protocolInUse;
    }

    public void setProtocol(Protocol protocol) throws IllegalStateException {
        assertNotOpen();
        if (protocol == null) {
            throw new IllegalArgumentException("protocol is null");
        }
        this.protocolInUse = protocol;
    }

    public InetAddress getLocalAddress() {
        return this.localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) throws IllegalStateException {
        assertNotOpen();
        this.localAddress = localAddress;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public boolean closeIfStale() throws IOException {
        if (this.isOpen && isStale()) {
            LOG.debug("Connection is stale, closing...");
            close();
            return true;
        }
        return false;
    }

    public boolean isStaleCheckingEnabled() {
        return this.params.isStaleCheckingEnabled();
    }

    public void setStaleCheckingEnabled(boolean staleCheckEnabled) {
        this.params.setStaleCheckingEnabled(staleCheckEnabled);
    }

    /* JADX WARN: Finally extract failed */
    protected boolean isStale() throws IOException {
        boolean isStale = true;
        if (this.isOpen) {
            isStale = false;
            try {
                if (this.inputStream.available() <= 0) {
                    try {
                        this.socket.setSoTimeout(1);
                        this.inputStream.mark(1);
                        int byteRead = this.inputStream.read();
                        if (byteRead == -1) {
                            isStale = true;
                        } else {
                            this.inputStream.reset();
                        }
                        this.socket.setSoTimeout(this.params.getSoTimeout());
                    } catch (Throwable th) {
                        this.socket.setSoTimeout(this.params.getSoTimeout());
                        throw th;
                    }
                }
            } catch (InterruptedIOException e) {
                if (!ExceptionUtil.isSocketTimeoutException(e)) {
                    throw e;
                }
            } catch (IOException e2) {
                LOG.debug("An error occurred while reading from the socket, is appears to be stale", e2);
                isStale = true;
            }
        }
        return isStale;
    }

    public boolean isProxied() {
        return null != this.proxyHostName && 0 < this.proxyPortNumber;
    }

    public void setLastResponseInputStream(InputStream inStream) {
        this.lastResponseInputStream = inStream;
    }

    public InputStream getLastResponseInputStream() {
        return this.lastResponseInputStream;
    }

    public HttpConnectionParams getParams() {
        return this.params;
    }

    public void setParams(HttpConnectionParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }

    public void setSoTimeout(int timeout) throws IllegalStateException, SocketException {
        this.params.setSoTimeout(timeout);
        if (this.socket != null) {
            this.socket.setSoTimeout(timeout);
        }
    }

    public void setSocketTimeout(int timeout) throws IllegalStateException, SocketException {
        assertOpen();
        if (this.socket != null) {
            this.socket.setSoTimeout(timeout);
        }
    }

    public int getSoTimeout() throws SocketException {
        return this.params.getSoTimeout();
    }

    public void setConnectionTimeout(int timeout) {
        this.params.setConnectionTimeout(timeout);
    }

    public void open() throws IllegalStateException, IOException {
        ProtocolSocketFactory socketFactory;
        LOG.trace("enter HttpConnection.open()");
        String host = this.proxyHostName == null ? this.hostName : this.proxyHostName;
        int port = this.proxyHostName == null ? this.portNumber : this.proxyPortNumber;
        assertNotOpen();
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Open connection to ").append(host).append(":").append(port).toString());
        }
        try {
            if (this.socket == null) {
                this.usingSecureSocket = isSecure() && !isProxied();
                if (isSecure() && isProxied()) {
                    Protocol defaultprotocol = Protocol.getProtocol("http");
                    socketFactory = defaultprotocol.getSocketFactory();
                } else {
                    socketFactory = this.protocolInUse.getSocketFactory();
                }
                this.socket = socketFactory.createSocket(host, port, this.localAddress, 0, this.params);
            }
            this.socket.setTcpNoDelay(this.params.getTcpNoDelay());
            this.socket.setSoTimeout(this.params.getSoTimeout());
            int linger = this.params.getLinger();
            if (linger >= 0) {
                this.socket.setSoLinger(linger > 0, linger);
            }
            int sndBufSize = this.params.getSendBufferSize();
            if (sndBufSize >= 0) {
                this.socket.setSendBufferSize(sndBufSize);
            }
            int rcvBufSize = this.params.getReceiveBufferSize();
            if (rcvBufSize >= 0) {
                this.socket.setReceiveBufferSize(rcvBufSize);
            }
            int outbuffersize = this.socket.getSendBufferSize();
            if (outbuffersize > 2048 || outbuffersize <= 0) {
                outbuffersize = 2048;
            }
            int inbuffersize = this.socket.getReceiveBufferSize();
            if (inbuffersize > 2048 || inbuffersize <= 0) {
                inbuffersize = 2048;
            }
            this.inputStream = new BufferedInputStream(this.socket.getInputStream(), inbuffersize);
            this.outputStream = new BufferedOutputStream(this.socket.getOutputStream(), outbuffersize);
            this.isOpen = true;
        } catch (IOException e) {
            closeSocketAndStreams();
            throw e;
        }
    }

    public void tunnelCreated() throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.tunnelCreated()");
        if (!isSecure() || !isProxied()) {
            throw new IllegalStateException("Connection must be secure and proxied to use this feature");
        }
        if (this.usingSecureSocket) {
            throw new IllegalStateException("Already using a secure socket");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("Secure tunnel to ").append(this.hostName).append(":").append(this.portNumber).toString());
        }
        SecureProtocolSocketFactory socketFactory = (SecureProtocolSocketFactory) this.protocolInUse.getSocketFactory();
        this.socket = socketFactory.createSocket(this.socket, this.hostName, this.portNumber, true);
        int sndBufSize = this.params.getSendBufferSize();
        if (sndBufSize >= 0) {
            this.socket.setSendBufferSize(sndBufSize);
        }
        int rcvBufSize = this.params.getReceiveBufferSize();
        if (rcvBufSize >= 0) {
            this.socket.setReceiveBufferSize(rcvBufSize);
        }
        int outbuffersize = this.socket.getSendBufferSize();
        if (outbuffersize > 2048) {
            outbuffersize = 2048;
        }
        int inbuffersize = this.socket.getReceiveBufferSize();
        if (inbuffersize > 2048) {
            inbuffersize = 2048;
        }
        this.inputStream = new BufferedInputStream(this.socket.getInputStream(), inbuffersize);
        this.outputStream = new BufferedOutputStream(this.socket.getOutputStream(), outbuffersize);
        this.usingSecureSocket = true;
        this.tunnelEstablished = true;
    }

    public boolean isTransparent() {
        return !isProxied() || this.tunnelEstablished;
    }

    public void flushRequestOutputStream() throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.flushRequestOutputStream()");
        assertOpen();
        this.outputStream.flush();
    }

    public OutputStream getRequestOutputStream() throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.getRequestOutputStream()");
        assertOpen();
        OutputStream out = this.outputStream;
        if (Wire.CONTENT_WIRE.enabled()) {
            out = new WireLogOutputStream(out, Wire.CONTENT_WIRE);
        }
        return out;
    }

    public InputStream getResponseInputStream() throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.getResponseInputStream()");
        assertOpen();
        return this.inputStream;
    }

    public boolean isResponseAvailable() throws IOException {
        LOG.trace("enter HttpConnection.isResponseAvailable()");
        return this.isOpen && this.inputStream.available() > 0;
    }

    public boolean isResponseAvailable(int timeout) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.isResponseAvailable(int)");
        assertOpen();
        boolean result = false;
        try {
            if (this.inputStream.available() > 0) {
                result = true;
            } else {
                try {
                    this.socket.setSoTimeout(timeout);
                    this.inputStream.mark(1);
                    int byteRead = this.inputStream.read();
                    if (byteRead != -1) {
                        this.inputStream.reset();
                        LOG.debug("Input data available");
                        result = true;
                    } else {
                        LOG.debug("Input data not available");
                    }
                    try {
                        this.socket.setSoTimeout(this.params.getSoTimeout());
                    } catch (IOException ioe) {
                        LOG.debug("An error ocurred while resetting soTimeout, we will assume that no response is available.", ioe);
                        result = false;
                    }
                } catch (InterruptedIOException e) {
                    if (!ExceptionUtil.isSocketTimeoutException(e)) {
                        throw e;
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(new StringBuffer().append("Input data not available after ").append(timeout).append(" ms").toString());
                    }
                    try {
                        this.socket.setSoTimeout(this.params.getSoTimeout());
                    } catch (IOException ioe2) {
                        LOG.debug("An error ocurred while resetting soTimeout, we will assume that no response is available.", ioe2);
                        result = false;
                    }
                }
            }
            return result;
        } catch (Throwable th) {
            try {
                this.socket.setSoTimeout(this.params.getSoTimeout());
            } catch (IOException ioe3) {
                LOG.debug("An error ocurred while resetting soTimeout, we will assume that no response is available.", ioe3);
            }
            throw th;
        }
    }

    public void write(byte[] data) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.write(byte[])");
        write(data, 0, data.length);
    }

    public void write(byte[] data, int offset, int length) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.write(byte[], int, int)");
        if (offset < 0) {
            throw new IllegalArgumentException("Array offset may not be negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Array length may not be negative");
        }
        if (offset + length > data.length) {
            throw new IllegalArgumentException("Given offset and length exceed the array length");
        }
        assertOpen();
        this.outputStream.write(data, offset, length);
    }

    public void writeLine(byte[] data) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.writeLine(byte[])");
        write(data);
        writeLine();
    }

    public void writeLine() throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.writeLine()");
        write(CRLF);
    }

    public void print(String data) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.print(String)");
        write(EncodingUtil.getBytes(data, "ISO-8859-1"));
    }

    public void print(String data, String charset) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.print(String)");
        write(EncodingUtil.getBytes(data, charset));
    }

    public void printLine(String data) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.printLine(String)");
        writeLine(EncodingUtil.getBytes(data, "ISO-8859-1"));
    }

    public void printLine(String data, String charset) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.printLine(String)");
        writeLine(EncodingUtil.getBytes(data, charset));
    }

    public void printLine() throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.printLine()");
        writeLine();
    }

    public String readLine() throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.readLine()");
        assertOpen();
        return HttpParser.readLine(this.inputStream);
    }

    public String readLine(String charset) throws IllegalStateException, IOException {
        LOG.trace("enter HttpConnection.readLine()");
        assertOpen();
        return HttpParser.readLine(this.inputStream, charset);
    }

    public void shutdownOutput() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        LOG.trace("enter HttpConnection.shutdownOutput()");
        try {
            Class[] paramsClasses = new Class[0];
            Method shutdownOutput = this.socket.getClass().getMethod("shutdownOutput", paramsClasses);
            Object[] params = new Object[0];
            shutdownOutput.invoke(this.socket, params);
        } catch (Exception ex) {
            LOG.debug("Unexpected Exception caught", ex);
        }
    }

    public void close() throws IOException {
        LOG.trace("enter HttpConnection.close()");
        closeSocketAndStreams();
    }

    public HttpConnectionManager getHttpConnectionManager() {
        return this.httpConnectionManager;
    }

    public void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
        this.httpConnectionManager = httpConnectionManager;
    }

    public void releaseConnection() {
        LOG.trace("enter HttpConnection.releaseConnection()");
        if (this.locked) {
            LOG.debug("Connection is locked.  Call to releaseConnection() ignored.");
        } else if (this.httpConnectionManager != null) {
            LOG.debug("Releasing connection back to connection manager.");
            this.httpConnectionManager.releaseConnection(this);
        } else {
            LOG.warn("HttpConnectionManager is null.  Connection cannot be released.");
        }
    }

    protected boolean isLocked() {
        return this.locked;
    }

    protected void setLocked(boolean locked) {
        this.locked = locked;
    }

    protected void closeSocketAndStreams() throws IOException {
        LOG.trace("enter HttpConnection.closeSockedAndStreams()");
        this.isOpen = false;
        this.lastResponseInputStream = null;
        if (null != this.outputStream) {
            OutputStream temp = this.outputStream;
            this.outputStream = null;
            try {
                temp.close();
            } catch (Exception ex) {
                LOG.debug("Exception caught when closing output", ex);
            }
        }
        if (null != this.inputStream) {
            InputStream temp2 = this.inputStream;
            this.inputStream = null;
            try {
                temp2.close();
            } catch (Exception ex2) {
                LOG.debug("Exception caught when closing input", ex2);
            }
        }
        if (null != this.socket) {
            Socket temp3 = this.socket;
            this.socket = null;
            try {
                temp3.close();
            } catch (Exception ex3) {
                LOG.debug("Exception caught when closing socket", ex3);
            }
        }
        this.tunnelEstablished = false;
        this.usingSecureSocket = false;
    }

    protected void assertNotOpen() throws IllegalStateException {
        if (this.isOpen) {
            throw new IllegalStateException("Connection is open");
        }
    }

    protected void assertOpen() throws IllegalStateException {
        if (!this.isOpen) {
            throw new IllegalStateException("Connection is not open");
        }
    }

    public int getSendBufferSize() throws SocketException {
        if (this.socket == null) {
            return -1;
        }
        return this.socket.getSendBufferSize();
    }

    public void setSendBufferSize(int sendBufferSize) throws SocketException {
        this.params.setSendBufferSize(sendBufferSize);
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$HttpConnection == null) {
            clsClass$ = class$("org.apache.commons.httpclient.HttpConnection");
            class$org$apache$commons$httpclient$HttpConnection = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$HttpConnection;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

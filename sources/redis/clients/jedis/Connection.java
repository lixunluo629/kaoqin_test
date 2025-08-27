package redis.clients.jedis;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.IOUtils;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Connection.class */
public class Connection implements Closeable {
    private static final byte[][] EMPTY_ARGS = new byte[0];
    private String host;
    private int port;
    private Socket socket;
    private RedisOutputStream outputStream;
    private RedisInputStream inputStream;
    private int pipelinedCommands;
    private int connectionTimeout;
    private int soTimeout;
    private boolean broken;
    private boolean ssl;
    private SSLSocketFactory sslSocketFactory;
    private SSLParameters sslParameters;
    private HostnameVerifier hostnameVerifier;

    public Connection() {
        this.host = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.pipelinedCommands = 0;
        this.connectionTimeout = 2000;
        this.soTimeout = 2000;
        this.broken = false;
    }

    public Connection(String host) {
        this.host = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.pipelinedCommands = 0;
        this.connectionTimeout = 2000;
        this.soTimeout = 2000;
        this.broken = false;
        this.host = host;
    }

    public Connection(String host, int port) {
        this.host = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.pipelinedCommands = 0;
        this.connectionTimeout = 2000;
        this.soTimeout = 2000;
        this.broken = false;
        this.host = host;
        this.port = port;
    }

    public Connection(String host, int port, boolean ssl) {
        this.host = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.pipelinedCommands = 0;
        this.connectionTimeout = 2000;
        this.soTimeout = 2000;
        this.broken = false;
        this.host = host;
        this.port = port;
        this.ssl = ssl;
    }

    public Connection(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.host = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.pipelinedCommands = 0;
        this.connectionTimeout = 2000;
        this.soTimeout = 2000;
        this.broken = false;
        this.host = host;
        this.port = port;
        this.ssl = ssl;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setTimeoutInfinite() throws SocketException {
        try {
            if (!isConnected()) {
                connect();
            }
            this.socket.setSoTimeout(0);
        } catch (SocketException ex) {
            this.broken = true;
            throw new JedisConnectionException(ex);
        }
    }

    public void rollbackTimeout() throws SocketException {
        try {
            this.socket.setSoTimeout(this.soTimeout);
        } catch (SocketException ex) {
            this.broken = true;
            throw new JedisConnectionException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    protected Connection sendCommand(Protocol.Command cmd, String... args) {
        ?? r0 = new byte[args.length];
        for (int i = 0; i < args.length; i++) {
            r0[i] = SafeEncoder.encode(args[i]);
        }
        return sendCommand(cmd, (byte[][]) r0);
    }

    protected Connection sendCommand(Protocol.Command cmd) {
        return sendCommand(cmd, EMPTY_ARGS);
    }

    protected Connection sendCommand(Protocol.Command cmd, byte[]... args) {
        try {
            connect();
            Protocol.sendCommand(this.outputStream, cmd, args);
            this.pipelinedCommands++;
            return this;
        } catch (JedisConnectionException e) {
            ex = e;
            try {
                String errorMessage = Protocol.readErrorLineIfPossible(this.inputStream);
                if (errorMessage != null && errorMessage.length() > 0) {
                    ex = new JedisConnectionException(errorMessage, ex.getCause());
                }
            } catch (Exception e2) {
            }
            this.broken = true;
            throw ex;
        }
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        if (!isConnected()) {
            try {
                this.socket = new Socket();
                this.socket.setReuseAddress(true);
                this.socket.setKeepAlive(true);
                this.socket.setTcpNoDelay(true);
                this.socket.setSoLinger(true, 0);
                this.socket.connect(new InetSocketAddress(this.host, this.port), this.connectionTimeout);
                this.socket.setSoTimeout(this.soTimeout);
                if (this.ssl) {
                    if (null == this.sslSocketFactory) {
                        this.sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    }
                    this.socket = (SSLSocket) this.sslSocketFactory.createSocket(this.socket, this.host, this.port, true);
                    if (null != this.sslParameters) {
                        ((SSLSocket) this.socket).setSSLParameters(this.sslParameters);
                    }
                    if (null != this.hostnameVerifier && !this.hostnameVerifier.verify(this.host, ((SSLSocket) this.socket).getSession())) {
                        String message = String.format("The connection to '%s' failed ssl/tls hostname verification.", this.host);
                        throw new JedisConnectionException(message);
                    }
                }
                this.outputStream = new RedisOutputStream(this.socket.getOutputStream());
                this.inputStream = new RedisInputStream(this.socket.getInputStream());
            } catch (IOException ex) {
                this.broken = true;
                throw new JedisConnectionException("Failed connecting to host " + this.host + ":" + this.port, ex);
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        disconnect();
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                try {
                    this.outputStream.flush();
                    this.socket.close();
                    IOUtils.closeQuietly(this.socket);
                } catch (IOException ex) {
                    this.broken = true;
                    throw new JedisConnectionException(ex);
                }
            } catch (Throwable th) {
                IOUtils.closeQuietly(this.socket);
                throw th;
            }
        }
    }

    public boolean isConnected() {
        return (this.socket == null || !this.socket.isBound() || this.socket.isClosed() || !this.socket.isConnected() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) ? false : true;
    }

    public String getStatusCodeReply() {
        flush();
        this.pipelinedCommands--;
        byte[] resp = (byte[]) readProtocolWithCheckingBroken();
        if (null == resp) {
            return null;
        }
        return SafeEncoder.encode(resp);
    }

    public String getBulkReply() {
        byte[] result = getBinaryBulkReply();
        if (null != result) {
            return SafeEncoder.encode(result);
        }
        return null;
    }

    public byte[] getBinaryBulkReply() {
        flush();
        this.pipelinedCommands--;
        return (byte[]) readProtocolWithCheckingBroken();
    }

    public Long getIntegerReply() {
        flush();
        this.pipelinedCommands--;
        return (Long) readProtocolWithCheckingBroken();
    }

    public List<String> getMultiBulkReply() {
        return BuilderFactory.STRING_LIST.build(getBinaryMultiBulkReply());
    }

    public List<byte[]> getBinaryMultiBulkReply() {
        flush();
        this.pipelinedCommands--;
        return (List) readProtocolWithCheckingBroken();
    }

    public void resetPipelinedCount() {
        this.pipelinedCommands = 0;
    }

    public List<Object> getRawObjectMultiBulkReply() {
        flush();
        this.pipelinedCommands--;
        return (List) readProtocolWithCheckingBroken();
    }

    public List<Object> getObjectMultiBulkReply() {
        return getRawObjectMultiBulkReply();
    }

    public List<Long> getIntegerMultiBulkReply() {
        flush();
        this.pipelinedCommands--;
        return (List) readProtocolWithCheckingBroken();
    }

    public List<Object> getAll() {
        return getAll(0);
    }

    public List<Object> getAll(int except) {
        List<Object> all = new ArrayList<>();
        flush();
        while (this.pipelinedCommands > except) {
            try {
                all.add(readProtocolWithCheckingBroken());
            } catch (JedisDataException e) {
                all.add(e);
            }
            this.pipelinedCommands--;
        }
        return all;
    }

    public Object getOne() {
        flush();
        this.pipelinedCommands--;
        return readProtocolWithCheckingBroken();
    }

    public boolean isBroken() {
        return this.broken;
    }

    protected void flush() {
        try {
            this.outputStream.flush();
        } catch (IOException ex) {
            this.broken = true;
            throw new JedisConnectionException(ex);
        }
    }

    protected Object readProtocolWithCheckingBroken() {
        try {
            return Protocol.read(this.inputStream);
        } catch (JedisConnectionException exc) {
            this.broken = true;
            throw exc;
        }
    }
}

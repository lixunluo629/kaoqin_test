package com.mysql.jdbc;

import com.mysql.jdbc.SocketMetadata;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/StandardSocketFactory.class */
public class StandardSocketFactory implements SocketFactory, SocketMetadata {
    public static final String TCP_NO_DELAY_PROPERTY_NAME = "tcpNoDelay";
    public static final String TCP_KEEP_ALIVE_DEFAULT_VALUE = "true";
    public static final String TCP_KEEP_ALIVE_PROPERTY_NAME = "tcpKeepAlive";
    public static final String TCP_RCV_BUF_PROPERTY_NAME = "tcpRcvBuf";
    public static final String TCP_SND_BUF_PROPERTY_NAME = "tcpSndBuf";
    public static final String TCP_TRAFFIC_CLASS_PROPERTY_NAME = "tcpTrafficClass";
    public static final String TCP_RCV_BUF_DEFAULT_VALUE = "0";
    public static final String TCP_SND_BUF_DEFAULT_VALUE = "0";
    public static final String TCP_TRAFFIC_CLASS_DEFAULT_VALUE = "0";
    public static final String TCP_NO_DELAY_DEFAULT_VALUE = "true";
    protected String host = null;
    protected int port = 3306;
    protected Socket rawSocket = null;
    protected int loginTimeoutCountdown = DriverManager.getLoginTimeout() * 1000;
    protected long loginTimeoutCheckTimestamp = System.currentTimeMillis();
    protected int socketTimeoutBackup = 0;

    @Override // com.mysql.jdbc.SocketFactory
    public Socket afterHandshake() throws IOException {
        resetLoginTimeCountdown();
        this.rawSocket.setSoTimeout(this.socketTimeoutBackup);
        return this.rawSocket;
    }

    @Override // com.mysql.jdbc.SocketFactory
    public Socket beforeHandshake() throws IOException {
        resetLoginTimeCountdown();
        this.socketTimeoutBackup = this.rawSocket.getSoTimeout();
        this.rawSocket.setSoTimeout(getRealTimeout(this.socketTimeoutBackup));
        return this.rawSocket;
    }

    protected Socket createSocket(Properties props) {
        return new Socket();
    }

    private void configureSocket(Socket sock, Properties props) throws NumberFormatException, IOException {
        sock.setTcpNoDelay(Boolean.valueOf(props.getProperty(TCP_NO_DELAY_PROPERTY_NAME, "true")).booleanValue());
        String keepAlive = props.getProperty(TCP_KEEP_ALIVE_PROPERTY_NAME, "true");
        if (keepAlive != null && keepAlive.length() > 0) {
            sock.setKeepAlive(Boolean.valueOf(keepAlive).booleanValue());
        }
        int receiveBufferSize = Integer.parseInt(props.getProperty(TCP_RCV_BUF_PROPERTY_NAME, "0"));
        if (receiveBufferSize > 0) {
            sock.setReceiveBufferSize(receiveBufferSize);
        }
        int sendBufferSize = Integer.parseInt(props.getProperty(TCP_SND_BUF_PROPERTY_NAME, "0"));
        if (sendBufferSize > 0) {
            sock.setSendBufferSize(sendBufferSize);
        }
        int trafficClass = Integer.parseInt(props.getProperty(TCP_TRAFFIC_CLASS_PROPERTY_NAME, "0"));
        if (trafficClass > 0) {
            sock.setTrafficClass(trafficClass);
        }
    }

    @Override // com.mysql.jdbc.SocketFactory
    public Socket connect(String hostname, int portNumber, Properties props) throws NumberFormatException, IOException {
        if (props != null) {
            this.host = hostname;
            this.port = portNumber;
            String localSocketHostname = props.getProperty("localSocketAddress");
            InetSocketAddress localSockAddr = null;
            if (localSocketHostname != null && localSocketHostname.length() > 0) {
                localSockAddr = new InetSocketAddress(InetAddress.getByName(localSocketHostname), 0);
            }
            String connectTimeoutStr = props.getProperty("connectTimeout");
            int connectTimeout = 0;
            if (connectTimeoutStr != null) {
                try {
                    connectTimeout = Integer.parseInt(connectTimeoutStr);
                } catch (NumberFormatException e) {
                    throw new SocketException("Illegal value '" + connectTimeoutStr + "' for connectTimeout");
                }
            }
            if (this.host != null) {
                InetAddress[] possibleAddresses = InetAddress.getAllByName(this.host);
                if (possibleAddresses.length == 0) {
                    throw new SocketException("No addresses for host");
                }
                SocketException lastException = null;
                for (InetAddress inetAddress : possibleAddresses) {
                    try {
                        this.rawSocket = createSocket(props);
                        configureSocket(this.rawSocket, props);
                        InetSocketAddress sockAddr = new InetSocketAddress(inetAddress, this.port);
                        if (localSockAddr != null) {
                            this.rawSocket.bind(localSockAddr);
                        }
                        this.rawSocket.connect(sockAddr, getRealTimeout(connectTimeout));
                        break;
                    } catch (SocketException ex) {
                        lastException = ex;
                        resetLoginTimeCountdown();
                        this.rawSocket = null;
                    }
                }
                if (this.rawSocket == null && lastException != null) {
                    throw lastException;
                }
                resetLoginTimeCountdown();
                return this.rawSocket;
            }
        }
        throw new SocketException("Unable to create socket");
    }

    @Override // com.mysql.jdbc.SocketMetadata
    public boolean isLocallyConnected(ConnectionImpl conn) throws SQLException {
        return SocketMetadata.Helper.isLocallyConnected(conn);
    }

    protected void resetLoginTimeCountdown() throws SocketException {
        if (this.loginTimeoutCountdown > 0) {
            long now = System.currentTimeMillis();
            this.loginTimeoutCountdown = (int) (this.loginTimeoutCountdown - (now - this.loginTimeoutCheckTimestamp));
            if (this.loginTimeoutCountdown <= 0) {
                throw new SocketException(Messages.getString("Connection.LoginTimeout"));
            }
            this.loginTimeoutCheckTimestamp = now;
        }
    }

    protected int getRealTimeout(int expectedTimeout) {
        if (this.loginTimeoutCountdown > 0 && (expectedTimeout == 0 || expectedTimeout > this.loginTimeoutCountdown)) {
            return this.loginTimeoutCountdown;
        }
        return expectedTimeout;
    }
}

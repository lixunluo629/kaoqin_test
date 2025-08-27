package com.mysql.jdbc;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/SocketMetadata.class */
public interface SocketMetadata {
    boolean isLocallyConnected(ConnectionImpl connectionImpl) throws SQLException;

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/SocketMetadata$Helper.class */
    public static class Helper {
        public static final String IS_LOCAL_HOSTNAME_REPLACEMENT_PROPERTY_NAME = "com.mysql.jdbc.test.isLocalHostnameReplacement";

        public static boolean isLocallyConnected(ConnectionImpl conn) throws SQLException, UnknownHostException {
            String processHost;
            long threadId = conn.getId();
            java.sql.Statement processListStmt = conn.getMetadataSafeStatement();
            if (System.getProperty(IS_LOCAL_HOSTNAME_REPLACEMENT_PROPERTY_NAME) != null) {
                processHost = System.getProperty(IS_LOCAL_HOSTNAME_REPLACEMENT_PROPERTY_NAME);
            } else if (conn.getProperties().getProperty(IS_LOCAL_HOSTNAME_REPLACEMENT_PROPERTY_NAME) != null) {
                processHost = conn.getProperties().getProperty(IS_LOCAL_HOSTNAME_REPLACEMENT_PROPERTY_NAME);
            } else {
                try {
                    processHost = findProcessHost(threadId, processListStmt);
                    if (processHost == null) {
                        conn.getLog().logWarn(String.format("Connection id %d not found in \"SHOW PROCESSLIST\", assuming 32-bit overflow, using SELECT CONNECTION_ID() instead", Long.valueOf(threadId)));
                        ResultSet rs = processListStmt.executeQuery("SELECT CONNECTION_ID()");
                        if (rs.next()) {
                            processHost = findProcessHost(rs.getLong(1), processListStmt);
                        } else {
                            conn.getLog().logError("No rows returned for statement \"SELECT CONNECTION_ID()\", local connection check will most likely be incorrect");
                        }
                    }
                } finally {
                    processListStmt.close();
                }
            }
            if (processHost != null) {
                conn.getLog().logDebug(Messages.getString("SocketMetadata.0", new Object[]{processHost}));
                int endIndex = processHost.lastIndexOf(":");
                if (endIndex != -1) {
                    processHost = processHost.substring(0, endIndex);
                }
                try {
                    InetAddress[] allHostAddr = InetAddress.getAllByName(processHost);
                    SocketAddress remoteSocketAddr = conn.getIO().mysqlConnection.getRemoteSocketAddress();
                    if (remoteSocketAddr instanceof InetSocketAddress) {
                        InetAddress whereIConnectedTo = ((InetSocketAddress) remoteSocketAddr).getAddress();
                        for (InetAddress hostAddr : allHostAddr) {
                            if (hostAddr.equals(whereIConnectedTo)) {
                                conn.getLog().logDebug(Messages.getString("SocketMetadata.1", new Object[]{hostAddr, whereIConnectedTo}));
                                return true;
                            }
                            conn.getLog().logDebug(Messages.getString("SocketMetadata.2", new Object[]{hostAddr, whereIConnectedTo}));
                        }
                        return false;
                    }
                    conn.getLog().logDebug(Messages.getString("SocketMetadata.3", new Object[]{remoteSocketAddr}));
                    return false;
                } catch (UnknownHostException e) {
                    conn.getLog().logWarn(Messages.getString("Connection.CantDetectLocalConnect", new Object[]{processHost}), e);
                    return false;
                }
            }
            return false;
        }

        private static String findProcessHost(long threadId, java.sql.Statement processListStmt) throws SQLException {
            String processHost = null;
            String ps = ((MySQLConnection) processListStmt.getConnection()).getServerVariable("performance_schema");
            ResultSet rs = (((MySQLConnection) processListStmt.getConnection()).versionMeetsMinimum(5, 6, 0) && ps != null && ("1".contentEquals(ps) || "ON".contentEquals(ps))) ? processListStmt.executeQuery("select PROCESSLIST_ID, PROCESSLIST_USER, PROCESSLIST_HOST from performance_schema.threads where PROCESSLIST_ID=" + threadId) : processListStmt.executeQuery("SHOW PROCESSLIST");
            while (true) {
                if (!rs.next()) {
                    break;
                }
                long id = rs.getLong(1);
                if (threadId == id) {
                    processHost = rs.getString(3);
                    break;
                }
            }
            return processHost;
        }
    }
}

package com.mysql.jdbc;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionFeatureNotAvailableException.class */
public class ConnectionFeatureNotAvailableException extends CommunicationsException {
    static final long serialVersionUID = -5065030488729238287L;

    public ConnectionFeatureNotAvailableException(MySQLConnection conn, long lastPacketSentTimeMs, Exception underlyingException) {
        super(conn, lastPacketSentTimeMs, 0L, underlyingException);
    }

    @Override // com.mysql.jdbc.CommunicationsException, java.lang.Throwable
    public String getMessage() {
        return "Feature not available in this distribution of Connector/J";
    }

    @Override // com.mysql.jdbc.CommunicationsException, java.sql.SQLException
    public String getSQLState() {
        return SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE;
    }
}

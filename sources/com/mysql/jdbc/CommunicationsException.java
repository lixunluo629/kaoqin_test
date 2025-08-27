package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/CommunicationsException.class */
public class CommunicationsException extends SQLException implements StreamingNotifiable {
    static final long serialVersionUID = 3193864990663398317L;
    private String exceptionMessage;

    public CommunicationsException(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException) {
        this.exceptionMessage = null;
        this.exceptionMessage = SQLError.createLinkFailureMessageBasedOnHeuristics(conn, lastPacketSentTimeMs, lastPacketReceivedTimeMs, underlyingException);
        if (underlyingException != null) {
            initCause(underlyingException);
        }
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.exceptionMessage;
    }

    @Override // java.sql.SQLException
    public String getSQLState() {
        return SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE;
    }

    @Override // com.mysql.jdbc.StreamingNotifiable
    public void setWasStreamingResults() {
        this.exceptionMessage = Messages.getString("CommunicationsException.ClientWasStreaming");
    }
}

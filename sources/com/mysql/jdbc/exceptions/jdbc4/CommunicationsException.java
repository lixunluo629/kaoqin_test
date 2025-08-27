package com.mysql.jdbc.exceptions.jdbc4;

import com.mysql.jdbc.Messages;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.StreamingNotifiable;
import java.sql.SQLRecoverableException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/jdbc4/CommunicationsException.class */
public class CommunicationsException extends SQLRecoverableException implements StreamingNotifiable {
    static final long serialVersionUID = 4317904269797988677L;
    private String exceptionMessage;

    public CommunicationsException(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException) {
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

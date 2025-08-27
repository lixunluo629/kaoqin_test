package com.mysql.jdbc;

import java.io.InputStream;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Statement.class */
public interface Statement extends java.sql.Statement, Wrapper {
    void enableStreamingResults() throws SQLException;

    void disableStreamingResults() throws SQLException;

    void setLocalInfileInputStream(InputStream inputStream);

    InputStream getLocalInfileInputStream();

    void setPingTarget(PingTarget pingTarget);

    ExceptionInterceptor getExceptionInterceptor();

    void removeOpenResultSet(ResultSetInternalMethods resultSetInternalMethods);

    int getOpenResultSetCount();

    void setHoldResultsOpenOverClose(boolean z);

    int getId();
}

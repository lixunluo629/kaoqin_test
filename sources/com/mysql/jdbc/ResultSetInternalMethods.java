package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ResultSetInternalMethods.class */
public interface ResultSetInternalMethods extends ResultSet {
    ResultSetInternalMethods copy() throws SQLException;

    boolean reallyResult();

    Object getObjectStoredProc(int i, int i2) throws SQLException;

    Object getObjectStoredProc(int i, Map<Object, Object> map, int i2) throws SQLException;

    Object getObjectStoredProc(String str, int i) throws SQLException;

    Object getObjectStoredProc(String str, Map<Object, Object> map, int i) throws SQLException;

    String getServerInfo();

    long getUpdateCount();

    long getUpdateID();

    void realClose(boolean z) throws SQLException;

    @Override // java.sql.ResultSet
    boolean isClosed() throws SQLException;

    void setFirstCharOfQuery(char c);

    void setOwningStatement(StatementImpl statementImpl);

    char getFirstCharOfQuery();

    void clearNextResult();

    ResultSetInternalMethods getNextResultSet();

    void setStatementUsedForFetchingRows(PreparedStatement preparedStatement);

    void setWrapperStatement(java.sql.Statement statement);

    void buildIndexMapping() throws SQLException;

    void initializeWithMetadata() throws SQLException;

    void redefineFieldsForDBMD(Field[] fieldArr);

    void populateCachedMetaData(CachedResultSetMetaData cachedResultSetMetaData) throws SQLException;

    void initializeFromCachedMetaData(CachedResultSetMetaData cachedResultSetMetaData);

    int getBytesSize() throws SQLException;

    int getId();
}

package org.apache.ibatis.executor.resultset;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.ibatis.cursor.Cursor;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/resultset/ResultSetHandler.class */
public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement statement) throws SQLException;

    <E> Cursor<E> handleCursorResultSets(Statement statement) throws SQLException;

    void handleOutputParameters(CallableStatement callableStatement) throws SQLException;
}

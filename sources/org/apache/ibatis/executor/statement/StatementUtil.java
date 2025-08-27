package org.apache.ibatis.executor.statement;

import java.sql.SQLException;
import java.sql.Statement;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/statement/StatementUtil.class */
public class StatementUtil {
    private StatementUtil() {
    }

    public static void applyTransactionTimeout(Statement statement, Integer queryTimeout, Integer transactionTimeout) throws SQLException {
        if (transactionTimeout == null) {
            return;
        }
        Integer timeToLiveOfQuery = null;
        if (queryTimeout == null || queryTimeout.intValue() == 0 || transactionTimeout.intValue() < queryTimeout.intValue()) {
            timeToLiveOfQuery = transactionTimeout;
        }
        if (timeToLiveOfQuery != null) {
            statement.setQueryTimeout(timeToLiveOfQuery.intValue());
        }
    }
}

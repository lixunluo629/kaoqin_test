package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/BatchUpdateUtils.class */
public abstract class BatchUpdateUtils {
    public static int[] executeBatchUpdate(String sql, final List<Object[]> batchArgs, final int[] columnTypes, JdbcOperations jdbcOperations) {
        if (batchArgs.isEmpty()) {
            return new int[0];
        }
        return jdbcOperations.batchUpdate(sql, new BatchPreparedStatementSetter() { // from class: org.springframework.jdbc.core.BatchUpdateUtils.1
            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] values = (Object[]) batchArgs.get(i);
                BatchUpdateUtils.setStatementParameters(values, ps, columnTypes);
            }

            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public int getBatchSize() {
                return batchArgs.size();
            }
        });
    }

    protected static void setStatementParameters(Object[] values, PreparedStatement ps, int[] columnTypes) throws SQLException {
        int colType;
        int colIndex = 0;
        for (Object value : values) {
            colIndex++;
            if (value instanceof SqlParameterValue) {
                SqlParameterValue paramValue = (SqlParameterValue) value;
                StatementCreatorUtils.setParameterValue(ps, colIndex, paramValue, paramValue.getValue());
            } else {
                if (columnTypes == null || columnTypes.length < colIndex) {
                    colType = Integer.MIN_VALUE;
                } else {
                    colType = columnTypes[colIndex - 1];
                }
                StatementCreatorUtils.setParameterValue(ps, colIndex, colType, value);
            }
        }
    }
}

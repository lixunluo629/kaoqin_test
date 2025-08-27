package org.springframework.jdbc.core.namedparam;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BatchUpdateUtils;
import org.springframework.jdbc.core.JdbcOperations;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/NamedParameterBatchUpdateUtils.class */
public class NamedParameterBatchUpdateUtils extends BatchUpdateUtils {
    public static int[] executeBatchUpdateWithNamedParameters(final ParsedSql parsedSql, final SqlParameterSource[] batchArgs, JdbcOperations jdbcOperations) {
        if (batchArgs.length == 0) {
            return new int[0];
        }
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, batchArgs[0]);
        return jdbcOperations.batchUpdate(sqlToUse, new BatchPreparedStatementSetter() { // from class: org.springframework.jdbc.core.namedparam.NamedParameterBatchUpdateUtils.1
            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] values = NamedParameterUtils.buildValueArray(parsedSql, batchArgs[i], null);
                int[] columnTypes = NamedParameterUtils.buildSqlTypeArray(parsedSql, batchArgs[i]);
                NamedParameterBatchUpdateUtils.setStatementParameters(values, ps, columnTypes);
            }

            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public int getBatchSize() {
                return batchArgs.length;
            }
        });
    }
}

package org.springframework.jdbc.support;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.BadSqlGrammarException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/SQLStateSQLExceptionTranslator.class */
public class SQLStateSQLExceptionTranslator extends AbstractFallbackSQLExceptionTranslator {
    private static final Set<String> BAD_SQL_GRAMMAR_CODES = new HashSet(8);
    private static final Set<String> DATA_INTEGRITY_VIOLATION_CODES = new HashSet(8);
    private static final Set<String> DATA_ACCESS_RESOURCE_FAILURE_CODES = new HashSet(8);
    private static final Set<String> TRANSIENT_DATA_ACCESS_RESOURCE_CODES = new HashSet(8);
    private static final Set<String> CONCURRENCY_FAILURE_CODES = new HashSet(4);

    static {
        BAD_SQL_GRAMMAR_CODES.add("07");
        BAD_SQL_GRAMMAR_CODES.add("21");
        BAD_SQL_GRAMMAR_CODES.add("2A");
        BAD_SQL_GRAMMAR_CODES.add(ANSIConstants.WHITE_FG);
        BAD_SQL_GRAMMAR_CODES.add("42");
        BAD_SQL_GRAMMAR_CODES.add("65");
        DATA_INTEGRITY_VIOLATION_CODES.add("01");
        DATA_INTEGRITY_VIOLATION_CODES.add("02");
        DATA_INTEGRITY_VIOLATION_CODES.add("22");
        DATA_INTEGRITY_VIOLATION_CODES.add("23");
        DATA_INTEGRITY_VIOLATION_CODES.add("27");
        DATA_INTEGRITY_VIOLATION_CODES.add("44");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("08");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("53");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("54");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("57");
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("58");
        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JW");
        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JZ");
        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("S1");
        CONCURRENCY_FAILURE_CODES.add("40");
        CONCURRENCY_FAILURE_CODES.add("61");
    }

    @Override // org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator
    protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
        String sqlState = getSqlState(ex);
        if (sqlState != null && sqlState.length() >= 2) {
            String classCode = sqlState.substring(0, 2);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Extracted SQL state class '" + classCode + "' from value '" + sqlState + "'");
            }
            if (BAD_SQL_GRAMMAR_CODES.contains(classCode)) {
                return new BadSqlGrammarException(task, sql, ex);
            }
            if (DATA_INTEGRITY_VIOLATION_CODES.contains(classCode)) {
                return new DataIntegrityViolationException(buildMessage(task, sql, ex), ex);
            }
            if (DATA_ACCESS_RESOURCE_FAILURE_CODES.contains(classCode)) {
                return new DataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
            }
            if (TRANSIENT_DATA_ACCESS_RESOURCE_CODES.contains(classCode)) {
                return new TransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
            }
            if (CONCURRENCY_FAILURE_CODES.contains(classCode)) {
                return new ConcurrencyFailureException(buildMessage(task, sql, ex), ex);
            }
        }
        if (ex.getClass().getName().contains("Timeout")) {
            return new QueryTimeoutException(buildMessage(task, sql, ex), ex);
        }
        return null;
    }

    private String getSqlState(SQLException ex) {
        SQLException nestedEx;
        String sqlState = ex.getSQLState();
        if (sqlState == null && (nestedEx = ex.getNextException()) != null) {
            sqlState = nestedEx.getSQLState();
        }
        return sqlState;
    }
}

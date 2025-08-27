package org.springframework.jdbc;

import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/JdbcUpdateAffectedIncorrectNumberOfRowsException.class */
public class JdbcUpdateAffectedIncorrectNumberOfRowsException extends IncorrectUpdateSemanticsDataAccessException {
    private int expected;
    private int actual;

    public JdbcUpdateAffectedIncorrectNumberOfRowsException(String sql, int expected, int actual) {
        super("SQL update '" + sql + "' affected " + actual + " rows, not " + expected + " as expected");
        this.expected = expected;
        this.actual = actual;
    }

    public int getExpectedRowsAffected() {
        return this.expected;
    }

    public int getActualRowsAffected() {
        return this.actual;
    }

    @Override // org.springframework.dao.IncorrectUpdateSemanticsDataAccessException
    public boolean wasDataUpdated() {
        return getActualRowsAffected() > 0;
    }
}

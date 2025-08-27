package org.springframework.jdbc;

import org.springframework.dao.DataRetrievalFailureException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/IncorrectResultSetColumnCountException.class */
public class IncorrectResultSetColumnCountException extends DataRetrievalFailureException {
    private int expectedCount;
    private int actualCount;

    public IncorrectResultSetColumnCountException(int expectedCount, int actualCount) {
        super("Incorrect column count: expected " + expectedCount + ", actual " + actualCount);
        this.expectedCount = expectedCount;
        this.actualCount = actualCount;
    }

    public IncorrectResultSetColumnCountException(String msg, int expectedCount, int actualCount) {
        super(msg);
        this.expectedCount = expectedCount;
        this.actualCount = actualCount;
    }

    public int getExpectedCount() {
        return this.expectedCount;
    }

    public int getActualCount() {
        return this.actualCount;
    }
}

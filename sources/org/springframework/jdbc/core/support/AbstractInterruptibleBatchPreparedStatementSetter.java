package org.springframework.jdbc.core.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/support/AbstractInterruptibleBatchPreparedStatementSetter.class */
public abstract class AbstractInterruptibleBatchPreparedStatementSetter implements InterruptibleBatchPreparedStatementSetter {
    private boolean exhausted;

    protected abstract boolean setValuesIfAvailable(PreparedStatement preparedStatement, int i) throws SQLException;

    @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
    public final void setValues(PreparedStatement ps, int i) throws SQLException {
        this.exhausted = !setValuesIfAvailable(ps, i);
    }

    @Override // org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter
    public final boolean isBatchExhausted(int i) {
        return this.exhausted;
    }

    @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
    public int getBatchSize() {
        return Integer.MAX_VALUE;
    }
}

package org.springframework.jdbc.object;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/SqlOperation.class */
public abstract class SqlOperation extends RdbmsOperation {
    private PreparedStatementCreatorFactory preparedStatementFactory;
    private ParsedSql cachedSql;
    private final Object parsedSqlMonitor = new Object();

    @Override // org.springframework.jdbc.object.RdbmsOperation
    protected final void compileInternal() {
        this.preparedStatementFactory = new PreparedStatementCreatorFactory(getSql(), getDeclaredParameters());
        this.preparedStatementFactory.setResultSetType(getResultSetType());
        this.preparedStatementFactory.setUpdatableResults(isUpdatableResults());
        this.preparedStatementFactory.setReturnGeneratedKeys(isReturnGeneratedKeys());
        if (getGeneratedKeysColumnNames() != null) {
            this.preparedStatementFactory.setGeneratedKeysColumnNames(getGeneratedKeysColumnNames());
        }
        this.preparedStatementFactory.setNativeJdbcExtractor(getJdbcTemplate().getNativeJdbcExtractor());
        onCompileInternal();
    }

    protected void onCompileInternal() {
    }

    protected ParsedSql getParsedSql() {
        ParsedSql parsedSql;
        synchronized (this.parsedSqlMonitor) {
            if (this.cachedSql == null) {
                this.cachedSql = NamedParameterUtils.parseSqlStatement(getSql());
            }
            parsedSql = this.cachedSql;
        }
        return parsedSql;
    }

    protected final PreparedStatementSetter newPreparedStatementSetter(Object[] params) {
        return this.preparedStatementFactory.newPreparedStatementSetter(params);
    }

    protected final PreparedStatementCreator newPreparedStatementCreator(Object[] params) {
        return this.preparedStatementFactory.newPreparedStatementCreator(params);
    }

    protected final PreparedStatementCreator newPreparedStatementCreator(String sqlToUse, Object[] params) {
        return this.preparedStatementFactory.newPreparedStatementCreator(sqlToUse, params);
    }
}

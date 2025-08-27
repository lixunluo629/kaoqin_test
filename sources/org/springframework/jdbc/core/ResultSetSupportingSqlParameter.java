package org.springframework.jdbc.core;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/ResultSetSupportingSqlParameter.class */
public class ResultSetSupportingSqlParameter extends SqlParameter {
    private ResultSetExtractor<?> resultSetExtractor;
    private RowCallbackHandler rowCallbackHandler;
    private RowMapper<?> rowMapper;

    public ResultSetSupportingSqlParameter(String name, int sqlType) {
        super(name, sqlType);
    }

    public ResultSetSupportingSqlParameter(String name, int sqlType, int scale) {
        super(name, sqlType, scale);
    }

    public ResultSetSupportingSqlParameter(String name, int sqlType, String typeName) {
        super(name, sqlType, typeName);
    }

    public ResultSetSupportingSqlParameter(String name, int sqlType, ResultSetExtractor<?> rse) {
        super(name, sqlType);
        this.resultSetExtractor = rse;
    }

    public ResultSetSupportingSqlParameter(String name, int sqlType, RowCallbackHandler rch) {
        super(name, sqlType);
        this.rowCallbackHandler = rch;
    }

    public ResultSetSupportingSqlParameter(String name, int sqlType, RowMapper<?> rm) {
        super(name, sqlType);
        this.rowMapper = rm;
    }

    public boolean isResultSetSupported() {
        return (this.resultSetExtractor == null && this.rowCallbackHandler == null && this.rowMapper == null) ? false : true;
    }

    public ResultSetExtractor<?> getResultSetExtractor() {
        return this.resultSetExtractor;
    }

    public RowCallbackHandler getRowCallbackHandler() {
        return this.rowCallbackHandler;
    }

    public RowMapper<?> getRowMapper() {
        return this.rowMapper;
    }

    @Override // org.springframework.jdbc.core.SqlParameter
    public boolean isInputValueProvided() {
        return false;
    }
}

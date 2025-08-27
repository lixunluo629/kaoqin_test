package org.springframework.jdbc.core;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlOutParameter.class */
public class SqlOutParameter extends ResultSetSupportingSqlParameter {
    private SqlReturnType sqlReturnType;

    public SqlOutParameter(String name, int sqlType) {
        super(name, sqlType);
    }

    public SqlOutParameter(String name, int sqlType, int scale) {
        super(name, sqlType, scale);
    }

    public SqlOutParameter(String name, int sqlType, String typeName) {
        super(name, sqlType, typeName);
    }

    public SqlOutParameter(String name, int sqlType, String typeName, SqlReturnType sqlReturnType) {
        super(name, sqlType, typeName);
        this.sqlReturnType = sqlReturnType;
    }

    public SqlOutParameter(String name, int sqlType, ResultSetExtractor<?> rse) {
        super(name, sqlType, rse);
    }

    public SqlOutParameter(String name, int sqlType, RowCallbackHandler rch) {
        super(name, sqlType, rch);
    }

    public SqlOutParameter(String name, int sqlType, RowMapper<?> rm) {
        super(name, sqlType, rm);
    }

    public SqlReturnType getSqlReturnType() {
        return this.sqlReturnType;
    }

    public boolean isReturnTypeSupported() {
        return this.sqlReturnType != null;
    }
}

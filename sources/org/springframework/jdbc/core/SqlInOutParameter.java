package org.springframework.jdbc.core;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlInOutParameter.class */
public class SqlInOutParameter extends SqlOutParameter {
    public SqlInOutParameter(String name, int sqlType) {
        super(name, sqlType);
    }

    public SqlInOutParameter(String name, int sqlType, int scale) {
        super(name, sqlType, scale);
    }

    public SqlInOutParameter(String name, int sqlType, String typeName) {
        super(name, sqlType, typeName);
    }

    public SqlInOutParameter(String name, int sqlType, String typeName, SqlReturnType sqlReturnType) {
        super(name, sqlType, typeName, sqlReturnType);
    }

    public SqlInOutParameter(String name, int sqlType, ResultSetExtractor<?> rse) {
        super(name, sqlType, rse);
    }

    public SqlInOutParameter(String name, int sqlType, RowCallbackHandler rch) {
        super(name, sqlType, rch);
    }

    public SqlInOutParameter(String name, int sqlType, RowMapper<?> rm) {
        super(name, sqlType, rm);
    }

    @Override // org.springframework.jdbc.core.ResultSetSupportingSqlParameter, org.springframework.jdbc.core.SqlParameter
    public boolean isInputValueProvided() {
        return true;
    }
}

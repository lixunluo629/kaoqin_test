package org.springframework.jdbc.core;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlReturnResultSet.class */
public class SqlReturnResultSet extends ResultSetSupportingSqlParameter {
    public SqlReturnResultSet(String name, ResultSetExtractor<?> extractor) {
        super(name, 0, extractor);
    }

    public SqlReturnResultSet(String name, RowCallbackHandler handler) {
        super(name, 0, handler);
    }

    public SqlReturnResultSet(String name, RowMapper<?> mapper) {
        super(name, 0, mapper);
    }

    @Override // org.springframework.jdbc.core.SqlParameter
    public boolean isResultsParameter() {
        return true;
    }
}

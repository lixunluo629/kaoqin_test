package org.springframework.jdbc.core;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlReturnUpdateCount.class */
public class SqlReturnUpdateCount extends SqlParameter {
    public SqlReturnUpdateCount(String name) {
        super(name, 4);
    }

    @Override // org.springframework.jdbc.core.SqlParameter
    public boolean isInputValueProvided() {
        return false;
    }

    @Override // org.springframework.jdbc.core.SqlParameter
    public boolean isResultsParameter() {
        return true;
    }
}

package org.springframework.jdbc.core.metadata;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/TableParameterMetaData.class */
public class TableParameterMetaData {
    private final String parameterName;
    private final int sqlType;
    private final boolean nullable;

    public TableParameterMetaData(String columnName, int sqlType, boolean nullable) {
        this.parameterName = columnName;
        this.sqlType = sqlType;
        this.nullable = nullable;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public int getSqlType() {
        return this.sqlType;
    }

    public boolean isNullable() {
        return this.nullable;
    }
}

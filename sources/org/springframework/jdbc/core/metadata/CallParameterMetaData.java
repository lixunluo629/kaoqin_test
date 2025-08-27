package org.springframework.jdbc.core.metadata;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/CallParameterMetaData.class */
public class CallParameterMetaData {
    private String parameterName;
    private int parameterType;
    private int sqlType;
    private String typeName;
    private boolean nullable;

    public CallParameterMetaData(String columnName, int columnType, int sqlType, String typeName, boolean nullable) {
        this.parameterName = columnName;
        this.parameterType = columnType;
        this.sqlType = sqlType;
        this.typeName = typeName;
        this.nullable = nullable;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public int getParameterType() {
        return this.parameterType;
    }

    public boolean isReturnParameter() {
        return this.parameterType == 5 || this.parameterType == 3;
    }

    public int getSqlType() {
        return this.sqlType;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public boolean isNullable() {
        return this.nullable;
    }
}

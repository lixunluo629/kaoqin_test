package org.springframework.jdbc.core.namedparam;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/EmptySqlParameterSource.class */
public class EmptySqlParameterSource implements SqlParameterSource {
    public static final EmptySqlParameterSource INSTANCE = new EmptySqlParameterSource();

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public boolean hasValue(String paramName) {
        return false;
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public Object getValue(String paramName) throws IllegalArgumentException {
        throw new IllegalArgumentException("This SqlParameterSource is empty");
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public int getSqlType(String paramName) {
        return Integer.MIN_VALUE;
    }

    @Override // org.springframework.jdbc.core.namedparam.SqlParameterSource
    public String getTypeName(String paramName) {
        return null;
    }
}

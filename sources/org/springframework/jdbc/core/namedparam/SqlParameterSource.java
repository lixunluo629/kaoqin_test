package org.springframework.jdbc.core.namedparam;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/SqlParameterSource.class */
public interface SqlParameterSource {
    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;

    boolean hasValue(String str);

    Object getValue(String str) throws IllegalArgumentException;

    int getSqlType(String str);

    String getTypeName(String str);
}

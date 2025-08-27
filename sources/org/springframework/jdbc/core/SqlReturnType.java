package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlReturnType.class */
public interface SqlReturnType {
    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;

    Object getTypeValue(CallableStatement callableStatement, int i, int i2, String str) throws SQLException;
}

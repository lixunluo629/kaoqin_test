package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/SqlTypeValue.class */
public interface SqlTypeValue {
    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;

    void setTypeValue(PreparedStatement preparedStatement, int i, int i2, String str) throws SQLException;
}

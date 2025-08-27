package org.springframework.jdbc.core.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.SqlTypeValue;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/support/AbstractSqlTypeValue.class */
public abstract class AbstractSqlTypeValue implements SqlTypeValue {
    protected abstract Object createTypeValue(Connection connection, int i, String str) throws SQLException;

    @Override // org.springframework.jdbc.core.SqlTypeValue
    public final void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
        Object value = createTypeValue(ps.getConnection(), sqlType, typeName);
        if (sqlType == Integer.MIN_VALUE) {
            ps.setObject(paramIndex, value);
        } else {
            ps.setObject(paramIndex, value, sqlType);
        }
    }
}

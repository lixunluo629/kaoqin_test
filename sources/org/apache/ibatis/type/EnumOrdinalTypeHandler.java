package org.apache.ibatis.type;

import java.lang.Enum;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/EnumOrdinalTypeHandler.class */
public class EnumOrdinalTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private final Class<E> type;
    private final E[] enums;

    public EnumOrdinalTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.ordinal());
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int i = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        try {
            return this.enums[i];
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + i + " to " + this.type.getSimpleName() + " by ordinal value.", ex);
        }
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int i = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        try {
            return this.enums[i];
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + i + " to " + this.type.getSimpleName() + " by ordinal value.", ex);
        }
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int i = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        try {
            return this.enums[i];
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + i + " to " + this.type.getSimpleName() + " by ordinal value.", ex);
        }
    }
}

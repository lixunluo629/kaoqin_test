package org.apache.ibatis.type;

import java.lang.Enum;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/EnumTypeHandler.class */
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, parameter.name());
        } else {
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE);
        }
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public E getNullableResult(ResultSet resultSet, String str) throws SQLException {
        String string = resultSet.getString(str);
        if (string == null) {
            return null;
        }
        return (E) Enum.valueOf(this.type, string);
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String string = resultSet.getString(i);
        if (string == null) {
            return null;
        }
        return (E) Enum.valueOf(this.type, string);
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String string = callableStatement.getString(i);
        if (string == null) {
            return null;
        }
        return (E) Enum.valueOf(this.type, string);
    }
}

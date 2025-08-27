package org.apache.ibatis.type;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/ArrayTypeHandler.class */
public class ArrayTypeHandler extends BaseTypeHandler<Object> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setArray(i, (Array) parameter);
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        if (array == null) {
            return null;
        }
        return array.getArray();
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        if (array == null) {
            return null;
        }
        return array.getArray();
    }

    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        if (array == null) {
            return null;
        }
        return array.getArray();
    }
}

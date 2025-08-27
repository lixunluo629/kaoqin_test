package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/ShortTypeHandler.class */
public class ShortTypeHandler extends BaseTypeHandler<Short> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Short parameter, JdbcType jdbcType) throws SQLException {
        ps.setShort(i, parameter.shortValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Short getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Short.valueOf(rs.getShort(columnName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Short getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Short.valueOf(rs.getShort(columnIndex));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Short getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Short.valueOf(cs.getShort(columnIndex));
    }
}

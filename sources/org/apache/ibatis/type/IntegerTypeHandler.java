package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/IntegerTypeHandler.class */
public class IntegerTypeHandler extends BaseTypeHandler<Integer> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Integer getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Integer.valueOf(rs.getInt(columnName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Integer getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Integer.valueOf(rs.getInt(columnIndex));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Integer getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Integer.valueOf(cs.getInt(columnIndex));
    }
}

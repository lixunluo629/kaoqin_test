package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/BooleanTypeHandler.class */
public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        ps.setBoolean(i, parameter.booleanValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Boolean.valueOf(rs.getBoolean(columnName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Boolean.valueOf(rs.getBoolean(columnIndex));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Boolean.valueOf(cs.getBoolean(columnIndex));
    }
}

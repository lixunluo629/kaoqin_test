package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/LongTypeHandler.class */
public class LongTypeHandler extends BaseTypeHandler<Long> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Long getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Long.valueOf(rs.getLong(columnName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Long getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Long.valueOf(rs.getLong(columnIndex));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Long getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Long.valueOf(cs.getLong(columnIndex));
    }
}

package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/SqlTimeTypeHandler.class */
public class SqlTimeTypeHandler extends BaseTypeHandler<Time> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Time parameter, JdbcType jdbcType) throws SQLException {
        ps.setTime(i, parameter);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Time getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getTime(columnName);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Time getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Time getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getTime(columnIndex);
    }
}

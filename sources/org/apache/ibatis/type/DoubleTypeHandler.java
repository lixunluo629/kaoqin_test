package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/DoubleTypeHandler.class */
public class DoubleTypeHandler extends BaseTypeHandler<Double> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Double parameter, JdbcType jdbcType) throws SQLException {
        ps.setDouble(i, parameter.doubleValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Double getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Double.valueOf(rs.getDouble(columnName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Double getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Double.valueOf(rs.getDouble(columnIndex));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Double getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Double.valueOf(cs.getDouble(columnIndex));
    }
}

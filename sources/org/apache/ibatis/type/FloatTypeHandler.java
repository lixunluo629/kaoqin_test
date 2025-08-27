package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/FloatTypeHandler.class */
public class FloatTypeHandler extends BaseTypeHandler<Float> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Float parameter, JdbcType jdbcType) throws SQLException {
        ps.setFloat(i, parameter.floatValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Float getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Float.valueOf(rs.getFloat(columnName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Float getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Float.valueOf(rs.getFloat(columnIndex));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Float getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Float.valueOf(cs.getFloat(columnIndex));
    }
}

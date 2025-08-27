package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/ByteTypeHandler.class */
public class ByteTypeHandler extends BaseTypeHandler<Byte> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Byte parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter.byteValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Byte getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Byte.valueOf(rs.getByte(columnName));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Byte getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Byte.valueOf(rs.getByte(columnIndex));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Byte getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Byte.valueOf(cs.getByte(columnIndex));
    }
}

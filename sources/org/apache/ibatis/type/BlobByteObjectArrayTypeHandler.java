package org.apache.ibatis.type;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/BlobByteObjectArrayTypeHandler.class */
public class BlobByteObjectArrayTypeHandler extends BaseTypeHandler<Byte[]> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Byte[] parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis = new ByteArrayInputStream(ByteArrayUtils.convertToPrimitiveArray(parameter));
        ps.setBinaryStream(i, (InputStream) bis, parameter.length);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Byte[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = rs.getBlob(columnName);
        return getBytes(blob);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Byte[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Blob blob = rs.getBlob(columnIndex);
        return getBytes(blob);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Byte[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        return getBytes(blob);
    }

    private Byte[] getBytes(Blob blob) throws SQLException {
        Byte[] returnValue = null;
        if (blob != null) {
            returnValue = ByteArrayUtils.convertToObjectArray(blob.getBytes(1L, (int) blob.length()));
        }
        return returnValue;
    }
}

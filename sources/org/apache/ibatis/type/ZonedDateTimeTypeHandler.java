package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.apache.ibatis.lang.UsesJava8;

@UsesJava8
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/ZonedDateTimeTypeHandler.class */
public class ZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, ZonedDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter.toInstant()));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return getZonedDateTime(timestamp);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public ZonedDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        return getZonedDateTime(timestamp);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public ZonedDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        return getZonedDateTime(timestamp);
    }

    private static ZonedDateTime getZonedDateTime(Timestamp timestamp) {
        if (timestamp != null) {
            return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }
}

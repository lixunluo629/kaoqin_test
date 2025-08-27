package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import org.apache.ibatis.lang.UsesJava8;

@UsesJava8
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/YearMonthTypeHandler.class */
public class YearMonthTypeHandler extends BaseTypeHandler<YearMonth> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, YearMonth yearMonth, JdbcType jt) throws SQLException {
        ps.setString(i, yearMonth.toString());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public YearMonth getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null;
        }
        return YearMonth.parse(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public YearMonth getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null) {
            return null;
        }
        return YearMonth.parse(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public YearMonth getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null) {
            return null;
        }
        return YearMonth.parse(value);
    }
}

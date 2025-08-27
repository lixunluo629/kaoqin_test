package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import org.apache.ibatis.lang.UsesJava8;

@UsesJava8
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/MonthTypeHandler.class */
public class MonthTypeHandler extends BaseTypeHandler<Month> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Month month, JdbcType type) throws SQLException {
        ps.setInt(i, month.getValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Month getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int month = rs.getInt(columnName);
        if (month == 0) {
            return null;
        }
        return Month.of(month);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Month getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int month = rs.getInt(columnIndex);
        if (month == 0) {
            return null;
        }
        return Month.of(month);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Month getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int month = cs.getInt(columnIndex);
        if (month == 0) {
            return null;
        }
        return Month.of(month);
    }
}

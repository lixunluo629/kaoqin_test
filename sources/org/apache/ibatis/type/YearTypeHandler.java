package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import org.apache.ibatis.lang.UsesJava8;

@UsesJava8
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/YearTypeHandler.class */
public class YearTypeHandler extends BaseTypeHandler<Year> {
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public void setNonNullParameter(PreparedStatement ps, int i, Year year, JdbcType type) throws SQLException {
        ps.setInt(i, year.getValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Year getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int year = rs.getInt(columnName);
        if (year == 0) {
            return null;
        }
        return Year.of(year);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Year getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int year = rs.getInt(columnIndex);
        if (year == 0) {
            return null;
        }
        return Year.of(year);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.ibatis.type.BaseTypeHandler
    public Year getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int year = cs.getInt(columnIndex);
        if (year == 0) {
            return null;
        }
        return Year.of(year);
    }
}

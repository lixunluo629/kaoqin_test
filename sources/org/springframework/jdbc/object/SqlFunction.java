package org.springframework.jdbc.object;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/SqlFunction.class */
public class SqlFunction<T> extends MappingSqlQuery<T> {
    private final SingleColumnRowMapper<T> rowMapper = new SingleColumnRowMapper<>();

    public SqlFunction() {
        setRowsExpected(1);
    }

    public SqlFunction(DataSource ds, String sql) {
        setRowsExpected(1);
        setDataSource(ds);
        setSql(sql);
    }

    public SqlFunction(DataSource ds, String sql, int[] types) {
        setRowsExpected(1);
        setDataSource(ds);
        setSql(sql);
        setTypes(types);
    }

    public SqlFunction(DataSource ds, String sql, int[] types, Class<T> resultType) {
        setRowsExpected(1);
        setDataSource(ds);
        setSql(sql);
        setTypes(types);
        setResultType(resultType);
    }

    public void setResultType(Class<T> resultType) {
        this.rowMapper.setRequiredType(resultType);
    }

    @Override // org.springframework.jdbc.object.MappingSqlQuery
    protected T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return this.rowMapper.mapRow(rs, rowNum);
    }

    public int run() {
        return run(new Object[0]);
    }

    public int run(int parameter) {
        return run(Integer.valueOf(parameter));
    }

    public int run(Object... parameters) {
        Object obj = super.findObject(parameters);
        if (!(obj instanceof Number)) {
            throw new TypeMismatchDataAccessException("Couldn't convert result object [" + obj + "] to int");
        }
        return ((Number) obj).intValue();
    }

    public Object runGeneric() {
        return findObject((Object[]) null);
    }

    public Object runGeneric(int parameter) {
        return findObject(parameter);
    }

    public Object runGeneric(Object[] parameters) {
        return findObject(parameters);
    }
}

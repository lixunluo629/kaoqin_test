package org.springframework.jdbc.object;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/MappingSqlQuery.class */
public abstract class MappingSqlQuery<T> extends MappingSqlQueryWithParameters<T> {
    protected abstract T mapRow(ResultSet resultSet, int i) throws SQLException;

    public MappingSqlQuery() {
    }

    public MappingSqlQuery(DataSource ds, String sql) {
        super(ds, sql);
    }

    @Override // org.springframework.jdbc.object.MappingSqlQueryWithParameters
    protected final T mapRow(ResultSet rs, int rowNum, Object[] parameters, Map<?, ?> context) throws SQLException {
        return mapRow(rs, rowNum);
    }
}

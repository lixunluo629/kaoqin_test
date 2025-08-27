package org.springframework.jdbc.object;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/MappingSqlQueryWithParameters.class */
public abstract class MappingSqlQueryWithParameters<T> extends SqlQuery<T> {
    protected abstract T mapRow(ResultSet resultSet, int i, Object[] objArr, Map<?, ?> map) throws SQLException;

    public MappingSqlQueryWithParameters() {
    }

    public MappingSqlQueryWithParameters(DataSource ds, String sql) {
        super(ds, sql);
    }

    @Override // org.springframework.jdbc.object.SqlQuery
    protected RowMapper<T> newRowMapper(Object[] parameters, Map<?, ?> context) {
        return new RowMapperImpl(parameters, context);
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/MappingSqlQueryWithParameters$RowMapperImpl.class */
    protected class RowMapperImpl implements RowMapper<T> {
        private final Object[] params;
        private final Map<?, ?> context;

        public RowMapperImpl(Object[] parameters, Map<?, ?> context) {
            this.params = parameters;
            this.context = context;
        }

        @Override // org.springframework.jdbc.core.RowMapper
        public T mapRow(ResultSet resultSet, int i) throws SQLException {
            return (T) MappingSqlQueryWithParameters.this.mapRow(resultSet, i, this.params, this.context);
        }
    }
}

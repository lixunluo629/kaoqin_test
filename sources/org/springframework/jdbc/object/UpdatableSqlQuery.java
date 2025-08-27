package org.springframework.jdbc.object;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/UpdatableSqlQuery.class */
public abstract class UpdatableSqlQuery<T> extends SqlQuery<T> {
    protected abstract T updateRow(ResultSet resultSet, int i, Map<?, ?> map) throws SQLException;

    public UpdatableSqlQuery() {
        setUpdatableResults(true);
    }

    public UpdatableSqlQuery(DataSource ds, String sql) {
        super(ds, sql);
        setUpdatableResults(true);
    }

    @Override // org.springframework.jdbc.object.SqlQuery
    protected RowMapper<T> newRowMapper(Object[] parameters, Map<?, ?> context) {
        return new RowMapperImpl(context);
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/UpdatableSqlQuery$RowMapperImpl.class */
    protected class RowMapperImpl implements RowMapper<T> {
        private final Map<?, ?> context;

        public RowMapperImpl(Map<?, ?> context) {
            this.context = context;
        }

        @Override // org.springframework.jdbc.core.RowMapper
        public T mapRow(ResultSet resultSet, int i) throws SQLException {
            T t = (T) UpdatableSqlQuery.this.updateRow(resultSet, i, this.context);
            resultSet.updateRow();
            return t;
        }
    }
}

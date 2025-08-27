package org.springframework.jdbc.object;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/SqlQuery.class */
public abstract class SqlQuery<T> extends SqlOperation {
    private int rowsExpected = 0;

    protected abstract RowMapper<T> newRowMapper(Object[] objArr, Map<?, ?> map);

    public SqlQuery() {
    }

    public SqlQuery(DataSource ds, String sql) {
        setDataSource(ds);
        setSql(sql);
    }

    public void setRowsExpected(int rowsExpected) {
        this.rowsExpected = rowsExpected;
    }

    public int getRowsExpected() {
        return this.rowsExpected;
    }

    public List<T> execute(Object[] params, Map<?, ?> context) throws DataAccessException {
        validateParameters(params);
        RowMapper<T> rowMapper = newRowMapper(params, context);
        return getJdbcTemplate().query(newPreparedStatementCreator(params), rowMapper);
    }

    public List<T> execute(Object... params) throws DataAccessException {
        return execute(params, (Map<?, ?>) null);
    }

    public List<T> execute(Map<?, ?> context) throws DataAccessException {
        return execute((Object[]) null, context);
    }

    public List<T> execute() throws DataAccessException {
        return execute((Object[]) null);
    }

    public List<T> execute(int p1, Map<?, ?> context) throws DataAccessException {
        return execute(new Object[]{Integer.valueOf(p1)}, context);
    }

    public List<T> execute(int p1) throws DataAccessException {
        return execute(p1, (Map<?, ?>) null);
    }

    public List<T> execute(int p1, int p2, Map<?, ?> context) throws DataAccessException {
        return execute(new Object[]{Integer.valueOf(p1), Integer.valueOf(p2)}, context);
    }

    public List<T> execute(int p1, int p2) throws DataAccessException {
        return execute(p1, p2, null);
    }

    public List<T> execute(long p1, Map<?, ?> context) throws DataAccessException {
        return execute(new Object[]{Long.valueOf(p1)}, context);
    }

    public List<T> execute(long p1) throws DataAccessException {
        return execute(p1, (Map<?, ?>) null);
    }

    public List<T> execute(String p1, Map<?, ?> context) throws DataAccessException {
        return execute(new Object[]{p1}, context);
    }

    public List<T> execute(String p1) throws DataAccessException {
        return execute(p1, (Map<?, ?>) null);
    }

    public List<T> executeByNamedParam(Map<String, ?> paramMap, Map<?, ?> context) throws DataAccessException {
        validateNamedParameters(paramMap);
        ParsedSql parsedSql = getParsedSql();
        MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, getDeclaredParameters());
        RowMapper<T> rowMapper = newRowMapper(params, context);
        return getJdbcTemplate().query(newPreparedStatementCreator(sqlToUse, params), rowMapper);
    }

    public List<T> executeByNamedParam(Map<String, ?> paramMap) throws DataAccessException {
        return executeByNamedParam(paramMap, null);
    }

    public T findObject(Object[] objArr, Map<?, ?> map) throws DataAccessException {
        return (T) DataAccessUtils.singleResult(execute(objArr, map));
    }

    public T findObject(Object... params) throws DataAccessException {
        return findObject(params, (Map<?, ?>) null);
    }

    public T findObject(int p1, Map<?, ?> context) throws DataAccessException {
        return findObject(new Object[]{Integer.valueOf(p1)}, context);
    }

    public T findObject(int p1) throws DataAccessException {
        return findObject(p1, (Map<?, ?>) null);
    }

    public T findObject(int p1, int p2, Map<?, ?> context) throws DataAccessException {
        return findObject(new Object[]{Integer.valueOf(p1), Integer.valueOf(p2)}, context);
    }

    public T findObject(int p1, int p2) throws DataAccessException {
        return findObject(p1, p2, null);
    }

    public T findObject(long p1, Map<?, ?> context) throws DataAccessException {
        return findObject(new Object[]{Long.valueOf(p1)}, context);
    }

    public T findObject(long p1) throws DataAccessException {
        return findObject(p1, (Map<?, ?>) null);
    }

    public T findObject(String p1, Map<?, ?> context) throws DataAccessException {
        return findObject(new Object[]{p1}, context);
    }

    public T findObject(String p1) throws DataAccessException {
        return findObject(p1, (Map<?, ?>) null);
    }

    public T findObjectByNamedParam(Map<String, ?> map, Map<?, ?> map2) throws DataAccessException {
        return (T) DataAccessUtils.singleResult(executeByNamedParam(map, map2));
    }

    public T findObjectByNamedParam(Map<String, ?> paramMap) throws DataAccessException {
        return findObjectByNamedParam(paramMap, null);
    }
}

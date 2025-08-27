package org.springframework.jdbc.object;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.support.KeyHolder;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/SqlUpdate.class */
public class SqlUpdate extends SqlOperation {
    private int maxRowsAffected;
    private int requiredRowsAffected;

    public SqlUpdate() {
        this.maxRowsAffected = 0;
        this.requiredRowsAffected = 0;
    }

    public SqlUpdate(DataSource ds, String sql) {
        this.maxRowsAffected = 0;
        this.requiredRowsAffected = 0;
        setDataSource(ds);
        setSql(sql);
    }

    public SqlUpdate(DataSource ds, String sql, int[] types) {
        this.maxRowsAffected = 0;
        this.requiredRowsAffected = 0;
        setDataSource(ds);
        setSql(sql);
        setTypes(types);
    }

    public SqlUpdate(DataSource ds, String sql, int[] types, int maxRowsAffected) {
        this.maxRowsAffected = 0;
        this.requiredRowsAffected = 0;
        setDataSource(ds);
        setSql(sql);
        setTypes(types);
        this.maxRowsAffected = maxRowsAffected;
    }

    public void setMaxRowsAffected(int maxRowsAffected) {
        this.maxRowsAffected = maxRowsAffected;
    }

    public void setRequiredRowsAffected(int requiredRowsAffected) {
        this.requiredRowsAffected = requiredRowsAffected;
    }

    protected void checkRowsAffected(int rowsAffected) throws JdbcUpdateAffectedIncorrectNumberOfRowsException {
        if (this.maxRowsAffected > 0 && rowsAffected > this.maxRowsAffected) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(getSql(), this.maxRowsAffected, rowsAffected);
        }
        if (this.requiredRowsAffected > 0 && rowsAffected != this.requiredRowsAffected) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(getSql(), this.requiredRowsAffected, rowsAffected);
        }
    }

    public int update(Object... params) throws DataAccessException {
        validateParameters(params);
        int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(params));
        checkRowsAffected(rowsAffected);
        return rowsAffected;
    }

    public int update(Object[] params, KeyHolder generatedKeyHolder) throws DataAccessException {
        if (!isReturnGeneratedKeys() && getGeneratedKeysColumnNames() == null) {
            throw new InvalidDataAccessApiUsageException("The update method taking a KeyHolder should only be used when generated keys have been configured by calling either 'setReturnGeneratedKeys' or 'setGeneratedKeysColumnNames'.");
        }
        validateParameters(params);
        int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(params), generatedKeyHolder);
        checkRowsAffected(rowsAffected);
        return rowsAffected;
    }

    public int update() throws DataAccessException {
        return update((Object[]) null);
    }

    public int update(int p1) throws DataAccessException {
        return update(Integer.valueOf(p1));
    }

    public int update(int p1, int p2) throws DataAccessException {
        return update(Integer.valueOf(p1), Integer.valueOf(p2));
    }

    public int update(long p1) throws DataAccessException {
        return update(Long.valueOf(p1));
    }

    public int update(long p1, long p2) throws DataAccessException {
        return update(Long.valueOf(p1), Long.valueOf(p2));
    }

    public int update(String p) throws DataAccessException {
        return update(p);
    }

    public int update(String p1, String p2) throws DataAccessException {
        return update(p1, p2);
    }

    public int updateByNamedParam(Map<String, ?> paramMap) throws DataAccessException {
        validateNamedParameters(paramMap);
        ParsedSql parsedSql = getParsedSql();
        MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, getDeclaredParameters());
        int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(sqlToUse, params));
        checkRowsAffected(rowsAffected);
        return rowsAffected;
    }

    public int updateByNamedParam(Map<String, ?> paramMap, KeyHolder generatedKeyHolder) throws DataAccessException {
        validateNamedParameters(paramMap);
        ParsedSql parsedSql = getParsedSql();
        MapSqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, getDeclaredParameters());
        int rowsAffected = getJdbcTemplate().update(newPreparedStatementCreator(sqlToUse, params), generatedKeyHolder);
        checkRowsAffected(rowsAffected);
        return rowsAffected;
    }
}

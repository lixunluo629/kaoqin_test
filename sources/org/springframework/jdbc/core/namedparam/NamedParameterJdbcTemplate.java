package org.springframework.jdbc.core.namedparam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/NamedParameterJdbcTemplate.class */
public class NamedParameterJdbcTemplate implements NamedParameterJdbcOperations {
    public static final int DEFAULT_CACHE_LIMIT = 256;
    private final JdbcOperations classicJdbcTemplate;
    private volatile int cacheLimit = 256;
    private final Map<String, ParsedSql> parsedSqlCache = new LinkedHashMap<String, ParsedSql>(256, 0.75f, true) { // from class: org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.1
        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<String, ParsedSql> eldest) {
            return size() > NamedParameterJdbcTemplate.this.getCacheLimit();
        }
    };

    public NamedParameterJdbcTemplate(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");
        this.classicJdbcTemplate = new JdbcTemplate(dataSource);
    }

    public NamedParameterJdbcTemplate(JdbcOperations classicJdbcTemplate) {
        Assert.notNull(classicJdbcTemplate, "JdbcTemplate must not be null");
        this.classicJdbcTemplate = classicJdbcTemplate;
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public JdbcOperations getJdbcOperations() {
        return this.classicJdbcTemplate;
    }

    public void setCacheLimit(int cacheLimit) {
        this.cacheLimit = cacheLimit;
    }

    public int getCacheLimit() {
        return this.cacheLimit;
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T execute(String str, SqlParameterSource sqlParameterSource, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException {
        return (T) getJdbcOperations().execute(getPreparedStatementCreator(str, sqlParameterSource), preparedStatementCallback);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T execute(String str, Map<String, ?> map, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException {
        return (T) execute(str, new MapSqlParameterSource(map), preparedStatementCallback);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T execute(String str, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException {
        return (T) execute(str, EmptySqlParameterSource.INSTANCE, preparedStatementCallback);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T query(String str, SqlParameterSource sqlParameterSource, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        return (T) getJdbcOperations().query(getPreparedStatementCreator(str, sqlParameterSource), resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T query(String str, Map<String, ?> map, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        return (T) query(str, new MapSqlParameterSource(map), resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T query(String str, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        return (T) query(str, EmptySqlParameterSource.INSTANCE, resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public void query(String sql, SqlParameterSource paramSource, RowCallbackHandler rch) throws DataAccessException {
        getJdbcOperations().query(getPreparedStatementCreator(sql, paramSource), rch);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch) throws DataAccessException {
        query(sql, new MapSqlParameterSource(paramMap), rch);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        query(sql, EmptySqlParameterSource.INSTANCE, rch);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
        return getJdbcOperations().query(getPreparedStatementCreator(sql, paramSource), rowMapper);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
        return query(sql, new MapSqlParameterSource(paramMap), rowMapper);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return query(sql, EmptySqlParameterSource.INSTANCE, rowMapper);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T queryForObject(String str, SqlParameterSource sqlParameterSource, RowMapper<T> rowMapper) throws DataAccessException {
        return (T) DataAccessUtils.requiredSingleResult(getJdbcOperations().query(getPreparedStatementCreator(str, sqlParameterSource), rowMapper));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T queryForObject(String str, Map<String, ?> map, RowMapper<T> rowMapper) throws DataAccessException {
        return (T) queryForObject(str, new MapSqlParameterSource(map), rowMapper);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T queryForObject(String str, SqlParameterSource sqlParameterSource, Class<T> cls) throws DataAccessException {
        return (T) queryForObject(str, sqlParameterSource, new SingleColumnRowMapper(cls));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> T queryForObject(String str, Map<String, ?> map, Class<T> cls) throws DataAccessException {
        return (T) queryForObject(str, map, new SingleColumnRowMapper(cls));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return (Map) queryForObject(sql, paramSource, new ColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return (Map) queryForObject(sql, paramMap, new ColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> List<T> queryForList(String sql, SqlParameterSource paramSource, Class<T> elementType) throws DataAccessException {
        return query(sql, paramSource, new SingleColumnRowMapper(elementType));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType) throws DataAccessException {
        return queryForList(sql, new MapSqlParameterSource(paramMap), elementType);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public List<Map<String, Object>> queryForList(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return query(sql, paramSource, new ColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return queryForList(sql, new MapSqlParameterSource(paramMap));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return (SqlRowSet) getJdbcOperations().query(getPreparedStatementCreator(sql, paramSource), new SqlRowSetResultSetExtractor());
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return queryForRowSet(sql, new MapSqlParameterSource(paramMap));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public int update(String sql, SqlParameterSource paramSource) throws DataAccessException {
        return getJdbcOperations().update(getPreparedStatementCreator(sql, paramSource));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
        return update(sql, new MapSqlParameterSource(paramMap));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder) throws DataAccessException {
        return update(sql, paramSource, generatedKeyHolder, null);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder, String[] keyColumnNames) throws DataAccessException {
        ParsedSql parsedSql = getParsedSql(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
        if (keyColumnNames != null) {
            pscf.setGeneratedKeysColumnNames(keyColumnNames);
        } else {
            pscf.setReturnGeneratedKeys(true);
        }
        return getJdbcOperations().update(pscf.newPreparedStatementCreator(params), generatedKeyHolder);
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
        return batchUpdate(sql, SqlParameterSourceUtils.createBatch(batchValues));
    }

    @Override // org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
    public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
        return NamedParameterBatchUpdateUtils.executeBatchUpdateWithNamedParameters(getParsedSql(sql), batchArgs, getJdbcOperations());
    }

    protected PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource) {
        ParsedSql parsedSql = getParsedSql(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
        return pscf.newPreparedStatementCreator(params);
    }

    protected ParsedSql getParsedSql(String sql) {
        ParsedSql parsedSql;
        if (getCacheLimit() <= 0) {
            return NamedParameterUtils.parseSqlStatement(sql);
        }
        synchronized (this.parsedSqlCache) {
            ParsedSql parsedSql2 = this.parsedSqlCache.get(sql);
            if (parsedSql2 == null) {
                parsedSql2 = NamedParameterUtils.parseSqlStatement(sql);
                this.parsedSqlCache.put(sql, parsedSql2);
            }
            parsedSql = parsedSql2;
        }
        return parsedSql;
    }
}

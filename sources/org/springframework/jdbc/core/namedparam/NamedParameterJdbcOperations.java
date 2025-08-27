package org.springframework.jdbc.core.namedparam;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/namedparam/NamedParameterJdbcOperations.class */
public interface NamedParameterJdbcOperations {
    JdbcOperations getJdbcOperations();

    <T> T execute(String str, SqlParameterSource sqlParameterSource, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException;

    <T> T execute(String str, Map<String, ?> map, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException;

    <T> T execute(String str, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException;

    <T> T query(String str, SqlParameterSource sqlParameterSource, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    <T> T query(String str, Map<String, ?> map, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    <T> T query(String str, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    void query(String str, SqlParameterSource sqlParameterSource, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    void query(String str, Map<String, ?> map, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    void query(String str, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    <T> List<T> query(String str, SqlParameterSource sqlParameterSource, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String str, Map<String, ?> map, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String str, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String str, SqlParameterSource sqlParameterSource, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String str, Map<String, ?> map, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String str, SqlParameterSource sqlParameterSource, Class<T> cls) throws DataAccessException;

    <T> T queryForObject(String str, Map<String, ?> map, Class<T> cls) throws DataAccessException;

    Map<String, Object> queryForMap(String str, SqlParameterSource sqlParameterSource) throws DataAccessException;

    Map<String, Object> queryForMap(String str, Map<String, ?> map) throws DataAccessException;

    <T> List<T> queryForList(String str, SqlParameterSource sqlParameterSource, Class<T> cls) throws DataAccessException;

    <T> List<T> queryForList(String str, Map<String, ?> map, Class<T> cls) throws DataAccessException;

    List<Map<String, Object>> queryForList(String str, SqlParameterSource sqlParameterSource) throws DataAccessException;

    List<Map<String, Object>> queryForList(String str, Map<String, ?> map) throws DataAccessException;

    SqlRowSet queryForRowSet(String str, SqlParameterSource sqlParameterSource) throws DataAccessException;

    SqlRowSet queryForRowSet(String str, Map<String, ?> map) throws DataAccessException;

    int update(String str, SqlParameterSource sqlParameterSource) throws DataAccessException;

    int update(String str, Map<String, ?> map) throws DataAccessException;

    int update(String str, SqlParameterSource sqlParameterSource, KeyHolder keyHolder) throws DataAccessException;

    int update(String str, SqlParameterSource sqlParameterSource, KeyHolder keyHolder, String[] strArr) throws DataAccessException;

    int[] batchUpdate(String str, Map<String, ?>[] mapArr);

    int[] batchUpdate(String str, SqlParameterSource[] sqlParameterSourceArr);
}

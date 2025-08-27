package org.springframework.jdbc.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcOperations.class */
public interface JdbcOperations {
    <T> T execute(ConnectionCallback<T> connectionCallback) throws DataAccessException;

    <T> T execute(StatementCallback<T> statementCallback) throws DataAccessException;

    void execute(String str) throws DataAccessException;

    <T> T query(String str, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    void query(String str, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    <T> List<T> query(String str, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String str, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String str, Class<T> cls) throws DataAccessException;

    Map<String, Object> queryForMap(String str) throws DataAccessException;

    <T> List<T> queryForList(String str, Class<T> cls) throws DataAccessException;

    List<Map<String, Object>> queryForList(String str) throws DataAccessException;

    SqlRowSet queryForRowSet(String str) throws DataAccessException;

    int update(String str) throws DataAccessException;

    int[] batchUpdate(String... strArr) throws DataAccessException;

    <T> T execute(PreparedStatementCreator preparedStatementCreator, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException;

    <T> T execute(String str, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException;

    <T> T query(PreparedStatementCreator preparedStatementCreator, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    <T> T query(String str, PreparedStatementSetter preparedStatementSetter, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    <T> T query(String str, Object[] objArr, int[] iArr, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    <T> T query(String str, Object[] objArr, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException;

    <T> T query(String str, ResultSetExtractor<T> resultSetExtractor, Object... objArr) throws DataAccessException;

    void query(PreparedStatementCreator preparedStatementCreator, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    void query(String str, PreparedStatementSetter preparedStatementSetter, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    void query(String str, Object[] objArr, int[] iArr, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    void query(String str, Object[] objArr, RowCallbackHandler rowCallbackHandler) throws DataAccessException;

    void query(String str, RowCallbackHandler rowCallbackHandler, Object... objArr) throws DataAccessException;

    <T> List<T> query(PreparedStatementCreator preparedStatementCreator, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String str, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String str, Object[] objArr, int[] iArr, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String str, Object[] objArr, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String str, RowMapper<T> rowMapper, Object... objArr) throws DataAccessException;

    <T> T queryForObject(String str, Object[] objArr, int[] iArr, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String str, Object[] objArr, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String str, RowMapper<T> rowMapper, Object... objArr) throws DataAccessException;

    <T> T queryForObject(String str, Object[] objArr, int[] iArr, Class<T> cls) throws DataAccessException;

    <T> T queryForObject(String str, Object[] objArr, Class<T> cls) throws DataAccessException;

    <T> T queryForObject(String str, Class<T> cls, Object... objArr) throws DataAccessException;

    Map<String, Object> queryForMap(String str, Object[] objArr, int[] iArr) throws DataAccessException;

    Map<String, Object> queryForMap(String str, Object... objArr) throws DataAccessException;

    <T> List<T> queryForList(String str, Object[] objArr, int[] iArr, Class<T> cls) throws DataAccessException;

    <T> List<T> queryForList(String str, Object[] objArr, Class<T> cls) throws DataAccessException;

    <T> List<T> queryForList(String str, Class<T> cls, Object... objArr) throws DataAccessException;

    List<Map<String, Object>> queryForList(String str, Object[] objArr, int[] iArr) throws DataAccessException;

    List<Map<String, Object>> queryForList(String str, Object... objArr) throws DataAccessException;

    SqlRowSet queryForRowSet(String str, Object[] objArr, int[] iArr) throws DataAccessException;

    SqlRowSet queryForRowSet(String str, Object... objArr) throws DataAccessException;

    int update(PreparedStatementCreator preparedStatementCreator) throws DataAccessException;

    int update(PreparedStatementCreator preparedStatementCreator, KeyHolder keyHolder) throws DataAccessException;

    int update(String str, PreparedStatementSetter preparedStatementSetter) throws DataAccessException;

    int update(String str, Object[] objArr, int[] iArr) throws DataAccessException;

    int update(String str, Object... objArr) throws DataAccessException;

    int[] batchUpdate(String str, BatchPreparedStatementSetter batchPreparedStatementSetter) throws DataAccessException;

    int[] batchUpdate(String str, List<Object[]> list) throws DataAccessException;

    int[] batchUpdate(String str, List<Object[]> list, int[] iArr) throws DataAccessException;

    <T> int[][] batchUpdate(String str, Collection<T> collection, int i, ParameterizedPreparedStatementSetter<T> parameterizedPreparedStatementSetter) throws DataAccessException;

    <T> T execute(CallableStatementCreator callableStatementCreator, CallableStatementCallback<T> callableStatementCallback) throws DataAccessException;

    <T> T execute(String str, CallableStatementCallback<T> callableStatementCallback) throws DataAccessException;

    Map<String, Object> call(CallableStatementCreator callableStatementCreator, List<SqlParameter> list) throws DataAccessException;
}

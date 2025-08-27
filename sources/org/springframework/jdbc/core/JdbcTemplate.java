package org.springframework.jdbc.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.datasource.ConnectionProxy;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate.class */
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {
    private static final String RETURN_RESULT_SET_PREFIX = "#result-set-";
    private static final String RETURN_UPDATE_COUNT_PREFIX = "#update-count-";
    private NativeJdbcExtractor nativeJdbcExtractor;
    private boolean ignoreWarnings = true;
    private int fetchSize = -1;
    private int maxRows = -1;
    private int queryTimeout = -1;
    private boolean skipResultsProcessing = false;
    private boolean skipUndeclaredResults = false;
    private boolean resultsMapCaseInsensitive = false;

    public JdbcTemplate() {
    }

    public JdbcTemplate(DataSource dataSource) {
        setDataSource(dataSource);
        afterPropertiesSet();
    }

    public JdbcTemplate(DataSource dataSource, boolean lazyInit) {
        setDataSource(dataSource);
        setLazyInit(lazyInit);
        afterPropertiesSet();
    }

    public void setNativeJdbcExtractor(NativeJdbcExtractor extractor) {
        this.nativeJdbcExtractor = extractor;
    }

    public NativeJdbcExtractor getNativeJdbcExtractor() {
        return this.nativeJdbcExtractor;
    }

    public void setIgnoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }

    public boolean isIgnoreWarnings() {
        return this.ignoreWarnings;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getFetchSize() {
        return this.fetchSize;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getMaxRows() {
        return this.maxRows;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public int getQueryTimeout() {
        return this.queryTimeout;
    }

    public void setSkipResultsProcessing(boolean skipResultsProcessing) {
        this.skipResultsProcessing = skipResultsProcessing;
    }

    public boolean isSkipResultsProcessing() {
        return this.skipResultsProcessing;
    }

    public void setSkipUndeclaredResults(boolean skipUndeclaredResults) {
        this.skipUndeclaredResults = skipUndeclaredResults;
    }

    public boolean isSkipUndeclaredResults() {
        return this.skipUndeclaredResults;
    }

    public void setResultsMapCaseInsensitive(boolean resultsMapCaseInsensitive) {
        this.resultsMapCaseInsensitive = resultsMapCaseInsensitive;
    }

    public boolean isResultsMapCaseInsensitive() {
        return this.resultsMapCaseInsensitive;
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
        Connection conToUse;
        Assert.notNull(action, "Callback object must not be null");
        Connection con = DataSourceUtils.getConnection(getDataSource());
        try {
            try {
                if (this.nativeJdbcExtractor != null) {
                    conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
                } else {
                    conToUse = createConnectionProxy(con);
                }
                T tDoInConnection = action.doInConnection(conToUse);
                DataSourceUtils.releaseConnection(con, getDataSource());
                return tDoInConnection;
            } catch (SQLException ex) {
                DataSourceUtils.releaseConnection(con, getDataSource());
                con = null;
                throw getExceptionTranslator().translate("ConnectionCallback", getSql(action), ex);
            }
        } catch (Throwable th) {
            DataSourceUtils.releaseConnection(con, getDataSource());
            throw th;
        }
    }

    protected Connection createConnectionProxy(Connection con) {
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[]{ConnectionProxy.class}, new CloseSuppressingInvocationHandler(con));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T execute(StatementCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object must not be null");
        Connection con = DataSourceUtils.getConnection(getDataSource());
        Statement stmt = null;
        try {
            try {
                Connection conToUse = con;
                if (this.nativeJdbcExtractor != null && this.nativeJdbcExtractor.isNativeConnectionNecessaryForNativeStatements()) {
                    conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
                }
                stmt = conToUse.createStatement();
                applyStatementSettings(stmt);
                Statement stmtToUse = stmt;
                if (this.nativeJdbcExtractor != null) {
                    stmtToUse = this.nativeJdbcExtractor.getNativeStatement(stmt);
                }
                T result = action.doInStatement(stmtToUse);
                handleWarnings(stmt);
                JdbcUtils.closeStatement(stmt);
                DataSourceUtils.releaseConnection(con, getDataSource());
                return result;
            } catch (SQLException ex) {
                JdbcUtils.closeStatement(stmt);
                DataSourceUtils.releaseConnection(con, getDataSource());
                throw getExceptionTranslator().translate("StatementCallback", getSql(action), ex);
            }
        } catch (Throwable th) {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(con, getDataSource());
            throw th;
        }
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public void execute(String sql) throws DataAccessException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Executing SQL statement [" + sql + "]");
        }
        execute(new C1ExecuteStatementCallback(sql));
    }

    /* renamed from: org.springframework.jdbc.core.JdbcTemplate$1ExecuteStatementCallback, reason: invalid class name */
    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$1ExecuteStatementCallback.class */
    class C1ExecuteStatementCallback implements StatementCallback<Object>, SqlProvider {
        final /* synthetic */ String val$sql;

        C1ExecuteStatementCallback(String str) {
            this.val$sql = str;
        }

        @Override // org.springframework.jdbc.core.StatementCallback
        public Object doInStatement(Statement stmt) throws SQLException {
            stmt.execute(this.val$sql);
            return null;
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return this.val$sql;
        }
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T query(String str, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        Assert.notNull(str, "SQL must not be null");
        Assert.notNull(resultSetExtractor, "ResultSetExtractor must not be null");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Executing SQL query [" + str + "]");
        }
        return (T) execute(new C1QueryStatementCallback(str, resultSetExtractor));
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: org.springframework.jdbc.core.JdbcTemplate$1QueryStatementCallback, reason: invalid class name */
    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$1QueryStatementCallback.class */
    class C1QueryStatementCallback<T> implements StatementCallback<T>, SqlProvider {
        final /* synthetic */ String val$sql;
        final /* synthetic */ ResultSetExtractor val$rse;

        C1QueryStatementCallback(String str, ResultSetExtractor resultSetExtractor) {
            this.val$sql = str;
            this.val$rse = resultSetExtractor;
        }

        @Override // org.springframework.jdbc.core.StatementCallback
        public T doInStatement(Statement statement) throws SQLException {
            ResultSet resultSetExecuteQuery = null;
            try {
                resultSetExecuteQuery = statement.executeQuery(this.val$sql);
                ResultSet nativeResultSet = resultSetExecuteQuery;
                if (JdbcTemplate.this.nativeJdbcExtractor != null) {
                    nativeResultSet = JdbcTemplate.this.nativeJdbcExtractor.getNativeResultSet(resultSetExecuteQuery);
                }
                T t = (T) this.val$rse.extractData(nativeResultSet);
                JdbcUtils.closeResultSet(resultSetExecuteQuery);
                return t;
            } catch (Throwable th) {
                JdbcUtils.closeResultSet(resultSetExecuteQuery);
                throw th;
            }
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return this.val$sql;
        }
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        query(sql, new RowCallbackHandlerResultSetExtractor(rch));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return (List) query(sql, new RowMapperResultSetExtractor(rowMapper));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public Map<String, Object> queryForMap(String sql) throws DataAccessException {
        return (Map) queryForObject(sql, getColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, RowMapper<T> rowMapper) throws DataAccessException {
        return (T) DataAccessUtils.requiredSingleResult(query(str, rowMapper));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, Class<T> cls) throws DataAccessException {
        return (T) queryForObject(str, getSingleColumnRowMapper(cls));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
        return query(sql, getSingleColumnRowMapper(elementType));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
        return query(sql, getColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
        return (SqlRowSet) query(sql, new SqlRowSetResultSetExtractor());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int update(String sql) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Executing SQL update [" + sql + "]");
        }
        return ((Integer) execute(new C1UpdateStatementCallback(sql))).intValue();
    }

    /* renamed from: org.springframework.jdbc.core.JdbcTemplate$1UpdateStatementCallback, reason: invalid class name */
    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$1UpdateStatementCallback.class */
    class C1UpdateStatementCallback implements StatementCallback<Integer>, SqlProvider {
        final /* synthetic */ String val$sql;

        C1UpdateStatementCallback(String str) {
            this.val$sql = str;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.jdbc.core.StatementCallback
        public Integer doInStatement(Statement stmt) throws SQLException {
            int rows = stmt.executeUpdate(this.val$sql);
            if (JdbcTemplate.this.logger.isDebugEnabled()) {
                JdbcTemplate.this.logger.debug("SQL update affected " + rows + " rows");
            }
            return Integer.valueOf(rows);
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return this.val$sql;
        }
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int[] batchUpdate(String... sql) throws DataAccessException {
        Assert.notEmpty(sql, "SQL array must not be empty");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Executing SQL batch update of " + sql.length + " statements");
        }
        return (int[]) execute(new C1BatchUpdateStatementCallback(sql));
    }

    /* renamed from: org.springframework.jdbc.core.JdbcTemplate$1BatchUpdateStatementCallback, reason: invalid class name */
    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$1BatchUpdateStatementCallback.class */
    class C1BatchUpdateStatementCallback implements StatementCallback<int[]>, SqlProvider {
        private String currSql;
        final /* synthetic */ String[] val$sql;

        C1BatchUpdateStatementCallback(String[] strArr) {
            this.val$sql = strArr;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.jdbc.core.StatementCallback
        public int[] doInStatement(Statement stmt) throws SQLException, DataAccessException {
            int[] rowsAffected = new int[this.val$sql.length];
            if (JdbcUtils.supportsBatchUpdates(stmt.getConnection())) {
                for (String sqlStmt : this.val$sql) {
                    this.currSql = appendSql(this.currSql, sqlStmt);
                    stmt.addBatch(sqlStmt);
                }
                try {
                    rowsAffected = stmt.executeBatch();
                } catch (BatchUpdateException ex) {
                    String batchExceptionSql = null;
                    for (int i = 0; i < ex.getUpdateCounts().length; i++) {
                        if (ex.getUpdateCounts()[i] == -3) {
                            batchExceptionSql = appendSql(batchExceptionSql, this.val$sql[i]);
                        }
                    }
                    if (StringUtils.hasLength(batchExceptionSql)) {
                        this.currSql = batchExceptionSql;
                    }
                    throw ex;
                }
            } else {
                for (int i2 = 0; i2 < this.val$sql.length; i2++) {
                    this.currSql = this.val$sql[i2];
                    if (!stmt.execute(this.val$sql[i2])) {
                        rowsAffected[i2] = stmt.getUpdateCount();
                    } else {
                        throw new InvalidDataAccessApiUsageException("Invalid batch SQL statement: " + this.val$sql[i2]);
                    }
                }
            }
            return rowsAffected;
        }

        private String appendSql(String sql, String statement) {
            return StringUtils.isEmpty(sql) ? statement : sql + "; " + statement;
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return this.currSql;
        }
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
        Assert.notNull(psc, "PreparedStatementCreator must not be null");
        Assert.notNull(action, "Callback object must not be null");
        if (this.logger.isDebugEnabled()) {
            String sql = getSql(psc);
            this.logger.debug("Executing prepared SQL statement" + (sql != null ? " [" + sql + "]" : ""));
        }
        Connection con = DataSourceUtils.getConnection(getDataSource());
        PreparedStatement ps = null;
        try {
            try {
                Connection conToUse = con;
                if (this.nativeJdbcExtractor != null && this.nativeJdbcExtractor.isNativeConnectionNecessaryForNativePreparedStatements()) {
                    conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
                }
                ps = psc.createPreparedStatement(conToUse);
                applyStatementSettings(ps);
                PreparedStatement psToUse = ps;
                if (this.nativeJdbcExtractor != null) {
                    psToUse = this.nativeJdbcExtractor.getNativePreparedStatement(ps);
                }
                T result = action.doInPreparedStatement(psToUse);
                handleWarnings(ps);
                if (psc instanceof ParameterDisposer) {
                    ((ParameterDisposer) psc).cleanupParameters();
                }
                JdbcUtils.closeStatement(ps);
                DataSourceUtils.releaseConnection(con, getDataSource());
                return result;
            } catch (SQLException ex) {
                if (psc instanceof ParameterDisposer) {
                    ((ParameterDisposer) psc).cleanupParameters();
                }
                String sql2 = getSql(psc);
                psc = null;
                JdbcUtils.closeStatement(ps);
                ps = null;
                DataSourceUtils.releaseConnection(con, getDataSource());
                con = null;
                throw getExceptionTranslator().translate("PreparedStatementCallback", sql2, ex);
            }
        } catch (Throwable th) {
            if (psc instanceof ParameterDisposer) {
                ((ParameterDisposer) psc).cleanupParameters();
            }
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(con, getDataSource());
            throw th;
        }
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T execute(String str, PreparedStatementCallback<T> preparedStatementCallback) throws DataAccessException {
        return (T) execute(new SimplePreparedStatementCreator(str), preparedStatementCallback);
    }

    public <T> T query(PreparedStatementCreator preparedStatementCreator, final PreparedStatementSetter preparedStatementSetter, final ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        Assert.notNull(resultSetExtractor, "ResultSetExtractor must not be null");
        this.logger.debug("Executing prepared SQL query");
        return (T) execute(preparedStatementCreator, new PreparedStatementCallback<T>() { // from class: org.springframework.jdbc.core.JdbcTemplate.1
            @Override // org.springframework.jdbc.core.PreparedStatementCallback
            public T doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                ResultSet resultSetExecuteQuery = null;
                try {
                    if (preparedStatementSetter != null) {
                        preparedStatementSetter.setValues(preparedStatement);
                    }
                    resultSetExecuteQuery = preparedStatement.executeQuery();
                    ResultSet nativeResultSet = resultSetExecuteQuery;
                    if (JdbcTemplate.this.nativeJdbcExtractor != null) {
                        nativeResultSet = JdbcTemplate.this.nativeJdbcExtractor.getNativeResultSet(resultSetExecuteQuery);
                    }
                    T t = (T) resultSetExtractor.extractData(nativeResultSet);
                    JdbcUtils.closeResultSet(resultSetExecuteQuery);
                    if (preparedStatementSetter instanceof ParameterDisposer) {
                        ((ParameterDisposer) preparedStatementSetter).cleanupParameters();
                    }
                    return t;
                } catch (Throwable th) {
                    JdbcUtils.closeResultSet(resultSetExecuteQuery);
                    if (preparedStatementSetter instanceof ParameterDisposer) {
                        ((ParameterDisposer) preparedStatementSetter).cleanupParameters();
                    }
                    throw th;
                }
            }
        });
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T query(PreparedStatementCreator preparedStatementCreator, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        return (T) query(preparedStatementCreator, (PreparedStatementSetter) null, resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T query(String str, PreparedStatementSetter preparedStatementSetter, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        return (T) query(new SimplePreparedStatementCreator(str), preparedStatementSetter, resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T query(String str, Object[] objArr, int[] iArr, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        return (T) query(str, newArgTypePreparedStatementSetter(objArr, iArr), resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T query(String str, Object[] objArr, ResultSetExtractor<T> resultSetExtractor) throws DataAccessException {
        return (T) query(str, newArgPreparedStatementSetter(objArr), resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T query(String str, ResultSetExtractor<T> resultSetExtractor, Object... objArr) throws DataAccessException {
        return (T) query(str, newArgPreparedStatementSetter(objArr), resultSetExtractor);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
        query(psc, new RowCallbackHandlerResultSetExtractor(rch));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
        query(sql, pss, new RowCallbackHandlerResultSetExtractor(rch));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
        query(sql, newArgTypePreparedStatementSetter(args, argTypes), rch);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
        query(sql, newArgPreparedStatementSetter(args), rch);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
        query(sql, newArgPreparedStatementSetter(args), rch);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
        return (List) query(psc, new RowMapperResultSetExtractor(rowMapper));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
        return (List) query(sql, pss, new RowMapperResultSetExtractor(rowMapper));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
        return (List) query(sql, args, argTypes, new RowMapperResultSetExtractor(rowMapper));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        return (List) query(sql, args, new RowMapperResultSetExtractor(rowMapper));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        return (List) query(sql, args, new RowMapperResultSetExtractor(rowMapper));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, Object[] objArr, int[] iArr, RowMapper<T> rowMapper) throws DataAccessException {
        return (T) DataAccessUtils.requiredSingleResult((List) query(str, objArr, iArr, new RowMapperResultSetExtractor(rowMapper, 1)));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, Object[] objArr, RowMapper<T> rowMapper) throws DataAccessException {
        return (T) DataAccessUtils.requiredSingleResult((List) query(str, objArr, new RowMapperResultSetExtractor(rowMapper, 1)));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, RowMapper<T> rowMapper, Object... objArr) throws DataAccessException {
        return (T) DataAccessUtils.requiredSingleResult((List) query(str, objArr, new RowMapperResultSetExtractor(rowMapper, 1)));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, Object[] objArr, int[] iArr, Class<T> cls) throws DataAccessException {
        return (T) queryForObject(str, objArr, iArr, getSingleColumnRowMapper(cls));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, Object[] objArr, Class<T> cls) throws DataAccessException {
        return (T) queryForObject(str, objArr, getSingleColumnRowMapper(cls));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T queryForObject(String str, Class<T> cls, Object... objArr) throws DataAccessException {
        return (T) queryForObject(str, objArr, getSingleColumnRowMapper(cls));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return (Map) queryForObject(sql, args, argTypes, getColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
        return (Map) queryForObject(sql, args, getColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) throws DataAccessException {
        return query(sql, args, argTypes, getSingleColumnRowMapper(elementType));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
        return query(sql, args, getSingleColumnRowMapper(elementType));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
        return query(sql, args, getSingleColumnRowMapper(elementType));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return query(sql, args, argTypes, getColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
        return query(sql, args, getColumnMapRowMapper());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return (SqlRowSet) query(sql, args, argTypes, new SqlRowSetResultSetExtractor());
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
        return (SqlRowSet) query(sql, args, new SqlRowSetResultSetExtractor());
    }

    protected int update(PreparedStatementCreator psc, final PreparedStatementSetter pss) throws DataAccessException {
        this.logger.debug("Executing prepared SQL update");
        return ((Integer) execute(psc, new PreparedStatementCallback<Integer>() { // from class: org.springframework.jdbc.core.JdbcTemplate.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.jdbc.core.PreparedStatementCallback
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException {
                try {
                    if (pss != null) {
                        pss.setValues(ps);
                    }
                    int rows = ps.executeUpdate();
                    if (JdbcTemplate.this.logger.isDebugEnabled()) {
                        JdbcTemplate.this.logger.debug("SQL update affected " + rows + " rows");
                    }
                    Integer numValueOf = Integer.valueOf(rows);
                    if (pss instanceof ParameterDisposer) {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                    return numValueOf;
                } catch (Throwable th) {
                    if (pss instanceof ParameterDisposer) {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                    throw th;
                }
            }
        })).intValue();
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int update(PreparedStatementCreator psc) throws DataAccessException {
        return update(psc, (PreparedStatementSetter) null);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int update(PreparedStatementCreator psc, final KeyHolder generatedKeyHolder) throws DataAccessException {
        Assert.notNull(generatedKeyHolder, "KeyHolder must not be null");
        this.logger.debug("Executing SQL update and returning generated keys");
        return ((Integer) execute(psc, new PreparedStatementCallback<Integer>() { // from class: org.springframework.jdbc.core.JdbcTemplate.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.jdbc.core.PreparedStatementCallback
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException {
                int rows = ps.executeUpdate();
                List<Map<String, Object>> generatedKeys = generatedKeyHolder.getKeyList();
                generatedKeys.clear();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys != null) {
                    try {
                        RowMapperResultSetExtractor<Map<String, Object>> rse = new RowMapperResultSetExtractor<>(JdbcTemplate.this.getColumnMapRowMapper(), 1);
                        generatedKeys.addAll(rse.extractData(keys));
                        JdbcUtils.closeResultSet(keys);
                    } catch (Throwable th) {
                        JdbcUtils.closeResultSet(keys);
                        throw th;
                    }
                }
                if (JdbcTemplate.this.logger.isDebugEnabled()) {
                    JdbcTemplate.this.logger.debug("SQL update affected " + rows + " rows and returned " + generatedKeys.size() + " keys");
                }
                return Integer.valueOf(rows);
            }
        })).intValue();
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        return update(new SimplePreparedStatementCreator(sql), pss);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return update(sql, newArgTypePreparedStatementSetter(args, argTypes));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int update(String sql, Object... args) throws DataAccessException {
        return update(sql, newArgPreparedStatementSetter(args));
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int[] batchUpdate(String sql, final BatchPreparedStatementSetter pss) throws DataAccessException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Executing SQL batch update [" + sql + "]");
        }
        return (int[]) execute(sql, new PreparedStatementCallback<int[]>() { // from class: org.springframework.jdbc.core.JdbcTemplate.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.jdbc.core.PreparedStatementCallback
            public int[] doInPreparedStatement(PreparedStatement ps) throws SQLException {
                try {
                    int batchSize = pss.getBatchSize();
                    InterruptibleBatchPreparedStatementSetter ipss = pss instanceof InterruptibleBatchPreparedStatementSetter ? (InterruptibleBatchPreparedStatementSetter) pss : null;
                    if (JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
                        for (int i = 0; i < batchSize; i++) {
                            pss.setValues(ps, i);
                            if (ipss != null && ipss.isBatchExhausted(i)) {
                                break;
                            }
                            ps.addBatch();
                        }
                        int[] iArrExecuteBatch = ps.executeBatch();
                        if (pss instanceof ParameterDisposer) {
                            ((ParameterDisposer) pss).cleanupParameters();
                        }
                        return iArrExecuteBatch;
                    }
                    List<Integer> rowsAffected = new ArrayList<>();
                    for (int i2 = 0; i2 < batchSize; i2++) {
                        pss.setValues(ps, i2);
                        if (ipss != null && ipss.isBatchExhausted(i2)) {
                            break;
                        }
                        rowsAffected.add(Integer.valueOf(ps.executeUpdate()));
                    }
                    int[] rowsAffectedArray = new int[rowsAffected.size()];
                    for (int i3 = 0; i3 < rowsAffectedArray.length; i3++) {
                        rowsAffectedArray[i3] = rowsAffected.get(i3).intValue();
                    }
                    return rowsAffectedArray;
                } finally {
                    if (pss instanceof ParameterDisposer) {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                }
            }
        });
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) throws DataAccessException {
        return batchUpdate(sql, batchArgs, new int[0]);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) throws DataAccessException {
        return BatchUpdateUtils.executeBatchUpdate(sql, batchArgs, argTypes, this);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> int[][] batchUpdate(String sql, final Collection<T> batchArgs, final int batchSize, final ParameterizedPreparedStatementSetter<T> pss) throws DataAccessException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Executing SQL batch update [" + sql + "] with a batch size of " + batchSize);
        }
        return (int[][]) execute(sql, new PreparedStatementCallback<int[][]>() { // from class: org.springframework.jdbc.core.JdbcTemplate.5
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Type inference failed for: r0v19, types: [int[], int[][]] */
            @Override // org.springframework.jdbc.core.PreparedStatementCallback
            public int[][] doInPreparedStatement(PreparedStatement ps) throws SQLException {
                List<int[]> rowsAffected = new ArrayList<>();
                try {
                    boolean batchSupported = JdbcUtils.supportsBatchUpdates(ps.getConnection());
                    int n = 0;
                    Iterator it = batchArgs.iterator();
                    while (it.hasNext()) {
                        pss.setValues(ps, it.next());
                        n++;
                        if (batchSupported) {
                            ps.addBatch();
                            if (n % batchSize == 0 || n == batchArgs.size()) {
                                if (JdbcTemplate.this.logger.isDebugEnabled()) {
                                    int batchIdx = n % batchSize == 0 ? n / batchSize : (n / batchSize) + 1;
                                    int items = n - ((n % batchSize == 0 ? (n / batchSize) - 1 : n / batchSize) * batchSize);
                                    JdbcTemplate.this.logger.debug("Sending SQL batch update #" + batchIdx + " with " + items + " items");
                                }
                                rowsAffected.add(ps.executeBatch());
                            }
                        } else {
                            int i = ps.executeUpdate();
                            rowsAffected.add(new int[]{i});
                        }
                    }
                    ?? r0 = new int[rowsAffected.size()];
                    for (int i2 = 0; i2 < r0.length; i2++) {
                        r0[i2] = rowsAffected.get(i2);
                    }
                    return r0;
                } finally {
                    if (pss instanceof ParameterDisposer) {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                }
            }
        });
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
        Assert.notNull(csc, "CallableStatementCreator must not be null");
        Assert.notNull(action, "Callback object must not be null");
        if (this.logger.isDebugEnabled()) {
            String sql = getSql(csc);
            this.logger.debug("Calling stored procedure" + (sql != null ? " [" + sql + "]" : ""));
        }
        Connection con = DataSourceUtils.getConnection(getDataSource());
        CallableStatement cs = null;
        try {
            try {
                Connection conToUse = con;
                if (this.nativeJdbcExtractor != null) {
                    conToUse = this.nativeJdbcExtractor.getNativeConnection(con);
                }
                cs = csc.createCallableStatement(conToUse);
                applyStatementSettings(cs);
                CallableStatement csToUse = cs;
                if (this.nativeJdbcExtractor != null) {
                    csToUse = this.nativeJdbcExtractor.getNativeCallableStatement(cs);
                }
                T result = action.doInCallableStatement(csToUse);
                handleWarnings(cs);
                if (csc instanceof ParameterDisposer) {
                    ((ParameterDisposer) csc).cleanupParameters();
                }
                JdbcUtils.closeStatement(cs);
                DataSourceUtils.releaseConnection(con, getDataSource());
                return result;
            } catch (SQLException ex) {
                if (csc instanceof ParameterDisposer) {
                    ((ParameterDisposer) csc).cleanupParameters();
                }
                String sql2 = getSql(csc);
                JdbcUtils.closeStatement(cs);
                DataSourceUtils.releaseConnection(con, getDataSource());
                throw getExceptionTranslator().translate("CallableStatementCallback", sql2, ex);
            }
        } catch (Throwable th) {
            if (csc instanceof ParameterDisposer) {
                ((ParameterDisposer) csc).cleanupParameters();
            }
            JdbcUtils.closeStatement(cs);
            DataSourceUtils.releaseConnection(con, getDataSource());
            throw th;
        }
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public <T> T execute(String str, CallableStatementCallback<T> callableStatementCallback) throws DataAccessException {
        return (T) execute(new SimpleCallableStatementCreator(str), callableStatementCallback);
    }

    @Override // org.springframework.jdbc.core.JdbcOperations
    public Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters) throws DataAccessException {
        final List<SqlParameter> updateCountParameters = new ArrayList<>();
        final List<SqlParameter> resultSetParameters = new ArrayList<>();
        final List<SqlParameter> callParameters = new ArrayList<>();
        for (SqlParameter parameter : declaredParameters) {
            if (parameter.isResultsParameter()) {
                if (parameter instanceof SqlReturnResultSet) {
                    resultSetParameters.add(parameter);
                } else {
                    updateCountParameters.add(parameter);
                }
            } else {
                callParameters.add(parameter);
            }
        }
        return (Map) execute(csc, new CallableStatementCallback<Map<String, Object>>() { // from class: org.springframework.jdbc.core.JdbcTemplate.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.jdbc.core.CallableStatementCallback
            public Map<String, Object> doInCallableStatement(CallableStatement cs) throws SQLException {
                boolean retVal = cs.execute();
                int updateCount = cs.getUpdateCount();
                if (JdbcTemplate.this.logger.isDebugEnabled()) {
                    JdbcTemplate.this.logger.debug("CallableStatement.execute() returned '" + retVal + "'");
                    JdbcTemplate.this.logger.debug("CallableStatement.getUpdateCount() returned " + updateCount);
                }
                Map<String, Object> resultsMap = JdbcTemplate.this.createResultsMap();
                if (retVal || updateCount != -1) {
                    resultsMap.putAll(JdbcTemplate.this.extractReturnedResults(cs, updateCountParameters, resultSetParameters, updateCount));
                }
                resultsMap.putAll(JdbcTemplate.this.extractOutputParameters(cs, callParameters));
                return resultsMap;
            }
        });
    }

    protected Map<String, Object> extractReturnedResults(CallableStatement cs, List<SqlParameter> updateCountParameters, List<SqlParameter> resultSetParameters, int updateCount) throws SQLException {
        Map<String, Object> results = new LinkedHashMap<>(4);
        int rsIndex = 0;
        int updateIndex = 0;
        if (!this.skipResultsProcessing) {
            while (true) {
                if (updateCount == -1) {
                    if (resultSetParameters != null && resultSetParameters.size() > rsIndex) {
                        SqlReturnResultSet declaredRsParam = (SqlReturnResultSet) resultSetParameters.get(rsIndex);
                        results.putAll(processResultSet(cs.getResultSet(), declaredRsParam));
                        rsIndex++;
                    } else if (!this.skipUndeclaredResults) {
                        String rsName = RETURN_RESULT_SET_PREFIX + (rsIndex + 1);
                        SqlReturnResultSet undeclaredRsParam = new SqlReturnResultSet(rsName, getColumnMapRowMapper());
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Added default SqlReturnResultSet parameter named '" + rsName + "'");
                        }
                        results.putAll(processResultSet(cs.getResultSet(), undeclaredRsParam));
                        rsIndex++;
                    }
                } else if (updateCountParameters != null && updateCountParameters.size() > updateIndex) {
                    SqlReturnUpdateCount ucParam = (SqlReturnUpdateCount) updateCountParameters.get(updateIndex);
                    String declaredUcName = ucParam.getName();
                    results.put(declaredUcName, Integer.valueOf(updateCount));
                    updateIndex++;
                } else if (!this.skipUndeclaredResults) {
                    String undeclaredName = RETURN_UPDATE_COUNT_PREFIX + (updateIndex + 1);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Added default SqlReturnUpdateCount parameter named '" + undeclaredName + "'");
                    }
                    results.put(undeclaredName, Integer.valueOf(updateCount));
                    updateIndex++;
                }
                boolean moreResults = cs.getMoreResults();
                updateCount = cs.getUpdateCount();
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("CallableStatement.getUpdateCount() returned " + updateCount);
                }
                if (!moreResults && updateCount == -1) {
                    break;
                }
            }
        }
        return results;
    }

    protected Map<String, Object> extractOutputParameters(CallableStatement cs, List<SqlParameter> parameters) throws SQLException {
        Map<String, Object> results = new LinkedHashMap<>(parameters.size());
        int sqlColIndex = 1;
        for (SqlParameter param : parameters) {
            if (param instanceof SqlOutParameter) {
                SqlOutParameter outParam = (SqlOutParameter) param;
                if (outParam.isReturnTypeSupported()) {
                    results.put(outParam.getName(), outParam.getSqlReturnType().getTypeValue(cs, sqlColIndex, outParam.getSqlType(), outParam.getTypeName()));
                } else {
                    Object out = cs.getObject(sqlColIndex);
                    if (out instanceof ResultSet) {
                        if (outParam.isResultSetSupported()) {
                            results.putAll(processResultSet((ResultSet) out, outParam));
                        } else {
                            String rsName = outParam.getName();
                            SqlReturnResultSet rsParam = new SqlReturnResultSet(rsName, getColumnMapRowMapper());
                            results.putAll(processResultSet((ResultSet) out, rsParam));
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("Added default SqlReturnResultSet parameter named '" + rsName + "'");
                            }
                        }
                    } else {
                        results.put(outParam.getName(), out);
                    }
                }
            }
            if (!param.isResultsParameter()) {
                sqlColIndex++;
            }
        }
        return results;
    }

    protected Map<String, Object> processResultSet(ResultSet rs, ResultSetSupportingSqlParameter param) throws SQLException {
        if (rs != null) {
            try {
                ResultSet rsToUse = rs;
                if (this.nativeJdbcExtractor != null) {
                    rsToUse = this.nativeJdbcExtractor.getNativeResultSet(rs);
                }
                if (param.getRowMapper() != null) {
                    RowMapper rowMapper = param.getRowMapper();
                    Object data = new RowMapperResultSetExtractor(rowMapper).extractData(rsToUse);
                    Map<String, Object> mapSingletonMap = Collections.singletonMap(param.getName(), data);
                    JdbcUtils.closeResultSet(rs);
                    return mapSingletonMap;
                }
                if (param.getRowCallbackHandler() != null) {
                    RowCallbackHandler rch = param.getRowCallbackHandler();
                    new RowCallbackHandlerResultSetExtractor(rch).extractData(rsToUse);
                    Map<String, Object> mapSingletonMap2 = Collections.singletonMap(param.getName(), "ResultSet returned from stored procedure was processed");
                    JdbcUtils.closeResultSet(rs);
                    return mapSingletonMap2;
                }
                if (param.getResultSetExtractor() != null) {
                    Object data2 = param.getResultSetExtractor().extractData(rsToUse);
                    Map<String, Object> mapSingletonMap3 = Collections.singletonMap(param.getName(), data2);
                    JdbcUtils.closeResultSet(rs);
                    return mapSingletonMap3;
                }
                JdbcUtils.closeResultSet(rs);
            } catch (Throwable th) {
                JdbcUtils.closeResultSet(rs);
                throw th;
            }
        }
        return Collections.emptyMap();
    }

    protected RowMapper<Map<String, Object>> getColumnMapRowMapper() {
        return new ColumnMapRowMapper();
    }

    protected <T> RowMapper<T> getSingleColumnRowMapper(Class<T> requiredType) {
        return new SingleColumnRowMapper(requiredType);
    }

    protected Map<String, Object> createResultsMap() {
        if (isResultsMapCaseInsensitive()) {
            return new LinkedCaseInsensitiveMap();
        }
        return new LinkedHashMap();
    }

    protected void applyStatementSettings(Statement stmt) throws SQLException {
        int fetchSize = getFetchSize();
        if (fetchSize != -1) {
            stmt.setFetchSize(fetchSize);
        }
        int maxRows = getMaxRows();
        if (maxRows != -1) {
            stmt.setMaxRows(maxRows);
        }
        DataSourceUtils.applyTimeout(stmt, getDataSource(), getQueryTimeout());
    }

    protected PreparedStatementSetter newArgPreparedStatementSetter(Object[] args) {
        return new ArgumentPreparedStatementSetter(args);
    }

    protected PreparedStatementSetter newArgTypePreparedStatementSetter(Object[] args, int[] argTypes) {
        return new ArgumentTypePreparedStatementSetter(args, argTypes);
    }

    protected void handleWarnings(Statement stmt) throws SQLException, SQLWarningException {
        if (isIgnoreWarnings()) {
            if (this.logger.isDebugEnabled()) {
                SQLWarning warnings = stmt.getWarnings();
                while (true) {
                    SQLWarning warningToLog = warnings;
                    if (warningToLog != null) {
                        this.logger.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" + warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
                        warnings = warningToLog.getNextWarning();
                    } else {
                        return;
                    }
                }
            }
        } else {
            handleWarnings(stmt.getWarnings());
        }
    }

    protected void handleWarnings(SQLWarning warning) throws SQLWarningException {
        if (warning != null) {
            throw new SQLWarningException("Warning not ignored", warning);
        }
    }

    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        }
        return null;
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$CloseSuppressingInvocationHandler.class */
    private class CloseSuppressingInvocationHandler implements InvocationHandler {
        private final Connection target;

        public CloseSuppressingInvocationHandler(Connection target) {
            this.target = target;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("equals")) {
                return Boolean.valueOf(proxy == args[0]);
            }
            if (method.getName().equals(IdentityNamingStrategy.HASH_CODE_KEY)) {
                return Integer.valueOf(System.identityHashCode(proxy));
            }
            if (method.getName().equals("unwrap")) {
                if (((Class) args[0]).isInstance(proxy)) {
                    return proxy;
                }
            } else if (method.getName().equals("isWrapperFor")) {
                if (((Class) args[0]).isInstance(proxy)) {
                    return true;
                }
            } else {
                if (method.getName().equals("close")) {
                    return null;
                }
                if (method.getName().equals("isClosed")) {
                    return false;
                }
                if (method.getName().equals("getTargetConnection")) {
                    return this.target;
                }
            }
            try {
                Object retVal = method.invoke(this.target, args);
                if (retVal instanceof Statement) {
                    JdbcTemplate.this.applyStatementSettings((Statement) retVal);
                }
                return retVal;
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$SimplePreparedStatementCreator.class */
    private static class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {
        private final String sql;

        public SimplePreparedStatementCreator(String sql) {
            Assert.notNull(sql, "SQL must not be null");
            this.sql = sql;
        }

        @Override // org.springframework.jdbc.core.PreparedStatementCreator
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            return con.prepareStatement(this.sql);
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return this.sql;
        }
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$SimpleCallableStatementCreator.class */
    private static class SimpleCallableStatementCreator implements CallableStatementCreator, SqlProvider {
        private final String callString;

        public SimpleCallableStatementCreator(String callString) {
            Assert.notNull(callString, "Call string must not be null");
            this.callString = callString;
        }

        @Override // org.springframework.jdbc.core.CallableStatementCreator
        public CallableStatement createCallableStatement(Connection con) throws SQLException {
            return con.prepareCall(this.callString);
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return this.callString;
        }
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/JdbcTemplate$RowCallbackHandlerResultSetExtractor.class */
    private static class RowCallbackHandlerResultSetExtractor implements ResultSetExtractor<Object> {
        private final RowCallbackHandler rch;

        public RowCallbackHandlerResultSetExtractor(RowCallbackHandler rch) {
            this.rch = rch;
        }

        @Override // org.springframework.jdbc.core.ResultSetExtractor
        public Object extractData(ResultSet rs) throws SQLException {
            while (rs.next()) {
                this.rch.processRow(rs);
            }
            return null;
        }
    }
}

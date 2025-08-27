package org.springframework.jdbc.core.simple;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/simple/AbstractJdbcInsert.class */
public abstract class AbstractJdbcInsert {
    protected final Log logger;
    private final JdbcTemplate jdbcTemplate;
    private final TableMetaDataContext tableMetaDataContext;
    private final List<String> declaredColumns;
    private String[] generatedKeyNames;
    private volatile boolean compiled;
    private String insertString;
    private int[] insertTypes;

    protected AbstractJdbcInsert(DataSource dataSource) {
        this.logger = LogFactory.getLog(getClass());
        this.tableMetaDataContext = new TableMetaDataContext();
        this.declaredColumns = new ArrayList();
        this.generatedKeyNames = new String[0];
        this.compiled = false;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected AbstractJdbcInsert(JdbcTemplate jdbcTemplate) {
        this.logger = LogFactory.getLog(getClass());
        this.tableMetaDataContext = new TableMetaDataContext();
        this.declaredColumns = new ArrayList();
        this.generatedKeyNames = new String[0];
        this.compiled = false;
        Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");
        this.jdbcTemplate = jdbcTemplate;
        setNativeJdbcExtractor(jdbcTemplate.getNativeJdbcExtractor());
    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    public void setTableName(String tableName) {
        checkIfConfigurationModificationIsAllowed();
        this.tableMetaDataContext.setTableName(tableName);
    }

    public String getTableName() {
        return this.tableMetaDataContext.getTableName();
    }

    public void setSchemaName(String schemaName) {
        checkIfConfigurationModificationIsAllowed();
        this.tableMetaDataContext.setSchemaName(schemaName);
    }

    public String getSchemaName() {
        return this.tableMetaDataContext.getSchemaName();
    }

    public void setCatalogName(String catalogName) {
        checkIfConfigurationModificationIsAllowed();
        this.tableMetaDataContext.setCatalogName(catalogName);
    }

    public String getCatalogName() {
        return this.tableMetaDataContext.getCatalogName();
    }

    public void setColumnNames(List<String> columnNames) {
        checkIfConfigurationModificationIsAllowed();
        this.declaredColumns.clear();
        this.declaredColumns.addAll(columnNames);
    }

    public List<String> getColumnNames() {
        return Collections.unmodifiableList(this.declaredColumns);
    }

    public void setGeneratedKeyName(String generatedKeyName) {
        checkIfConfigurationModificationIsAllowed();
        this.generatedKeyNames = new String[]{generatedKeyName};
    }

    public void setGeneratedKeyNames(String... generatedKeyNames) {
        checkIfConfigurationModificationIsAllowed();
        this.generatedKeyNames = generatedKeyNames;
    }

    public String[] getGeneratedKeyNames() {
        return this.generatedKeyNames;
    }

    public void setAccessTableColumnMetaData(boolean accessTableColumnMetaData) {
        this.tableMetaDataContext.setAccessTableColumnMetaData(accessTableColumnMetaData);
    }

    public void setOverrideIncludeSynonymsDefault(boolean override) {
        this.tableMetaDataContext.setOverrideIncludeSynonymsDefault(override);
    }

    public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor) {
        this.tableMetaDataContext.setNativeJdbcExtractor(nativeJdbcExtractor);
    }

    public String getInsertString() {
        return this.insertString;
    }

    public int[] getInsertTypes() {
        return this.insertTypes;
    }

    public final synchronized void compile() throws InvalidDataAccessApiUsageException {
        if (!isCompiled()) {
            if (getTableName() == null) {
                throw new InvalidDataAccessApiUsageException("Table name is required");
            }
            try {
                this.jdbcTemplate.afterPropertiesSet();
                compileInternal();
                this.compiled = true;
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("JdbcInsert for table [" + getTableName() + "] compiled");
                }
            } catch (IllegalArgumentException ex) {
                throw new InvalidDataAccessApiUsageException(ex.getMessage());
            }
        }
    }

    protected void compileInternal() {
        this.tableMetaDataContext.processMetaData(getJdbcTemplate().getDataSource(), getColumnNames(), getGeneratedKeyNames());
        this.insertString = this.tableMetaDataContext.createInsertString(getGeneratedKeyNames());
        this.insertTypes = this.tableMetaDataContext.createInsertTypes();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Compiled insert object: insert string is [" + getInsertString() + "]");
        }
        onCompileInternal();
    }

    protected void onCompileInternal() {
    }

    public boolean isCompiled() {
        return this.compiled;
    }

    protected void checkCompiled() throws InvalidDataAccessApiUsageException {
        if (!isCompiled()) {
            this.logger.debug("JdbcInsert not compiled before execution - invoking compile");
            compile();
        }
    }

    protected void checkIfConfigurationModificationIsAllowed() {
        if (isCompiled()) {
            throw new InvalidDataAccessApiUsageException("Configuration cannot be altered once the class has been compiled or used");
        }
    }

    protected int doExecute(Map<String, ?> args) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<Object> values = matchInParameterValuesWithInsertColumns(args);
        return executeInsertInternal(values);
    }

    protected int doExecute(SqlParameterSource parameterSource) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<Object> values = matchInParameterValuesWithInsertColumns(parameterSource);
        return executeInsertInternal(values);
    }

    private int executeInsertInternal(List<?> values) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("The following parameters are used for insert " + getInsertString() + " with: " + values);
        }
        return getJdbcTemplate().update(getInsertString(), values.toArray(), getInsertTypes());
    }

    protected Number doExecuteAndReturnKey(Map<String, ?> args) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<Object> values = matchInParameterValuesWithInsertColumns(args);
        return executeInsertAndReturnKeyInternal(values);
    }

    protected Number doExecuteAndReturnKey(SqlParameterSource parameterSource) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<Object> values = matchInParameterValuesWithInsertColumns(parameterSource);
        return executeInsertAndReturnKeyInternal(values);
    }

    protected KeyHolder doExecuteAndReturnKeyHolder(Map<String, ?> args) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<Object> values = matchInParameterValuesWithInsertColumns(args);
        return executeInsertAndReturnKeyHolderInternal(values);
    }

    protected KeyHolder doExecuteAndReturnKeyHolder(SqlParameterSource parameterSource) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<Object> values = matchInParameterValuesWithInsertColumns(parameterSource);
        return executeInsertAndReturnKeyHolderInternal(values);
    }

    private Number executeInsertAndReturnKeyInternal(List<?> values) throws DataAccessException {
        KeyHolder kh = executeInsertAndReturnKeyHolderInternal(values);
        if (kh != null && kh.getKey() != null) {
            return kh.getKey();
        }
        throw new DataIntegrityViolationException("Unable to retrieve the generated key for the insert: " + getInsertString());
    }

    private KeyHolder executeInsertAndReturnKeyHolderInternal(final List<?> values) throws DataAccessException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("The following parameters are used for call " + getInsertString() + " with: " + values);
        }
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        if (this.tableMetaDataContext.isGetGeneratedKeysSupported()) {
            getJdbcTemplate().update(new PreparedStatementCreator() { // from class: org.springframework.jdbc.core.simple.AbstractJdbcInsert.1
                @Override // org.springframework.jdbc.core.PreparedStatementCreator
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = AbstractJdbcInsert.this.prepareStatementForGeneratedKeys(con);
                    AbstractJdbcInsert.this.setParameterValues(ps, values, AbstractJdbcInsert.this.getInsertTypes());
                    return ps;
                }
            }, keyHolder);
        } else {
            if (!this.tableMetaDataContext.isGetGeneratedKeysSimulated()) {
                throw new InvalidDataAccessResourceUsageException("The getGeneratedKeys feature is not supported by this database");
            }
            if (getGeneratedKeyNames().length < 1) {
                throw new InvalidDataAccessApiUsageException("Generated Key Name(s) not specified. Using the generated keys features requires specifying the name(s) of the generated column(s)");
            }
            if (getGeneratedKeyNames().length > 1) {
                throw new InvalidDataAccessApiUsageException("Current database only supports retrieving the key for a single column. There are " + getGeneratedKeyNames().length + " columns specified: " + Arrays.asList(getGeneratedKeyNames()));
            }
            final String keyQuery = this.tableMetaDataContext.getSimpleQueryForGetGeneratedKey(this.tableMetaDataContext.getTableName(), getGeneratedKeyNames()[0]);
            Assert.notNull(keyQuery, "Query for simulating get generated keys can't be null");
            if (keyQuery.toUpperCase().startsWith("RETURNING")) {
                Object key = (Long) getJdbcTemplate().queryForObject(getInsertString() + SymbolConstants.SPACE_SYMBOL + keyQuery, values.toArray(), Long.class);
                Map<String, Object> keys = new HashMap<>(1);
                keys.put(getGeneratedKeyNames()[0], key);
                keyHolder.getKeyList().add(keys);
            } else {
                getJdbcTemplate().execute(new ConnectionCallback<Object>() { // from class: org.springframework.jdbc.core.simple.AbstractJdbcInsert.2
                    @Override // org.springframework.jdbc.core.ConnectionCallback
                    public Object doInConnection(Connection con) throws SQLException, DataAccessException {
                        Statement keyStmt = null;
                        try {
                            keyStmt = con.prepareStatement(AbstractJdbcInsert.this.getInsertString());
                            AbstractJdbcInsert.this.setParameterValues(keyStmt, values, AbstractJdbcInsert.this.getInsertTypes());
                            keyStmt.executeUpdate();
                            JdbcUtils.closeStatement(keyStmt);
                            Statement keyStmt2 = null;
                            ResultSet rs = null;
                            Map<String, Object> keys2 = new HashMap<>(1);
                            try {
                                keyStmt2 = con.createStatement();
                                rs = keyStmt2.executeQuery(keyQuery);
                                if (rs.next()) {
                                    long key2 = rs.getLong(1);
                                    keys2.put(AbstractJdbcInsert.this.getGeneratedKeyNames()[0], Long.valueOf(key2));
                                    keyHolder.getKeyList().add(keys2);
                                }
                                JdbcUtils.closeResultSet(rs);
                                JdbcUtils.closeStatement(keyStmt2);
                                return null;
                            } catch (Throwable th) {
                                JdbcUtils.closeResultSet(rs);
                                throw th;
                            }
                        } finally {
                            JdbcUtils.closeStatement(keyStmt);
                        }
                    }
                });
            }
        }
        return keyHolder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PreparedStatement prepareStatementForGeneratedKeys(Connection con) throws SQLException {
        PreparedStatement ps;
        if (getGeneratedKeyNames().length < 1) {
            throw new InvalidDataAccessApiUsageException("Generated Key Name(s) not specified. Using the generated keys features requires specifying the name(s) of the generated column(s).");
        }
        if (this.tableMetaDataContext.isGeneratedKeysColumnNameArraySupported()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Using generated keys support with array of column names.");
            }
            ps = con.prepareStatement(getInsertString(), getGeneratedKeyNames());
        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Using generated keys support with Statement.RETURN_GENERATED_KEYS.");
            }
            ps = con.prepareStatement(getInsertString(), 1);
        }
        return ps;
    }

    protected int[] doExecuteBatch(Map<String, ?>... batch) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<List<Object>> batchValues = new ArrayList<>(batch.length);
        for (Map<String, ?> args : batch) {
            batchValues.add(matchInParameterValuesWithInsertColumns(args));
        }
        return executeBatchInternal(batchValues);
    }

    protected int[] doExecuteBatch(SqlParameterSource... batch) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        List<List<Object>> batchValues = new ArrayList<>(batch.length);
        for (SqlParameterSource parameterSource : batch) {
            batchValues.add(matchInParameterValuesWithInsertColumns(parameterSource));
        }
        return executeBatchInternal(batchValues);
    }

    private int[] executeBatchInternal(final List<List<Object>> batchValues) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Executing statement " + getInsertString() + " with batch of size: " + batchValues.size());
        }
        return getJdbcTemplate().batchUpdate(getInsertString(), new BatchPreparedStatementSetter() { // from class: org.springframework.jdbc.core.simple.AbstractJdbcInsert.3
            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AbstractJdbcInsert.this.setParameterValues(ps, (List) batchValues.get(i), AbstractJdbcInsert.this.getInsertTypes());
            }

            @Override // org.springframework.jdbc.core.BatchPreparedStatementSetter
            public int getBatchSize() {
                return batchValues.size();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setParameterValues(PreparedStatement preparedStatement, List<?> values, int... columnTypes) throws SQLException {
        int colIndex = 0;
        for (Object value : values) {
            colIndex++;
            if (columnTypes == null || colIndex > columnTypes.length) {
                StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, Integer.MIN_VALUE, value);
            } else {
                StatementCreatorUtils.setParameterValue(preparedStatement, colIndex, columnTypes[colIndex - 1], value);
            }
        }
    }

    protected List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource parameterSource) {
        return this.tableMetaDataContext.matchInParameterValuesWithInsertColumns(parameterSource);
    }

    protected List<Object> matchInParameterValuesWithInsertColumns(Map<String, ?> args) {
        return this.tableMetaDataContext.matchInParameterValuesWithInsertColumns(args);
    }
}

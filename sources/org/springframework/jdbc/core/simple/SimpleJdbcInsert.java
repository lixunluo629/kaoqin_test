package org.springframework.jdbc.core.simple;

import java.util.Arrays;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/simple/SimpleJdbcInsert.class */
public class SimpleJdbcInsert extends AbstractJdbcInsert implements SimpleJdbcInsertOperations {
    public SimpleJdbcInsert(DataSource dataSource) {
        super(dataSource);
    }

    public SimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsert withTableName(String tableName) {
        setTableName(tableName);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsert withSchemaName(String schemaName) {
        setSchemaName(schemaName);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsert withCatalogName(String catalogName) {
        setCatalogName(catalogName);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsert usingColumns(String... columnNames) {
        setColumnNames(Arrays.asList(columnNames));
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsert usingGeneratedKeyColumns(String... columnNames) {
        setGeneratedKeyNames(columnNames);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsertOperations withoutTableColumnMetaDataAccess() {
        setAccessTableColumnMetaData(false);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsertOperations includeSynonymsForTableColumnMetaData() {
        setOverrideIncludeSynonymsDefault(true);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public SimpleJdbcInsertOperations useNativeJdbcExtractorForMetaData(NativeJdbcExtractor nativeJdbcExtractor) {
        setNativeJdbcExtractor(nativeJdbcExtractor);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public int execute(Map<String, ?> args) {
        return doExecute(args);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public int execute(SqlParameterSource parameterSource) {
        return doExecute(parameterSource);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public Number executeAndReturnKey(Map<String, ?> args) {
        return doExecuteAndReturnKey(args);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public Number executeAndReturnKey(SqlParameterSource parameterSource) {
        return doExecuteAndReturnKey(parameterSource);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public KeyHolder executeAndReturnKeyHolder(Map<String, ?> args) {
        return doExecuteAndReturnKeyHolder(args);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public KeyHolder executeAndReturnKeyHolder(SqlParameterSource parameterSource) {
        return doExecuteAndReturnKeyHolder(parameterSource);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public int[] executeBatch(Map<String, ?>... batch) {
        return doExecuteBatch(batch);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations
    public int[] executeBatch(SqlParameterSource... batch) {
        return doExecuteBatch(batch);
    }
}

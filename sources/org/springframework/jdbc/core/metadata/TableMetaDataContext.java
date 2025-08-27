package org.springframework.jdbc.core.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/TableMetaDataContext.class */
public class TableMetaDataContext {
    private String tableName;
    private String catalogName;
    private String schemaName;
    private TableMetaDataProvider metaDataProvider;
    NativeJdbcExtractor nativeJdbcExtractor;
    protected final Log logger = LogFactory.getLog(getClass());
    private List<String> tableColumns = new ArrayList();
    private boolean accessTableColumnMetaData = true;
    private boolean overrideIncludeSynonymsDefault = false;
    private boolean generatedKeyColumnsUsed = false;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogName() {
        return this.catalogName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public void setAccessTableColumnMetaData(boolean accessTableColumnMetaData) {
        this.accessTableColumnMetaData = accessTableColumnMetaData;
    }

    public boolean isAccessTableColumnMetaData() {
        return this.accessTableColumnMetaData;
    }

    public void setOverrideIncludeSynonymsDefault(boolean override) {
        this.overrideIncludeSynonymsDefault = override;
    }

    public boolean isOverrideIncludeSynonymsDefault() {
        return this.overrideIncludeSynonymsDefault;
    }

    public List<String> getTableColumns() {
        return this.tableColumns;
    }

    public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor) {
        this.nativeJdbcExtractor = nativeJdbcExtractor;
    }

    public void processMetaData(DataSource dataSource, List<String> declaredColumns, String[] generatedKeyNames) {
        this.metaDataProvider = TableMetaDataProviderFactory.createMetaDataProvider(dataSource, this, this.nativeJdbcExtractor);
        this.tableColumns = reconcileColumnsToUse(declaredColumns, generatedKeyNames);
    }

    protected List<String> reconcileColumnsToUse(List<String> declaredColumns, String[] generatedKeyNames) {
        if (generatedKeyNames.length > 0) {
            this.generatedKeyColumnsUsed = true;
        }
        if (!declaredColumns.isEmpty()) {
            return new ArrayList(declaredColumns);
        }
        Set<String> keys = new LinkedHashSet<>(generatedKeyNames.length);
        for (String key : generatedKeyNames) {
            keys.add(key.toUpperCase());
        }
        List<String> columns = new ArrayList<>();
        for (TableParameterMetaData meta : this.metaDataProvider.getTableParameterMetaData()) {
            if (!keys.contains(meta.getParameterName().toUpperCase())) {
                columns.add(meta.getParameterName());
            }
        }
        return columns;
    }

    public List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource parameterSource) {
        List<Object> values = new ArrayList<>();
        Map<String, String> caseInsensitiveParameterNames = SqlParameterSourceUtils.extractCaseInsensitiveParameterNames(parameterSource);
        for (String column : this.tableColumns) {
            if (parameterSource.hasValue(column)) {
                values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, column));
            } else {
                String lowerCaseName = column.toLowerCase();
                if (parameterSource.hasValue(lowerCaseName)) {
                    values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, lowerCaseName));
                } else {
                    String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(column);
                    if (parameterSource.hasValue(propertyName)) {
                        values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, propertyName));
                    } else if (caseInsensitiveParameterNames.containsKey(lowerCaseName)) {
                        values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, caseInsensitiveParameterNames.get(lowerCaseName)));
                    } else {
                        values.add(null);
                    }
                }
            }
        }
        return values;
    }

    public List<Object> matchInParameterValuesWithInsertColumns(Map<String, ?> inParameters) {
        List<Object> values = new ArrayList<>();
        Map<String, Object> source = new LinkedHashMap<>(inParameters.size());
        for (String key : inParameters.keySet()) {
            source.put(key.toLowerCase(), inParameters.get(key));
        }
        for (String column : this.tableColumns) {
            values.add(source.get(column.toLowerCase()));
        }
        return values;
    }

    public String createInsertString(String... generatedKeyNames) {
        Set<String> keys = new LinkedHashSet<>(generatedKeyNames.length);
        for (String key : generatedKeyNames) {
            keys.add(key.toUpperCase());
        }
        StringBuilder insertStatement = new StringBuilder();
        insertStatement.append("INSERT INTO ");
        if (getSchemaName() != null) {
            insertStatement.append(getSchemaName());
            insertStatement.append(".");
        }
        insertStatement.append(getTableName());
        insertStatement.append(" (");
        int columnCount = 0;
        for (String columnName : getTableColumns()) {
            if (!keys.contains(columnName.toUpperCase())) {
                columnCount++;
                if (columnCount > 1) {
                    insertStatement.append(", ");
                }
                insertStatement.append(columnName);
            }
        }
        insertStatement.append(") VALUES(");
        if (columnCount < 1) {
            if (this.generatedKeyColumnsUsed) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Unable to locate non-key columns for table '" + getTableName() + "' so an empty insert statement is generated");
                }
            } else {
                throw new InvalidDataAccessApiUsageException("Unable to locate columns for table '" + getTableName() + "' so an insert statement can't be generated");
            }
        }
        for (int i = 0; i < columnCount; i++) {
            if (i > 0) {
                insertStatement.append(", ");
            }
            insertStatement.append("?");
        }
        insertStatement.append(")");
        return insertStatement.toString();
    }

    public int[] createInsertTypes() {
        int[] types = new int[getTableColumns().size()];
        List<TableParameterMetaData> parameters = this.metaDataProvider.getTableParameterMetaData();
        Map<String, TableParameterMetaData> parameterMap = new LinkedHashMap<>(parameters.size());
        for (TableParameterMetaData tpmd : parameters) {
            parameterMap.put(tpmd.getParameterName().toUpperCase(), tpmd);
        }
        int typeIndx = 0;
        for (String column : getTableColumns()) {
            if (column == null) {
                types[typeIndx] = Integer.MIN_VALUE;
            } else {
                TableParameterMetaData tpmd2 = parameterMap.get(column.toUpperCase());
                if (tpmd2 != null) {
                    types[typeIndx] = tpmd2.getSqlType();
                } else {
                    types[typeIndx] = Integer.MIN_VALUE;
                }
            }
            typeIndx++;
        }
        return types;
    }

    public boolean isGetGeneratedKeysSupported() {
        return this.metaDataProvider.isGetGeneratedKeysSupported();
    }

    public boolean isGetGeneratedKeysSimulated() {
        return this.metaDataProvider.isGetGeneratedKeysSimulated();
    }

    @Deprecated
    public String getSimulationQueryForGetGeneratedKey(String tableName, String keyColumnName) {
        return getSimpleQueryForGetGeneratedKey(tableName, keyColumnName);
    }

    public String getSimpleQueryForGetGeneratedKey(String tableName, String keyColumnName) {
        return this.metaDataProvider.getSimpleQueryForGetGeneratedKey(tableName, keyColumnName);
    }

    public boolean isGeneratedKeysColumnNameArraySupported() {
        return this.metaDataProvider.isGeneratedKeysColumnNameArraySupported();
    }
}

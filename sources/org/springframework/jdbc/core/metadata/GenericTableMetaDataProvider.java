package org.springframework.jdbc.core.metadata;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/GenericTableMetaDataProvider.class */
public class GenericTableMetaDataProvider implements TableMetaDataProvider {
    protected static final Log logger = LogFactory.getLog(TableMetaDataProvider.class);
    private String databaseVersion;
    private String userName;
    private NativeJdbcExtractor nativeJdbcExtractor;
    private boolean tableColumnMetaDataUsed = false;
    private boolean storesUpperCaseIdentifiers = true;
    private boolean storesLowerCaseIdentifiers = false;
    private boolean getGeneratedKeysSupported = true;
    private boolean generatedKeysColumnNameArraySupported = true;
    private List<String> productsNotSupportingGeneratedKeysColumnNameArray = Arrays.asList("Apache Derby", "HSQL Database Engine");
    private List<TableParameterMetaData> tableParameterMetaData = new ArrayList();

    protected GenericTableMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        this.userName = databaseMetaData.getUserName();
    }

    public void setStoresUpperCaseIdentifiers(boolean storesUpperCaseIdentifiers) {
        this.storesUpperCaseIdentifiers = storesUpperCaseIdentifiers;
    }

    public boolean isStoresUpperCaseIdentifiers() {
        return this.storesUpperCaseIdentifiers;
    }

    public void setStoresLowerCaseIdentifiers(boolean storesLowerCaseIdentifiers) {
        this.storesLowerCaseIdentifiers = storesLowerCaseIdentifiers;
    }

    public boolean isStoresLowerCaseIdentifiers() {
        return this.storesLowerCaseIdentifiers;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public boolean isTableColumnMetaDataUsed() {
        return this.tableColumnMetaDataUsed;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public List<TableParameterMetaData> getTableParameterMetaData() {
        return this.tableParameterMetaData;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public boolean isGetGeneratedKeysSupported() {
        return this.getGeneratedKeysSupported;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public boolean isGetGeneratedKeysSimulated() {
        return false;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public String getSimpleQueryForGetGeneratedKey(String tableName, String keyColumnName) {
        return null;
    }

    public void setGetGeneratedKeysSupported(boolean getGeneratedKeysSupported) {
        this.getGeneratedKeysSupported = getGeneratedKeysSupported;
    }

    public void setGeneratedKeysColumnNameArraySupported(boolean generatedKeysColumnNameArraySupported) {
        this.generatedKeysColumnNameArraySupported = generatedKeysColumnNameArraySupported;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public boolean isGeneratedKeysColumnNameArraySupported() {
        return this.generatedKeysColumnNameArraySupported;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor) {
        this.nativeJdbcExtractor = nativeJdbcExtractor;
    }

    protected NativeJdbcExtractor getNativeJdbcExtractor() {
        return this.nativeJdbcExtractor;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
        try {
            if (databaseMetaData.supportsGetGeneratedKeys()) {
                logger.debug("GetGeneratedKeys is supported");
                setGetGeneratedKeysSupported(true);
            } else {
                logger.debug("GetGeneratedKeys is not supported");
                setGetGeneratedKeysSupported(false);
            }
        } catch (SQLException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.getGeneratedKeys': " + ex.getMessage());
            }
        }
        try {
            String databaseProductName = databaseMetaData.getDatabaseProductName();
            if (this.productsNotSupportingGeneratedKeysColumnNameArray.contains(databaseProductName)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("GeneratedKeysColumnNameArray is not supported for " + databaseProductName);
                }
                setGeneratedKeysColumnNameArraySupported(false);
            } else if (isGetGeneratedKeysSupported()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("GeneratedKeysColumnNameArray is supported for " + databaseProductName);
                }
                setGeneratedKeysColumnNameArraySupported(true);
            } else {
                setGeneratedKeysColumnNameArraySupported(false);
            }
        } catch (SQLException ex2) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.getDatabaseProductName': " + ex2.getMessage());
            }
        }
        try {
            this.databaseVersion = databaseMetaData.getDatabaseProductVersion();
        } catch (SQLException ex3) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.getDatabaseProductVersion': " + ex3.getMessage());
            }
        }
        try {
            setStoresUpperCaseIdentifiers(databaseMetaData.storesUpperCaseIdentifiers());
        } catch (SQLException ex4) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.storesUpperCaseIdentifiers': " + ex4.getMessage());
            }
        }
        try {
            setStoresLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
        } catch (SQLException ex5) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.storesLowerCaseIdentifiers': " + ex5.getMessage());
            }
        }
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public void initializeWithTableColumnMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String tableName) throws SQLException {
        this.tableColumnMetaDataUsed = true;
        locateTableAndProcessMetaData(databaseMetaData, catalogName, schemaName, tableName);
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public String tableNameToUse(String tableName) {
        if (tableName == null) {
            return null;
        }
        if (isStoresUpperCaseIdentifiers()) {
            return tableName.toUpperCase();
        }
        if (isStoresLowerCaseIdentifiers()) {
            return tableName.toLowerCase();
        }
        return tableName;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public String catalogNameToUse(String catalogName) {
        if (catalogName == null) {
            return null;
        }
        if (isStoresUpperCaseIdentifiers()) {
            return catalogName.toUpperCase();
        }
        if (isStoresLowerCaseIdentifiers()) {
            return catalogName.toLowerCase();
        }
        return catalogName;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public String schemaNameToUse(String schemaName) {
        if (schemaName == null) {
            return null;
        }
        if (isStoresUpperCaseIdentifiers()) {
            return schemaName.toUpperCase();
        }
        if (isStoresLowerCaseIdentifiers()) {
            return schemaName.toLowerCase();
        }
        return schemaName;
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public String metaDataCatalogNameToUse(String catalogName) {
        return catalogNameToUse(catalogName);
    }

    @Override // org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public String metaDataSchemaNameToUse(String schemaName) {
        if (schemaName == null) {
            return schemaNameToUse(getDefaultSchema());
        }
        return schemaNameToUse(schemaName);
    }

    protected String getDefaultSchema() {
        return this.userName;
    }

    protected String getDatabaseVersion() {
        return this.databaseVersion;
    }

    private void locateTableAndProcessMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String tableName) {
        Map<String, TableMetaData> tableMeta = new HashMap<>();
        ResultSet tables = null;
        try {
            try {
                tables = databaseMetaData.getTables(catalogNameToUse(catalogName), schemaNameToUse(schemaName), tableNameToUse(tableName), null);
                while (tables != null) {
                    if (!tables.next()) {
                        break;
                    }
                    TableMetaData tmd = new TableMetaData();
                    tmd.setCatalogName(tables.getString("TABLE_CAT"));
                    tmd.setSchemaName(tables.getString("TABLE_SCHEM"));
                    tmd.setTableName(tables.getString("TABLE_NAME"));
                    if (tmd.getSchemaName() == null) {
                        tableMeta.put(this.userName != null ? this.userName.toUpperCase() : "", tmd);
                    } else {
                        tableMeta.put(tmd.getSchemaName().toUpperCase(), tmd);
                    }
                }
                JdbcUtils.closeResultSet(tables);
            } catch (SQLException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Error while accessing table meta-data results: " + ex.getMessage());
                }
                JdbcUtils.closeResultSet(tables);
            }
            if (tableMeta.isEmpty()) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Unable to locate table meta-data for '" + tableName + "': column names must be provided");
                    return;
                }
                return;
            }
            processTableColumns(databaseMetaData, findTableMetaData(schemaName, tableName, tableMeta));
        } catch (Throwable th) {
            JdbcUtils.closeResultSet(tables);
            throw th;
        }
    }

    private TableMetaData findTableMetaData(String schemaName, String tableName, Map<String, TableMetaData> tableMeta) {
        if (schemaName != null) {
            TableMetaData tmd = tableMeta.get(schemaName.toUpperCase());
            if (tmd == null) {
                throw new DataAccessResourceFailureException("Unable to locate table meta-data for '" + tableName + "' in the '" + schemaName + "' schema");
            }
            return tmd;
        }
        if (tableMeta.size() == 1) {
            return tableMeta.values().iterator().next();
        }
        TableMetaData tmd2 = tableMeta.get(getDefaultSchema());
        if (tmd2 == null) {
            tmd2 = tableMeta.get(this.userName != null ? this.userName.toUpperCase() : "");
        }
        if (tmd2 == null) {
            tmd2 = tableMeta.get("PUBLIC");
        }
        if (tmd2 == null) {
            tmd2 = tableMeta.get("DBO");
        }
        if (tmd2 == null) {
            throw new DataAccessResourceFailureException("Unable to locate table meta-data for '" + tableName + "' in the default schema");
        }
        return tmd2;
    }

    private void processTableColumns(DatabaseMetaData databaseMetaData, TableMetaData tmd) {
        ResultSet tableColumns = null;
        String metaDataCatalogName = metaDataCatalogNameToUse(tmd.getCatalogName());
        String metaDataSchemaName = metaDataSchemaNameToUse(tmd.getSchemaName());
        String metaDataTableName = tableNameToUse(tmd.getTableName());
        if (logger.isDebugEnabled()) {
            logger.debug("Retrieving meta-data for " + metaDataCatalogName + '/' + metaDataSchemaName + '/' + metaDataTableName);
        }
        try {
            try {
                tableColumns = databaseMetaData.getColumns(metaDataCatalogName, metaDataSchemaName, metaDataTableName, null);
                while (tableColumns.next()) {
                    String columnName = tableColumns.getString("COLUMN_NAME");
                    int dataType = tableColumns.getInt("DATA_TYPE");
                    if (dataType == 3) {
                        String typeName = tableColumns.getString("TYPE_NAME");
                        int decimalDigits = tableColumns.getInt("DECIMAL_DIGITS");
                        if ("NUMBER".equals(typeName) && decimalDigits == 0) {
                            dataType = 2;
                            if (logger.isDebugEnabled()) {
                                logger.debug("Overriding meta-data: " + columnName + " now NUMERIC instead of DECIMAL");
                            }
                        }
                    }
                    boolean nullable = tableColumns.getBoolean("NULLABLE");
                    TableParameterMetaData meta = new TableParameterMetaData(columnName, dataType, nullable);
                    this.tableParameterMetaData.add(meta);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Retrieved meta-data: " + meta.getParameterName() + SymbolConstants.SPACE_SYMBOL + meta.getSqlType() + SymbolConstants.SPACE_SYMBOL + meta.isNullable());
                    }
                }
                JdbcUtils.closeResultSet(tableColumns);
            } catch (SQLException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Error while retrieving meta-data for table columns: " + ex.getMessage());
                }
                JdbcUtils.closeResultSet(tableColumns);
            }
        } catch (Throwable th) {
            JdbcUtils.closeResultSet(tableColumns);
            throw th;
        }
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/GenericTableMetaDataProvider$TableMetaData.class */
    private static class TableMetaData {
        private String catalogName;
        private String schemaName;
        private String tableName;

        private TableMetaData() {
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

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return this.tableName;
        }
    }
}

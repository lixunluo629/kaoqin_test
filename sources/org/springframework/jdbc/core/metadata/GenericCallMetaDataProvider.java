package org.springframework.jdbc.core.metadata;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/GenericCallMetaDataProvider.class */
public class GenericCallMetaDataProvider implements CallMetaDataProvider {
    protected static final Log logger = LogFactory.getLog(CallMetaDataProvider.class);
    private String userName;
    private boolean procedureColumnMetaDataUsed = false;
    private boolean supportsCatalogsInProcedureCalls = true;
    private boolean supportsSchemasInProcedureCalls = true;
    private boolean storesUpperCaseIdentifiers = true;
    private boolean storesLowerCaseIdentifiers = false;
    private List<CallParameterMetaData> callParameterMetaData = new ArrayList();

    protected GenericCallMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        this.userName = databaseMetaData.getUserName();
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
        try {
            setSupportsCatalogsInProcedureCalls(databaseMetaData.supportsCatalogsInProcedureCalls());
        } catch (SQLException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.supportsCatalogsInProcedureCalls': " + ex.getMessage());
            }
        }
        try {
            setSupportsSchemasInProcedureCalls(databaseMetaData.supportsSchemasInProcedureCalls());
        } catch (SQLException ex2) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.supportsSchemasInProcedureCalls': " + ex2.getMessage());
            }
        }
        try {
            setStoresUpperCaseIdentifiers(databaseMetaData.storesUpperCaseIdentifiers());
        } catch (SQLException ex3) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.storesUpperCaseIdentifiers': " + ex3.getMessage());
            }
        }
        try {
            setStoresLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
        } catch (SQLException ex4) {
            if (logger.isWarnEnabled()) {
                logger.warn("Error retrieving 'DatabaseMetaData.storesLowerCaseIdentifiers': " + ex4.getMessage());
            }
        }
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public void initializeWithProcedureColumnMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String procedureName) throws SQLException {
        this.procedureColumnMetaDataUsed = true;
        processProcedureColumns(databaseMetaData, catalogName, schemaName, procedureName);
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public List<CallParameterMetaData> getCallParameterMetaData() {
        return this.callParameterMetaData;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String procedureNameToUse(String procedureName) {
        if (procedureName == null) {
            return null;
        }
        if (isStoresUpperCaseIdentifiers()) {
            return procedureName.toUpperCase();
        }
        if (isStoresLowerCaseIdentifiers()) {
            return procedureName.toLowerCase();
        }
        return procedureName;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
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

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
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

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String metaDataCatalogNameToUse(String catalogName) {
        if (isSupportsCatalogsInProcedureCalls()) {
            return catalogNameToUse(catalogName);
        }
        return null;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String metaDataSchemaNameToUse(String schemaName) {
        if (isSupportsSchemasInProcedureCalls()) {
            return schemaNameToUse(schemaName);
        }
        return null;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String parameterNameToUse(String parameterName) {
        if (parameterName == null) {
            return null;
        }
        if (isStoresUpperCaseIdentifiers()) {
            return parameterName.toUpperCase();
        }
        if (isStoresLowerCaseIdentifiers()) {
            return parameterName.toLowerCase();
        }
        return parameterName;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean byPassReturnParameter(String parameterName) {
        return false;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public SqlParameter createDefaultOutParameter(String parameterName, CallParameterMetaData meta) {
        return new SqlOutParameter(parameterName, meta.getSqlType());
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public SqlParameter createDefaultInOutParameter(String parameterName, CallParameterMetaData meta) {
        return new SqlInOutParameter(parameterName, meta.getSqlType());
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public SqlParameter createDefaultInParameter(String parameterName, CallParameterMetaData meta) {
        return new SqlParameter(parameterName, meta.getSqlType());
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String getUserName() {
        return this.userName;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean isReturnResultSetSupported() {
        return true;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean isRefCursorSupported() {
        return false;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public int getRefCursorSqlType() {
        return MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean isProcedureColumnMetaDataUsed() {
        return this.procedureColumnMetaDataUsed;
    }

    protected void setSupportsCatalogsInProcedureCalls(boolean supportsCatalogsInProcedureCalls) {
        this.supportsCatalogsInProcedureCalls = supportsCatalogsInProcedureCalls;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean isSupportsCatalogsInProcedureCalls() {
        return this.supportsCatalogsInProcedureCalls;
    }

    protected void setSupportsSchemasInProcedureCalls(boolean supportsSchemasInProcedureCalls) {
        this.supportsSchemasInProcedureCalls = supportsSchemasInProcedureCalls;
    }

    @Override // org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean isSupportsSchemasInProcedureCalls() {
        return this.supportsSchemasInProcedureCalls;
    }

    protected void setStoresUpperCaseIdentifiers(boolean storesUpperCaseIdentifiers) {
        this.storesUpperCaseIdentifiers = storesUpperCaseIdentifiers;
    }

    protected boolean isStoresUpperCaseIdentifiers() {
        return this.storesUpperCaseIdentifiers;
    }

    protected void setStoresLowerCaseIdentifiers(boolean storesLowerCaseIdentifiers) {
        this.storesLowerCaseIdentifiers = storesLowerCaseIdentifiers;
    }

    protected boolean isStoresLowerCaseIdentifiers() {
        return this.storesLowerCaseIdentifiers;
    }

    private void processProcedureColumns(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String procedureName) throws SQLException {
        String metaDataCatalogName = metaDataCatalogNameToUse(catalogName);
        String metaDataSchemaName = metaDataSchemaNameToUse(schemaName);
        String metaDataProcedureName = procedureNameToUse(procedureName);
        if (logger.isDebugEnabled()) {
            logger.debug("Retrieving meta-data for " + metaDataCatalogName + '/' + metaDataSchemaName + '/' + metaDataProcedureName);
        }
        ResultSet procs = null;
        try {
            try {
                ResultSet procs2 = databaseMetaData.getProcedures(metaDataCatalogName, metaDataSchemaName, metaDataProcedureName);
                List<String> found = new ArrayList<>();
                while (procs2.next()) {
                    found.add(procs2.getString("PROCEDURE_CAT") + '.' + procs2.getString("PROCEDURE_SCHEM") + '.' + procs2.getString("PROCEDURE_NAME"));
                }
                procs2.close();
                if (found.size() > 1) {
                    throw new InvalidDataAccessApiUsageException("Unable to determine the correct call signature - multiple procedures/functions/signatures for '" + metaDataProcedureName + "': found " + found);
                }
                if (found.isEmpty()) {
                    if (metaDataProcedureName.contains(".") && !StringUtils.hasText(metaDataCatalogName)) {
                        String packageName = metaDataProcedureName.substring(0, metaDataProcedureName.indexOf(46));
                        throw new InvalidDataAccessApiUsageException("Unable to determine the correct call signature for '" + metaDataProcedureName + "' - package name should be specified separately using '.withCatalogName(\"" + packageName + "\")'");
                    }
                    if ("Oracle".equals(databaseMetaData.getDatabaseProductName())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Oracle JDBC driver did not return procedure/function/signature for '" + metaDataProcedureName + "' - assuming a non-exposed synonym");
                        }
                    } else {
                        throw new InvalidDataAccessApiUsageException("Unable to determine the correct call signature - no procedure/function/signature for '" + metaDataProcedureName + "'");
                    }
                }
                ResultSet procs3 = databaseMetaData.getProcedureColumns(metaDataCatalogName, metaDataSchemaName, metaDataProcedureName, null);
                while (procs3.next()) {
                    String columnName = procs3.getString("COLUMN_NAME");
                    int columnType = procs3.getInt("COLUMN_TYPE");
                    if (columnName == null && (columnType == 1 || columnType == 2 || columnType == 4)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Skipping meta-data for: " + columnType + SymbolConstants.SPACE_SYMBOL + procs3.getInt("DATA_TYPE") + SymbolConstants.SPACE_SYMBOL + procs3.getString("TYPE_NAME") + SymbolConstants.SPACE_SYMBOL + procs3.getInt("NULLABLE") + " (probably a member of a collection)");
                        }
                    } else {
                        CallParameterMetaData meta = new CallParameterMetaData(columnName, columnType, procs3.getInt("DATA_TYPE"), procs3.getString("TYPE_NAME"), procs3.getInt("NULLABLE") == 1);
                        this.callParameterMetaData.add(meta);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Retrieved meta-data: " + meta.getParameterName() + SymbolConstants.SPACE_SYMBOL + meta.getParameterType() + SymbolConstants.SPACE_SYMBOL + meta.getSqlType() + SymbolConstants.SPACE_SYMBOL + meta.getTypeName() + SymbolConstants.SPACE_SYMBOL + meta.isNullable());
                        }
                    }
                }
                if (procs3 != null) {
                    try {
                        procs3.close();
                    } catch (SQLException ex) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Problem closing ResultSet for procedure column meta-data: " + ex);
                        }
                    }
                }
            } catch (SQLException ex2) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Error while retrieving meta-data for procedure columns: " + ex2);
                }
                if (0 != 0) {
                    try {
                        procs.close();
                    } catch (SQLException ex3) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Problem closing ResultSet for procedure column meta-data: " + ex3);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    procs.close();
                } catch (SQLException ex4) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Problem closing ResultSet for procedure column meta-data: " + ex4);
                    }
                    throw th;
                }
            }
            throw th;
        }
    }
}

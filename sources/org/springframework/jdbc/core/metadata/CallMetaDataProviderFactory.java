package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/CallMetaDataProviderFactory.class */
public class CallMetaDataProviderFactory {
    public static final List<String> supportedDatabaseProductsForProcedures = Arrays.asList("Apache Derby", "DB2", "MySQL", "Microsoft SQL Server", "Oracle", "PostgreSQL", "Sybase");
    public static final List<String> supportedDatabaseProductsForFunctions = Arrays.asList("MySQL", "Microsoft SQL Server", "Oracle", "PostgreSQL");
    private static final Log logger = LogFactory.getLog(CallMetaDataProviderFactory.class);

    public static CallMetaDataProvider createMetaDataProvider(DataSource dataSource, final CallMetaDataContext context) {
        try {
            return (CallMetaDataProvider) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() { // from class: org.springframework.jdbc.core.metadata.CallMetaDataProviderFactory.1
                @Override // org.springframework.jdbc.support.DatabaseMetaDataCallback
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
                    CallMetaDataProvider provider;
                    String databaseProductName = JdbcUtils.commonDatabaseName(databaseMetaData.getDatabaseProductName());
                    boolean accessProcedureColumnMetaData = context.isAccessCallParameterMetaData();
                    if (context.isFunction()) {
                        if (!CallMetaDataProviderFactory.supportedDatabaseProductsForFunctions.contains(databaseProductName)) {
                            if (CallMetaDataProviderFactory.logger.isWarnEnabled()) {
                                CallMetaDataProviderFactory.logger.warn(databaseProductName + " is not one of the databases fully supported for function calls -- supported are: " + CallMetaDataProviderFactory.supportedDatabaseProductsForFunctions);
                            }
                            if (accessProcedureColumnMetaData) {
                                CallMetaDataProviderFactory.logger.warn("Metadata processing disabled - you must specify all parameters explicitly");
                                accessProcedureColumnMetaData = false;
                            }
                        }
                    } else if (!CallMetaDataProviderFactory.supportedDatabaseProductsForProcedures.contains(databaseProductName)) {
                        if (CallMetaDataProviderFactory.logger.isWarnEnabled()) {
                            CallMetaDataProviderFactory.logger.warn(databaseProductName + " is not one of the databases fully supported for procedure calls -- supported are: " + CallMetaDataProviderFactory.supportedDatabaseProductsForProcedures);
                        }
                        if (accessProcedureColumnMetaData) {
                            CallMetaDataProviderFactory.logger.warn("Metadata processing disabled - you must specify all parameters explicitly");
                            accessProcedureColumnMetaData = false;
                        }
                    }
                    if ("Oracle".equals(databaseProductName)) {
                        provider = new OracleCallMetaDataProvider(databaseMetaData);
                    } else if ("PostgreSQL".equals(databaseProductName)) {
                        provider = new PostgresCallMetaDataProvider(databaseMetaData);
                    } else if ("Apache Derby".equals(databaseProductName)) {
                        provider = new DerbyCallMetaDataProvider(databaseMetaData);
                    } else if ("DB2".equals(databaseProductName)) {
                        provider = new Db2CallMetaDataProvider(databaseMetaData);
                    } else if ("HDB".equals(databaseProductName)) {
                        provider = new HanaCallMetaDataProvider(databaseMetaData);
                    } else if ("Microsoft SQL Server".equals(databaseProductName)) {
                        provider = new SqlServerCallMetaDataProvider(databaseMetaData);
                    } else if ("Sybase".equals(databaseProductName)) {
                        provider = new SybaseCallMetaDataProvider(databaseMetaData);
                    } else {
                        provider = new GenericCallMetaDataProvider(databaseMetaData);
                    }
                    if (CallMetaDataProviderFactory.logger.isDebugEnabled()) {
                        CallMetaDataProviderFactory.logger.debug("Using " + provider.getClass().getName());
                    }
                    provider.initializeWithMetaData(databaseMetaData);
                    if (accessProcedureColumnMetaData) {
                        provider.initializeWithProcedureColumnMetaData(databaseMetaData, context.getCatalogName(), context.getSchemaName(), context.getProcedureName());
                    }
                    return provider;
                }
            });
        } catch (MetaDataAccessException ex) {
            throw new DataAccessResourceFailureException("Error retrieving database meta-data", ex);
        }
    }
}

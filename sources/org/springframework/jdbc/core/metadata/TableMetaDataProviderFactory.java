package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/TableMetaDataProviderFactory.class */
public class TableMetaDataProviderFactory {
    private static final Log logger = LogFactory.getLog(TableMetaDataProviderFactory.class);

    public static TableMetaDataProvider createMetaDataProvider(DataSource dataSource, TableMetaDataContext context) {
        return createMetaDataProvider(dataSource, context, null);
    }

    public static TableMetaDataProvider createMetaDataProvider(DataSource dataSource, final TableMetaDataContext context, final NativeJdbcExtractor nativeJdbcExtractor) {
        try {
            return (TableMetaDataProvider) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() { // from class: org.springframework.jdbc.core.metadata.TableMetaDataProviderFactory.1
                @Override // org.springframework.jdbc.support.DatabaseMetaDataCallback
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
                    TableMetaDataProvider provider;
                    String databaseProductName = JdbcUtils.commonDatabaseName(databaseMetaData.getDatabaseProductName());
                    boolean accessTableColumnMetaData = context.isAccessTableColumnMetaData();
                    if ("Oracle".equals(databaseProductName)) {
                        provider = new OracleTableMetaDataProvider(databaseMetaData, context.isOverrideIncludeSynonymsDefault());
                    } else if ("PostgreSQL".equals(databaseProductName)) {
                        provider = new PostgresTableMetaDataProvider(databaseMetaData);
                    } else if ("Apache Derby".equals(databaseProductName)) {
                        provider = new DerbyTableMetaDataProvider(databaseMetaData);
                    } else if ("HSQL Database Engine".equals(databaseProductName)) {
                        provider = new HsqlTableMetaDataProvider(databaseMetaData);
                    } else {
                        provider = new GenericTableMetaDataProvider(databaseMetaData);
                    }
                    if (nativeJdbcExtractor != null) {
                        provider.setNativeJdbcExtractor(nativeJdbcExtractor);
                    }
                    if (TableMetaDataProviderFactory.logger.isDebugEnabled()) {
                        TableMetaDataProviderFactory.logger.debug("Using " + provider.getClass().getSimpleName());
                    }
                    provider.initializeWithMetaData(databaseMetaData);
                    if (accessTableColumnMetaData) {
                        provider.initializeWithTableColumnMetaData(databaseMetaData, context.getCatalogName(), context.getSchemaName(), context.getTableName());
                    }
                    return provider;
                }
            });
        } catch (MetaDataAccessException ex) {
            throw new DataAccessResourceFailureException("Error retrieving database meta-data", ex);
        }
    }
}

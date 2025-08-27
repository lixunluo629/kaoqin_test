package org.springframework.jdbc.core.metadata;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/OracleTableMetaDataProvider.class */
public class OracleTableMetaDataProvider extends GenericTableMetaDataProvider {
    private final boolean includeSynonyms;
    private String defaultSchema;

    public OracleTableMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        this(databaseMetaData, false);
    }

    public OracleTableMetaDataProvider(DatabaseMetaData databaseMetaData, boolean includeSynonyms) throws SQLException {
        super(databaseMetaData);
        this.includeSynonyms = includeSynonyms;
        this.defaultSchema = lookupDefaultSchema(databaseMetaData);
    }

    private static String lookupDefaultSchema(DatabaseMetaData databaseMetaData) {
        CallableStatement cstmt = null;
        try {
            try {
                Connection con = databaseMetaData.getConnection();
                if (con == null) {
                    logger.debug("Cannot check default schema - no Connection from DatabaseMetaData");
                    if (0 != 0) {
                        cstmt.close();
                    }
                    return null;
                }
                CallableStatement cstmt2 = con.prepareCall("{? = call sys_context('USERENV', 'CURRENT_SCHEMA')}");
                cstmt2.registerOutParameter(1, 12);
                cstmt2.execute();
                String string = cstmt2.getString(1);
                if (cstmt2 != null) {
                    cstmt2.close();
                }
                return string;
            } catch (Throwable th) {
                if (0 != 0) {
                    cstmt.close();
                }
                throw th;
            }
        } catch (SQLException ex) {
            logger.debug("Exception encountered during default schema lookup", ex);
            return null;
        }
    }

    @Override // org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider
    protected String getDefaultSchema() {
        if (this.defaultSchema != null) {
            return this.defaultSchema;
        }
        return super.getDefaultSchema();
    }

    @Override // org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider, org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public void initializeWithTableColumnMetaData(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, String tableName) throws SQLException, ClassNotFoundException {
        if (!this.includeSynonyms) {
            logger.debug("Defaulting to no synonyms in table meta-data lookup");
            super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
            return;
        }
        Connection con = databaseMetaData.getConnection();
        if (con == null) {
            logger.warn("Unable to include synonyms in table meta-data lookup - no Connection from DatabaseMetaData");
            super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
            return;
        }
        NativeJdbcExtractor nativeJdbcExtractor = getNativeJdbcExtractor();
        if (nativeJdbcExtractor != null) {
            con = nativeJdbcExtractor.getNativeConnection(con);
        }
        boolean isOracleCon = false;
        try {
            Class<?> oracleConClass = con.getClass().getClassLoader().loadClass("oracle.jdbc.OracleConnection");
            isOracleCon = oracleConClass.isInstance(con);
            if (!isOracleCon) {
                con = (Connection) con.unwrap(oracleConClass);
                isOracleCon = oracleConClass.isInstance(con);
            }
        } catch (ClassNotFoundException ex) {
            if (logger.isInfoEnabled()) {
                logger.info("Could not find Oracle JDBC API: " + ex);
            }
        } catch (SQLException e) {
        }
        if (!isOracleCon) {
            if (logger.isWarnEnabled()) {
                logger.warn("Unable to include synonyms in table meta-data lookup - no Oracle Connection: " + con);
            }
            super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
            return;
        }
        logger.debug("Including synonyms in table meta-data lookup");
        try {
            Method getIncludeSynonyms = con.getClass().getMethod("getIncludeSynonyms", new Class[0]);
            ReflectionUtils.makeAccessible(getIncludeSynonyms);
            Boolean originalValueForIncludeSynonyms = (Boolean) getIncludeSynonyms.invoke(con, new Object[0]);
            Method setIncludeSynonyms = con.getClass().getMethod("setIncludeSynonyms", Boolean.TYPE);
            ReflectionUtils.makeAccessible(setIncludeSynonyms);
            setIncludeSynonyms.invoke(con, Boolean.TRUE);
            super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
            try {
                setIncludeSynonyms.invoke(con, originalValueForIncludeSynonyms);
            } catch (Throwable ex2) {
                throw new InvalidDataAccessApiUsageException("Could not reset Oracle Connection", ex2);
            }
        } catch (Throwable ex3) {
            throw new InvalidDataAccessApiUsageException("Could not prepare Oracle Connection", ex3);
        }
    }
}

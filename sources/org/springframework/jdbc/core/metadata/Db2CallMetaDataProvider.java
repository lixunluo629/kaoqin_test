package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/Db2CallMetaDataProvider.class */
public class Db2CallMetaDataProvider extends GenericCallMetaDataProvider {
    public Db2CallMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
        try {
            setSupportsCatalogsInProcedureCalls(databaseMetaData.supportsCatalogsInProcedureCalls());
        } catch (SQLException ex) {
            logger.debug("Error retrieving 'DatabaseMetaData.supportsCatalogsInProcedureCalls' - " + ex.getMessage());
        }
        try {
            setSupportsSchemasInProcedureCalls(databaseMetaData.supportsSchemasInProcedureCalls());
        } catch (SQLException ex2) {
            logger.debug("Error retrieving 'DatabaseMetaData.supportsSchemasInProcedureCalls' - " + ex2.getMessage());
        }
        try {
            setStoresUpperCaseIdentifiers(databaseMetaData.storesUpperCaseIdentifiers());
        } catch (SQLException ex3) {
            logger.debug("Error retrieving 'DatabaseMetaData.storesUpperCaseIdentifiers' - " + ex3.getMessage());
        }
        try {
            setStoresLowerCaseIdentifiers(databaseMetaData.storesLowerCaseIdentifiers());
        } catch (SQLException ex4) {
            logger.debug("Error retrieving 'DatabaseMetaData.storesLowerCaseIdentifiers' - " + ex4.getMessage());
        }
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String metaDataSchemaNameToUse(String schemaName) {
        if (schemaName != null) {
            return super.metaDataSchemaNameToUse(schemaName);
        }
        String userName = getUserName();
        if (userName != null) {
            return userName.toUpperCase();
        }
        return null;
    }
}

package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/DerbyCallMetaDataProvider.class */
public class DerbyCallMetaDataProvider extends GenericCallMetaDataProvider {
    public DerbyCallMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData);
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

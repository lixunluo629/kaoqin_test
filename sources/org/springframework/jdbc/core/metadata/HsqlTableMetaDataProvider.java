package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/HsqlTableMetaDataProvider.class */
public class HsqlTableMetaDataProvider extends GenericTableMetaDataProvider {
    public HsqlTableMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider, org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public boolean isGetGeneratedKeysSimulated() {
        return true;
    }

    @Override // org.springframework.jdbc.core.metadata.GenericTableMetaDataProvider, org.springframework.jdbc.core.metadata.TableMetaDataProvider
    public String getSimpleQueryForGetGeneratedKey(String tableName, String keyColumnName) {
        return "select max(identity()) from " + tableName;
    }
}

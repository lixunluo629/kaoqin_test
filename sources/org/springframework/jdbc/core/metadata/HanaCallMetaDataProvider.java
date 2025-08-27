package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/HanaCallMetaDataProvider.class */
public class HanaCallMetaDataProvider extends GenericCallMetaDataProvider {
    public HanaCallMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
        super.initializeWithMetaData(databaseMetaData);
        setStoresUpperCaseIdentifiers(false);
    }
}

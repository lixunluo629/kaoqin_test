package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/SybaseCallMetaDataProvider.class */
public class SybaseCallMetaDataProvider extends GenericCallMetaDataProvider {
    private static final String REMOVABLE_COLUMN_PREFIX = "@";
    private static final String RETURN_VALUE_NAME = "RETURN_VALUE";

    public SybaseCallMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String parameterNameToUse(String parameterName) {
        if (parameterName == null) {
            return null;
        }
        if (parameterName.length() > 1 && parameterName.startsWith(REMOVABLE_COLUMN_PREFIX)) {
            return super.parameterNameToUse(parameterName.substring(1));
        }
        return super.parameterNameToUse(parameterName);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean byPassReturnParameter(String parameterName) {
        return RETURN_VALUE_NAME.equals(parameterName) || RETURN_VALUE_NAME.equals(parameterNameToUse(parameterName)) || super.byPassReturnParameter(parameterName);
    }
}

package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/OracleCallMetaDataProvider.class */
public class OracleCallMetaDataProvider extends GenericCallMetaDataProvider {
    private static final String REF_CURSOR_NAME = "REF CURSOR";

    public OracleCallMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean isReturnResultSetSupported() {
        return false;
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean isRefCursorSupported() {
        return true;
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public int getRefCursorSqlType() {
        return -10;
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String metaDataCatalogNameToUse(String catalogName) {
        return catalogName == null ? "" : catalogNameToUse(catalogName);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String metaDataSchemaNameToUse(String schemaName) {
        return schemaName == null ? getUserName() : super.metaDataSchemaNameToUse(schemaName);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public SqlParameter createDefaultOutParameter(String parameterName, CallParameterMetaData meta) {
        if (meta.getSqlType() == 1111 && REF_CURSOR_NAME.equals(meta.getTypeName())) {
            return new SqlOutParameter(parameterName, getRefCursorSqlType(), new ColumnMapRowMapper());
        }
        return super.createDefaultOutParameter(parameterName, meta);
    }
}

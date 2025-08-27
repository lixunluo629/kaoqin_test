package org.springframework.jdbc.core.metadata;

import com.mysql.jdbc.MysqlErrorNumbers;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/PostgresCallMetaDataProvider.class */
public class PostgresCallMetaDataProvider extends GenericCallMetaDataProvider {
    private static final String RETURN_VALUE_NAME = "returnValue";

    public PostgresCallMetaDataProvider(DatabaseMetaData databaseMetaData) throws SQLException {
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
        return MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE;
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public String metaDataSchemaNameToUse(String schemaName) {
        return schemaName == null ? "public" : super.metaDataSchemaNameToUse(schemaName);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public SqlParameter createDefaultOutParameter(String parameterName, CallParameterMetaData meta) {
        if (meta.getSqlType() == 1111 && "refcursor".equals(meta.getTypeName())) {
            return new SqlOutParameter(parameterName, getRefCursorSqlType(), new ColumnMapRowMapper());
        }
        return super.createDefaultOutParameter(parameterName, meta);
    }

    @Override // org.springframework.jdbc.core.metadata.GenericCallMetaDataProvider, org.springframework.jdbc.core.metadata.CallMetaDataProvider
    public boolean byPassReturnParameter(String parameterName) {
        return RETURN_VALUE_NAME.equals(parameterName);
    }
}

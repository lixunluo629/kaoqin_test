package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.SqlParameter;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/CallMetaDataProvider.class */
public interface CallMetaDataProvider {
    void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException;

    void initializeWithProcedureColumnMetaData(DatabaseMetaData databaseMetaData, String str, String str2, String str3) throws SQLException;

    String procedureNameToUse(String str);

    String catalogNameToUse(String str);

    String schemaNameToUse(String str);

    String metaDataCatalogNameToUse(String str);

    String metaDataSchemaNameToUse(String str);

    String parameterNameToUse(String str);

    SqlParameter createDefaultOutParameter(String str, CallParameterMetaData callParameterMetaData);

    SqlParameter createDefaultInOutParameter(String str, CallParameterMetaData callParameterMetaData);

    SqlParameter createDefaultInParameter(String str, CallParameterMetaData callParameterMetaData);

    String getUserName();

    boolean isReturnResultSetSupported();

    boolean isRefCursorSupported();

    int getRefCursorSqlType();

    boolean isProcedureColumnMetaDataUsed();

    boolean byPassReturnParameter(String str);

    List<CallParameterMetaData> getCallParameterMetaData();

    boolean isSupportsCatalogsInProcedureCalls();

    boolean isSupportsSchemasInProcedureCalls();
}

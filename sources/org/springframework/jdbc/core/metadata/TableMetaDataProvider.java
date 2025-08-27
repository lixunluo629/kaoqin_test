package org.springframework.jdbc.core.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/metadata/TableMetaDataProvider.class */
public interface TableMetaDataProvider {
    void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException;

    void initializeWithTableColumnMetaData(DatabaseMetaData databaseMetaData, String str, String str2, String str3) throws SQLException;

    String tableNameToUse(String str);

    String catalogNameToUse(String str);

    String schemaNameToUse(String str);

    String metaDataCatalogNameToUse(String str);

    String metaDataSchemaNameToUse(String str);

    boolean isTableColumnMetaDataUsed();

    boolean isGetGeneratedKeysSupported();

    boolean isGetGeneratedKeysSimulated();

    String getSimpleQueryForGetGeneratedKey(String str, String str2);

    boolean isGeneratedKeysColumnNameArraySupported();

    List<TableParameterMetaData> getTableParameterMetaData();

    void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor);
}

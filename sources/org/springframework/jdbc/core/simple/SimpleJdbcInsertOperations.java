package org.springframework.jdbc.core.simple;

import java.util.Map;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/simple/SimpleJdbcInsertOperations.class */
public interface SimpleJdbcInsertOperations {
    SimpleJdbcInsertOperations withTableName(String str);

    SimpleJdbcInsertOperations withSchemaName(String str);

    SimpleJdbcInsertOperations withCatalogName(String str);

    SimpleJdbcInsertOperations usingColumns(String... strArr);

    SimpleJdbcInsertOperations usingGeneratedKeyColumns(String... strArr);

    SimpleJdbcInsertOperations withoutTableColumnMetaDataAccess();

    SimpleJdbcInsertOperations includeSynonymsForTableColumnMetaData();

    SimpleJdbcInsertOperations useNativeJdbcExtractorForMetaData(NativeJdbcExtractor nativeJdbcExtractor);

    int execute(Map<String, ?> map);

    int execute(SqlParameterSource sqlParameterSource);

    Number executeAndReturnKey(Map<String, ?> map);

    Number executeAndReturnKey(SqlParameterSource sqlParameterSource);

    KeyHolder executeAndReturnKeyHolder(Map<String, ?> map);

    KeyHolder executeAndReturnKeyHolder(SqlParameterSource sqlParameterSource);

    int[] executeBatch(Map<String, ?>... mapArr);

    int[] executeBatch(SqlParameterSource... sqlParameterSourceArr);
}

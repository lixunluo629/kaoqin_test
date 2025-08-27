package org.springframework.jdbc.core.simple;

import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/simple/SimpleJdbcCallOperations.class */
public interface SimpleJdbcCallOperations {
    SimpleJdbcCallOperations withProcedureName(String str);

    SimpleJdbcCallOperations withFunctionName(String str);

    SimpleJdbcCallOperations withSchemaName(String str);

    SimpleJdbcCallOperations withCatalogName(String str);

    SimpleJdbcCallOperations withReturnValue();

    SimpleJdbcCallOperations declareParameters(SqlParameter... sqlParameterArr);

    SimpleJdbcCallOperations useInParameterNames(String... strArr);

    SimpleJdbcCallOperations returningResultSet(String str, RowMapper<?> rowMapper);

    SimpleJdbcCallOperations withoutProcedureColumnMetaDataAccess();

    SimpleJdbcCallOperations withNamedBinding();

    <T> T executeFunction(Class<T> cls, Object... objArr);

    <T> T executeFunction(Class<T> cls, Map<String, ?> map);

    <T> T executeFunction(Class<T> cls, SqlParameterSource sqlParameterSource);

    <T> T executeObject(Class<T> cls, Object... objArr);

    <T> T executeObject(Class<T> cls, Map<String, ?> map);

    <T> T executeObject(Class<T> cls, SqlParameterSource sqlParameterSource);

    Map<String, Object> execute(Object... objArr);

    Map<String, Object> execute(Map<String, ?> map);

    Map<String, Object> execute(SqlParameterSource sqlParameterSource);
}

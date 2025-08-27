package org.springframework.jdbc.core.simple;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/simple/SimpleJdbcCall.class */
public class SimpleJdbcCall extends AbstractJdbcCall implements SimpleJdbcCallOperations {
    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public /* bridge */ /* synthetic */ SimpleJdbcCallOperations returningResultSet(String str, RowMapper rowMapper) {
        return returningResultSet(str, (RowMapper<?>) rowMapper);
    }

    public SimpleJdbcCall(DataSource dataSource) {
        super(dataSource);
    }

    public SimpleJdbcCall(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall withProcedureName(String procedureName) {
        setProcedureName(procedureName);
        setFunction(false);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall withFunctionName(String functionName) {
        setProcedureName(functionName);
        setFunction(true);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall withSchemaName(String schemaName) {
        setSchemaName(schemaName);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall withCatalogName(String catalogName) {
        setCatalogName(catalogName);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall withReturnValue() {
        setReturnValueRequired(true);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall declareParameters(SqlParameter... sqlParameters) {
        for (SqlParameter sqlParameter : sqlParameters) {
            if (sqlParameter != null) {
                addDeclaredParameter(sqlParameter);
            }
        }
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall useInParameterNames(String... inParameterNames) {
        setInParameterNames(new LinkedHashSet(Arrays.asList(inParameterNames)));
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall returningResultSet(String parameterName, RowMapper<?> rowMapper) {
        addDeclaredRowMapper(parameterName, rowMapper);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall withoutProcedureColumnMetaDataAccess() {
        setAccessCallParameterMetaData(false);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public SimpleJdbcCall withNamedBinding() {
        setNamedBinding(true);
        return this;
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public <T> T executeFunction(Class<T> cls, Object... objArr) {
        return (T) doExecute(objArr).get(getScalarOutParameterName());
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public <T> T executeFunction(Class<T> cls, Map<String, ?> map) {
        return (T) doExecute(map).get(getScalarOutParameterName());
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public <T> T executeFunction(Class<T> cls, SqlParameterSource sqlParameterSource) {
        return (T) doExecute(sqlParameterSource).get(getScalarOutParameterName());
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public <T> T executeObject(Class<T> cls, Object... objArr) {
        return (T) doExecute(objArr).get(getScalarOutParameterName());
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public <T> T executeObject(Class<T> cls, Map<String, ?> map) {
        return (T) doExecute(map).get(getScalarOutParameterName());
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public <T> T executeObject(Class<T> cls, SqlParameterSource sqlParameterSource) {
        return (T) doExecute(sqlParameterSource).get(getScalarOutParameterName());
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public Map<String, Object> execute(Object... args) {
        return doExecute(args);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public Map<String, Object> execute(Map<String, ?> args) {
        return doExecute(args);
    }

    @Override // org.springframework.jdbc.core.simple.SimpleJdbcCallOperations
    public Map<String, Object> execute(SqlParameterSource parameterSource) {
        return doExecute(parameterSource);
    }
}

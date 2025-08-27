package org.springframework.jdbc.core.simple;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.metadata.CallMetaDataContext;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/simple/AbstractJdbcCall.class */
public abstract class AbstractJdbcCall {
    private final JdbcTemplate jdbcTemplate;
    private String callString;
    private CallableStatementCreatorFactory callableStatementFactory;
    protected final Log logger = LogFactory.getLog(getClass());
    private final CallMetaDataContext callMetaDataContext = new CallMetaDataContext();
    private final List<SqlParameter> declaredParameters = new ArrayList();
    private final Map<String, RowMapper<?>> declaredRowMappers = new LinkedHashMap();
    private volatile boolean compiled = false;

    protected AbstractJdbcCall(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected AbstractJdbcCall(JdbcTemplate jdbcTemplate) {
        Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    public void setProcedureName(String procedureName) {
        this.callMetaDataContext.setProcedureName(procedureName);
    }

    public String getProcedureName() {
        return this.callMetaDataContext.getProcedureName();
    }

    public void setInParameterNames(Set<String> inParameterNames) {
        this.callMetaDataContext.setLimitedInParameterNames(inParameterNames);
    }

    public Set<String> getInParameterNames() {
        return this.callMetaDataContext.getLimitedInParameterNames();
    }

    public void setCatalogName(String catalogName) {
        this.callMetaDataContext.setCatalogName(catalogName);
    }

    public String getCatalogName() {
        return this.callMetaDataContext.getCatalogName();
    }

    public void setSchemaName(String schemaName) {
        this.callMetaDataContext.setSchemaName(schemaName);
    }

    public String getSchemaName() {
        return this.callMetaDataContext.getSchemaName();
    }

    public void setFunction(boolean function) {
        this.callMetaDataContext.setFunction(function);
    }

    public boolean isFunction() {
        return this.callMetaDataContext.isFunction();
    }

    public void setReturnValueRequired(boolean returnValueRequired) {
        this.callMetaDataContext.setReturnValueRequired(returnValueRequired);
    }

    public boolean isReturnValueRequired() {
        return this.callMetaDataContext.isReturnValueRequired();
    }

    public void setNamedBinding(boolean namedBinding) {
        this.callMetaDataContext.setNamedBinding(namedBinding);
    }

    public boolean isNamedBinding() {
        return this.callMetaDataContext.isNamedBinding();
    }

    public void setAccessCallParameterMetaData(boolean accessCallParameterMetaData) {
        this.callMetaDataContext.setAccessCallParameterMetaData(accessCallParameterMetaData);
    }

    public String getCallString() {
        return this.callString;
    }

    protected CallableStatementCreatorFactory getCallableStatementFactory() {
        return this.callableStatementFactory;
    }

    public void addDeclaredParameter(SqlParameter parameter) {
        Assert.notNull(parameter, "The supplied parameter must not be null");
        if (!StringUtils.hasText(parameter.getName())) {
            throw new InvalidDataAccessApiUsageException("You must specify a parameter name when declaring parameters for \"" + getProcedureName() + SymbolConstants.QUOTES_SYMBOL);
        }
        this.declaredParameters.add(parameter);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Added declared parameter for [" + getProcedureName() + "]: " + parameter.getName());
        }
    }

    public void addDeclaredRowMapper(String parameterName, RowMapper<?> rowMapper) {
        this.declaredRowMappers.put(parameterName, rowMapper);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Added row mapper for [" + getProcedureName() + "]: " + parameterName);
        }
    }

    public final synchronized void compile() throws InvalidDataAccessApiUsageException {
        if (!isCompiled()) {
            if (getProcedureName() == null) {
                throw new InvalidDataAccessApiUsageException("Procedure or Function name is required");
            }
            try {
                this.jdbcTemplate.afterPropertiesSet();
                compileInternal();
                this.compiled = true;
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("SqlCall for " + (isFunction() ? "function" : "procedure") + " [" + getProcedureName() + "] compiled");
                }
            } catch (IllegalArgumentException ex) {
                throw new InvalidDataAccessApiUsageException(ex.getMessage());
            }
        }
    }

    protected void compileInternal() {
        this.callMetaDataContext.initializeMetaData(getJdbcTemplate().getDataSource());
        for (Map.Entry<String, RowMapper<?>> entry : this.declaredRowMappers.entrySet()) {
            SqlParameter resultSetParameter = this.callMetaDataContext.createReturnResultSetParameter(entry.getKey(), entry.getValue());
            this.declaredParameters.add(resultSetParameter);
        }
        this.callMetaDataContext.processParameters(this.declaredParameters);
        this.callString = this.callMetaDataContext.createCallString();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Compiled stored procedure. Call string is [" + this.callString + "]");
        }
        this.callableStatementFactory = new CallableStatementCreatorFactory(getCallString(), this.callMetaDataContext.getCallParameters());
        this.callableStatementFactory.setNativeJdbcExtractor(getJdbcTemplate().getNativeJdbcExtractor());
        onCompileInternal();
    }

    protected void onCompileInternal() {
    }

    public boolean isCompiled() {
        return this.compiled;
    }

    protected void checkCompiled() throws InvalidDataAccessApiUsageException {
        if (!isCompiled()) {
            this.logger.debug("JdbcCall call not compiled before execution - invoking compile");
            compile();
        }
    }

    protected Map<String, Object> doExecute(SqlParameterSource parameterSource) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        Map<String, Object> params = matchInParameterValuesWithCallParameters(parameterSource);
        return executeCallInternal(params);
    }

    protected Map<String, Object> doExecute(Object... args) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        Map<String, ?> params = matchInParameterValuesWithCallParameters(args);
        return executeCallInternal(params);
    }

    protected Map<String, Object> doExecute(Map<String, ?> args) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        Map<String, ?> params = matchInParameterValuesWithCallParameters(args);
        return executeCallInternal(params);
    }

    private Map<String, Object> executeCallInternal(Map<String, ?> args) {
        CallableStatementCreator csc = getCallableStatementFactory().newCallableStatementCreator(args);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("The following parameters are used for call " + getCallString() + " with " + args);
            int i = 1;
            for (SqlParameter param : getCallParameters()) {
                this.logger.debug(i + ": " + param.getName() + ", SQL type " + param.getSqlType() + ", type name " + param.getTypeName() + ", parameter class [" + param.getClass().getName() + "]");
                i++;
            }
        }
        return getJdbcTemplate().call(csc, getCallParameters());
    }

    protected String getScalarOutParameterName() {
        return this.callMetaDataContext.getScalarOutParameterName();
    }

    protected List<SqlParameter> getCallParameters() {
        return this.callMetaDataContext.getCallParameters();
    }

    protected Map<String, Object> matchInParameterValuesWithCallParameters(SqlParameterSource parameterSource) {
        return this.callMetaDataContext.matchInParameterValuesWithCallParameters(parameterSource);
    }

    private Map<String, ?> matchInParameterValuesWithCallParameters(Object[] args) {
        return this.callMetaDataContext.matchInParameterValuesWithCallParameters(args);
    }

    protected Map<String, ?> matchInParameterValuesWithCallParameters(Map<String, ?> args) {
        return this.callMetaDataContext.matchInParameterValuesWithCallParameters(args);
    }
}

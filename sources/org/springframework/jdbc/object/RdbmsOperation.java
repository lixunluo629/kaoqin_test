package org.springframework.jdbc.object;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/RdbmsOperation.class */
public abstract class RdbmsOperation implements InitializingBean {
    private String[] generatedKeysColumnNames;
    private String sql;
    private volatile boolean compiled;
    protected final Log logger = LogFactory.getLog(getClass());
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private int resultSetType = 1003;
    private boolean updatableResults = false;
    private boolean returnGeneratedKeys = false;
    private final List<SqlParameter> declaredParameters = new LinkedList();

    protected abstract void compileInternal() throws InvalidDataAccessApiUsageException;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate.setDataSource(dataSource);
    }

    public void setFetchSize(int fetchSize) {
        this.jdbcTemplate.setFetchSize(fetchSize);
    }

    public void setMaxRows(int maxRows) {
        this.jdbcTemplate.setMaxRows(maxRows);
    }

    public void setQueryTimeout(int queryTimeout) {
        this.jdbcTemplate.setQueryTimeout(queryTimeout);
    }

    public void setResultSetType(int resultSetType) {
        this.resultSetType = resultSetType;
    }

    public int getResultSetType() {
        return this.resultSetType;
    }

    public void setUpdatableResults(boolean updatableResults) {
        if (isCompiled()) {
            throw new InvalidDataAccessApiUsageException("The updateableResults flag must be set before the operation is compiled");
        }
        this.updatableResults = updatableResults;
    }

    public boolean isUpdatableResults() {
        return this.updatableResults;
    }

    public void setReturnGeneratedKeys(boolean returnGeneratedKeys) {
        if (isCompiled()) {
            throw new InvalidDataAccessApiUsageException("The returnGeneratedKeys flag must be set before the operation is compiled");
        }
        this.returnGeneratedKeys = returnGeneratedKeys;
    }

    public boolean isReturnGeneratedKeys() {
        return this.returnGeneratedKeys;
    }

    public void setGeneratedKeysColumnNames(String... names) {
        if (isCompiled()) {
            throw new InvalidDataAccessApiUsageException("The column names for the generated keys must be set before the operation is compiled");
        }
        this.generatedKeysColumnNames = names;
    }

    public String[] getGeneratedKeysColumnNames() {
        return this.generatedKeysColumnNames;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }

    public void setTypes(int[] types) throws InvalidDataAccessApiUsageException {
        if (isCompiled()) {
            throw new InvalidDataAccessApiUsageException("Cannot add parameters once query is compiled");
        }
        if (types != null) {
            for (int type : types) {
                declareParameter(new SqlParameter(type));
            }
        }
    }

    public void declareParameter(SqlParameter param) throws InvalidDataAccessApiUsageException {
        if (isCompiled()) {
            throw new InvalidDataAccessApiUsageException("Cannot add parameters once the query is compiled");
        }
        this.declaredParameters.add(param);
    }

    public void setParameters(SqlParameter... parameters) {
        if (isCompiled()) {
            throw new InvalidDataAccessApiUsageException("Cannot add parameters once the query is compiled");
        }
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] != null) {
                this.declaredParameters.add(parameters[i]);
            } else {
                throw new InvalidDataAccessApiUsageException("Cannot add parameter at index " + i + " from " + Arrays.asList(parameters) + " since it is 'null'");
            }
        }
    }

    protected List<SqlParameter> getDeclaredParameters() {
        return this.declaredParameters;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws InvalidDataAccessApiUsageException {
        compile();
    }

    public final void compile() throws InvalidDataAccessApiUsageException {
        if (!isCompiled()) {
            if (getSql() == null) {
                throw new InvalidDataAccessApiUsageException("Property 'sql' is required");
            }
            try {
                this.jdbcTemplate.afterPropertiesSet();
                compileInternal();
                this.compiled = true;
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("RdbmsOperation with SQL [" + getSql() + "] compiled");
                }
            } catch (IllegalArgumentException ex) {
                throw new InvalidDataAccessApiUsageException(ex.getMessage());
            }
        }
    }

    public boolean isCompiled() {
        return this.compiled;
    }

    protected void checkCompiled() throws InvalidDataAccessApiUsageException {
        if (!isCompiled()) {
            this.logger.debug("SQL operation not compiled before execution - invoking compile");
            compile();
        }
    }

    protected void validateParameters(Object[] parameters) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        int declaredInParameters = 0;
        for (SqlParameter param : this.declaredParameters) {
            if (param.isInputValueProvided()) {
                if (!supportsLobParameters() && (param.getSqlType() == 2004 || param.getSqlType() == 2005)) {
                    throw new InvalidDataAccessApiUsageException("BLOB or CLOB parameters are not allowed for this kind of operation");
                }
                declaredInParameters++;
            }
        }
        validateParameterCount(parameters != null ? parameters.length : 0, declaredInParameters);
    }

    protected void validateNamedParameters(Map<String, ?> parameters) throws InvalidDataAccessApiUsageException {
        checkCompiled();
        Map<String, ?> paramsToUse = parameters != null ? parameters : Collections.emptyMap();
        int declaredInParameters = 0;
        for (SqlParameter param : this.declaredParameters) {
            if (param.isInputValueProvided()) {
                if (!supportsLobParameters() && (param.getSqlType() == 2004 || param.getSqlType() == 2005)) {
                    throw new InvalidDataAccessApiUsageException("BLOB or CLOB parameters are not allowed for this kind of operation");
                }
                if (param.getName() != null && !paramsToUse.containsKey(param.getName())) {
                    throw new InvalidDataAccessApiUsageException("The parameter named '" + param.getName() + "' was not among the parameters supplied: " + paramsToUse.keySet());
                }
                declaredInParameters++;
            }
        }
        validateParameterCount(paramsToUse.size(), declaredInParameters);
    }

    private void validateParameterCount(int suppliedParamCount, int declaredInParamCount) {
        if (suppliedParamCount < declaredInParamCount) {
            throw new InvalidDataAccessApiUsageException(suppliedParamCount + " parameters were supplied, but " + declaredInParamCount + " in parameters were declared in class [" + getClass().getName() + "]");
        }
        if (suppliedParamCount > this.declaredParameters.size() && !allowsUnusedParameters()) {
            throw new InvalidDataAccessApiUsageException(suppliedParamCount + " parameters were supplied, but " + declaredInParamCount + " parameters were declared in class [" + getClass().getName() + "]");
        }
    }

    protected boolean supportsLobParameters() {
        return true;
    }

    protected boolean allowsUnusedParameters() {
        return false;
    }
}

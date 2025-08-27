package org.springframework.jdbc.object;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.ParameterMapper;
import org.springframework.jdbc.core.SqlParameter;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/SqlCall.class */
public abstract class SqlCall extends RdbmsOperation {
    private boolean function = false;
    private boolean sqlReadyForUse = false;
    private String callString;
    private CallableStatementCreatorFactory callableStatementFactory;

    public SqlCall() {
    }

    public SqlCall(DataSource ds, String sql) {
        setDataSource(ds);
        setSql(sql);
    }

    public void setFunction(boolean function) {
        this.function = function;
    }

    public boolean isFunction() {
        return this.function;
    }

    public void setSqlReadyForUse(boolean sqlReadyForUse) {
        this.sqlReadyForUse = sqlReadyForUse;
    }

    public boolean isSqlReadyForUse() {
        return this.sqlReadyForUse;
    }

    @Override // org.springframework.jdbc.object.RdbmsOperation
    protected final void compileInternal() {
        if (isSqlReadyForUse()) {
            this.callString = getSql();
        } else {
            List<SqlParameter> parameters = getDeclaredParameters();
            int parameterCount = 0;
            if (isFunction()) {
                this.callString = "{? = call " + getSql() + "(";
                parameterCount = -1;
            } else {
                this.callString = "{call " + getSql() + "(";
            }
            for (SqlParameter parameter : parameters) {
                if (!parameter.isResultsParameter()) {
                    if (parameterCount > 0) {
                        this.callString += ", ";
                    }
                    if (parameterCount >= 0) {
                        this.callString += "?";
                    }
                    parameterCount++;
                }
            }
            this.callString += ")}";
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Compiled stored procedure. Call string is [" + getCallString() + "]");
        }
        this.callableStatementFactory = new CallableStatementCreatorFactory(getCallString(), getDeclaredParameters());
        this.callableStatementFactory.setResultSetType(getResultSetType());
        this.callableStatementFactory.setUpdatableResults(isUpdatableResults());
        this.callableStatementFactory.setNativeJdbcExtractor(getJdbcTemplate().getNativeJdbcExtractor());
        onCompileInternal();
    }

    protected void onCompileInternal() {
    }

    public String getCallString() {
        return this.callString;
    }

    protected CallableStatementCreator newCallableStatementCreator(Map<String, ?> inParams) {
        return this.callableStatementFactory.newCallableStatementCreator(inParams);
    }

    protected CallableStatementCreator newCallableStatementCreator(ParameterMapper inParamMapper) {
        return this.callableStatementFactory.newCallableStatementCreator(inParamMapper);
    }
}

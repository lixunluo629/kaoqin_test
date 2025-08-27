package org.springframework.jdbc.object;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterMapper;
import org.springframework.jdbc.core.SqlParameter;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/object/StoredProcedure.class */
public abstract class StoredProcedure extends SqlCall {
    protected StoredProcedure() {
    }

    protected StoredProcedure(DataSource ds, String name) {
        setDataSource(ds);
        setSql(name);
    }

    protected StoredProcedure(JdbcTemplate jdbcTemplate, String name) {
        setJdbcTemplate(jdbcTemplate);
        setSql(name);
    }

    @Override // org.springframework.jdbc.object.RdbmsOperation
    protected boolean allowsUnusedParameters() {
        return true;
    }

    @Override // org.springframework.jdbc.object.RdbmsOperation
    public void declareParameter(SqlParameter param) throws InvalidDataAccessApiUsageException {
        if (param.getName() == null) {
            throw new InvalidDataAccessApiUsageException("Parameters to stored procedures must have names as well as types");
        }
        super.declareParameter(param);
    }

    public Map<String, Object> execute(Object... inParams) {
        Map<String, Object> paramsToUse = new HashMap<>();
        validateParameters(inParams);
        int i = 0;
        for (SqlParameter sqlParameter : getDeclaredParameters()) {
            if (sqlParameter.isInputValueProvided() && i < inParams.length) {
                int i2 = i;
                i++;
                paramsToUse.put(sqlParameter.getName(), inParams[i2]);
            }
        }
        return getJdbcTemplate().call(newCallableStatementCreator((Map<String, ?>) paramsToUse), getDeclaredParameters());
    }

    public Map<String, Object> execute(Map<String, ?> inParams) throws DataAccessException {
        validateParameters(inParams.values().toArray());
        return getJdbcTemplate().call(newCallableStatementCreator(inParams), getDeclaredParameters());
    }

    public Map<String, Object> execute(ParameterMapper inParamMapper) throws DataAccessException {
        checkCompiled();
        return getJdbcTemplate().call(newCallableStatementCreator(inParamMapper), getDeclaredParameters());
    }
}

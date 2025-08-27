package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/CallableStatementCreatorFactory.class */
public class CallableStatementCreatorFactory {
    private final String callString;
    private final List<SqlParameter> declaredParameters;
    private int resultSetType;
    private boolean updatableResults;
    private NativeJdbcExtractor nativeJdbcExtractor;

    public CallableStatementCreatorFactory(String callString) {
        this.resultSetType = 1003;
        this.updatableResults = false;
        this.callString = callString;
        this.declaredParameters = new LinkedList();
    }

    public CallableStatementCreatorFactory(String callString, List<SqlParameter> declaredParameters) {
        this.resultSetType = 1003;
        this.updatableResults = false;
        this.callString = callString;
        this.declaredParameters = declaredParameters;
    }

    public void addParameter(SqlParameter param) {
        this.declaredParameters.add(param);
    }

    public void setResultSetType(int resultSetType) {
        this.resultSetType = resultSetType;
    }

    public void setUpdatableResults(boolean updatableResults) {
        this.updatableResults = updatableResults;
    }

    public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor) {
        this.nativeJdbcExtractor = nativeJdbcExtractor;
    }

    public CallableStatementCreator newCallableStatementCreator(Map<String, ?> params) {
        return new CallableStatementCreatorImpl(params != null ? params : new HashMap<>());
    }

    public CallableStatementCreator newCallableStatementCreator(ParameterMapper inParamMapper) {
        return new CallableStatementCreatorImpl(inParamMapper);
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/CallableStatementCreatorFactory$CallableStatementCreatorImpl.class */
    private class CallableStatementCreatorImpl implements CallableStatementCreator, SqlProvider, ParameterDisposer {
        private ParameterMapper inParameterMapper;
        private Map<String, ?> inParameters;

        public CallableStatementCreatorImpl(ParameterMapper inParamMapper) {
            this.inParameterMapper = inParamMapper;
        }

        public CallableStatementCreatorImpl(Map<String, ?> inParams) {
            this.inParameters = inParams;
        }

        @Override // org.springframework.jdbc.core.CallableStatementCreator
        public CallableStatement createCallableStatement(Connection con) throws SQLException {
            CallableStatement cs;
            if (this.inParameterMapper != null) {
                this.inParameters = this.inParameterMapper.createMap(con);
            } else if (this.inParameters == null) {
                throw new InvalidDataAccessApiUsageException("A ParameterMapper or a Map of parameters must be provided");
            }
            if (CallableStatementCreatorFactory.this.resultSetType == 1003 && !CallableStatementCreatorFactory.this.updatableResults) {
                cs = con.prepareCall(CallableStatementCreatorFactory.this.callString);
            } else {
                cs = con.prepareCall(CallableStatementCreatorFactory.this.callString, CallableStatementCreatorFactory.this.resultSetType, CallableStatementCreatorFactory.this.updatableResults ? 1008 : 1007);
            }
            CallableStatement csToUse = cs;
            if (CallableStatementCreatorFactory.this.nativeJdbcExtractor != null) {
                csToUse = CallableStatementCreatorFactory.this.nativeJdbcExtractor.getNativeCallableStatement(cs);
            }
            int sqlColIndx = 1;
            for (SqlParameter declaredParam : CallableStatementCreatorFactory.this.declaredParameters) {
                if (!declaredParam.isResultsParameter()) {
                    Object inValue = this.inParameters.get(declaredParam.getName());
                    if (declaredParam instanceof ResultSetSupportingSqlParameter) {
                        if (declaredParam instanceof SqlOutParameter) {
                            if (declaredParam.getTypeName() != null) {
                                cs.registerOutParameter(sqlColIndx, declaredParam.getSqlType(), declaredParam.getTypeName());
                            } else if (declaredParam.getScale() != null) {
                                cs.registerOutParameter(sqlColIndx, declaredParam.getSqlType(), declaredParam.getScale().intValue());
                            } else {
                                cs.registerOutParameter(sqlColIndx, declaredParam.getSqlType());
                            }
                            if (declaredParam.isInputValueProvided()) {
                                StatementCreatorUtils.setParameterValue(csToUse, sqlColIndx, declaredParam, inValue);
                            }
                        }
                    } else {
                        if (!this.inParameters.containsKey(declaredParam.getName())) {
                            throw new InvalidDataAccessApiUsageException("Required input parameter '" + declaredParam.getName() + "' is missing");
                        }
                        StatementCreatorUtils.setParameterValue(csToUse, sqlColIndx, declaredParam, inValue);
                    }
                    sqlColIndx++;
                }
            }
            return cs;
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return CallableStatementCreatorFactory.this.callString;
        }

        @Override // org.springframework.jdbc.core.ParameterDisposer
        public void cleanupParameters() {
            if (this.inParameters != null) {
                StatementCreatorUtils.cleanupParameters(this.inParameters.values());
            }
        }

        public String toString() {
            return "CallableStatementCreator: sql=[" + CallableStatementCreatorFactory.this.callString + "]; parameters=" + this.inParameters;
        }
    }
}

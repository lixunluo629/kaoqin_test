package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/PreparedStatementCreatorFactory.class */
public class PreparedStatementCreatorFactory {
    private final String sql;
    private final List<SqlParameter> declaredParameters;
    private int resultSetType;
    private boolean updatableResults;
    private boolean returnGeneratedKeys;
    private String[] generatedKeysColumnNames;
    private NativeJdbcExtractor nativeJdbcExtractor;

    public PreparedStatementCreatorFactory(String sql) {
        this.resultSetType = 1003;
        this.updatableResults = false;
        this.returnGeneratedKeys = false;
        this.sql = sql;
        this.declaredParameters = new LinkedList();
    }

    public PreparedStatementCreatorFactory(String sql, int... types) {
        this.resultSetType = 1003;
        this.updatableResults = false;
        this.returnGeneratedKeys = false;
        this.sql = sql;
        this.declaredParameters = SqlParameter.sqlTypesToAnonymousParameterList(types);
    }

    public PreparedStatementCreatorFactory(String sql, List<SqlParameter> declaredParameters) {
        this.resultSetType = 1003;
        this.updatableResults = false;
        this.returnGeneratedKeys = false;
        this.sql = sql;
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

    public void setReturnGeneratedKeys(boolean returnGeneratedKeys) {
        this.returnGeneratedKeys = returnGeneratedKeys;
    }

    public void setGeneratedKeysColumnNames(String... names) {
        this.generatedKeysColumnNames = names;
    }

    public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor) {
        this.nativeJdbcExtractor = nativeJdbcExtractor;
    }

    public PreparedStatementSetter newPreparedStatementSetter(List<?> params) {
        return new PreparedStatementCreatorImpl(this, params != null ? params : Collections.emptyList());
    }

    public PreparedStatementSetter newPreparedStatementSetter(Object[] params) {
        return new PreparedStatementCreatorImpl(this, params != null ? Arrays.asList(params) : Collections.emptyList());
    }

    public PreparedStatementCreator newPreparedStatementCreator(List<?> params) {
        return new PreparedStatementCreatorImpl(this, params != null ? params : Collections.emptyList());
    }

    public PreparedStatementCreator newPreparedStatementCreator(Object[] params) {
        return new PreparedStatementCreatorImpl(this, params != null ? Arrays.asList(params) : Collections.emptyList());
    }

    public PreparedStatementCreator newPreparedStatementCreator(String sqlToUse, Object[] params) {
        return new PreparedStatementCreatorImpl(sqlToUse, params != null ? Arrays.asList(params) : Collections.emptyList());
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/PreparedStatementCreatorFactory$PreparedStatementCreatorImpl.class */
    private class PreparedStatementCreatorImpl implements PreparedStatementCreator, PreparedStatementSetter, SqlProvider, ParameterDisposer {
        private final String actualSql;
        private final List<?> parameters;

        public PreparedStatementCreatorImpl(PreparedStatementCreatorFactory preparedStatementCreatorFactory, List<?> parameters) {
            this(preparedStatementCreatorFactory.sql, parameters);
        }

        public PreparedStatementCreatorImpl(String actualSql, List<?> parameters) {
            this.actualSql = actualSql;
            Assert.notNull(parameters, "Parameters List must not be null");
            this.parameters = parameters;
            if (this.parameters.size() != PreparedStatementCreatorFactory.this.declaredParameters.size()) {
                Set<String> names = new HashSet<>();
                for (int i = 0; i < parameters.size(); i++) {
                    Object param = parameters.get(i);
                    if (param instanceof SqlParameterValue) {
                        names.add(((SqlParameterValue) param).getName());
                    } else {
                        names.add("Parameter #" + i);
                    }
                }
                if (names.size() != PreparedStatementCreatorFactory.this.declaredParameters.size()) {
                    throw new InvalidDataAccessApiUsageException("SQL [" + PreparedStatementCreatorFactory.this.sql + "]: given " + names.size() + " parameters but expected " + PreparedStatementCreatorFactory.this.declaredParameters.size());
                }
            }
        }

        @Override // org.springframework.jdbc.core.PreparedStatementCreator
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            PreparedStatement ps;
            if (PreparedStatementCreatorFactory.this.generatedKeysColumnNames != null || PreparedStatementCreatorFactory.this.returnGeneratedKeys) {
                if (PreparedStatementCreatorFactory.this.generatedKeysColumnNames != null) {
                    ps = con.prepareStatement(this.actualSql, PreparedStatementCreatorFactory.this.generatedKeysColumnNames);
                } else {
                    ps = con.prepareStatement(this.actualSql, 1);
                }
            } else if (PreparedStatementCreatorFactory.this.resultSetType == 1003 && !PreparedStatementCreatorFactory.this.updatableResults) {
                ps = con.prepareStatement(this.actualSql);
            } else {
                ps = con.prepareStatement(this.actualSql, PreparedStatementCreatorFactory.this.resultSetType, PreparedStatementCreatorFactory.this.updatableResults ? 1008 : 1007);
            }
            setValues(ps);
            return ps;
        }

        @Override // org.springframework.jdbc.core.PreparedStatementSetter
        public void setValues(PreparedStatement ps) throws SQLException {
            SqlParameter declaredParameter;
            PreparedStatement psToUse = ps;
            if (PreparedStatementCreatorFactory.this.nativeJdbcExtractor != null) {
                psToUse = PreparedStatementCreatorFactory.this.nativeJdbcExtractor.getNativePreparedStatement(ps);
            }
            int sqlColIndx = 1;
            for (int i = 0; i < this.parameters.size(); i++) {
                Object in = this.parameters.get(i);
                if (!(in instanceof SqlParameterValue)) {
                    if (PreparedStatementCreatorFactory.this.declaredParameters.size() > i) {
                        declaredParameter = (SqlParameter) PreparedStatementCreatorFactory.this.declaredParameters.get(i);
                    } else {
                        throw new InvalidDataAccessApiUsageException("SQL [" + PreparedStatementCreatorFactory.this.sql + "]: unable to access parameter number " + (i + 1) + " given only " + PreparedStatementCreatorFactory.this.declaredParameters.size() + " parameters");
                    }
                } else {
                    SqlParameterValue paramValue = (SqlParameterValue) in;
                    in = paramValue.getValue();
                    declaredParameter = paramValue;
                }
                if ((in instanceof Collection) && declaredParameter.getSqlType() != 2003) {
                    Collection<?> entries = (Collection) in;
                    for (Object entry : entries) {
                        if (entry instanceof Object[]) {
                            Object[] valueArray = (Object[]) entry;
                            for (Object argValue : valueArray) {
                                int i2 = sqlColIndx;
                                sqlColIndx++;
                                StatementCreatorUtils.setParameterValue(psToUse, i2, declaredParameter, argValue);
                            }
                        } else {
                            int i3 = sqlColIndx;
                            sqlColIndx++;
                            StatementCreatorUtils.setParameterValue(psToUse, i3, declaredParameter, entry);
                        }
                    }
                } else {
                    int i4 = sqlColIndx;
                    sqlColIndx++;
                    StatementCreatorUtils.setParameterValue(psToUse, i4, declaredParameter, in);
                }
            }
        }

        @Override // org.springframework.jdbc.core.SqlProvider
        public String getSql() {
            return PreparedStatementCreatorFactory.this.sql;
        }

        @Override // org.springframework.jdbc.core.ParameterDisposer
        public void cleanupParameters() {
            StatementCreatorUtils.cleanupParameters(this.parameters);
        }

        public String toString() {
            return "PreparedStatementCreator: sql=[" + PreparedStatementCreatorFactory.this.sql + "]; parameters=" + this.parameters;
        }
    }
}

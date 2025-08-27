package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/ArgumentPreparedStatementSetter.class */
public class ArgumentPreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {
    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    @Override // org.springframework.jdbc.core.PreparedStatementSetter
    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                doSetValue(ps, i + 1, arg);
            }
        }
    }

    protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
        if (argValue instanceof SqlParameterValue) {
            SqlParameterValue paramValue = (SqlParameterValue) argValue;
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, paramValue.getValue());
        } else {
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, Integer.MIN_VALUE, argValue);
        }
    }

    @Override // org.springframework.jdbc.core.ParameterDisposer
    public void cleanupParameters() {
        StatementCreatorUtils.cleanupParameters(this.args);
    }
}

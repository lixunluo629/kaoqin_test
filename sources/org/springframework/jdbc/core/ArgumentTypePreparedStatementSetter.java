package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import org.springframework.dao.InvalidDataAccessApiUsageException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/ArgumentTypePreparedStatementSetter.class */
public class ArgumentTypePreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {
    private final Object[] args;
    private final int[] argTypes;

    public ArgumentTypePreparedStatementSetter(Object[] args, int[] argTypes) {
        if ((args != null && argTypes == null) || ((args == null && argTypes != null) || (args != null && args.length != argTypes.length))) {
            throw new InvalidDataAccessApiUsageException("args and argTypes parameters must match");
        }
        this.args = args;
        this.argTypes = argTypes;
    }

    @Override // org.springframework.jdbc.core.PreparedStatementSetter
    public void setValues(PreparedStatement ps) throws SQLException {
        int parameterPosition = 1;
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                if ((arg instanceof Collection) && this.argTypes[i] != 2003) {
                    Collection<?> entries = (Collection) arg;
                    for (Object entry : entries) {
                        if (entry instanceof Object[]) {
                            Object[] valueArray = (Object[]) entry;
                            for (Object argValue : valueArray) {
                                doSetValue(ps, parameterPosition, this.argTypes[i], argValue);
                                parameterPosition++;
                            }
                        } else {
                            doSetValue(ps, parameterPosition, this.argTypes[i], entry);
                            parameterPosition++;
                        }
                    }
                } else {
                    doSetValue(ps, parameterPosition, this.argTypes[i], arg);
                    parameterPosition++;
                }
            }
        }
    }

    protected void doSetValue(PreparedStatement ps, int parameterPosition, int argType, Object argValue) throws SQLException {
        StatementCreatorUtils.setParameterValue(ps, parameterPosition, argType, argValue);
    }

    @Override // org.springframework.jdbc.core.ParameterDisposer
    public void cleanupParameters() {
        StatementCreatorUtils.cleanupParameters(this.args);
    }
}

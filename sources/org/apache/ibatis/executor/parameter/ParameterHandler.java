package org.apache.ibatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/parameter/ParameterHandler.class */
public interface ParameterHandler {
    Object getParameterObject();

    void setParameters(PreparedStatement preparedStatement) throws SQLException;
}

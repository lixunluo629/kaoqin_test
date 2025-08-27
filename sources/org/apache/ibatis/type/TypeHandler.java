package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/TypeHandler.class */
public interface TypeHandler<T> {
    void setParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException;

    T getResult(ResultSet resultSet, String str) throws SQLException;

    T getResult(ResultSet resultSet, int i) throws SQLException;

    T getResult(CallableStatement callableStatement, int i) throws SQLException;
}

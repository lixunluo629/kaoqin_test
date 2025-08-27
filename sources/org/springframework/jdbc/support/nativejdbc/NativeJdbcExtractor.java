package org.springframework.jdbc.support.nativejdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/NativeJdbcExtractor.class */
public interface NativeJdbcExtractor {
    boolean isNativeConnectionNecessaryForNativeStatements();

    boolean isNativeConnectionNecessaryForNativePreparedStatements();

    boolean isNativeConnectionNecessaryForNativeCallableStatements();

    Connection getNativeConnection(Connection connection) throws SQLException;

    Connection getNativeConnectionFromStatement(Statement statement) throws SQLException;

    Statement getNativeStatement(Statement statement) throws SQLException;

    PreparedStatement getNativePreparedStatement(PreparedStatement preparedStatement) throws SQLException;

    CallableStatement getNativeCallableStatement(CallableStatement callableStatement) throws SQLException;

    ResultSet getNativeResultSet(ResultSet resultSet) throws SQLException;
}

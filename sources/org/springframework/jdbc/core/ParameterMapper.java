package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/ParameterMapper.class */
public interface ParameterMapper {
    Map<String, ?> createMap(Connection connection) throws SQLException;
}
